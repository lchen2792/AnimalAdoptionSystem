#!/bin/bash

echo "start pre-populating test data"

# onboard test animals
curl --location 'localhost:9000/animal-service/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation($request: CreateAnimalProfileRequest!) {\r\n    createAnimalProfile(request: $request)\r\n}","variables":{"request":{"basicInformation":{"species":"Dog","breed":"Alaskan Malamute","age":3,"gender":"male","size":"LARGE","neutered":false},"temperament":{"sociability":"HIGH","activity":"HIGH","trainability":"HIGH","stability":"HIGH","aggressivity":"LOW","independency":"MEDIUM","adaptability":"HIGH","preyDrive":"MEDIUM","communication":"HIGH"},"careRequirements":{"space":"EXTRA_HIGH","socializing":"MEDIUM","companionship":"HIGH","exercise":"EXTRA_HIGH","grooming":"HIGH","diet":"HIGH"}}}}'

curl --location 'localhost:9000/animal-service/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation($request: CreateAnimalProfileRequest!) {\r\n    createAnimalProfile(request: $request)\r\n}","variables":{"request":{"basicInformation":{"species":"Dog","breed":"Shiba Inu","age":2,"gender":"male","size":"SMALL","neutered":false},"temperament":{"sociability":"MEDIUM","activity":"MEDIUM","trainability":"HIGH","stability":"HIGH","aggressivity":"MEDIUM","independency":"MEDIUM","adaptability":"HIGH","preyDrive":"LOW","communication":"HIGH"},"careRequirements":{"space":"MEDIUM","socializing":"MEDIUM","companionship":"HIGH","exercise":"MEDIUM","grooming":"LOW","diet":"LOW"}}}}'

sleep 5

# register test user
token=$(curl --location 'localhost:8889/registration/user' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "johndoe@hotmail.com",
    "name": "john doe",
    "password": "1234"
}')

sleep 5

# create test user profile
userProfileId=$(curl --location 'localhost:9000/user-service/user-profiles' \
--header "Authorization: Bearer ${token}" \
--header 'Content-Type: application/json' \
--data-raw '{
    "basicInformation": {
        "name": {
            "firstName": "John",
            "middleName": null,
            "lastName": "Doe"
        },
        "address": {
            "addressLine1": "777 N. Mill Pond Street",
            "addressLine2": "Unit 206",
            "city": "New York",
            "state": "NY",
            "zipCode": "10025"
        },
        "contact": {
            "phone": "1234567890",
            "email": "johndoe123@real.com"
        }
    },
    "livingSituation": {
        "typeOfResidence": "apartment",
        "availableSpace": "MEDIUM"
    },
    "familySituation": {
        "numberOfAdults": 2,
        "numberOfChildren": 0,
        "pets": [
            {
                "petId": 12345678910,
                "species": "Dog",
                "breed": "German Shepherd Dog",
                "age": 2
            },
            {
                "petId": 1712260132392,
                "species": "Rabbit",
                "breed": "Flemish Giant Rabbit",
                "age": 1
            }
        ]
    },
    "experience": {
        "withAdoptingSpecies": "HIGH",
        "withAdoptingBreed": "LOW",
        "withAnimalAdoption": "HIGH"
    },
    "knowledge": {
        "ofAdoptingSpecies": "HIGH",
        "ofAdoptingBreed": "MEDIUM",
        "ofAnimalAdoption": "HIGH"
    },
    "personality": {
        "sociability": "MEDIUM",
        "activity": "HIGH",
        "stability": "HIGH",
        "patience": "EXTRA_HIGH",
        "motivation": "EXTRA_HIGH",
        "adaptability": "EXTRA_HIGH",
        "communication": "MEDIUM"
    }
}')

sleep 5

# update test user payment method
curl --location --request PUT "localhost:9000/user-service/user-profiles/${userProfileId}/payment" \
--header "Authorization: Bearer ${token}" \
--header 'Content-Type: application/json' \
--data '{
    "cardholderName": "JohnDoe",
    "cardNumber": "4242424242424242",
    "validThruYear": 2036,
    "validThruMonth": 12,
    "CVV": 123
}'
