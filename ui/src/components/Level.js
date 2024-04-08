import React from "react";

export default function Level({purpose, name, data, handleLevelChange, branch}){
    return (
        <div>
            <label className="descriptor">{purpose}</label>
            <input
                id="level-1"
                type="radio"
                name={name}
                value="EXTREME_LOW"
                checked={data === "EXTREME_LOW"}
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label className="level-descriptor" htmlFor="level-1">Extr low</label>
            <input
                id="level-2"
                type="radio"
                name={name}
                value="EXTRA_LOW"
                checked={data === "EXTRA_LOW"}
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label className="level-descriptor" htmlFor="level-2">Extra low</label>
            <input
                id="level-3"
                type="radio"
                name={name}
                value="LOW"
                checked={data === "LOW"}
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label className="level-descriptor" htmlFor="level-3">Low</label>
            <input
                id="level-4"
                type="radio"
                name={name}
                value="MEDIUM"
                checked={data === "MEDIUM"}
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label className="level-descriptor" htmlFor="level-4">Medium</label>
            <input
                id="level-5"
                type="radio"
                name={name}
                value="HIGH"
                checked={data === "HIGH"}
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label className="level-descriptor" htmlFor="level-5">High</label>
            <input
                id="level-6"
                type="radio"
                name={name}
                value="EXTRA_HIGH"
                checked={data === "EXTRA_HIGH"}
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label className="level-descriptor" htmlFor="level-6">Extra high</label>
            <input
                id="level-7"
                type="radio"
                name={name}
                value="EXTREME_HIGH"
                checked={data === "EXTREME_HIGH"}
                onChange={event => handleLevelChange(branch, "", event)}
            />
            <label className="level-descriptor" htmlFor="level-7">Extr high</label>
        </div>
    )
}