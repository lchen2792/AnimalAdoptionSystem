import React, { useState } from "react";
import UserBasicInformation from "./UserBasicInformation";
import LivingSituation from "./LivingSituation";
import FamilySituation from "./FamilySituation";
import Experience from "./Experience";
import Knowledge from "./Knowledge";
import Personality from "./Personality";


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
        },
        familySituation: {
            numberOfAdults: "",
            numberOfChildren: "",
            pets: []
        },
        experience: {
            withAdoptingSpecies: "",
            withAdoptingBreed: "",
            withAnimalAdoption: ""
        },
        Knowledge: {
            ofAdoptingSpecies: "",
            ofAdoptingBreed: "",
            ofAnimalAdoption: ""
        },
        personality: {
            sociability: "",
            activity: "",
            stability: "",
            patience: "",
            motivation: "",
            adaptability: "",
            communication: ""
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
            <UserBasicInformation handleOnChange={handleOnChange} />
            <LivingSituation handleOnChange={handleOnChange} />
            <FamilySituation handleOnChange={handleOnChange} />
            <Experience handleOnChange={handleOnChange} />
            <Knowledge handleOnChange={handleOnChange} />
            <Personality handleOnChange={handleOnChange} />
            <button>Submit</button>
        </form>
    </div>)
}