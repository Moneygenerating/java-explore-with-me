package ewm.stats;

import ewm.stats.dto.StatDto;
import ewm.stats.dto.StatHitDto;
import ewm.stats.service.StatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class StatController {

    @Autowired
    private StatService statService;

    @PostMapping("/hit")
    public void toHitEvent(@Valid @RequestBody StatHitDto statHitDto) {
        log.info("Запрос post toHitEvent /hit");
        statService.hit(statHitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getViewsStat(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statService.viewStatistics(start, end, uris, unique);
    }
}
