package ewm.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewsStatDto {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> uris;
    private Boolean unique;


    public ViewsStatDto(String start, String end, List<String> uris, Boolean unique) {
        this.start = LocalDateTime.parse(start, dateFormatter);
        this.end = LocalDateTime.parse(end, dateFormatter);
        this.uris = uris;
        this.unique = unique;
    }
}
