package ewm.request.service;

import ewm.errors.NotFoundException;
import ewm.errors.ValidationException;
import ewm.event.EventRepository;
import ewm.event.model.Event;
import ewm.request.RequestMapper;
import ewm.request.RequestRepository;
import ewm.request.dto.RequestDto;
import ewm.request.model.ParticipationRequest;
import ewm.request.model.Status;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<RequestDto> getAllReq(Long userId, Long eventId) {
        List<Event> eventsByInitiator = eventRepository.findAllById(Collections.singleton(userId));
        List<ParticipationRequest> requests = requestRepository.findAll((root, query, criteriaBuilder) ->
                root.get("events").in(eventsByInitiator.stream().map(Event::getId).collect(Collectors.toList())));

        return requests.stream().map(RequestMapper::requestToDto).collect(Collectors.toList());
    }

    @Override
    public RequestDto confirmRequest(Long userId, Long eventId, Long reqId) {
        Event event = eventRepository.getEventByIdAndInitiatorId(eventId, userId);
        Long countConfirmedReqs = countOfRequests(event);
        ParticipationRequest request = requestRepository.findOne((root, q, cb) ->
                cb.and(cb.equal(root.get("id"), reqId),
                        cb.equal(root.get("event"), event.getId())
                )).orElseThrow(() -> new NotFoundException("Не найден запрос с текущими параметрами"));

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= countConfirmedReqs) {
            throw new ValidationException("Лимит участников исчерпан");
        }
        if (!event.getRequestModeration() && event.getParticipantLimit() == 0) {
            return RequestMapper.requestToDto(request);
        }
        //обновим статус запроса + увеличим акт число принятых участников
        request.setStatus(Status.CONFIRMED);
        RequestDto requestDto = RequestMapper.requestToDto(requestRepository.save(request));
        event.incrementParticipants();
        eventRepository.save(event);

        //Проверка заполненности, при условии заполненности все остальные заявки отклоняются
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == countConfirmedReqs + 1) {
            List<ParticipationRequest> requestList = requestRepository.findAll(((root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("status"), Status.PENDING.ordinal()),
                            criteriaBuilder.equal(root.get("event"), event.getId())
                    )));
            requestList.forEach(el -> el.setStatus(Status.REJECTED));
            requestRepository.saveAll(requestList);
        }
        return requestDto;

    }

    @Override
    public RequestDto rejectRequest(Long userId, Long eventId, Long reqId) {
        Event event = eventRepository.getEventByIdAndInitiatorId(eventId, userId);
        ParticipationRequest request = requestRepository.findOne((root, q, cb) ->
                cb.and(cb.equal(root.get("id"), reqId),
                        cb.equal(root.get("event"), event.getId())
                )).orElseThrow(() -> new NotFoundException("Не найден запрос с текущими параметрами"));

        if (request.getStatus().equals(Status.CONFIRMED)) {
            event.decrementParticipants();
            eventRepository.save(event);
        }

        //обновим статус запроса + увеличим акт число принятых участников
        request.setStatus(Status.REJECTED);
        return RequestMapper.requestToDto(requestRepository.save(request));
    }


    private Long countOfRequests(Event event) {
        return requestRepository.count((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("event"), event.getId()),
                        criteriaBuilder.equal(root.get("status"), Status.CONFIRMED.ordinal())
                ));
    }
}
