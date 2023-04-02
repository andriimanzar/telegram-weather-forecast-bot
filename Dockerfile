FROM maven:3.8.3-openjdk-17 as build
WORKDIR /usr/app
COPY checkstyle.xml .
COPY pom.xml .
RUN mvn -q -ntp -B dependency:go-offline

COPY src/ ./src/
RUN mvn package

FROM openjdk:17-alpine as runtime
COPY --from=build /usr/app/target/weather-telegram-bot-0.0.1-SNAPSHOT.jar /app/runner.jar
ENTRYPOINT java -jar /app/runner.jar