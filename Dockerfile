FROM openjdk:8-jdk-alpine3.7

RUN mkdir -p /app/emulador
RUN mkdir -p /app/emulador/log
RUN mkdir -p /app/emulador/config
RUN mkdir -p /app/emulador/bin
RUN mkdir -p /app/emulador/lib

WORKDIR /app/emulador

COPY ./target/*.jar .
COPY ./target/lib/* ./lib
COPY ./bin/* ./bin
COPY ./config/* ./config

RUN chmod +x /app/emulador/bin/*

USER root

ENTRYPOINT ["/app/emulador/bin/start.sh"]

