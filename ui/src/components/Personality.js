import React from "react";
import Level from "./Level";

export default function Personality({handleOnChange}){
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

    const personalityInputs = personalityList.map((p, index) => {
        return <Level 
                    key={index}
                    purpose={p} 
                    name={p.toLowerCase()} 
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