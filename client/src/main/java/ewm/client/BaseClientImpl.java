package ewm.client;

import ewm.client.dto.HitDto;
import ewm.client.dto.StatDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BaseClientImpl implements BaseClient {
    private static final int rangeDays = 30;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final FeignClientStat feignClientStat;

    @Autowired
    public BaseClientImpl(FeignClientStat feignClientStat) {
        this.feignClientStat = feignClientStat;
    }

    @Override
    public Map<Long, Long> getViewsForEvents(Collection<Long> eventIds) {

        final String rangeStart = URLEncoder.encode(LocalDateTime.now().minusDays(rangeDays).format(dateTimeFormatter),
                StandardCharsets.UTF_8);
        final String rangeEnd = URLEncoder.encode(LocalDateTime.now().format(dateTimeFormatter), StandardCharsets.UTF_8);
        final List<String> uris = eventIds.stream().map(id -> "/events/" + id).collect(Collectors.toList());

        final List<StatDto> stats = feignClientStat.getStats(rangeStart, rangeEnd, uris, true);
        final Map<Long, Long> allStateMap = new HashMap<>();

        stats.forEach(stat -> allStateMap.put(getIdFromUri(stat.getUri()), stat.getHits()));
        return allStateMap;
    }

    private Long getIdFromUri(String uri) {
        String[] split = uri.split("/");
        return Long.parseLong(split[split.length - 1]);
    }

    @Override
    public void hit(HitDto hitDto) {
        feignClientStat.hit(hitDto);
    }
}

