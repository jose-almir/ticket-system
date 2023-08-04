FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN mvn clean package -Pprod -DskipTests

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar

ENTRYPOINT ["java", "-jar", "demo.jar"]
