package ewm.event.dto;

import ewm.event.dto.validation.EventTimeValidation;
import ewm.helper.Create;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EventTimeValidation(start = "start", end = "eventDate", typeValidate = "Admin", groups = {Create.class})//set Admin
public class EventAdminDto extends AbstractEventEditDto {
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
