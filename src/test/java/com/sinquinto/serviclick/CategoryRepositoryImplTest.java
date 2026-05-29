package com.sinquinto.serviclick;

import com.sinquinto.serviclick.Category.Domain.Category;
import com.sinquinto.serviclick.Category.Infrastructure.Entity.CategoryEntity;
import com.sinquinto.serviclick.Category.Infrastructure.Mapper.CategoryMapper;
import com.sinquinto.serviclick.Category.Infrastructure.Mapper.CategoryMapperImpl;
import com.sinquinto.serviclick.Category.Infrastructure.Repository.CategoryJpaRepository;
import com.sinquinto.serviclick.Category.Infrastructure.Repository.CategoryRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryImplTest {

    @Mock
    private CategoryJpaRepository jpaRepository;

    private CategoryMapper mapper;
    private CategoryRepositoryImpl repository;

    private CategoryEntity entity;
    private Category domain;

    @BeforeEach
    void setUp() {
        mapper = new CategoryMapperImpl();
        repository = new CategoryRepositoryImpl(jpaRepository, mapper);

        entity = CategoryEntity.builder()
                .categoryId(1L)
                .name("Hogar")
                .build();

        domain = Category.builder()
                .categoryId(1L)
                .name("Hogar")
                .build();
    }

    @Test
    @DisplayName("save debe convertir, guardar y retornar dominio")
    void save_shouldConvertAndSave() {
        when(jpaRepository.save(any())).thenReturn(entity);

        Category result = repository.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
        assertEquals("Hogar", result.getName());
        verify(jpaRepository).save(any());
    }

    @Test
    @DisplayName("findById con ID existente debe retornar dominio")
    void findById_whenExists_shouldReturn() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Category result = repository.findById(1L);

        assertNotNull(result);
        assertEquals("Hogar", result.getName());
    }

    @Test
    @DisplayName("findById con ID inexistente debe retornar null")
    void findById_whenNotFound_shouldReturnNull() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        Category result = repository.findById(99L);

        assertNull(result);
    }

    @Test
    @DisplayName("findAll debe retornar lista de dominios mapeada")
    void findAll_shouldReturnMappedList() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity));

        List<Category> result = repository.findAll();

        assertEquals(1, result.size());
        assertEquals("Hogar", result.get(0).getName());
    }

    @Test
    @DisplayName("deleteById debe delegar al jpaRepository")
    void deleteById_shouldDelegate() {
        repository.deleteById(1L);
        verify(jpaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("countCategories debe retornar el count del jpaRepository")
    void countCategories_shouldReturnCount() {
        when(jpaRepository.count()).thenReturn(4L);

        assertEquals(4L, repository.countCategories());
    }
}