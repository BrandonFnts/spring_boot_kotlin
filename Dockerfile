# --- Etapa 1: Construcción (Build) ---
FROM gradle:jdk21-alpine AS build
WORKDIR /app
COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew bootJar --no-daemon -x test

# --- Etapa 2: Ejecución (Run) ---
FROM eclipse-temurin:21-jre-alpine
LABEL authors="brand"
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]