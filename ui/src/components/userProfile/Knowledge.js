import React from "react";
import Level from "../Level";

export default function Knowledge({data, handleOnChange}){
    const branch = "knowledge";
    return (
        <div>
            <h3>Knowledge</h3>
            <Level purpose="Animal Adoption" name="ofAnimalAdoption" data={data.ofAnimalAdoption} handleLevelChange={handleOnChange} branch={branch}/>
            <Level purpose="Adopting Species" name="ofAdoptingSpecies" data={data.ofAdoptingSpecies} handleLevelChange={handleOnChange} branch={branch}/>
            <Level purpose="Adopting Breed" name="ofAdoptingBreed" data={data.ofAdoptingBreed} handleLevelChange={handleOnChange} branch={branch}/>
        </div>
    )
}