package ewm.stats;

import ewm.client.dto.HitDto;
import ewm.stats.model.Stat;

import java.net.URISyntaxException;

public class StatMapper {
    public static Stat statsHitDtoToStats(HitDto hitDto)  throws URISyntaxException {
        return new Stat(
                null,
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getTimestamp(),
                hitDto.getIp()
        );
    }
}
