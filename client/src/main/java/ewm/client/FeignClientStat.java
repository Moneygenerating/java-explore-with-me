package ewm.client;

import ewm.client.dto.HitDto;
import ewm.client.dto.StatDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@PropertySource(value = "/feign.properties")
@FeignClient(value = "feignClientStat", url = "${ewm.feign-client.url}")
public interface FeignClientStat {


    @RequestMapping(method = RequestMethod.GET, value = "/stats")
    List<StatDto> getStats(@RequestParam String start,
                           @RequestParam String end,
                           @RequestParam(required = false) List<String> uris,
                           @RequestParam(defaultValue = "false") Boolean unique);

    @RequestMapping(method = RequestMethod.POST, value = "/hit")
    void hit(@RequestBody HitDto hitDto);
}
