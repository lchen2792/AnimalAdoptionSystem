FROM amazoncorretto:21
COPY target/user-service-0.0.1-SNAPSHOT.jar docker-user-service.jar
EXPOSE 9004
CMD ["java", "-jar", "./docker-user-service.jar"]