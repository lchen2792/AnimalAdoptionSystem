import React from "react";

export default function Pet({petId, handlePetChange, handlePetRemoval}){    

    return (
        <div>
            <input 
                type="text" 
                name="species" 
                placeholder="Species" 
                onChange={event => handlePetChange(petId, event)}
            />
            <input 
                type="text" 
                name="breed" 
                placeholder="Breed" 
                onChange={event => handlePetChange(petId, event)}
            />
            <input 
                type="text" 
                name="age" 
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