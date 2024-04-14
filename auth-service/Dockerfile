FROM amazoncorretto:21
COPY target/auth-service-0.0.1-SNAPSHOT.jar docker-auth-service.jar
EXPOSE 8889
CMD ["java", "-jar", "./docker-auth-service.jar"]