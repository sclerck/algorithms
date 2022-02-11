# syntax=docker/dockerfile:1

FROM openjdk:13-jdk-alpine3.10

COPY target/algorithms-*-jar-with-dependencies.jar /algorithms.jar

ENV GRPC_PORT="5000"
ENV WEB_PORT="8080"

EXPOSE $GRPC_PORT
EXPOSE $WEB_PORT

CMD java -jar /algorithms.jar -p $GRPC_PORT -w $WEB_PORT