package ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.event.model.StateLifecycle;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto {

    private Long id;
    private Long eventId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private Long requester;
    private StateLifecycle status;
}
