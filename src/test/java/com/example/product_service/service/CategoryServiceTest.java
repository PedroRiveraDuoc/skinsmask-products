package com.example.product_service.service;

import com.example.product_service.dto.CategoryDto;
import com.example.product_service.model.Category;
import com.example.product_service.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
    MockitoAnnotations.openMocks(this);
    categoryService = new CategoryService(categoryRepository);
}

    @Test
    public void testGetAllCategories() {
        // Preparar datos
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Categoría 1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Categoría 2");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // Ejecutar método
        List<CategoryDto> result = categoryService.getAllCategories();

        // Verificar resultados
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetCategoryById_Success() {
        // Preparar datos
        Category category = new Category();
        category.setId(1L);
        category.setName("Categoría 1");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Ejecutar método
        CategoryDto result = categoryService.getCategoryById(1L);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Categoría 1", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCategoryById_NotFound() {
        // Preparar datos
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        assertEquals("Category with ID 1 not found", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateCategory_Success() {
        // Preparar datos
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Nueva Categoría");
        categoryDto.setDescription("Descripción");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Nueva Categoría");
        savedCategory.setDescription("Descripción");

        when(categoryRepository.existsByName("Nueva Categoría")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // Ejecutar método
        CategoryDto result = categoryService.createCategory(categoryDto);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nueva Categoría", result.getName());
        verify(categoryRepository, times(1)).existsByName("Nueva Categoría");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testCreateCategory_AlreadyExists() {
        // Preparar datos
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Categoría Existente");

        when(categoryRepository.existsByName("Categoría Existente")).thenReturn(true);

        // Ejecutar y verificar excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.createCategory(categoryDto);
        });

        assertEquals("Category with name 'Categoría Existente' already exists", exception.getMessage());
        verify(categoryRepository, times(1)).existsByName("Categoría Existente");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testUpdateCategory_Success() {
        // Preparar datos
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Categoría Actualizada");
        categoryDto.setDescription("Descripción Actualizada");

        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("Categoría Antigua");
        existingCategory.setDescription("Descripción Antigua");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Categoría Actualizada");
        updatedCategory.setDescription("Descripción Actualizada");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);

        // Ejecutar método
        CategoryDto result = categoryService.updateCategory(1L, categoryDto);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Categoría Actualizada", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    public void testUpdateCategory_NotFound() {
        // Preparar datos
        CategoryDto categoryDto = new CategoryDto();

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.updateCategory(1L, categoryDto);
        });

        assertEquals("Category with ID 1 not found", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testDeleteCategory_Success() {
        // Preparar datos
        when(categoryRepository.existsById(1L)).thenReturn(true);

        // Ejecutar método
        categoryService.deleteCategory(1L);

        // Verificar resultados
        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteCategory_NotFound() {
        // Preparar datos
        when(categoryRepository.existsById(1L)).thenReturn(false);

        // Ejecutar y verificar excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        assertEquals("Category with ID 1 not found", exception.getMessage());
        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}
