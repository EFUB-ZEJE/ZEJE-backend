FROM openjdk:11-jdk

ARG JAR_FILE=/home/ubuntu/app/*.jar
COPY ${JAR_FILE} myapp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/myapp.jar"]
