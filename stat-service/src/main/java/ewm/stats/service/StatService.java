package ewm.stats.service;

import ewm.client.dto.HitDto;
import ewm.stats.dto.StatDto;

import java.util.List;

public interface StatService {
    //Сохранение запроса
    void hit(HitDto hitDto);

    //Получение статистики посещений
    List<StatDto> viewStatistics(String start, String end, List<String> uris, Boolean distinct);
}
