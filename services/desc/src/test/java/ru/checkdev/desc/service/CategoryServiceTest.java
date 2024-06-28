package ru.checkdev.desc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.checkdev.desc.domain.Category;
import ru.checkdev.desc.dto.CategoryDTO;
import ru.checkdev.desc.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private static final int CATEGORY_ID = 1;
    @Mock
    public CategoryRepository categoryRepository;
    @InjectMocks
    public CategoryService categoryService;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(CATEGORY_ID);
        categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
    }


    @Test
    void whenFindByIdAndIdNotExistThenReturnEmptyOptional() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThat(categoryService.findById(CATEGORY_ID)).isEmpty();
    }

    @Test
    void whenFindByIdAndThenReturnOptionalOfCategory() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        assertThat(categoryService.findById(CATEGORY_ID)).isEqualTo(Optional.of(category));
    }

    @Test
    void whenCreateThenReturnCategory() {
        when(categoryRepository.save(any())).thenReturn(category);
        assertThat(categoryService.create(category)).isEqualTo(category);
    }

    @Test
    void whenGetMostPopularThenReturnListOfCategories() {
        when(categoryRepository.findAllByOrderTotalDescLimit(any())).thenReturn(List.of(category));
        assertThat(categoryService.getMostPopular()).isEqualTo(List.of(category));
    }
}