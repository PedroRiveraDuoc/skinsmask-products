package com.example.product_service.service;

import com.example.product_service.dto.ProductDto;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.model.Category;
import com.example.product_service.model.Product;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
        return ProductDto.fromEntity(product);
    }

    public ProductDto createProduct(ProductDto productDto) {
        // Validar que la categoría exista
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ProductNotFoundException("Category with ID " + productDto.getCategoryId() + " not found"));

        // Crear la entidad del producto desde el DTO
        Product product = productDto.toEntity();
        product.setCategory(category);
        product.setImageUrls(productDto.getImageUrls()); // Guardar las URLs de las imágenes

        // Guardar el producto y convertirlo de nuevo a DTO
        Product savedProduct = productRepository.save(product);
        return ProductDto.fromEntity(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        // Validar que el producto exista
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));

        // Actualizar los campos del producto
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());
        existingProduct.setImageUrls(productDto.getImageUrls()); // Actualizar las URLs de las imágenes

        // Validar que la categoría exista
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ProductNotFoundException("Category with ID " + productDto.getCategoryId() + " not found"));
        existingProduct.setCategory(category);

        // Guardar el producto actualizado y convertirlo a DTO
        Product updatedProduct = productRepository.save(existingProduct);
        return ProductDto.fromEntity(updatedProduct);
    }

    public void deleteProduct(Long id) {
        // Validar que el producto exista
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }
        // Eliminar el producto
        productRepository.deleteById(id);
    }
}
