### Animal Adoption System

Architecture
![Project3 flow - Flowchart](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/50bea282-f03d-4bae-a274-924c67178c67)

Tech Stack
![Project3 flow - TechStack](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/d2beb9d8-fac6-4ecc-98e0-e733b3c1a6b4)

Orchestration-based Saga
![Project3 flow - Page 3](https://github.com/lchen2792/AnimalAdoptionSystem/assets/79290606/e8aaf8e1-0ad7-4710-b485-bb2987643712)

### Steps to use the services

1. Get a Google Gemini Api key if you don't already have following https://ai.google.dev/tutorials/setup
2. Run `./start.sh ${your Google Gemini Api key}` and wait for all services up and running
3. You should be able to access:
    - Eureka Server at http://localhost:8761
    - Swagger for auth service at http://localhost:8889/swagger-ui.html
    - Centralized Swagger for core services at http://localhost:9000/swagger-ui.html (navigate to each core service via the dropdown menu)
    - Axon Server at http://localhost:8024
    - User UI at http://localhost:3000
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
8. The Employee/Admin UI currently is not available. To use those endpoints, please import this [Postman script](https://github.com/lchen2792/AnimalAdoptionSystem/blob/main/AnimalAdoptionSystem.postman_collection.json) and refer to Swagger API docs if needed

