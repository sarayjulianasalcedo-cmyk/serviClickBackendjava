package com.sinquinto.serviclick;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinquinto.serviclick.Category.Application.CategoryService;
import com.sinquinto.serviclick.Category.Domain.Category;
import com.sinquinto.serviclick.Category.Infrastructure.CategoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CategoryService service;

    @InjectMocks
    private CategoryController controller;

    private Category category;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        category = Category.builder().categoryId(1L).name("Hogar").build();
    }

    @Test
    @DisplayName("POST /api/categories debe crear y retornar 201")
    void save_shouldReturn201() throws Exception {
        when(service.saveCategory(any())).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated());

        verify(service).saveCategory(any());
    }

    @Test
    @DisplayName("GET /api/categories debe retornar lista 200 OK")
    void getAll_shouldReturn200() throws Exception {
        when(service.findAllCategories()).thenReturn(List.of(category));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());

        verify(service).findAllCategories();
    }

    @Test
    @DisplayName("GET /api/categories/{id} debe retornar categoría 200 OK")
    void findById_shouldReturn200() throws Exception {
        when(service.findCategoryById(1L)).thenReturn(category);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk());

        verify(service).findCategoryById(1L);
    }

    @Test
    @DisplayName("GET /api/categories/count debe retornar conteo 200 OK")
    void count_shouldReturn200() throws Exception {
        when(service.countCategories()).thenReturn(5L);

        mockMvc.perform(get("/api/categories/count"))
                .andExpect(status().isOk());

        verify(service).countCategories();
    }

    @Test
    @DisplayName("PUT /api/categories/{id} debe actualizar y retornar 200 OK")
    void update_shouldReturn200() throws Exception {
        when(service.updateCategory(anyLong(), any())).thenReturn(category);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk());

        verify(service).updateCategory(anyLong(), any());
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} debe eliminar 200 OK")
    void delete_shouldReturn200() throws Exception {
        doNothing().when(service).deleteCategoryById(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk());

        verify(service).deleteCategoryById(1L);
    }
}