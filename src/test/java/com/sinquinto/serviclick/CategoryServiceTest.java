package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Category.Application.CategoryService;
import com.sinquinto.serviclick.Category.Domain.Category;
import com.sinquinto.serviclick.Category.Domain.CategoryRepository;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .categoryId(1L)
                .name("Hogar")
                .build();
    }

    @Test
    @DisplayName("saveCategory debe guardar y retornar la categoría")
    void saveCategory_shouldSaveAndReturn() {
        when(repository.save(any())).thenReturn(category);

        Category result = service.saveCategory(category);

        assertNotNull(result);
        assertEquals("Hogar", result.getName());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("findCategoryById con ID existente debe retornar la categoría")
    void findCategoryById_whenExists_shouldReturn() {
        when(repository.findById(1L)).thenReturn(category);

        Category result = service.findCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
    }

    @Test
    @DisplayName("findCategoryById con ID inexistente debe lanzar ResourceNotFoundException")
    void findCategoryById_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.findCategoryById(99L));
    }

    @Test
    @DisplayName("findAllCategories debe retornar lista completa")
    void findAllCategories_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(category));

        List<Category> result = service.findAllCategories();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("updateCategory con ID existente debe actualizar el nombre y retornar")
    void updateCategory_whenExists_shouldUpdateAndReturn() {
        Category updateData = Category.builder().name("Tecnología").build();
        when(repository.findById(1L)).thenReturn(category);
        when(repository.save(any())).thenReturn(category);

        Category result = service.updateCategory(1L, updateData);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("updateCategory con ID inexistente debe lanzar excepción")
    void updateCategory_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateCategory(99L, category));
    }

    @Test
    @DisplayName("deleteCategoryById con ID existente debe eliminar")
    void deleteCategoryById_whenExists_shouldDelete() {
        when(repository.findById(1L)).thenReturn(category);

        service.deleteCategoryById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteCategoryById con ID inexistente debe lanzar excepción")
    void deleteCategoryById_whenNotFound_shouldThrow() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCategoryById(99L));
    }

    @Test
    @DisplayName("countCategories debe retornar el conteo del repositorio")
    void countCategories_shouldReturnCount() {
        when(repository.countCategories()).thenReturn(8L);

        assertEquals(8L, service.countCategories());
    }
}