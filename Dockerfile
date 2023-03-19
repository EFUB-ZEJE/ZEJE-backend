FROM openjdk:11-jdk

WORKDIR /home/ubuntu/app

ARG JAR_FILE=/*.jar
COPY ${JAR_FILE} myapp.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Dspring.config.location=classpath:/application.properties,classpath:/application-aws.properties,classpath:/application-oauth.properties", "-jar", "/myapp.jar"]
