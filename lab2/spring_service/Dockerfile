FROM amazoncorretto:17
WORKDIR /app

RUN apt-get update && \
    apt-get install wget postgresql-client --yes && \
    mkdir --parents ~/.postgresql && \
    wget "https://storage.yandexcloud.net/cloud-certs/CA.pem" \
         --output-document ~/.postgresql/root.crt && \
    chmod 0600 ~/.postgresql/root.crt



ENTRYPOINT [ "java","-jar","/app/build/spring_service-latest.jar" ]