import React, { useState } from "react";
import Level from "./Level";


export default function LivingSituation({handler}){
    const branch = "livingSituation";
    return (
        <div>
            <h3>Living Situation</h3>
            <select name="typeOfResidence" onChange={event => handler(branch, "", event)}>
                <option defaultChecked>Choose residence type</option>
                <option>Apartment</option>
                <option>Townhouse</option>
                <option>House</option>
                <option>Ranch</option>
                <option>Other</option>
            </select>
            <br />
            <Level handleLevelChange={handler} branch={branch}/>
        </div>
    )
}