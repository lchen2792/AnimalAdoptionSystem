import React, { useEffect, useState } from "react";
import Pet from "./Pet";


export default function FamilySituation({handler}){
    const branch = "familySituation";
    const [petList, setPetList] = useState([]);
    const handlePetAddition = () => {
        setPetList(prev => [...prev, new Date().getTime()]);
    }
    const handlePetRemoval = petId => {
        setPetList(prev => prev.filter(p => p !== petId));
    }

    const petInputList = petList.map(petId => {
        return <Pet 
                    key={petId} 
                    petId={petId}
                    branch={branch} 
                    handlePetChange={handler} 
                    handlePetRemoval={handlePetRemoval}
                />;
    })

    return (
        <div className="user-family-situation">
            <h3>Family Situation</h3>
            <input 
                type="text"
                name="numberOfAdults"
                pattern="[0-9]{1,2}"
                placeholder="Number of Adults"
                onChange={event => handler(branch, "", event)}
            />
            <br />
            <input 
                type="text"
                name="numberOfChildren"
                pattern="[0-9]{1,2}"
                placeholder="Number of children"
                onChange={event => handler(branch, "", event)}
            />
            <br />
            Add info about the pets you own         
            <input type="button" name="addPet" value="+" onClick={handlePetAddition} />
            <div className="pet-info">
                {petInputList}
            </div>            
        </div>
    )
}