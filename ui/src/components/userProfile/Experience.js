import React from "react";
import Level from "../Level";

export default function Experience({data, handleOnChange}){
    const branch = "experience";
    return (
        <div>
            <h3>Experience</h3>
            <Level purpose="Animal Adoption" name="withAnimalAdoption" data={data.withAnimalAdoption} handleLevelChange={handleOnChange} branch={branch}/>
            <Level purpose="Adopting Species" name="withAdoptingSpecies" data={data.withAdoptingSpecies} handleLevelChange={handleOnChange} branch={branch}/>
            <Level purpose="Adopting Breed" name="withAdoptingBreed" data={data.withAdoptingBreed} handleLevelChange={handleOnChange} branch={branch}/>
        </div>
    )
}