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
@RequestMapping(path = "/users/{userId}/events/{eventId}/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserEventRequestController {
    @Autowired
    RequestService requestService;


    @GetMapping()
    public List<RequestDto> getRequestsByUserIdAndEventPrivate(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {

        log.info("Запрос request PRIVATE Get getRequestsByUserIdAndEventPrivate /users/{userId}/events/{eventId}/requests");
        return requestService.getAllReq(userId, eventId);
    }

    @PatchMapping("/{reqId}/confirm")
    public RequestDto confirmRequest(@PathVariable Long userId,
                                       @PathVariable Long eventId,
                                       @PathVariable Long reqId) {
        log.info("Запрос request PRIVATE Patch confirmRequest /{reqId/confirm}");
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{reqId}/reject")
    public RequestDto rejectRequest(@PathVariable Long userId,
                                      @PathVariable Long eventId,
                                      @PathVariable Long reqId) {
        log.info("Запрос request PRIVATE Patch confirmRequest /{reqId/confirm}");
        return requestService.rejectRequest(userId, eventId, reqId);
    }

}
