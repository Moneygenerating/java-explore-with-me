package ewm.compilation.controllers.admin;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.service.CompilationService;
import ewm.helper.Create;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationController {

    @Autowired
    CompilationService compilationService;

    @PostMapping
    public CompilationDto postCompilationAdmin(@Validated(Create.class) @RequestBody NewCompilationDto newCompilationDto) {

        log.info("Запрос postCompilationAdmin /admin/compilations");
        return compilationService.createComp(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompByIdAdmin(@PathVariable Long compId) {

        log.info("Запрос deleteCompByIdAdmin /admin/compilations/{compId}");
        compilationService.deleteCompById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventInCompByIdAdmin(@PathVariable Long compId, @PathVariable Long eventId) {

        log.info("Запрос deleteEventInCompByIdAdmin /admin/compilations/{compId}/events/{eventId}");
        compilationService.deleteEventInCompById(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventInCompByIdAdmin(@PathVariable Long compId, @PathVariable Long eventId) {

        log.info("Запрос addEventInCompByIdAdmin /admin/compilations/{compId}/events/{eventId}");
        compilationService.addEventInCompById(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deleteCompilationInPinAdmin(@PathVariable Long compId) {
        log.info("Запрос deleteCompilationInPinAdmin /admin/compilations/{compId}/pin");
        compilationService.deleteCompPin(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void addCompilationInPinAdmin(@PathVariable Long compId) {
        log.info("Запрос addCompilationInPinAdmin /admin/compilations/{compId}/pin");
        compilationService.addCompPin(compId);
    }

}
