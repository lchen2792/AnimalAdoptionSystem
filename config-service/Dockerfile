FROM amazoncorretto:21
COPY target/config-service-0.0.1-SNAPSHOT.jar docker-config-service.jar
EXPOSE 8888
CMD ["java", "-jar", "./docker-config-service.jar"]