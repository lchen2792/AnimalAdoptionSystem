import React from "react";
import Level from "./Level";

export default function Knowledge({handleOnChange}){
    const branch = "knowledge";
    return (
        <div>
            <h3>Knowledge</h3>
            <Level purpose="Animal Adoption" name="ofAnimalAdoption" handleLevelChange={handleOnChange} branch={branch}/>
            <Level purpose="Adopting Species" name="ofAdoptingSpecies" handleLevelChange={handleOnChange} branch={branch}/>
            <Level purpose="Adopting Breed" name="ofAdoptingBreed" handleLevelChange={handleOnChange} branch={branch}/>
        </div>
    )
}