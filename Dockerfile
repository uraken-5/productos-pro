FROM openjdk:17-jdk-slim

# Instala Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src src

# Construye el proyecto usando Maven
RUN mvn package

EXPOSE 8080

ENTRYPOINT ["java","-jar","target/productos-pro-0.0.1-SNAPSHOT.jar"]
