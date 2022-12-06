package ewm.conversation.dto;

import ewm.helper.Create;
import ewm.helper.Update;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewMessageDto {
    @NotNull(groups = {Create.class, Update.class})
    private String message;
}
