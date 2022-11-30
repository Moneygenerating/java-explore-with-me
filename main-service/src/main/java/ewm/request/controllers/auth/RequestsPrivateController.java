package ewm.request.controllers.auth;

import ewm.request.dto.RequestDto;
import ewm.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestsPrivateController {
    RequestService requestService;

    @Autowired
    public RequestsPrivateController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping()
    public List<RequestDto> getReqByUserPrivate(@PathVariable Long userId) {

        log.info("Запрос request PRIVATE Get getReqByUserPrivate /users/{userId}/requests");
        return requestService.getAllReq(userId);
    }

    @PostMapping
    public RequestDto addRequestByUserPrivate(@PathVariable Long userId, @RequestParam Long eventId) {

        log.info("Запрос request PRIVATE Post addRequestByUserPrivate/users/{userId}/requests");
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelReqByUserPrivate(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Запрос request PRIVATE Post addRequestByUserPrivate/users/{userId}/requests");

        return requestService.cancelRequest(userId, requestId);
    }

}
