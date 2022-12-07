package ewm.conversation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageChatDto {
    private Long id;
    private String message;
    private String userName;
    private LocalDateTime createdOn;
}
