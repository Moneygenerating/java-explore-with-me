package ewm.compilation;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.model.Compilation;
import ewm.event.model.Event;

import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto compilationToDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getEvents()
                        .stream()
                        .map(CompilationMapper::toEventShortDto)
                        .collect(Collectors.toSet()),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    private static CompilationDto.EventShortDto toEventShortDto(Event event) {
        return new CompilationDto.EventShortDto(
                event.getId(),
                event.getAnnotation(),
                new CompilationDto.EventShortDto.CategoryDto(
                        event.getCategory().getId(),
                        event.getCategory().getName()
                ),
                event.getConfirmedRequests(),
                event.getEventDate(),
                new CompilationDto.EventShortDto.UserShortDto(
                        event.getInitiator().getId(),
                        event.getInitiator().getName()
                ),
                event.getPaid(),
                event.getTitle(),
                null
        );
    }

    public static Compilation newDtoToCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(
                null,
                null, // Set events in service
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle()
        );
    }
}
