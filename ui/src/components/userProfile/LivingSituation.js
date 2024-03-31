import React from "react";
import Level from "../Level";


export default function LivingSituation({data, handleOnChange}){
    const branch = "livingSituation";
    return (
        <div>
            <h3>Living Situation</h3>
            <select name="typeOfResidence" value={data.typeOfResidence} onChange={event => handleOnChange(branch, "", event)}>
                <option value="">Choose residence type</option>
                <option value="apartment">Apartment</option>
                <option value="townhouse">Townhouse</option>
                <option value="house">House</option>
                <option value="ranch">Ranch</option>
                <option value="other">Other</option>
            </select>
            <br />
            <Level purpose="Available Space" name="availableSpace" data={data.availableSpace} handleLevelChange={handleOnChange} branch={branch}/>
        </div>
    )
}