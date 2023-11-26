#!/bin/bash
# shellcheck disable=SC2164

# BUILD spring service
cd ../spring_service
./gradlew clean
./gradlew build
cd ..

# BUILD jax_rs service
cd jax_rs_service
./gradlew clean
./gradlew build
cd ..

# BUILD docker compose
docker-compose build
docker-compose down
docker-compose up --detach