FROM eclipse-temurin:11-alpine
#ARG JAR_FILE=target/*.jar
COPY ./*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]