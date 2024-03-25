import React, { useState } from "react";
import UserBasicInformation from "./UserBasicInformation";
import LivingSituation from "./LivingSituation";


export default function UserProfile({ token }) {

    const [formData, setFormData] = useState({
        basicInformation: {
            name_firstName: "",
            name_middleName: "",
            name_lastName: "",
            address_addressLine1: "",
            address_addressLine2: "",
            address_city: "",
            address_state: "",
            address_zipCode: "",
            contact_phone: "",
            contact_email: ""
        },
        livingSituation: {
            typeOfResidence: "",
            availableSpace: ""
        }
    });

    const handleFieldChange = (field, name, value) => {
        setFormData(prevForm => {
            return {
                ...prevForm,
                [field]: {
                    ...prevForm[field],
                    [name]: value
                }
            }
        });
    }

    const handleOnChange = (branch, path, event) => {
        const {name, value} = event.target;
        const fieldName = (path && `${path}_${name}`) || name;
        handleFieldChange(branch, fieldName, value);
    }

    const handleOnSubmit = event => {
        event.preventDefault();
        console.log(formData);
    }

    return (<div>
        <form onSubmit={handleOnSubmit}>
            <UserBasicInformation handler={handleOnChange} />
            <LivingSituation handler={handleOnChange} />
            <button>Submit</button>
        </form>
    </div>)
}