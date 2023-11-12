FROM openjdk:17-jdk-alpine

MAINTAINER romix

COPY target/demo-0.0.1-SNAPSHOT.jar app/demo.jar

ENTRYPOINT ["java", "-jar", "/app/demo.jar"]