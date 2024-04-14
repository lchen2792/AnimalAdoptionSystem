FROM amazoncorretto:21
COPY target/application-service-0.0.1-SNAPSHOT.jar docker-application-service.jar
EXPOSE 9002
CMD ["java", "-jar", "./docker-application-service.jar"]