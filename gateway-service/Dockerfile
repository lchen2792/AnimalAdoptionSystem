FROM amazoncorretto:21
COPY target/gateway-service-0.0.1-SNAPSHOT.jar docker-gateway-service.jar
EXPOSE 9000
CMD ["java", "-jar", "./docker-gateway-service.jar"]