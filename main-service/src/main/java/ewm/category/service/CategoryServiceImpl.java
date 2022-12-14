package ewm.category.service;

import ewm.category.CategoryMapper;
import ewm.category.CategoryRepository;
import ewm.category.dto.CategoryDto;
import ewm.category.model.Category;
import ewm.errors.ConflictErrorException;
import ewm.errors.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> get(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        return CategoryMapper.toCategoryDto(categoryRepository.getReferenceById(id));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public CategoryDto update(CategoryDto categoryDto) {
        CategoryDto catCheck = getById(categoryDto.getId());
        if (catCheck != null) {
            if (validateCategory(categoryDto)) {
                catCheck.setName(categoryDto.getName());
            }

            Category category = CategoryMapper.toCategory(catCheck);
            category.setId(catCheck.getId());
            categoryRepository.save(category);
        }
        return catCheck;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void delete(Long catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public CategoryDto create(CategoryDto categoryDto) {
        if (validateCategory(categoryDto)) {
            Category category = CategoryMapper.toCategory(categoryDto);
            return CategoryMapper.toCategoryDto(categoryRepository.save(category));
        }
        return null;
    }

    private boolean validateCategory(CategoryDto categoryDto) {
        if (categoryDto.getName() == null || categoryDto.getName().isBlank() || categoryDto.getName().isEmpty()) {
            throw new ValidationException("?????????????????????? ???????????????? ??????????????????.");
        }

        if (categoryRepository.findAll().stream().map(Category::getName)
                .collect(Collectors.toList()).contains(categoryDto.getName())) {
            throw new ConflictErrorException("?????????? ?????????????????? ?????? ????????, ?????????????? ????????????");
        }
        return true;
    }
}

