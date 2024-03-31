import React, { useEffect, useState } from "react";
import UserBasicInformation from "./BasicInformation";
import LivingSituation from "./LivingSituation";
import FamilySituation from "./FamilySituation";
import Experience from "./Experience";
import Knowledge from "./Knowledge";
import Personality from "./Personality";
import Match from "../Match";
import { unflatten, flatten } from "../../utils/JsonUtil";

const userProfileTemplate = {
    userProfileId: "",
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
    knowledge: {
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
};

const baseUrl = "localhost:9000/user-service";

export default function UserProfile({ navigate }) {
    const [create, setCreate] = useState(false);
    const [section, setSection] = useState(0);
    const token = localStorage.getItem("token");

    useEffect(() => {
        // if (token) {
        //     (async () => {
        //         const response = await fetch(
        //             `${baseUrl}/user-profiles/me`,
        //             {
        //                 method: "GET",
        //                 headers: {
        //                     "Authorization": "Bearer " + token,
        //                     "Content-Type": "application/json"
        //                 }
        //             }
        //         );

        //         if (!response.ok) {
        //             if (response.status === 401) {
        //                 //remove token
        //                 localStorage.removeItem("token");
        //                 navigate("/login");
        //                 //refresh page
        //             } else if (response.status === 404) {
        //                 setCreate(true);
        //             }
        //         } else {
        //             const userProfile = await response.json();
        //             setFormData(userProfile);
        //         }
        //     })();
        // } else {
        //     navigate("/login");
        // }
    }, [])

    const [formData, setFormData] = useState(userProfileTemplate);


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
        const { name, value } = event.target;
        const fieldName = (path && `${path}_${name}`) || name;
        handleFieldChange(branch, fieldName, value);
    }

    const handleOnSubmit = async event => {
        event.preventDefault();
        const unflattenedBasicInformation = unflatten(formData.basicInformation);
        const unflattenedFormData = {
            ...formData,
            basicInformation: unflattenedBasicInformation
        }

        const response = await fetch(
            baseUrl,
            {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(unflattenedFormData)
            }
        );

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const userProfileId = await response.text();

        const userProfileResponse = await fetch(
            `${baseUrl}/${userProfileId}`,
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );

        if (!userProfileResponse.ok) {
            throw new Error(response.statusText);
        }

        const userProfile = await userProfileResponse.json();
        const flattenedBasicInformation = flatten(userProfile.basicInformation);
        const flattenedFormData = {
            ...userProfile,
            basicInformation: flattenedBasicInformation
        }
        setFormData(flattenedFormData);
    }

    return (<div>
        <nav>
            <span onClick={() => setSection(1)}>Create/Update Profile</span>
            <span onClick={() => setSection(2)}>Find Animal Match</span>
        </nav>
        {section === 1 &&
            <form onSubmit={handleOnSubmit}>
                <UserBasicInformation data={formData.basicInformation} handleOnChange={handleOnChange} />
                <LivingSituation data={formData.livingSituation} handleOnChange={handleOnChange} />
                <FamilySituation data={formData.familySituation} handleOnChange={handleOnChange} />
                <Experience data={formData.experience} handleOnChange={handleOnChange} />
                <Knowledge data={formData.knowledge} handleOnChange={handleOnChange} />
                <Personality data={formData.personality} handleOnChange={handleOnChange} />
                <button>{create ? "Create" : "Update"}</button>
            </form>
        }
        {section === 2 && (!create ?
            <Match userProfileId={formData.userProfileId} />
            : "Please create your profile first")}
    </div>)
}