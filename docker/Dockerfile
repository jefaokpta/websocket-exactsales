FROM openjdk:11-jre-slim
#ARG JAR_FILE=target/*.jar
COPY ./*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]