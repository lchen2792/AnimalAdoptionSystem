# Animal Adoption System

## Overview
### Backend four core microservices:
- Animal service is a Spring MVC Async MongoDB application that supports dynamic queries through GraphQL at the API level and MongoDB's Criteria API at database level.
- User service is a RESTful Spring MVC MySQL application handling user profile CRUD, files upload/download, and leverages Google Gemini API to match user profiles with animal profiles.
- Application service is a RESTful Spring Webflux MongoDB application that serves as the orchestrator for distributed transactions about application submissions and reviews.
- Payment service is a RESTful Spring Webflux MySQL application using the Stripe API for payment validation and processing.

### Backend four additonal microservices:
- Config service externalizes configurations to Git and supplies them to the backend services. 
- Auth service, secured with Spring Security and JWT, holds and provide user auth data.
- Discovery service uses Eureka to register and track the health of backend services.
- Gateway Service provides role-based JWT authentication and authorization for User and Application services using auth data provided by Auth service. It also manages load balancing and routing using data provided by Discovery service.

### Backend microservice communication: 
Communication between backend services is asynchronous, with most being done through the Evenbus provided by Axon framework. In User service, WebClient is also used for remote HTTP calls with internal and external APIs. To improve resilience, WebClient-based calls are protected with resilience4j's circuit breaker and retry.

### Frontend:
On the font end, so far only User UI has been developed using ReactJS.

### Platform:
All services are dockerized and the User UI is served by Nginx.

### Documentation:
- All APIs have been tested and documented in Postman. 
- All RESTful APIs have been documented using OPEN API and Swagger.

## Tech Stack
![TechStack](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/d2beb9d8-fac6-4ecc-98e0-e733b3c1a6b4)

## Architecture
![Architecture](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/50bea282-f03d-4bae-a274-924c67178c67)

## Orchestration-based Saga
![Saga](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/e8aaf8e1-0ad7-4710-b485-bb2987643712)

## Start the services
1. Get a Google Gemini Api key if you don't already have following https://ai.google.dev/tutorials/setup
2. Run `./start.sh ${your Google Gemini Api key}` and wait for all services up and running
3. You should be able to access:
    - Eureka Server at http://localhost:8761
   ![image](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/64a4fd76-c794-47ec-9821-339f0d9240ec)
    - Swagger for auth service at http://localhost:8889/swagger-ui.html
   ![image](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/2fa306b0-b268-4a74-adbf-668d94fd93f1)
    - Centralized Swagger for core services at http://localhost:9000/swagger-ui.html (navigate to each core service via the dropdown menu)
   ![image](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/5ca52974-1263-4751-b9b4-d0609affe958)
    - Axon Server at http://localhost:8024
   ![image](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/920064ea-ce8d-481c-9287-bb814744e144)
    - User UI at http://localhost:3000
    ![image](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/ad2f2e17-f554-4451-8f32-8a010c923cac)

4. Run `./pre-populate.sh` and wait for test data to persist
6. You can choose either to log in using the test user credentials (email `johndoe@hotmail.com` and password `1234`) or to register a new user
7. The User UI allows you to:
   - Log in/log out
   - Register
   - Create/update user profile
   - Add and validate payment method (powered by Stripe API)
   - Find your best animal matches (powered by Google Gemini API)
   - Browse all available animal profiles
   - Apply for adoption
8. The User UI only uses a limited number of backend APIs. To interact with the rest, please import this [Postman script](https://github.com/lchen2792/AnimalAdoptionSystem/blob/main/AnimalAdoptionSystem.postman_collection.json) and refer to Swagger API docs if needed

