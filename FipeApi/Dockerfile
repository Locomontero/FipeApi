# Usa uma imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho no container
WORKDIR /app

# Copie o JAR gerado pelo Maven para dentro do container
COPY target/*.jar app.jar

# Exponha a porta em que a aplicação será executada
EXPOSE 8080

# Defina o comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
