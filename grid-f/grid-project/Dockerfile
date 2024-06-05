FROM node:lts-alpine

# curl 설치(client-url): 필수는 아님(postman 같은 것)!!!
RUN apk add --no-cache curl

WORKDIR /app
COPY . ./
RUN npm install

RUN npm rebuild @rollup/rollup-linux-x64-musl

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]