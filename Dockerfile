FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY productos-pro-0.0.1-SNAPSHOT.jar productos-pro-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/productos-pro-0.0.1-SNAPSHOT.jar"]