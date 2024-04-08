import React from "react";


export default function UserBasicInformation({data, handleOnChange}){
    const branch = "basicInformation";

    return (
    <div className="user-basic-information">
        <h3>Basic Information</h3>
        <label className="descriptor">First Name</label>
        <input 
            type="text"
            name="firstName"
            value={data.name_firstName}
            placeholder="First Name"
            onChange={event => handleOnChange(branch, "name", event)}
        />
        <label className="descriptor">Middle Name</label>
        <input 
            type="text"
            name="middleName"
            value={data.name_middleName}
            placeholder="Middle Name"
            onChange={event => handleOnChange(branch, "name", event)}
        />
        <label className="descriptor">Last Name</label>
        <input 
            type="text"
            name="lastName"
            value={data.name_lastName}
            placeholder="Last Name"
            onChange={event => handleOnChange(branch, "name", event)}
        />
        <br/>
        <label className="descriptor">Address Line 1</label>
        <input 
            type="text"
            name="addressLine1"
            value={data.address_addressLine1}
            placeholder="Address Line 1"
            onChange={event => handleOnChange(branch, "address", event)}
        />
        <br/>
        <label className="descriptor">Address Line 2</label>
        <input 
            type="text"
            name="addressLine2"
            value={data.address_addressLine2}
            placeholder="Address Line 2"
            onChange={event => handleOnChange(branch, "address", event)}
        />
        <br/>
        <label className="descriptor">City</label>
        <input 
            type="text"
            name="city"
            value={data.address_city}
            placeholder="City"
            onChange={event => handleOnChange(branch, "address", event)}
        />
        <label className="descriptor">State</label>
        <input 
            type="text"
            name="state"
            value={data.address_state}
            placeholder="State"
            onChange={event => handleOnChange(branch, "address", event)}
        />
        <br />
        <label className="descriptor">Zip Code</label>
        <input 
            type="text"
            name="zipCode"
            value={data.address_zipCode}
            placeholder="ZipCode 12345"
            pattern="[0-9]{5}"
            onChange={event => handleOnChange(branch, "address", event)}
        />
        <br />
        <label className="descriptor">Phone</label>
        <input 
            type="tel"
            name="phone"
            value={data.contact_phone}
            placeholder="Phone Number 1234567890"
            pattern="[0-9]{10}"
            onChange={event => handleOnChange(branch, "contact", event)}
        />
        <br />
        <label className="descriptor">Email</label>
        <input 
            type="email"
            name="email"
            value={data.contact_email}
            placeholder="Email"
            onChange={event => handleOnChange(branch, "contact", event)}
        />
    </div>);
}