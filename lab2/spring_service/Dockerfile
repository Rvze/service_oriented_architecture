FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY build/libs/spring_service-0.0.1-SNAPSHOT.jar build/

WORKDIR app/build
ENTRYPOINT java -jar spring_service-0.0.1-SNAPSHOT.jar