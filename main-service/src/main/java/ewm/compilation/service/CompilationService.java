package ewm.compilation.service;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompilationService {
    //Получение подборок событий public
    List<CompilationDto> getAll(Pageable pageable, Boolean pinned);

    //Получение подборки по его id public
    CompilationDto getById(Long compId);

    //добавление подборки Admin
    CompilationDto createComp(NewCompilationDto newCompilationDto);

    //удаление подборки по id Admin
    void deleteCompById(Long compId);

    //удаление события в подборке Admin
    void deleteEventInCompById(Long compId, Long eventId);

    //добавить событие в подборку Admin
    void addEventInCompById(Long compId, Long eventId);

    //удалить подборку из топа
    void deleteCompPin(Long compId);

    //добавить подборку в топ
    void addCompPin(Long compId);
}
