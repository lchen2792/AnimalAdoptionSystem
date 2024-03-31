import React, { useEffect, useState } from "react";
import AnimalCard from "./AnimalCard";

const query =
    `
query($request: FindAnimalProfilesByCriteriaRequest!) {
    findAnimalProfilesByCriteria(request: $request) {
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

export default function Animals() {
    const [animalList, setAnimalList] = useState([]);

    useEffect(() => {
        (async () => {
            const response = await fetch(
                "http://localhost:9000/animal-service/graphql",
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        query: query,
                        variables: {
                            request: {

                            }
                        }
                    })
                }
            );
            if (!response.ok) {
                throw new Error(response.statusText);
            }
            const animalData = await response.json();
            setAnimalList(animalData);
        })();
    }, []);

    const animalCards = animalList.map(a => <AnimalCard key={a.animalProfileId} {...a} />);

    return (
        <div>
            {animalCards}
        </div>
    )
}