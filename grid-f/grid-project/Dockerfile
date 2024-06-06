FROM node:lts-alpine

# curl 및 tzdata 설치
RUN apk add --no-cache curl tzdata

# 타임존 설정
ENV TZ=Asia/Seoul
RUN cp /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드 복사 및 의존성 설치
COPY . ./
RUN npm install

# 특정 패키지 재빌드
RUN npm rebuild @rollup/rollup-linux-x64-musl

# 애플리케이션 실행
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
