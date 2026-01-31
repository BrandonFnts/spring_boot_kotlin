# --- Etapa 1: Construcción (Build) ---
FROM gradle:jdk21-alpine AS build
WORKDIR /app
COPY . .

# Evitar correr los tests
RUN ./gradlew bootJar --no-daemon -x test

# --- Etapa 2: Ejecución (Run) ---
FROM eclipse-temurin:21-jre-alpine
LABEL authors="brand"
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]