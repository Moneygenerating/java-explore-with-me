package ewm.event.controllers.admin;

import ewm.event.dto.EventAdminDto;
import ewm.event.dto.EventFullDto;
import ewm.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {
    @Autowired
    EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllEventsAdmin(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false, defaultValue = "") String rangeStart,
                                                @RequestParam(required = false, defaultValue = "") String rangeEnd,
                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("Запрос getAllEventsAdmin /admin/events");

        return eventService.getAllAdmin((PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, "id"))), users, states, categories, rangeStart, rangeEnd);
    }

    @PostMapping("/{eventId}")
    public EventFullDto editEventAdmin(@PathVariable Long eventId, @RequestBody @Validated EventAdminDto eventAdminDto) {

        log.info("Запрос Post editEventAdmin /admin/events/{eventId}");

        return eventService.saveAdmin(eventAdminDto, eventId);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishingEventAdmin(@PathVariable Long eventId) {

        log.info("Запрос PATCH publishingEventAdmin /admin/events/{eventId}/publish");

        return eventService.publishEventAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectingEventAdmin(@PathVariable Long eventId) {

        log.info("Запрос PATCH rejectingEventAdmin /admin/events/{eventId}/reject");

        return eventService.rejectEventAdmin(eventId);
    }
}
