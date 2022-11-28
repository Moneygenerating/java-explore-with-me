package ewm.stats.service;

import ewm.client.dto.HitDto;
import ewm.stats.StatMapper;
import ewm.stats.StatRepository;
import ewm.stats.dto.StatDto;
import ewm.stats.model.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {

    @Autowired
    private StatRepository statRepository;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void hit(HitDto hitDto) {
        try {
            Stat stat = StatMapper.statsHitDtoToStats(hitDto);
            statRepository.save(stat);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<StatDto> viewStatistics(String start, String end, List<String> uris, Boolean distinct) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startPoint = LocalDateTime.parse(start, formatter);
        LocalDateTime endPoint = LocalDateTime.parse(end, formatter);
        if (distinct) {
            return statRepository.countIpDistinct(startPoint, endPoint, uris);
        } else {
            return statRepository.countIp(startPoint, endPoint, uris);
        }
    }
}
