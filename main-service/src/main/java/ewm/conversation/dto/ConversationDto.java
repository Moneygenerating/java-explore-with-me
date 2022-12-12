package ewm.conversation.dto;

import ewm.helper.Create;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ConversationDto {
    private Long id;
    @NotNull(groups = {Create.class})
    private UserShortDto creator;
    private UserShortDto received;
    private List<MessageShortDto> messages = new ArrayList<>();
    private LocalDateTime createdOn;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserShortDto {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageShortDto {
        private Long id;
        private String message;
        private String userName;
        private LocalDateTime createdOn;
    }
}
