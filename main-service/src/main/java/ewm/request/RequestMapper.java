package ewm.request;

import ewm.request.dto.RequestDto;
import ewm.request.model.ParticipationRequest;

public class RequestMapper {

    public static RequestDto requestToDto(ParticipationRequest participationRequest) {
        return new RequestDto(participationRequest.getId(),
                participationRequest.getEvent().getId(),
                participationRequest.getCreated(),
                participationRequest.getEvent().getId(),
                participationRequest.getStatus());
    }
}
