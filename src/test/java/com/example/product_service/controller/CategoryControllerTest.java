package com.example.product_service.controller;

import com.example.product_service.dto.CategoryDto;
import com.example.product_service.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper; // Para serializar/deserializar JSON

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateCategory_Success() throws Exception {
        // Preparar datos
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Nueva Categoría");
        categoryDto.setDescription("Descripción de la categoría");

        CategoryDto createdCategory = new CategoryDto();
        createdCategory.setId(1L);
        createdCategory.setName("Nueva Categoría");
        createdCategory.setDescription("Descripción de la categoría");

        when(categoryService.createCategory(any(CategoryDto.class))).thenReturn(createdCategory);

        // Ejecutar y verificar
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Nueva Categoría"));

        verify(categoryService, times(1)).createCategory(any(CategoryDto.class));
    }

    @Test
    public void testCreateCategory_InvalidInput() throws Exception {
        // Preparar datos: Categoría sin nombre (violación de validación)
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("Descripción de la categoría");

        // Ejecutar y verificar
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).createCategory(any(CategoryDto.class));
    }

    @Test
    public void testGetAllCategories() throws Exception {
        // Preparar datos
        CategoryDto category1 = new CategoryDto();
        category1.setId(1L);
        category1.setName("Categoría 1");

        CategoryDto category2 = new CategoryDto();
        category2.setId(2L);
        category2.setName("Categoría 2");

        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category1, category2));

        // Ejecutar y verificar
        mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Categoría 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Categoría 2"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    public void testGetCategoryById_Success() throws Exception {
        // Preparar datos
        CategoryDto category = new CategoryDto();
        category.setId(1L);
        category.setName("Categoría 1");

        when(categoryService.getCategoryById(1L)).thenReturn(category);

        // Ejecutar y verificar
        mockMvc.perform(get("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Categoría 1"));

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    public void testGetCategoryById_NotFound() throws Exception {
        // Preparar datos
        when(categoryService.getCategoryById(1L)).thenThrow(new RuntimeException("Category with ID 1 not found"));

        // Ejecutar y verificar
        mockMvc.perform(get("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    public void testUpdateCategory_Success() throws Exception {
        // Preparar datos
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Categoría Actualizada");
        categoryDto.setDescription("Descripción Actualizada");

        CategoryDto updatedCategory = new CategoryDto();
        updatedCategory.setId(1L);
        updatedCategory.setName("Categoría Actualizada");
        updatedCategory.setDescription("Descripción Actualizada");

        when(categoryService.updateCategory(eq(1L), any(CategoryDto.class))).thenReturn(updatedCategory);

        // Ejecutar y verificar
        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Categoría Actualizada"));

        verify(categoryService, times(1)).updateCategory(eq(1L), any(CategoryDto.class));
    }

    @Test
    public void testUpdateCategory_NotFound() throws Exception {
        // Preparar datos
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Categoría Actualizada");

        when(categoryService.updateCategory(eq(1L), any(CategoryDto.class)))
                .thenThrow(new RuntimeException("Category with ID 1 not found"));

        // Ejecutar y verificar
        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).updateCategory(eq(1L), any(CategoryDto.class));
    }

    @Test
    public void testDeleteCategory_Success() throws Exception {
        // No se requiere configuración adicional ya que el método es void

        // Ejecutar y verificar
        mockMvc.perform(delete("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    public void testDeleteCategory_NotFound() throws Exception {
        // Preparar datos
        doThrow(new RuntimeException("Category with ID 1 not found")).when(categoryService).deleteCategory(1L);

        // Ejecutar y verificar
        mockMvc.perform(delete("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).deleteCategory(1L);
    }
}
