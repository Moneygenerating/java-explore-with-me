package ewm.category.controllers.admin;

import ewm.category.dto.CategoryDto;
import ewm.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdmCategoryController {

    @Autowired
    private final CategoryService categoryService;

    @PatchMapping()
    public CategoryDto update(@RequestBody CategoryDto categoryDto) {
        log.info("Запрос user Update /admin/categories");
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Long catId) {
        log.info("Запрос cat Delete  /admin/categories");
        categoryService.delete(catId);
    }

    @PostMapping
    public CategoryDto create(@RequestBody CategoryDto categoryDto) {
        log.info("Запрос user Post createNewUser /admin/users");
        return categoryService.create(categoryDto);
    }

}
