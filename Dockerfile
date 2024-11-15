# Etapa de construcción
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . /app
RUN ./mvnw clean package -DskipTests

# Etapa final
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Copiar la wallet al contenedor
COPY Wallet_YQ95ND3X7BHK88DG /app/Wallet_YQ95ND3X7BHK88DG

# Configurar la variable de entorno TNS_ADMIN
ENV TNS_ADMIN=/app/Wallet_YQ95ND3X7BHK88DG

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
