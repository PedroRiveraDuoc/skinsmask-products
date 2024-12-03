// ProductServiceTest.java
package com.example.product_service.service;

import com.example.product_service.dto.ProductDto;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.model.Category;
import com.example.product_service.model.Product;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
public void setUp() {
    MockitoAnnotations.openMocks(this);
    productService = new ProductService(productRepository, categoryRepository);
}

    @Test
    void testGetAllProducts() {
        // Crear categorías y productos mock
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category1");

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");
        product1.setPrice(new BigDecimal("10.0"));
        product1.setCategory(category1);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product2");
        product2.setPrice(new BigDecimal("20.0"));
        product2.setCategory(category1);

        // Mockear la lista de productos
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // Llamar al método de servicio
        List<ProductDto> products = productService.getAllProducts();

        // Validaciones
        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
void testGetProductById_Success() {
    // Crear un objeto Category mock
    Category mockCategory = new Category();
    mockCategory.setId(1L);
    mockCategory.setName("Category1");

    // Crear un objeto Product mock con la categoría asignada
    Product mockProduct = new Product();
    mockProduct.setId(1L);
    mockProduct.setName("Product1");
    mockProduct.setDescription("Description1");
    mockProduct.setPrice(new BigDecimal("10.0")); // BigDecimal usado para el precio
    mockProduct.setCategory(mockCategory); // Asignar la categoría

    // Mockear la llamada al repositorio
    when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

    // Llamada al método de servicio
    ProductDto product = productService.getProductById(1L);

    // Validaciones
    assertNotNull(product);
    assertEquals("Product1", product.getName());
    assertEquals(new BigDecimal("10.0"), product.getPrice()); // Comparación con BigDecimal

    // Verificar interacción con el mock
    verify(productRepository, times(1)).findById(1L);
}

    @Test
    public void testGetProductById_NotFound() {
        // Preparar datos
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals("Product with ID 1 not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateProduct_Success() {
        // Preparar datos
        ProductDto productDto = new ProductDto();
        productDto.setName("Nuevo Producto");
        productDto.setCategoryId(1L);
        productDto.setImageUrls(Arrays.asList("url1", "url2"));

        Category category = new Category();
        category.setId(1L);
        category.setName("Categoría 1");

        Product product = productDto.toEntity();
        product.setCategory(category);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Ejecutar método
        ProductDto result = productService.createProduct(productDto);

        // Verificar resultados
        assertNotNull(result);
        assertEquals("Nuevo Producto", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testCreateProduct_CategoryNotFound() {
        // Preparar datos
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.createProduct(productDto);
        });

        assertEquals("Category with ID 1 not found", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct_Success() {
        // Preparar datos
        ProductDto productDto = new ProductDto();
        productDto.setName("Producto Actualizado");
        productDto.setCategoryId(1L);
        productDto.setImageUrls(Arrays.asList("url1", "url2"));

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Producto Antiguo");

        Category category = new Category();
        category.setId(1L);
        category.setName("Categoría 1");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // Ejecutar método
        ProductDto result = productService.updateProduct(1L, productDto);

        // Verificar resultados
        assertNotNull(result);
        assertEquals("Producto Actualizado", result.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void testUpdateProduct_ProductNotFound() {
        // Preparar datos
        ProductDto productDto = new ProductDto();

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(1L, productDto);
        });

        assertEquals("Product with ID 1 not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(categoryRepository, never()).findById(anyLong());
    }

    @Test
    public void testUpdateProduct_CategoryNotFound() {
        // Preparar datos
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(1L);

        Product existingProduct = new Product();
        existingProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(1L, productDto);
        });

        assertEquals("Category with ID 1 not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct_Success() {
        // Preparar datos
        when(productRepository.existsById(1L)).thenReturn(true);

        // Ejecutar método
        productService.deleteProduct(1L);

        // Verificar resultados
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        // Preparar datos
        when(productRepository.existsById(1L)).thenReturn(false);

        // Ejecutar y verificar excepción
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Product with ID 1 not found", exception.getMessage());
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
