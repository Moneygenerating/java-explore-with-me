package ewm.stats.service;

import ewm.stats.dto.StatDto;
import ewm.stats.dto.StatHitDto;

import java.util.List;

public interface StatService {
    //Сохранение запроса
    void hit(StatHitDto statHitDto);

    //Получение статистики посещений
    List<StatDto> viewStatistics(String start, String end, List<String> uris, Boolean distinct);
}
