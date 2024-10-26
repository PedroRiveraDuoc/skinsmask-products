# 🛒 ProductService-SpringBoot

## 📋 Descripción
Este proyecto es un microservicio para la gestión de productos de una tienda en línea, desarrollado con **Spring Boot**. Permite realizar operaciones CRUD sobre productos y categorías. Está diseñado para integrarse en una arquitectura de microservicios.

## 🛠️ Tecnologías
- ☕ **Java 17**
- 🚀 **Spring Boot 3**
- 🗃️ **Spring Data JPA**
- 🏦 **Oracle Database**: Configurado en `application.properties`
- 📦 **Maven**: Para la gestión de dependencias

## ⚙️ Funcionalidades
- 🛍️ **Gestión de Productos**: Crear, leer, actualizar y eliminar productos.
- 📂 **Gestión de Categorías**: CRUD para categorías de productos.
- 🧩 **Arquitectura en Capas**: Organización en controladores, servicios y repositorios.
  
## 🚀 Configuración
1. ⚙️ Configura la conexión a Oracle en `src/main/resources/application.properties`.
2. 📄 Ejecuta los scripts SQL para crear las tablas y datos iniciales.

## ▶️ Ejecución
1. 🔄 Compila y ejecuta el proyecto con Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
