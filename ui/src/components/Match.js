import React, { useState, useEffect } from "react";
import AnimalCard from "../components/animals/AnimalCard";
import { wait } from "@testing-library/user-event/dist/utils";

export default function Match({ userProfileId }) {
    const [animalCards, setAnimalCards] = useState([]);

    console.log(animalCards);

    useEffect(() => {
        (async () => {
            // const response = await fetch();
            // if (!response.ok) {
            //     console.log(response.statusText);
            // }
            //const matchMap = await response.json();
            const matchMap = {"a": 0.8, "b": 0.6};
            const matchList = Object.entries(matchMap).sort((e1, e2) => e2[1] - e1[1]).map(e => e[0]);

            const matchPromises = matchList
                .map(match => {
                    // const response = await fetch();
                    //const animalProfile = await response.json();

                    return <AnimalCard
                        // key={animalProfile.animalProfileId}
                        // animalProfileId={animalProfile.animalProfileId}
                        // basicInformation={animalProfile.basicInformation}
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
        <div>
            {animalCards}
        </div>
    )
}