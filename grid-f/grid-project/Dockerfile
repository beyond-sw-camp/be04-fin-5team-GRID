# 베이스 이미지로 node:alpine을 사용
FROM node:lts-alpine

RUN apk add --no-cache curl

# 작업 디렉토리를 설정
WORKDIR /usr/src/app

# package.json과 package-lock.json을 복사
COPY package*.json ./

# npm install 로 의존성 설치 
RUN npm install

# 소스 파일들을 복사
COPY . .

# 애플리케이션 빌드
RUN npm run build

# 애플리케이션 시작 명령어
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
