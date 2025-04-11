FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]