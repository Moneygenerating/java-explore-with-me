package ewm.category.controllers;

import ewm.category.dto.CategoryDto;
import ewm.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> get(@RequestParam(value = "from", required = false, defaultValue = "0")
                                 @PositiveOrZero int from,
                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        log.info("Запрос category Get getAll /categories");
        return categoryService.get(PageRequest.of(from / size, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable Long catId) {
        log.info("Запрос category Get getById /categories/{catId}");
        return categoryService.getById(catId);
    }
}
