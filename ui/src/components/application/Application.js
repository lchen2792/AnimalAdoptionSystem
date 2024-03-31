import React, {useState, useEffect } from "react";
import { useParams } from "react-router-dom";

export default function Application({login, navigate}){
    const {animalId} = useParams();
    const token = localStorage.getItem("token");
    const {userProfile, setUserProfile} = useState("");

    useEffect(()=>{
        if (token) {
            (async () => {
                const response = await fetch(
                    "localhost:9000/user-service/user-profiles/me",
                    {
                        method: "GET",
                        headers: {
                            "Authorization": "Bearer " + token,
                            "Content-Type": "application/json"
                        }
                    }
                );

                if (!response.ok) {
                    if (response.status === 401) {
                        //remove token
                        localStorage.removeItem("token");
                        navigate("/login");
                        //refresh page
                    }
                } else {
                    const userProfile = await response.json();
                    setUserProfile(userProfile);
                }
            })();
        } else {
            navigate("/login");
        }
    }
    , []);

    return (<div>
        {login&&userProfile&&(userProfile.customerId ? "": "")}
    </div>)

}