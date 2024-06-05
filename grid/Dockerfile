FROM openjdk:17-alpine

RUN apk add --no-cache redis

COPY build/libs/*.jar app.jar

ARG JASYPT_KEY
ENV JASYPT_KEY=$JASYPT_KEY

#엔트리 포인트 지정
ENTRYPOINT ["java", "-jar", "/app.jar"]