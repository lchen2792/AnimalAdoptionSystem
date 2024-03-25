import React, { useState } from "react";
import UserBasicInformation from "./UserBasicInformation";


export default function UserProfile({ token }) {

    const [formData, setFormData] = useState({
        basicInformation: {
            name: {
                firstName: "",
                middleName: "",
                lastName: ""
            },
            address: {
                addressLine1: "",
                addressLine2: "",
                city: "",
                state: "",
                zipCode: ""
            },
            contact: {
                phone: "",
                email: ""
            }
        }
    });

    const handleFieldChange = (field, path, name, value) => {
        setFormData(prevForm => {
            return {
                ...prevForm,
                [field]: {
                    ...prevForm[field],
                    [path]: {
                        ...prevForm[field][path],
                        [name]: value
                    }
                }
            }
        });
    }

    const handleOnSubmit = event => {
        event.preventDefault();
        console.log(formData);
    }

    return (<div>
        <form onSubmit={handleOnSubmit}>
            <UserBasicInformation onFieldChange={handleFieldChange} />
            <button>Submit</button>
        </form>
    </div>)
}