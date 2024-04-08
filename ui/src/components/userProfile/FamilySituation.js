import React, { useEffect, useState } from "react";
import Pet from "./Pet";


export default function FamilySituation({data, handleOnChange}){
    const branch = "familySituation";
    console.log(data.pets);
    const [petList, setPetList] = useState(data.pets);
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
                    data={pet}
                    branch={branch} 
                    handlePetChange={handlePetChange} 
                    handlePetRemoval={handlePetRemoval}
                />;
    });

    useEffect(()=> {
        handleOnChange(branch, "", {target: {name: "pets", value: petList}});
    }, [petList]);

    return (
        <div className="user-family-situation">
            <h3>Family Situation</h3>
            <label className="descriptor">No. of Adults</label>
            <input 
                type="text"
                name="numberOfAdults"
                value={data.numberOfAdults}
                pattern="[0-9]{1,2}"
                placeholder="Number of Adults"
                onChange={event => handleOnChange(branch, "", event)}
            />
            <br />
            <label className="descriptor">No. of Children</label>
            <input 
                type="text"
                name="numberOfChildren"
                value={data.numberOfChildren}
                pattern="[0-9]{1,2}"
                placeholder="Number of children"
                onChange={event => handleOnChange(branch, "", event)}
            />
            <br />
            <label>Tell us about the pets you own</label>         
            <input type="button" name="addPet" value="Add" onClick={handlePetAddition} />
            <div className="pet-info">
                {petInputList}
            </div>            
        </div>
    );
}