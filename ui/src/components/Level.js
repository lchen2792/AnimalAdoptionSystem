import React from "react";

export default function Level({handleLevelChange, branch}){
    return (
        <div>
            <span>Available space</span>
            <input
                id="level-1"
                type="radio"
                name="availableSpace"
                value="1"
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label htmlFor="level-1">Extreme low</label>
            <input
                id="level-2"
                type="radio"
                name="availableSpace"
                value="2"
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label htmlFor="level-2">Extra low</label>
            <input
                id="level-3"
                type="radio"
                name="availableSpace"
                value="3"
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label htmlFor="level-3">Low</label>
            <input
                id="level-4"
                type="radio"
                name="availableSpace"
                value="4"
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label htmlFor="level-4">Medium</label>
            <input
                id="level-5"
                type="radio"
                name="availableSpace"
                value="5"
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label htmlFor="level-5">High</label>
            <input
                id="level-6"
                type="radio"
                name="availableSpace"
                value="6"
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label htmlFor="level-6">Extra high</label>
            <input
                id="level-7"
                type="radio"
                name="availableSpace"
                value="7"
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label htmlFor="level-7">Extreme high</label>
        </div>
    )
}