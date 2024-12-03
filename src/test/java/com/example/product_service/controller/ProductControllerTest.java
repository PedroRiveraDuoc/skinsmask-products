package com.example.product_service.controller;

import com.example.product_service.dto.ProductDto;
import com.example.product_service.exception.GlobalExceptionHandler;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.service.ProductService;
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

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productController = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        ProductDto product1 = new ProductDto();
        product1.setId(1L);
        product1.setName("Producto 1");

        ProductDto product2 = new ProductDto();
        product2.setId(2L);
        product2.setName("Producto 2");

        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Producto 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Producto 2"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testGetProductById_Success() throws Exception {
        ProductDto product = new ProductDto();
        product.setId(1L);
        product.setName("Producto 1");

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Producto 1"));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(1L)).thenThrow(new ProductNotFoundException("Product with ID 1 not found"));

        mockMvc.perform(get("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    public void testCreateProduct_Success() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setName("Nuevo Producto");
        productDto.setCategoryId(1L);
        productDto.setDescription("Descripción");
        productDto.setPrice(BigDecimal.valueOf(100.0));
        productDto.setStock(10);
        productDto.setImageUrls(Arrays.asList("http://example.com/image1.jpg")); // Asegúrate de que tenga al menos un
                                                                                 // valor

        ProductDto createdProduct = new ProductDto();
        createdProduct.setId(1L);
        createdProduct.setName("Nuevo Producto");
        createdProduct.setCategoryId(1L);
        createdProduct.setDescription("Descripción");
        createdProduct.setPrice(BigDecimal.valueOf(100.0));
        createdProduct.setStock(10);
        createdProduct.setImageUrls(Arrays.asList("http://example.com/image1.jpg"));

        when(productService.createProduct(any(ProductDto.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Nuevo Producto"))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.description").value("Descripción"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.imageUrls[0]").value("http://example.com/image1.jpg"));

        verify(productService, times(1)).createProduct(any(ProductDto.class));
    }

    @Test
    public void testUpdateProduct_Success() throws Exception {
        // Arrange: Configuramos los datos de entrada y salida esperados
        ProductDto productDto = createValidProductDto();
        ProductDto updatedProduct = createUpdatedProductDto();

        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(updatedProduct);

        // Act & Assert: Realizamos la solicitud y validamos la respuesta
        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Producto Actualizado"))
                .andExpect(jsonPath("$.description").value("Descripción Actualizada"))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.stock").value(20));

        // Verificamos que el servicio fue invocado correctamente
        verify(productService, times(1)).updateProduct(eq(1L), any(ProductDto.class));
    }

    // Helper para crear un ProductDto válido para las pruebas
    private ProductDto createValidProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Producto Actualizado");
        productDto.setCategoryId(1L);
        productDto.setDescription("Descripción Actualizada");
        productDto.setPrice(BigDecimal.valueOf(150.0));
        productDto.setStock(20);
        productDto.setImageUrls(Arrays.asList("http://example.com/image1.jpg"));
        return productDto;
    }

    // Helper para crear un ProductDto actualizado (simula el resultado del
    // servicio)
    private ProductDto createUpdatedProductDto() {
        ProductDto updatedProduct = new ProductDto();
        updatedProduct.setId(1L);
        updatedProduct.setName("Producto Actualizado");
        updatedProduct.setCategoryId(1L);
        updatedProduct.setDescription("Descripción Actualizada");
        updatedProduct.setPrice(BigDecimal.valueOf(150.0));
        updatedProduct.setStock(20);
        updatedProduct.setImageUrls(Arrays.asList("http://example.com/image1.jpg"));
        return updatedProduct;
    }

    @Test
    public void testDeleteProduct_Success() throws Exception {
        mockMvc.perform(delete("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    public void testCreateProduct_ValidationFailed() throws Exception {
        ProductDto invalidProductDto = new ProductDto(); // DTO vacío, no válido

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidProductDto)))
                .andExpect(status().isBadRequest());
    }
}
