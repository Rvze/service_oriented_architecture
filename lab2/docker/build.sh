#!/bin/bash
# shellcheck disable=SC2164

# BUILD spring service
cd ../spring_service
gradle clean
gradle build
docker build -t spring-image .
cd ..

# BUILD jax_rs service
cd jax_rs_service
gradle clean
gradle build
docker build -t jax_rs-image .
cd ../docker

# BUILD docker compose
docker-compose build
docker-compose down
docker-compose up --detach