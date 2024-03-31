import React from "react";

export default function Pet({petId, data, handlePetChange, handlePetRemoval}){    

    return (
        <div>
            <input 
                type="text" 
                name="species" 
                value={data.species}
                placeholder="Species" 
                onChange={event => handlePetChange(petId, event)}
            />
            <input 
                type="text" 
                name="breed" 
                value={data.breed}
                placeholder="Breed" 
                onChange={event => handlePetChange(petId, event)}
            />
            <input 
                type="text" 
                name="age" 
                value={data.age}
                placeholder="Age"
                pattern="[0-9]{1,2}"
                onChange={event => handlePetChange(petId, event)}
            />
            <input 
                type="button" 
                name="removePet" 
                value="-" 
                onClick={() => handlePetRemoval(petId)} 
            />
        </div>
    );
}