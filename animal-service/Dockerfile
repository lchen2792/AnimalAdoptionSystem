FROM amazoncorretto:21
COPY target/animal-service-0.0.1-SNAPSHOT.jar docker-animal-service.jar
EXPOSE 9001
CMD ["java", "-jar", "./docker-animal-service.jar"]