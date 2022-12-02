package ewm.user.dto;

import ewm.helper.Create;
import ewm.helper.Update;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    //уникальный идентификатор пользователя
    private Long id;
    //имя или логин пользователя
    @NotEmpty(groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class, Update.class})
    private String name;
    @NotEmpty(groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class, Update.class})
    @Email(groups = {Create.class, Update.class})
    private String email;

}
