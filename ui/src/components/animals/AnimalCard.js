import React from "react";
import { Link } from "react-router-dom";

export default function AnimalCard({animalProfileId, basicInformation}){
    return (
        <div>
            <img src={`../../images/${animalProfileId}.jpg`} />
            <span>{basicInformation.species}</span>
            <span>{basicInformation.breed}</span>
            <span>{basicInformation.age}</span>
            <span>{basicInformation.gender}</span>
            <Link to={`/animals/${animalProfileId}`}>View details</Link>
        </div>
    )
}