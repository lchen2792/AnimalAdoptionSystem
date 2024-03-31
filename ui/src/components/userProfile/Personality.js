import React from "react";
import Level from "../Level";

export default function Personality({data, handleOnChange}){
    const branch = "personality";
    const personalityList = [
        "Sociability",
        "Activity",
        "Stability",
        "Patience",
        "Motivation",
        "Adaptability",
        "Communication"
    ];

    const personalityInputs = personalityList.map(p => {
        const lowerCasedP = p.toLowerCase();
        return <Level 
                    key={lowerCasedP}
                    purpose={p} 
                    name={lowerCasedP}
                    data={data[lowerCasedP]}
                    handleLevelChange={handleOnChange} 
                    branch={branch}
                />; 
    });

    return (
        <div>
            <h3>Personality</h3>
            {personalityInputs}
        </div>
    )
}