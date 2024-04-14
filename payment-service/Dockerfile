FROM amazoncorretto:21
COPY target/payment-service-0.0.1-SNAPSHOT.jar docker-payment-service.jar
EXPOSE 9003
CMD ["java", "-jar", "./docker-payment-service.jar"]