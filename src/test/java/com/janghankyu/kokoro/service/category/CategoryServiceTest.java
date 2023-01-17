package com.janghankyu.kokoro.service.category;

import com.janghankyu.kokoro.dto.category.CategoryCreateRequest;
import com.janghankyu.kokoro.dto.category.CategoryDto;
import com.janghankyu.kokoro.exception.CategoryNotFoundException;
import com.janghankyu.kokoro.repository.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.janghankyu.kokoro.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static com.janghankyu.kokoro.factory.entity.CategoryFactory.createCategory;
import static com.janghankyu.kokoro.factory.entity.CategoryFactory.createCategoryWithName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks CategoryService categoryService;
    @Mock CategoryRepository categoryRepository;

    @Test
    void readAllTest() {
        // given
        given(categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc())
                .willReturn(
                        List.of(createCategoryWithName("name1"),
                                createCategoryWithName("name2")
                        )
                );

        // when
        List<CategoryDto> result = categoryService.readAll();

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("name1");
        assertThat(result.get(1).getName()).isEqualTo("name2");
    }

    @Test
    void createTest() {
        // given
        CategoryCreateRequest req = createCategoryCreateRequest();

        // when
        categoryService.create(req);

        // then
        verify(categoryRepository).save(any());
    }

    @Test
    void deleteTest() {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));

        // when
        categoryService.delete(1L);

        // then
        verify(categoryRepository).delete(any());
    }

    @Test
    void deleteExceptionByCategoryNotFoundTest() {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> categoryService.delete(1L)).isInstanceOf(CategoryNotFoundException.class);
    }
}