package ewm.compilation.dto;

import ewm.helper.Create;
import ewm.helper.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private Set<Long> events;
    private Boolean pinned = false;
    @NotBlank(groups = {Create.class, Update.class})
    private String title;
}
