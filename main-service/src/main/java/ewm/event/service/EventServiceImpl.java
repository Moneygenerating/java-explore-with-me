package ewm.event.service;

import ewm.category.CategoryRepository;
import ewm.errors.NotFoundException;
import ewm.errors.ValidationException;
import ewm.event.EventMapper;
import ewm.event.EventRepository;
import ewm.event.dto.EventAdminDto;
import ewm.event.dto.EventFullDto;
import ewm.event.model.Event;
import ewm.event.model.Location;
import ewm.event.model.StateLifecycle;
import ewm.request.RequestRepository;
import ewm.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
//@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<EventFullDto> getAllPublic(Pageable pageable, String text, List<Long> categories, Boolean paid,
                                           String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort) {

        List<EventFullDto> events = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;


        //time valid
        if (rangeStart == null || rangeEnd == null) {
            startTime = LocalDateTime.now();
        } else {
            startTime = LocalDateTime.parse(rangeStart, formatter);
            endTime = LocalDateTime.parse(rangeEnd, formatter);
        }

        for (Long catId : categories) {

            Event event = eventRepository.getPublicEvent(text.toLowerCase(), paid, startTime, endTime);
            Integer confirmedRequests = requestRepository.getCountOfConfirmedRequests(event.getId());
            if (event.getParticipantLimit() < confirmedRequests && Objects.equals(event.getCategory().getId(), catId)) {
                EventFullDto dto = EventMapper.eventToDto(event);
                setEventsDtoEntity(event, dto);
                events.add(dto);
                //save to static
            }
        }

        if (sort.equals("VIEWS")) {
            return events.stream()
                    .sorted(Comparator.comparing(EventFullDto::getViews))
                    .collect(Collectors.toList());
        } else {
            return events.stream()
                    .sorted(Comparator.comparing(EventFullDto::getEventDate))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto getById(Long ids, String state) {
        Event event = eventRepository.findByIdAndState(ids);
        if (event == null) {
            return null;
        }
        EventFullDto dto = EventMapper.eventToDto(event);

        setEventsDtoEntity(event, dto);
        //save to static
        return dto;
    }

    @Override
    public List<EventFullDto> getAllEventByIdPrivate(Long ids, Pageable pageable) {

        List<Event> events = eventRepository.getAllByInitiatorId(ids);

        List<EventFullDto> eventsDto = getEventsDtoToList(events);

        return eventsDto;
    }

    @Override
    public EventFullDto pathEventByIdPrivate(Long userId, EventFullDto eventFullDto) {

        Event event = eventRepository.getReferenceById(eventFullDto.getEventId());
        if (event.getId() == null) {
            throw new NotFoundException("Event с таким id не найден");
        }

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Можно редактировать только свои события");
        }

        Event eventForUpdate = EventMapper.dtoToEvent(eventFullDto);
        eventForUpdate.setInitiator(userRepository.getReferenceById(userId));
        EventFullDto dto;

        if (event.getState().name().equals("PENDING")) {
            event = eventRepository.save(eventForUpdate);
            event.setInitiator(userRepository.getReferenceById(userId));
            dto = EventMapper.eventToDto(event);
        } else if (event.getState().name().equals("CANCELED")) {
            eventForUpdate.setState(StateLifecycle.valueOf("PENDING"));
            event = eventRepository.save(eventForUpdate);
            event.setInitiator(userRepository.getReferenceById(userId));
            dto = EventMapper.eventToDto(event);
        } else {
            throw new NotFoundException("Нельзя редактировать опубликованные события");
        }
        setEventsDtoEntity(event, dto);
        return dto;
    }

    @Override
    public EventFullDto addEventPrivate(Long userId, EventFullDto eventFullDto) {
        Event eventForSave = EventMapper.dtoToEvent(eventFullDto);
        eventForSave.setInitiator(userRepository.getReferenceById(userId));
        eventRepository.save(eventForSave);

        EventFullDto dto = EventMapper.eventToDto(eventForSave);
        setEventsDtoEntity(eventForSave, dto);
        return dto;
    }

    @Override
    public EventFullDto getEventFullPrivate(Long userId, Long eventId) {
        Event event = eventRepository.getEventByIdAndInitiatorId(eventId, userId);

        EventFullDto dto = EventMapper.eventToDto(event);
        setEventsDtoEntity(event, dto);
        return dto;
    }

    @Override
    public EventFullDto pathEventCansel(Long userId, Long eventId) {
        Event event = eventRepository.getEventByIdAndInitiatorId(eventId, userId);
        if (event.getId() == null) {
            throw new NotFoundException("Event с таким id не найден");
        }

        if (event.getState().name().equals("PENDING")) {
            event.setState(StateLifecycle.valueOf("CANCELED"));
        } else {
            throw new ValidationException("Нельзя отменить опубликованное или уже отмененное событие");
        }

        event.setInitiator(userRepository.getReferenceById(userId));

        EventFullDto dto = EventMapper.eventToDto(event);
        setEventsDtoEntity(event, dto);

        return dto;
    }

    @Override
    public List<EventFullDto> getAllAdmin(Pageable pageable, List<Long> users, List<String> states, List<Long> categories,
                                          String rangeStart, String rangeEnd) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();

        Page<Event> events = eventRepository.findAll((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(
                                (users != null) ? root.get("initiator").in(users) : root.isNotNull(),
                                (states != null) ? root.get("state").in(states.stream().map(state ->
                                        StateLifecycle.valueOf(state).ordinal()).collect(Collectors.toList())) : root.isNotNull(),
                                (categories != null) ? root.get("categories").in(categories) : root.isNotNull(),
                                (!rangeStart.isEmpty() && !rangeEnd.isEmpty()) ?
                                        criteriaBuilder.and(
                                                //test between
                                                criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.parse(rangeStart, formatter)),
                                                criteriaBuilder.lessThan(root.get("eventDate"), LocalDateTime.parse(rangeEnd, formatter))
                                        ) : criteriaBuilder.lessThan(root.get("eventDate"), currentTime)
                        ),
                pageable);

        return events.stream().map(EventMapper::eventToDto).collect(Collectors.toList());
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

        return EventMapper.eventToDto(eventRepository.save(event));

    }

    @Override
    public EventFullDto publishEventAdmin(Long id) {
        Event event = eventRepository.getReferenceById(id);
        if (event.getId() == null) {
            throw new NotFoundException("Событие для публикации не найдено");
        }
        event.setState(StateLifecycle.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());

        return EventMapper.eventToDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEventAdmin(Long id) {
        Event event = eventRepository.getReferenceById(id);
        if (event.getId() == null) {
            throw new NotFoundException("Событие для публикации не найдено");
        }
        event.setState(StateLifecycle.CANCELED);

        return EventMapper.eventToDto(eventRepository.save(event));
    }

    private void setEventsDtoEntity(Event event, EventFullDto dto) {
        dto.setCategory(EventMapper.categoryToEventNewDto(event.getCategory()));
        dto.setInitiator(EventMapper.userToEventNewDto(event.getInitiator()));
        dto.setLocation(EventMapper.locationToEventDto(new Location(event.getLat(), event.getLon())));
        //dto.setViews();
        //get запрос про views из id из контроллера

        Integer confirmedRequests = requestRepository.getCountOfConfirmedRequests(event.getId());
        dto.setCurrentParticipants(confirmedRequests);
    }

    private void setChangesEntities(Event event, EventAdminDto eventDto) {
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
        if (eventDto.getLocation() != null) {
            event.setLat(eventDto.getLocation().getLat());
            event.setLon(eventDto.getLocation().getLon());
        }
    }

    private List<EventFullDto> getEventsDtoToList(List<Event> events) {
        List<EventFullDto> eventFullDtoList = events.stream().map(event -> {
            EventFullDto eventFullDto = EventMapper.eventToDto(event);
            setEventsDtoEntity(event, eventFullDto);
            return eventFullDto;
        }).collect(Collectors.toList());
        if (eventFullDtoList.size() > 0) {
            return eventFullDtoList;
        }

        throw new NotFoundException("Не удалось найти список событий");
    }
}
