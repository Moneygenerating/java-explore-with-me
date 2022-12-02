package ewm.compilation.dto;

import ewm.helper.Create;
import ewm.helper.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private Set<EventShortDto> events = new HashSet<>();
    private Boolean pinned;
    private String title;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventShortDto {

        private Long id;
        @NotBlank(groups = {Create.class, Update.class})
        private String annotation;
        @NotNull(groups = {Create.class, Update.class})
        private CategoryDto category;
        private Integer confirmedRequests;
        @NotNull(groups = {Create.class, Update.class})
        private LocalDateTime eventDate;
        @NotNull(groups = {Create.class, Update.class})
        private UserShortDto initiator;
        @NotNull(groups = {Create.class, Update.class})
        private Boolean paid;
        @NotNull(groups = {Create.class, Update.class})
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
}
