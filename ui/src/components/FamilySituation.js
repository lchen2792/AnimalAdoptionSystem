import React, { useEffect, useState } from "react";
import Pet from "./Pet";


export default function FamilySituation({handleOnChange}){
    const branch = "familySituation";
    const [petList, setPetList] = useState([]);
    const handlePetAddition = () => {
        const petId = new Date().getTime();
        const newPet = {
            petId: petId,
            species: "",
            breed: "",
            age: ""
        }
        setPetList(prev => [...prev, newPet]);
    };

    const handlePetChange = (petId, event) => {
        const {name, value} = event.target;
        
        setPetList(prev => {
            const idx = prev.findIndex(pet=>pet.petId === petId);
            const newPetList = [...prev];
            newPetList[idx] = {
                ...prev[idx],
                [name]:value
            };
            return newPetList;
        });
    }

    const handlePetRemoval = petId => {
        setPetList(prev => prev.filter(p => p.petId !== petId));
    };

    const petInputList = petList.map(pet => {
        return <Pet 
                    key={pet.petId} 
                    petId={pet.petId}
                    branch={branch} 
                    handlePetChange={handlePetChange} 
                    handlePetRemoval={handlePetRemoval}
                />;
    });

    useEffect(()=> {
        console.log(petList);
        handleOnChange(branch, "", {target: {name: "pets", value: petList}});
    }, [petList]);

    return (
        <div className="user-family-situation">
            <h3>Family Situation</h3>
            <input 
                type="text"
                name="numberOfAdults"
                pattern="[0-9]{1,2}"
                placeholder="Number of Adults"
                onChange={event => handleOnChange(branch, "", event)}
            />
            <br />
            <input 
                type="text"
                name="numberOfChildren"
                pattern="[0-9]{1,2}"
                placeholder="Number of children"
                onChange={event => handleOnChange(branch, "", event)}
            />
            <br />
            Add info about the pets you own         
            <input type="button" name="addPet" value="+" onClick={handlePetAddition} />
            <div className="pet-info">
                {petInputList}
            </div>            
        </div>
    );
}