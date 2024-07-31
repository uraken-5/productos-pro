# Etapa 1: Construcción
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Copia el archivo pom.xml y descarga las dependencias necesarias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el resto de los archivos del proyecto y construye el JAR
COPY src ./src
RUN mvn clean package

# Etapa 2: Ejecución
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copia el JAR construido en la etapa de construcción
COPY --from=build /app/target/productos-pro-0.0.1-SNAPSHOT.jar /app/productos-pro-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/productos-pro-0.0.1-SNAPSHOT.jar"]

