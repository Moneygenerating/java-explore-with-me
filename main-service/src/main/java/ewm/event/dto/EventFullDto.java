package ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.event.model.StateLifecycle;
import ewm.helper.Create;
import ewm.helper.Patch;
import ewm.helper.Update;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EventTimeValidation(start = "createdOn", end = "eventDate", groups = {Create.class, Update.class})
public class EventFullDto {
    @NotNull(groups = {Patch.class})
    private Long id;
    @NotNull(groups = {Patch.class})
    private String annotation;
    @NotNull(groups = {Create.class, Patch.class})
    private CategoryDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @NotNull(groups = {Create.class, Patch.class})
    private String description;
    @NotNull(groups = {Create.class, Patch.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    @NotNull(groups = {Create.class})
    private LocationDto location;
    @NotNull(groups = {Create.class})
    private Boolean paid;
    @NotNull(groups = {Create.class})
    private Integer participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    @NotNull(groups = {Create.class})
    private Boolean requestModeration;
    private StateLifecycle state;
    @NotNull(groups = {Create.class})
    private String title;
    private Long views;//service stats
    private Integer confirmedRequests;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserShortDto {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class LocationDto {
        private Float lat;
        private Float lon;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class CategoryDto {
        private Long id;
        private String name;
    }
}
