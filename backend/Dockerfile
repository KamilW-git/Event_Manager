#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#ARG JAR_FILE=target/event-manager-1.0.0.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#FROM openjdk:17-jdk-slim
#COPY ../target/event-manager-1.0.0.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]




FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
