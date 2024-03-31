import React from "react";


export default function UserBasicInformation({data, handleOnChange}){
    const branch = "basicInformation";

    return (
    <div className="user-basic-information">
        <h3>Basic Information</h3>
        <input 
            type="text"
            name="firstName"
            value={data.name_firstName}
            placeholder="First Name"
            onChange={event => handleOnChange(branch, "name", event)}
        />
        <input 
            type="text"
            name="middleName"
            value={data.name_middleName}
            placeholder="Middle Name"
            onChange={event => handleOnChange(branch, "name", event)}
        />
        <input 
            type="text"
            name="lastName"
            value={data.name_lastName}
            placeholder="Last Name"
            onChange={event => handleOnChange(branch, "name", event)}
        />
        <br/>
        <input 
            type="text"
            name="addressLine1"
            value={data.adress_addressLine1}
            placeholder="Address Line 1"
            onChange={event => handleOnChange(branch, "address", event)}
        />
        <br/>
        <input 
            type="text"
            name="addressLine2"
            value={data.adress_addressLine2}
            placeholder="Address Line 2"
            onChange={event => handleOnChange(branch, "address", event)}
        />
        <br/>
        <input 
            type="text"
            name="city"
            value={data.address_city}
            placeholder="City"
            onChange={event => handleOnChange(branch, "address", event)}
        />
        <input 
            type="text"
            name="state"
            value={data.address_state}
            placeholder="State"
            onChange={event => handleOnChange(branch, "address", event)}
        />
        <input 
            type="text"
            name="zipCode"
            value={data.address_zipCode}
            placeholder="ZipCode 12345"
            pattern="[0-9]{5}"
            onChange={event => handleOnChange(branch, "address", event)}
        />
        <br />
        <input 
            type="tel"
            name="phone"
            value={data.contact_phone}
            placeholder="Phone Number 123-456-7890"
            pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}"
            onChange={event => handleOnChange(branch, "contact", event)}
        />
        <input 
            type="email"
            name="email"
            value={data.contact_email}
            placeholder="Email"
            onChange={event => handleOnChange(branch, "contact", event)}
        />
    </div>);
}