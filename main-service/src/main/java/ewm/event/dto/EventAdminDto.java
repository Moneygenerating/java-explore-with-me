package ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.event.dto.validationAdmin.EventTimeValidation;
import ewm.helper.Create;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EventTimeValidation(start = "start", end = "eventDate", typeValidate = "Admin", groups = {Create.class})//set Admin
public class EventAdminDto {
    @NotNull(groups = {Create.class})
    private String annotation;
    @NotNull(groups = {Create.class})
    private Long category;
    @NotNull(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private String title;
    @NotNull
    private LocationDto location;
    private Boolean requestModeration = true;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationDto {
        Float lat;
        Float lon;
    }
}
