#!/bin/bash
nohup java -jar -Dserver.port=8082 -Dspring.profiles.active=i1 build/libs/spring_service-latest.jar >log8082.log &
nohup java -jar -Dserver.port=8083 -Dspring.profiles.active=i2 build/libs/spring_service-latest.jar >log8083.log &
