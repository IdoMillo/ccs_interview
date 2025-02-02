FROM maven:3.9.6-eclipse-temurin-22 AS build

WORKDIR /app

COPY . .

RUN mvn clean package

FROM openjdk:22

WORKDIR /app
COPY --from=build /app/target/*.jar GameServer.jar

EXPOSE 8080

CMD ["java", "-jar", "GameServer.jar"]
