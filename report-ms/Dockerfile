# ---- Build Stage ----
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app

# 1. Copia todos los archivos necesarios para Gradle
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

# 2. CONVERSIÓN CRÍTICA: Convierte los saltos de línea de Windows (CRLF) a Unix (LF)
# Esto asegura que el script sea interpretable por el shell de Linux.
RUN sed -i 's/\r$//' gradlew

# 3. ASEGURA los permisos de ejecución del Wrapper
RUN chmod +x gradlew

# 4. Copia el resto del código fuente del proyecto
COPY src ./src

# 5. Construye el proyecto
RUN ./gradlew clean build -x test --no-daemon

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]