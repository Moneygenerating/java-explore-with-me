package ewm.category.service;

import ewm.category.dto.CategoryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> get(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto update(CategoryDto categoryDto);

    void delete(Long id);

    CategoryDto create(CategoryDto categoryDto);
}
