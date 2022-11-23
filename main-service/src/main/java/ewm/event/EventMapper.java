package ewm.event;

import ewm.category.model.Category;
import ewm.event.dto.EventFullDto;
import ewm.event.model.Event;
import ewm.event.model.Location;
import ewm.user.model.User;

public class EventMapper {
    public static EventFullDto eventToDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                new EventFullDto.CategoryDto(event.getCategory().getId(),
                        event.getCategory().getName()),
                event.getCreatedOn(), //set event.getCreatedOn()
                event.getDescription(),
                event.getEventDate(),
                null,
                null,
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                null, // set in service from requests
                null  //set countConfirmedRequests in
        );
    }

    public static Event dtoToEvent(EventFullDto eventFullDto) {
        Event event = new Event(
                eventFullDto.getEventId(),
                eventFullDto.getAnnotation(),
                new Category(
                        eventFullDto.getCategory().getId(),
                        eventFullDto.getCategory().getName()
                ),
                eventFullDto.getCreatedOn(),
                eventFullDto.getDescription(),
                eventFullDto.getEventDate(),
                null, //set user by evenFullDto
                eventFullDto.getLocation().getLon(),
                eventFullDto.getLocation().getLat(),
                eventFullDto.getPaid(),
                eventFullDto.getParticipantLimit(),
                eventFullDto.getPublishedOn(),
                eventFullDto.getCurrentParticipants(),
                eventFullDto.getRequestModeration(),
                eventFullDto.getState(),
                eventFullDto.getTitle()
        );
        return event;
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

}
