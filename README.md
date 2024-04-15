# Animal Adoption System

## Overview
Full-stack microservices developed using Spring Boot, Spring Cloud, MySQL, MongoDB, Axon and Docker 

## Tech Stack
![TechStack](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/d2beb9d8-fac6-4ecc-98e0-e733b3c1a6b4)

## Architecture
![Architecture](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/50bea282-f03d-4bae-a274-924c67178c67)

## Orchestration-based Saga
![Saga](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/e8aaf8e1-0ad7-4710-b485-bb2987643712)

## Use Services

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
8. As you may notice, this User UI only uses a limited number of backend APIs. To use the rest, please import this [Postman script](https://github.com/lchen2792/AnimalAdoptionSystem/blob/main/AnimalAdoptionSystem.postman_collection.json) and refer to Swagger API docs if needed

