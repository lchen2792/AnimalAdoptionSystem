import React, { useState } from "react";
import fallbackImgSrc from "../../images/default.jpg";
import { Link } from "react-router-dom";

export default function AnimalCard({animalProfileId, basicInformation}){
    // const fallbackImgSrc = `src\images\default.jpg`;
    const[imgSrc, setImgSrc] = useState(`../../images/${animalProfileId}.jpg`);
    const triggerFallbackImgSrc = ()=>{
        setImgSrc(fallbackImgSrc);
    }
    return (
        <div className="animal-card">
            <img 
                className="animal-card-thumbnail" 
                src={imgSrc}
                onError={triggerFallbackImgSrc}
            />
            <div>Species: {basicInformation.species}</div>
            <div>Breed: {basicInformation.breed}</div>
            <div>Age: {basicInformation.age}</div>
            <div>Gender: {basicInformation.gender}</div>
            <Link to={`/animals/${animalProfileId}`}>View details</Link>
        </div>
    )
}