FROM openjdk:11-jdk

WORKDIR /home/ubuntu/app

ARG JAR_FILE=/*.jar
COPY ${JAR_FILE} myapp.jar

ENTRYPOINT ["java", "-jar", "/myapp.jar"]
