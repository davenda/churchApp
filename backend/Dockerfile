FROM openjdk:19-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
RUN mkdir -p /app/data
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]