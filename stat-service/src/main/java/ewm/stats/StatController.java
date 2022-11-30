package ewm.stats;

import ewm.client.dto.HitDto;
import ewm.stats.dto.StatDto;
import ewm.stats.service.StatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@Slf4j
public class StatController {

    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/hit")
    public void toHitEvent(@Valid @RequestBody HitDto hitDto) {
        log.info("Запрос post toHitEvent /hit");
        statService.hit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getViewsStat(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        String startDecoded = URLDecoder.decode(start, StandardCharsets.UTF_8);
        String endDecoded = URLDecoder.decode(end, StandardCharsets.UTF_8);
        return statService.viewStatistics(startDecoded, endDecoded, uris, unique);
    }
}
