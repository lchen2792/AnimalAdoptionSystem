import React from "react";
import Level from "./Level";

export default function Experience({handleOnChange}){
    const branch = "experience";
    return (
        <div>
            <h3>Experience</h3>
            <Level purpose="Animal Adoption" name="withAnimalAdoption" handleLevelChange={handleOnChange} branch={branch}/>
            <Level purpose="Adopting Species" name="withAdoptingSpecies" handleLevelChange={handleOnChange} branch={branch}/>
            <Level purpose="Adopting Breed" name="withAdoptingBreed" handleLevelChange={handleOnChange} branch={branch}/>
        </div>
    )
}