FROM openjdk:17-alpine
RUN apk add --no-cache redis
COPY build/libs/*.jar app.jar

ARG JASYPT_KEY
ENV JASYPT_KEY=$JASYPT_KEY

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]