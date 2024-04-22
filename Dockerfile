FROM openjdk:17

ARG mode_input
ENV mode=$mode_input

RUN ./gradlew build -x test

CMD ls build/libs/*SNAPSHOT.jar | xargs -I{} java -jar {}