package ewm.errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Errors {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;

    public static Errors makeRequest(List<String> errors, String reason, String message, String goStatus) {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        return Errors.builder()
                .reason(reason)
                .errors(errors)
                .message(message)
                .status(goStatus)
                .timestamp(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(timestamp))
                .build();
    }
}
