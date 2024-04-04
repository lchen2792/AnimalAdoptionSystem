import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";

const query =
    `query($id: ID!) {
        findAnimalProfileById(animalProfileId: $id) {
        basicInformation {
            species,
            breed,
            age,
            gender,
            size,
            neutered
        },
        temperament {
            sociability,
            activity,
            trainability,
            stability,
            aggressivity,
            independency,
            adaptability,
            preyDrive,
            communication
        },
        careRequirements {
            space,
            socializing,
            companionship,
            exercise,
            grooming,
            diet
        },
        media
    }
}`

export default function AnimalProfile({login, navigate}) {
    const { id } = useParams();
    const [animalProfile, setAnimalProfile] = useState("");

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
                            id: id
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

            setAnimalProfile(data.data.findAnimalProfileById);
        })();
    }, []);

    const transformObjectAsList = (obj, formatter) => Object.entries(obj).map(([k, v], i) => {
        return <li key={i}>{formatter(k, v)}</li>;
    })

    const keyValueFormatter = (k, v) => `${k}: ${v}`;

    const handleApply = async () => {
        navigate(`/application/${id}`);
    };

    return (<div>
        {login && <div onClick={handleApply}>Apply</div>}
        {animalProfile && <div>
            <h3>Basic Information</h3>
            <ul>
                {transformObjectAsList(animalProfile.basicInformation, keyValueFormatter)}
            </ul>
            <h3>Temperament</h3>
            <ul>
                {transformObjectAsList(animalProfile.temperament, keyValueFormatter)}
            </ul>
            <h3>Care Requirements</h3>
            <ul>
                {transformObjectAsList(animalProfile.careRequirements, keyValueFormatter)}
            </ul>
        </div>
        }
    </div>);
}