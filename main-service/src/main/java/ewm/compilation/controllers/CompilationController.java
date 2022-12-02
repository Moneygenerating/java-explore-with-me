package ewm.compilation.controllers;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.service.CompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@Validated
public class CompilationController {

    CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getAllPublic(@RequestParam(required = false) Boolean pinned,
                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("Запрос getAllPublic /compilations");

        return compilationService.getAll((PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, "id"))), pinned);
    }

    @GetMapping("/{compId}")
    public CompilationDto getByIdPublic(@PathVariable Long compId) {

        log.info("Запрос getByIdPublic /compilations/{compId}");
        return compilationService.getById(compId);
    }
}
