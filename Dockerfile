# Etapa 1: Build (Compilação)
FROM eclipse-temurin:25-jdk-alpine AS build
COPY . .
# Garante permissão de execução
RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests

# Etapa 2: Runtime (Execução)
FROM eclipse-temurin:25-jre-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]