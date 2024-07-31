FROM openjdk:17-jdk-slim
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn package
EXPOSE 8080
ENTRYPOINT ["java","-jar","target/productos-pro-0.0.1-SNAPSHOT.jar"]
