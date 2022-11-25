package ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.helper.Create;
import ewm.helper.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEventEditDto {
    @NotBlank(groups = {Create.class, Update.class})
    protected String annotation;
    @NotNull(groups = {Create.class, Update.class})
    protected Long category;
    @NotBlank(groups = {Create.class, Update.class})
    protected String description;
    @NotNull(groups = {Create.class, Update.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime eventDate;
    protected Boolean paid = false;
    protected Integer participantLimit = 0;
    protected String title;
}
