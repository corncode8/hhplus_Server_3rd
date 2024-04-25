FROM openjdk:17

ENV TZ=Asia/Seoul

COPY build/libs/server-java-0.0.1-SNAPSHOT.jar /app/server-java.jar

EXPOSE 9000

ENTRYPOINT java \
  -jar /app/server-java.jar \
  --spring.profiles.active=${PROFILE} \