package ewm.event.service;

import ewm.category.CategoryRepository;
import ewm.client.BaseClient;
import ewm.errors.NotFoundException;
import ewm.errors.ValidationException;
import ewm.event.EventMapper;
import ewm.event.EventRepository;
import ewm.event.dto.*;
import ewm.event.model.Event;
import ewm.event.model.StateLifecycle;
import ewm.user.UserRepository;
import ewm.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
//@Transactional(readOnly = true)
@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BaseClient baseClient;

    @Override
    public List<EventShortDto> getAllPublic(Pageable pageable, String text, List<Long> categories, Boolean paid,
                                            String rangeStart, String rangeEnd, Boolean onlyAvailable) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();

        Page<Event> events = eventRepository.findAll((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get("state"), StateLifecycle.PUBLISHED),
                                root.get("category").in(categories),
                                criteriaBuilder.equal(root.get("paid"), paid),
                                (!rangeStart.isEmpty() && !rangeEnd.isEmpty()) ?
                                        criteriaBuilder.and(
                                                criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.parse(rangeStart, formatter)),
                                                criteriaBuilder.lessThan(root.get("eventDate"), LocalDateTime.parse(rangeEnd, formatter))
                                        ) : criteriaBuilder.lessThan(root.get("eventDate"), currentTime),
                                (onlyAvailable) ? criteriaBuilder.or(
                                        criteriaBuilder.equal(root.get("participantLimit"), 0),
                                        criteriaBuilder.and(
                                                criteriaBuilder.notEqual(root.get("participantLimit"), 0),
                                                criteriaBuilder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"))
                                        )) : root.isNotNull(),
                                criteriaBuilder.or(
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%")
                                )),
                pageable);

        List<EventShortDto> eventsDto = events.stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
        return fillEventsOfViews(eventsDto);
    }

    @Override
    public EventFullDto getById(Long ids, String state) {
        Event event = eventRepository.findByIdAndState(ids, StateLifecycle.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Событие с таким id не найдено");
        }
        EventFullDto dtoEvent = EventMapper.eventToFullDto(event);
        dtoEvent.setViews(getViewsForEvent(dtoEvent.getId()));
        return EventMapper.eventToFullDto(event);
    }

    @Override
    public List<EventFullDto> getAllEventByIdPrivate(Long ids, Pageable pageable) {
        User user = userRepository.getReferenceById(ids);

        return eventRepository.findAll(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("initiator"),
                        user), pageable).stream().map(EventMapper::eventToFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto pathEventByIdPrivate(Long userId, EditEventDto editEventDto) {

        Event event = eventRepository.getReferenceById(editEventDto.getEventId());
        if (event.getId() == null) {
            throw new NotFoundException("Event с таким id не найден");
        }

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Можно редактировать только свои события");
        }

        if (event.getState().equals(StateLifecycle.PUBLISHED)) {
            throw new ValidationException("Нельзя изменить уже опубликованные события");
        }

        if (event.getState().equals(StateLifecycle.CANCELED)) {
            event.setState(StateLifecycle.PENDING);
        }
        setChangesEntities(event, editEventDto);

        return EventMapper.eventToFullDto(event);

    }

    @Override
    public EventFullDto addEventPrivate(Long userId, AddEventDto addEventDto) {
        Event eventForSave = EventMapper.addDtoToEvent(addEventDto);
        //todoCheck
        eventForSave.setInitiator(userRepository.getReferenceById(userId));
        eventForSave.setCategory(categoryRepository.getReferenceById(addEventDto.getCategory()));
        eventForSave.setRequestModeration(addEventDto.getRequestModeration());
        eventForSave.setLon(addEventDto.getLocation().getLon());
        eventForSave.setLat(addEventDto.getLocation().getLat());

        return EventMapper.eventToFullDto(eventRepository.save(eventForSave));
    }

    @Override
    public EventFullDto getEventFullPrivate(Long userId, Long eventId) {
        Event event = eventRepository.getEventByIdAndInitiatorId(eventId, userId);
        return EventMapper.eventToFullDto(event);
    }

    @Override
    public EventFullDto pathEventCansel(Long userId, Long eventId) {
        Event event = eventRepository.getEventByIdAndInitiatorId(eventId, userId);
        if (event.getId() == null) {
            throw new NotFoundException("Event с таким id не найден");
        }
        if (event.getState().equals(StateLifecycle.PUBLISHED)) {
            throw new ValidationException("Нельзя отменить опубликованное событие");
        }

        event.setState(StateLifecycle.CANCELED);

        return EventMapper.eventToFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getAllAdmin(Pageable pageable, List<Long> users, List<String> states, List<Long> categories,
                                          String rangeStart, String rangeEnd) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();

        Page<Event> events = eventRepository.findAll((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(
                                (users != null) ? root.get("initiator").in(users) : root.isNotNull(),
                                (states != null) ? root.get("state").in(states.stream().map(StateLifecycle::valueOf).collect(Collectors.toList())) : root.isNotNull(),
                                (categories != null) ? root.get("category").in(categories) : root.isNotNull(),
                                (!rangeStart.isEmpty() && !rangeEnd.isEmpty()) ?
                                        criteriaBuilder.and(
                                                //test between
                                                criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.parse(rangeStart, formatter)),
                                                criteriaBuilder.lessThan(root.get("eventDate"), LocalDateTime.parse(rangeEnd, formatter))
                                        ) : criteriaBuilder.lessThan(root.get("eventDate"), currentTime)
                        ),
                pageable);

        return events.stream().map(EventMapper::eventToFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto saveAdmin(EventAdminDto eventAdminDto, Long id) {
        Event event = eventRepository.getReferenceById(id);
        if (event.getId() == null) {
            throw new NotFoundException("Редактируемое событие не найдено");
        }
        setChangesEntities(event, eventAdminDto);

        if (eventAdminDto.getRequestModeration() != null) {
            event.setRequestModeration(eventAdminDto.getRequestModeration());
        }

        if (eventAdminDto.getLocation() != null) {
            event.setLat(eventAdminDto.getLocation().getLat());
            event.setLon(eventAdminDto.getLocation().getLon());
        }
        event.setRequestModeration(eventAdminDto.getRequestModeration());
        return EventMapper.eventToFullDto(eventRepository.save(event));

    }

    @Override
    public EventFullDto publishEventAdmin(Long id) {
        Event event = eventRepository.getReferenceById(id);
        if (event.getId() == null) {
            throw new NotFoundException("Событие для публикации не найдено");
        }
        event.setState(StateLifecycle.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());

        return EventMapper.eventToFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEventAdmin(Long id) {
        Event event = eventRepository.getReferenceById(id);
        if (event.getId() == null) {
            throw new NotFoundException("Событие для публикации не найдено");
        }
        event.setState(StateLifecycle.CANCELED);

        return EventMapper.eventToFullDto(eventRepository.save(event));
    }

    private <T extends AbstractEventEditDto> void setChangesEntities(Event event, T eventDto) {
        if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().isEmpty()) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(categoryRepository.getReferenceById(eventDto.getCategory()));
        }
        if (eventDto.getDescription() != null && !eventDto.getDescription().isEmpty()) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getTitle() != null && !eventDto.getTitle().isEmpty()) {
            event.setTitle(eventDto.getTitle());
        }
    }

    private Long getViewsForEvent(Long eventId) {
        log.info("Отправка запроса клиентом на /stats получение кол-ва просмотров");
        return baseClient.getViewsForEvents(List.of(eventId)).getOrDefault(eventId, 0L);
    }

    private List<EventShortDto> fillEventsOfViews(List<EventShortDto> eventsDto) {
        final Map<Long, Long> eventsViews;
        if (eventsDto.size() != 0) {
            List<Long> eventIds = eventsDto.stream().map(EventShortDto::getId).collect(Collectors.toList());
            log.info("Отправка запроса клиентом на /stats получение кол-ва просмотров");
            eventsViews = baseClient.getViewsForEvents(eventIds);
        } else {
            eventsViews = new HashMap<>();
        }

        eventsDto.forEach(e -> e.setViews(eventsViews.getOrDefault(e.getId(), 0L)));
        return eventsDto;
    }
}
