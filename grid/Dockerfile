FROM openjdk:17-alpine

COPY build/libs/*.jar app.jar

#터미널에서의 java -jar app.jar --jasypt.encryptor.password=team5esc 와 같다.
#이거 여기다 적으면 password 빼는 의미가 있는지? Jenkins 설정할 때 넣어보기
ENTRYPOINT ["java", "-jar", "app.jar", "--jasypt.encryptor.password="]