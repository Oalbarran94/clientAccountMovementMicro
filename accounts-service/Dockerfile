FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY target/accounts-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
