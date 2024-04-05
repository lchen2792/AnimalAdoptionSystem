import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import PaymentMethod from "../payment/PaymentMethod";

export default function Application({ setLogin, login, navigate }) {
    const [messages, setMessages] = useState([]);
    const { animalId } = useParams();
    const [userProfile, setUserProfile] = useState("");

    useEffect(() => {
        if (login) {
            (async () => {
                const response = await fetch(
                    "http://localhost:9000/user-service/user-profiles/me",
                    {
                        method: "GET",
                        headers: {
                            "Authorization": "Bearer " + login,
                            "Content-Type": "application/json"
                        }
                    }
                );

                if (!response.ok) {
                    if (response.status === 401) {
                        localStorage.removeItem("token");
                        setLogin(null);
                        navigate("/login");
                    } else {
                        throw new Error(response.statusText);
                    }
                }

                const userProfile = await response.json();
                setUserProfile(userProfile);
            })();
        } else {
            navigate("/login");
        }
    }, []);

    const handleSubmit = async () => {

        const response = await fetch("http://localhost:9000/application-service/applications",
            {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + login,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ userProfileId: userProfile.userProfileId, animalProfileId: animalId })
            })

        if (!response.ok) {
            if (response.status === 401) {
                localStorage.removeItem("token");
                setLogin(null);
                navigate("/login");
            } else {
                throw new Error(response.statusText);
            }
        }

        const summaries = await response.json();

        setMessages(summaries);
    }

    const statuses = messages
        .map((summary, idx) =>
        (<div key={new Date().getTime() + idx}>
            <div>status: {summary.applicationStatus}</div>
            {summary.message && <div>message: {summary.message}</div>}
        </div>)
        );

    return (<div>
        {login && userProfile && (userProfile.customerId
            ? <button onClick={handleSubmit}>Submit application</button>
            : <PaymentMethod {...{
                login: login,
                setLogin: setLogin,
                userProfile: userProfile,
                setUserProfile: setUserProfile,
                navigate: navigate
            }} />
        )}
        <div>
            {statuses}
        </div>
    </div>)

}