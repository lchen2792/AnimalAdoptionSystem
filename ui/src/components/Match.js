import React, { useState, useEffect } from "react";
import AnimalCard from "../components/animals/AnimalCard";

const query =
    `
query($id: ID!) {
    findAnimalProfileById(animalProfileId: $id) {
        animalProfileId,
        basicInformation {
            species,
            breed,
            age,
            gender,
            size,
            neutered
        }
    }
}
`;

export default function Match({ login, setLogin, userProfile, navigate }) {
    const [animalCards, setAnimalCards] = useState([]);

    useEffect(() => {
        (async () => {
            const {identifications, ...userProfileForMatch} = userProfile;
            const response = await fetch(
                `http://localhost:9000/user-service/match/animals`,
                {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + login,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        userProfileForMatch: userProfileForMatch,
                        /** @todo Add support for match based on user input */
                        request: {}
                    })
                }
            );
            if (!response.ok) {
                if (response.status === 401) {
                    localStorage.removeItem("token");
                    setLogin(null);
                    navigate("/login");
                } else {
                    throw new Error(response.statusText);
                }
            }

            const matchMap = await response.json();
            const matchIdList = Object.entries(matchMap).sort((e1, e2) => e2[1] - e1[1]).map(e => e[0]);

            const matchPromises = matchIdList
                .map(async matchId => {
                    const response = await fetch(
                        `http://localhost:9000/animal-service/graphql`,
                        {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({
                                query: query,
                                variables: {
                                    id: matchId
                                }
                            })
                        }
                    );

                    if (!response.ok) {
                        throw new Error(response.statusText);
                    }

                    const data = await response.json();

                    if (data["errors"]) {
                        throw new Error(data["errors"]);
                    }

                    const animalProfile = data.data.findAnimalProfileById;

                    return <AnimalCard
                        key={animalProfile.animalProfileId}
                        animalProfileId={animalProfile.animalProfileId}
                        basicInformation={animalProfile.basicInformation}
                    />
                });

            Promise
                .allSettled(matchPromises)
                .then(res => res
                    .filter(match => match.status === "fulfilled")
                    .map(match => match.value))
                .then(cards => setAnimalCards(cards));
        })();
    }, []);

    return (
        <div className="match-wrapper">
            {animalCards}
        </div>
    )
}