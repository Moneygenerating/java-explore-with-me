package ewm.event.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddEventDto extends AbstractEventEditDto {
    @NotNull
    private Location location;
    private Boolean requestModeration = true;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private Float lat; //Широта
        private Float lon; //Долгота
    }

}
