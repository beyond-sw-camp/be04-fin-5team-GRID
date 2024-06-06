FROM openjdk:17-alpine

# redis 및 tzdata 설치
RUN apk add --no-cache redis tzdata

# 타임존 설정
ENV TZ=Asia/Seoul
RUN cp /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# JASYPT_KEY ARG 및 ENV 설정
ARG JASYPT_KEY
ENV JASYPT_KEY=$JASYPT_KEY

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
