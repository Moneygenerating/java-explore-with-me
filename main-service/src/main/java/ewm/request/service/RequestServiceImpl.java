package ewm.request.service;

import ewm.errors.NotFoundException;
import ewm.errors.ValidationException;
import ewm.event.EventRepository;
import ewm.event.model.Event;
import ewm.event.model.StateLifecycle;
import ewm.request.RequestMapper;
import ewm.request.RequestRepository;
import ewm.request.dto.RequestDto;
import ewm.request.model.ParticipationRequest;
import ewm.request.model.Status;
import ewm.user.UserRepository;
import ewm.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<RequestDto> getAllReq(Long userId, Long eventId) {
        List<Event> eventsByInitiator = eventRepository.findAllByInitiatorId(userId);
        List<ParticipationRequest> requests = requestRepository.findAll((root, query, criteriaBuilder) ->
                root.get("event").in(eventsByInitiator.stream().map(Event::getId).collect(Collectors.toList())));

        return requests.stream().map(RequestMapper::requestToDto).collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getAllReq(Long userId) {
        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);

        return requests.stream().map(RequestMapper::requestToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
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
        if (event.getConfirmedRequests() == null) {
            event.setConfirmedRequests(0);
        }
        event.incrementParticipants();
        eventRepository.save(event);

        //Проверка заполненности, при условии заполненности все остальные заявки отклоняются
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == countConfirmedReqs + 1) {
            List<ParticipationRequest> requestList = requestRepository.findAll(((root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("status"), Status.PENDING),
                            criteriaBuilder.equal(root.get("event"), event.getId())
                    )));
            requestList.forEach(el -> el.setStatus(Status.REJECTED));
            requestRepository.saveAll(requestList);
        }
        return requestDto;

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
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

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public RequestDto addRequest(Long userId, Long eventId) {
        ParticipationRequest req = new ParticipationRequest();
        User user = userRepository.getReferenceById(userId);
        Event eventDumb = eventRepository.getReferenceById(eventId);


        req.setRequester(user);
        req.setCreated(LocalDateTime.now());
        req.setStatus(Status.PENDING);
        req.setEvent(eventDumb);
        Event event = getEvent(eventId, userId, false);

        Long countOfRequests = countOfRequests(event);

        if (event.getParticipantLimit() != 0 && countOfRequests >= event.getParticipantLimit()) {
            throw new ValidationException("Участие уже невозможно, достигнут лимит события");
        }

        ParticipationRequest reqCheck = requestRepository.findByRequesterAndEvent(user, event);

        if (reqCheck != null) {
            throw new ValidationException("Нельзя добавить запрос в свое же событие");
        }

        if (!event.getRequestModeration()) {
            req.setStatus(Status.CONFIRMED);
            event.incrementParticipants();
            eventRepository.save(event);
        }

        return RequestMapper.requestToDto(requestRepository.save(req));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public RequestDto cancelRequest(Long userId, Long requestId) {

        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId);
        //проверка id
        if (request.getRequester() == null) {
            throw new NotFoundException("Запрос для отмены события не найден");
        }

        Event event = eventRepository.findOne(((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("id"), request.getEvent().getId()),
                        criteriaBuilder.equal(root.get("state"), StateLifecycle.PUBLISHED)
                ))).orElseThrow(() -> new NotFoundException("Не найдено событие по которому отменяется запрос"));

        //логика присваивания статуса и декремента
        if (request.getStatus().equals(Status.CONFIRMED)) {
            event.decrementParticipants();
            eventRepository.save(event);
        }

        request.setStatus(Status.CANCELED);

        return RequestMapper.requestToDto(requestRepository.save(request));
    }

    private Event getEvent(Long eventId, Long userId, Boolean initiator) {
        return eventRepository.findOne(((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("id"), eventId),
                        (initiator) ? criteriaBuilder.equal(root.get("initiator"),
                                userId) : criteriaBuilder.notEqual(root.get("initiator"), userId),
                        criteriaBuilder.equal(root.get("state"), StateLifecycle.PUBLISHED)
                ))).orElseThrow(() -> new NotFoundException("Событие не найдено"));
    }

    private Long countOfRequests(Event event) {
        return requestRepository.count((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("event"), event.getId()),
                        criteriaBuilder.equal(root.get("status"), Status.CONFIRMED)
                ));
    }
}
