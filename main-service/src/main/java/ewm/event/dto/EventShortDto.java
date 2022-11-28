package ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.event.dto.validation.EventTimeValidation;
import ewm.helper.Create;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EventTimeValidation(start = "createdOn", end = "eventDate",typeValidate = "User", groups = {Create.class})
public class EventShortDto {
    private Long id;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Integer confirmedRequests;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Boolean paid;
    @NotNull
    private String title;

    private Long views;

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
    public static class CategoryDto {
        private Long id;
        private String name;
    }

}
