# Usa uma imagem do Java 17 (ajuste para a versão que você usa, ex: 21)
FROM eclipse-temurin:17-jdk-alpine AS build

# Copia os arquivos do projeto para o container
COPY . .

# Compila o projeto (usando o wrapper do maven que já existe no seu projeto)
RUN ./mvnw clean install -DskipTests

# Define a etapa final para rodar a aplicação
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /target/*.jar app.jar

# Define a porta que o Spring usa (geralmente 8080)
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "/app.jar"]