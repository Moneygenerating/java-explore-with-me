package ewm.event;

import ewm.category.model.Category;
import ewm.event.dto.AddEventDto;
import ewm.event.dto.EventFullDto;
import ewm.event.dto.EventShortDto;
import ewm.event.model.Event;
import ewm.event.model.Location;
import ewm.event.model.StateLifecycle;
import ewm.user.model.User;

import java.time.LocalDateTime;

public class EventMapper {
    public static EventFullDto eventToFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                categoryToEventNewDto(event.getCategory()),
                event.getCreatedOn(), //set event.getCreatedOn()
                event.getDescription(),
                event.getEventDate(),
                userToEventNewDto(event.getInitiator()),
                locationToEventDto(new Location(event.getLat(), event.getLon())),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                0L, // state
                event.getConfirmedRequests()  //set countConfirmedRequests in
        );
    }

    public static Event dtoToEvent(EventFullDto eventFullDto) {
        return new Event(
                eventFullDto.getId(),
                eventFullDto.getAnnotation(),
                new Category(
                        eventFullDto.getCategory().getId(),
                        eventFullDto.getCategory().getName()
                ),
                LocalDateTime.now(), //createdOn
                eventFullDto.getDescription(),
                eventFullDto.getEventDate(),
                null, //set user by evenFullDto
                eventFullDto.getLocation().getLon(),
                eventFullDto.getLocation().getLat(),
                eventFullDto.getPaid(),
                eventFullDto.getParticipantLimit(),
                eventFullDto.getPublishedOn(),
                eventFullDto.getConfirmedRequests(),
                eventFullDto.getRequestModeration(),
                StateLifecycle.PENDING,
                eventFullDto.getTitle()
        );
    }

    public static Event addDtoToEvent(AddEventDto addEventDto) {
        return new Event(
                null,
                addEventDto.getAnnotation(),
                null,
                LocalDateTime.now(), //createdOn
                addEventDto.getDescription(),
                addEventDto.getEventDate(),
                null, //set user by evenFullDto
                addEventDto.getLocation().getLon(),
                addEventDto.getLocation().getLat(),
                addEventDto.getPaid(),
                addEventDto.getParticipantLimit(),
                null,
                null,
                addEventDto.getRequestModeration(),
                StateLifecycle.PENDING,
                addEventDto.getTitle()
        );
    }

    public static EventFullDto.LocationDto locationToEventDto(Location location) {
        return new EventFullDto.LocationDto(
                location.getLat(),
                location.getLon()
        );
    }

    public static EventFullDto.UserShortDto userToEventNewDto(User user) {
        return new EventFullDto.UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static EventFullDto.CategoryDto categoryToEventNewDto(Category category) {
        return new EventFullDto.CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static EventShortDto eventToShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                new EventShortDto.CategoryDto(event.getCategory().getId(), event.getCategory().getName()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                new EventShortDto.UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()),
                event.getPaid(),
                event.getTitle(),
                0L//set countConfirmedRequests in
        );
    }
}
