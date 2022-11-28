package ewm.stats;

import ewm.stats.dto.StatHitDto;
import ewm.stats.model.Stat;

import java.net.URISyntaxException;

public class StatMapper {
    public static Stat statsHitDtoToStats(StatHitDto statHitDto)  throws URISyntaxException {
        return new Stat(
                null,
                statHitDto.getApp(),
                statHitDto.getUri(),
                statHitDto.getTimestamp(),
                statHitDto.getIp()
        );
    }
}
