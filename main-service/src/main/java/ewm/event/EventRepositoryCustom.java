package ewm.event;

import ewm.event.model.Event;

import java.time.LocalDateTime;

public interface EventRepositoryCustom {
    Event getPublicEvent(String text, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd);
}
