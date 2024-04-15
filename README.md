### Animal Adoption System

Architecture
![Project3 flow - Flowchart](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/50bea282-f03d-4bae-a274-924c67178c67)

Tech Stack
![Project3 flow - TechStack](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/d2beb9d8-fac6-4ecc-98e0-e733b3c1a6b4)

Orchestration-based Saga
![Project3 flow - Page 3](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/e8aaf8e1-0ad7-4710-b485-bb2987643712)

### Steps to use the services

1. get a Google Gemini Api key if you don't already have following https://ai.google.dev/tutorials/setup
2. run `./start.sh ${your Google Gemini Api key}` and wait for all services up and running
3. you should be able to access:
  - Eureka Server at http://localhost:8761
  - Swagger for auth service at http://localhost:8889/swagger-ui.html
  - Centralized Swagger for core services at http://localhost:9000/swagger-ui.html (navigate to each core service via the dropdown menu)
  - Axon Server at http://localhost:8024
  - User UI at http://localhost:3000
4. run `./pre-populate.sh` and wait for test data to persist
6. you can choose either to log in using the test user credentials (email `johndoe@hotmail.com` and password `1234`) or to register a new user
7. the UI allows you to:
   - log in/log out
   - register
   - create/update user profile
   - add and validate payment method (powered by Stripe API)
   - find your best animal matches (powered by Google Gemini API)
   - browse all available animal profiles
   - apply for adoption
