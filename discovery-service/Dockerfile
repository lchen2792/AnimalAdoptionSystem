FROM amazoncorretto:21
COPY target/discovery-service-0.0.1-SNAPSHOT.jar docker-discovery-service.jar
EXPOSE 8761
CMD ["java", "-jar", "./docker-discovery-service.jar"]