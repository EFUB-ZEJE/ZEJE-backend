FROM openjdk:11-jdk

ARG JAR_FILE=/home/ubuntu/app/*.jar
COPY ${JAR_FILE} myapp.jar

ENTRYPOINT ["java", "-jar", "myapp.jar"]
