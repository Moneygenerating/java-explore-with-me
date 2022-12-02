package ewm.user.dto;

import ewm.helper.Create;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequestDto {
    private Long id;
    //имя или логин пользователя
    @NotNull(groups = {Create.class})
    private String name;
}
