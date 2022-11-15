package ewm.category.dto;

import ewm.helper.Create;
import ewm.helper.Update;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotNull(groups = {Create.class, Update.class})
    private String name;
}
