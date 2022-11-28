package ewm.client;

import ewm.client.dto.HitDto;

import java.util.Collection;
import java.util.Map;

public interface BaseClient {
    Map<Long, Long> getViewsForEvents(Collection<Long> eventIds);

    void hit(HitDto hitDto);
}
