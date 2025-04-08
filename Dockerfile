#FROM openjdk:11-jre-slim
FROM eclipse-temurin:11-alpine
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
