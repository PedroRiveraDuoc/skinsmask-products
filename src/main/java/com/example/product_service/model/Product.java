package com.example.product_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @NotNull(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción debe tener como máximo 255 caracteres")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1, message = "El precio debe ser positivo")
    private Integer price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "La categoría es obligatoria")
    @Column(name = "category_id")
    private Long categoryId;

    @NotNull(message = "La URL de la imagen es obligatoria")
    @Size(max = 255, message = "La URL de la imagen debe tener como máximo 255 caracteres")
    private String imageUrl;

    @NotNull(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca debe tener como máximo 50 caracteres")
    private String brand;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación debe ser entre 1 y 5")
    @Max(value = 5, message = "La calificación debe ser entre 1 y 5")
    private Integer rating;

    @NotNull(message = "La disponibilidad es obligatoria")
    @Pattern(regexp = "Y|N", message = "La disponibilidad debe ser 'Y' (sí) o 'N' (no)")
    private String available;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
