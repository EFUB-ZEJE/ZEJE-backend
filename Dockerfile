FROM openjdk:11-jdk

WORKDIR /home/ubuntu/app

ARG JAR_FILE=/*.jar
COPY ${JAR_FILE} myapp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/myapp.jar"]
