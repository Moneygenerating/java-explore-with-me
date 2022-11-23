package ewm.event.controllers;


import ewm.event.dto.EventFullDto;
import ewm.event.model.StateLifecycle;
import ewm.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {
    @Autowired
    EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllEventsPublic(@RequestParam(value = "from", required = false, defaultValue = "0")
                                                 @PositiveOrZero int from,
                                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                 @RequestParam("text") String text,
                                                 @RequestParam("categories") List<Long> categories,
                                                 @RequestParam("paid") Boolean paid,
                                                 @RequestParam("rangeStart") String rangeStart,
                                                 @RequestParam("rangeEnd") String rangeEnd,
                                                 @RequestParam("onlyAvailable") Boolean onlyAvailable,
                                                 @RequestParam("sort") String sort) {
        log.info("Запрос getAllEventsPublic getAll /events");
        return eventService.getAllPublic(PageRequest.of(from / size, size), text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort);
    }

    @GetMapping("/{id}")
    public EventFullDto getByIdAndPublished(@PathVariable Long id) {
        log.info("Запрос event Get getByIdAndPublished /categories/{catId}");
        String state = "PUBLISHED";
        return eventService.getById(id, state);
    }
}
