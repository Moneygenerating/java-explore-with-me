package ewm.request.service;

import ewm.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> getAllReq(Long userId);

    List<RequestDto> getAllReq(Long userId, Long eventId);

    RequestDto confirmRequest(Long userId, Long eventId, Long reqId);

    RequestDto rejectRequest(Long userId, Long eventId, Long reqId);

    RequestDto addRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);
}
