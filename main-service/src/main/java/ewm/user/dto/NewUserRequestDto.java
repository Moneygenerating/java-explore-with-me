package ewm.user.dto;

import ewm.helper.Create;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequestDto {
    private Long id;
    //имя или логин пользователя
    @NotNull(groups = {Create.class})
    private String name;
}
