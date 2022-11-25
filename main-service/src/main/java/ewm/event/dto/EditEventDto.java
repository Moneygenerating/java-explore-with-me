package ewm.event.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EditEventDto extends AbstractEventEditDto {

    private Long eventId;
}
