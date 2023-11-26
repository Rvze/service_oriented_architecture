#!/bin/bash
# shellcheck disable=SC2164

# BUILD spring service
cd ../spring_service
./gradlew clean
./gradlew build
chmod +x build/libs/spring_service-latest.jar
docker build -t spring-image .
cd ..

# BUILD jax_rs service
cd jax_rs_service
./gradlew clean
./gradlew build
docker build -t jax_rs-image .
cd ../docker

# BUILD docker compose
docker-compose build
docker-compose down
docker-compose up --detach