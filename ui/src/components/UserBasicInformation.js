import React from "react";


export default function UserBasicInformation({handler}){
    const branch = "basicInformation";

    return (
    <div className="user-basic-information">
        <h3>Basic Information</h3>
        <input 
            type="text"
            name="firstName"
            placeholder="First Name"
            onChange={event => handler(branch, "name", event)}
        />
        <input 
            type="text"
            name="middleName"
            placeholder="Middle Name"
            onChange={event => handler(branch, "name", event)}
        />
        <input 
            type="text"
            name="lastName"
            placeholder="Last Name"
            onChange={event => handler(branch, "name", event)}
        />
        <br/>
        <input 
            type="text"
            name="addressLine1"
            placeholder="Address Line 1"
            onChange={event => handler(branch, "address", event)}
        />
        <br/>
        <input 
            type="text"
            name="addressLine2"
            placeholder="Address Line 2"
            onChange={event => handler(branch, "address", event)}
        />
        <br/>
        <input 
            type="text"
            name="city"
            placeholder="City"
            onChange={event => handler(branch, "address", event)}
        />
        <input 
            type="text"
            name="state"
            placeholder="State"
            onChange={event => handler(branch, "address", event)}
        />
        <input 
            type="text"
            name="zipCode"
            placeholder="ZipCode 12345"
            pattern="[0-9]{5}"
            onChange={event => handler(branch, "address", event)}
        />
        <br />
        <input 
            type="tel"
            name="phone"
            placeholder="Phone Number 123-456-7890"
            pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}"
            onChange={event => handler(branch, "contact", event)}
        />
        <input 
            type="email"
            name="email"
            placeholder="Email"
            onChange={event => handler(branch, "contact", event)}
        />
    </div>);
}