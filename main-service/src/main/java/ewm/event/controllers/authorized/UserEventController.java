package ewm.event.controllers.authorized;

import ewm.event.dto.AddEventDto;
import ewm.event.dto.EditEventDto;
import ewm.event.dto.EventFullDto;
import ewm.event.service.EventService;
import ewm.helper.Create;
import ewm.helper.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserEventController {
    @Autowired
    EventService eventService;

    @GetMapping()
    public List<EventFullDto> getByIdAndPrivate(@PathVariable Long userId,
                                                @RequestParam(value = "from", required = false, defaultValue = "0")
                                                @PositiveOrZero int from,
                                                @RequestParam(value = "size", required = false,
                                                        defaultValue = "10") int size) {

        log.info("Запрос event PRIVATE Get getByIdAndPrivate /users/{userId}/events");
        return eventService.getAllEventByIdPrivate(userId, PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id")));
    }

    @PatchMapping()
    public EventFullDto updateEventPrivate(@Validated({Update.class}) @RequestBody EditEventDto editEventDto,
                                           @PathVariable Long userId) {
        log.info("Запрос event PRIVATE patch updateEventPrivate /users/{userId}/events");
        return eventService.pathEventByIdPrivate(userId, editEventDto);
    }


    @PostMapping()
    public EventFullDto saveEventPrivate(@Validated({Create.class}) @RequestBody AddEventDto addEventDto,
                                         @PathVariable Long userId) {
        log.info("Запрос event PRIVATE Post saveEventPrivate /users/{userId}/events");
        return eventService.addEventPrivate(userId, addEventDto);
    }

    @GetMapping({"/{eventId}"})
    public EventFullDto getByIdAndUserIdPrivate(@PathVariable Long userId,
                                                @PathVariable Long eventId) {

        log.info("Запрос event PRIVATE Get getByIdAndUserIdPrivate /users/{userId}/events/{eventId}");
        return eventService.getEventFullPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEventPrivate(@PathVariable Long userId,
                                           @PathVariable Long eventId) {
        log.info("Запрос event PRIVATE patch updateEventPrivate /users/{userId}/events");
        return eventService.pathEventCansel(userId, eventId);
    }
}
