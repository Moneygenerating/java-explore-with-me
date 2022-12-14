package ewm.event.controllers;

import ewm.client.BaseClient;
import ewm.client.dto.HitDto;
import ewm.errors.ValidationException;
import ewm.event.dto.EventFullDto;
import ewm.event.dto.EventShortDto;
import ewm.event.model.EventsSort;
import ewm.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {

    private EventService eventService;

    private BaseClient baseClient;

    @Value("${ewmservice.app.id}")
    private String ewmAppId;

    @Autowired
    public EventController(EventService eventService, BaseClient baseClient) {
        this.eventService = eventService;
        this.baseClient = baseClient;
    }

    @GetMapping
    public List<EventShortDto> getAllEventsPublic(HttpServletRequest request,
                                                  @RequestParam(value = "from", required = false, defaultValue = "0")
                                                  @PositiveOrZero int from,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                  @RequestParam(required = false, defaultValue = "") String text,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false, defaultValue = "") String rangeStart,
                                                  @RequestParam(required = false, defaultValue = "") String rangeEnd,
                                                  @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(required = false, defaultValue = "ID") String sort) {

        log.info("???????????? getAllEventsPublic getAll /events");

        EventsSort eventSort;
        try {
            eventSort = EventsSort.valueOf(sort);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("?????????????? ???????????????????????? ???????????????????? : " + sort);
        }
        Sort pageReqParamOfSort;
        switch (eventSort) {
            case EVENT_DATE:
                pageReqParamOfSort = Sort.by(Sort.Direction.ASC, "eventDate");
                break;
            case VIEWS:
                pageReqParamOfSort = Sort.by(Sort.Direction.ASC, "views");
                break;
            default:
                pageReqParamOfSort = Sort.by(Sort.Direction.ASC, "id");
        }

        HitDto hitDto = new HitDto(
                ewmAppId,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );

        baseClient.hit(hitDto);
        log.info("???????????? getAllEventsPublic ???????????????? ??????????????  /hit ????????????????");

        return eventService.getAllPublic(PageRequest.of(from / size, size, pageReqParamOfSort),
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
    }

    @GetMapping("/{id}")
    public EventFullDto getByIdAndPublished(@PathVariable Long id, HttpServletRequest request) {
        log.info("???????????? event Get getByIdAndPublished /categories/{catId}");
        String state = "PUBLISHED";

        HitDto hitDto = new HitDto(
                ewmAppId,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );

        baseClient.hit(hitDto);
        log.info("???????????? getByIdAndPublished ???????????????? ??????????????  /hit ????????????????");
        return eventService.getById(id, state);
    }
}
