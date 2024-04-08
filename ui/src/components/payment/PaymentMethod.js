import React, { useState } from "react";

const cardTemplate = {
    cardholderName: "",
    cardNumber: "",
    validThruYear: "",
    validThruMonth: "",
    CVV: ""
}

export default function PaymentMethod({ login, setLogin, userProfile, setUserProfile, navigate }) {
    const [card, setCard] = useState(cardTemplate);

    const handleOnSubmit = async event => {
        event.preventDefault();
        const response = await fetch(`http://localhost:9000/user-service/${userProfile.userProfileId}/payment`,
            {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + login,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(card)
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

        const customerId = await response.text();
        setUserProfile(prev => {
            return {
                ...prev,
                customerId: customerId
            }
        });
    }

    const handleOnChange = event => {
        const [name, value] = event.target;
        setCard(prev => {
            return {
                ...prev,
                [name]: value
            }
        })
    }

    return (
        <form onSubmit={handleOnSubmit}>
            <h3>Please add your payment method</h3>
            <label className="descriptor">Card Holder</label>
            <input type="text" name="cardholderName" onChange={handleOnChange} value={card.cardholderName} placeholder="Card holder name" />
            <label className="descriptor">Card Number</label>
            <input type="text" name="cardNumber" onChange={handleOnChange} value={card.cardNumber} placeholder="Card number" />
            <label className="descriptor">Valid thru Year</label>
            <input type="text" name="validThruYear" onChange={handleOnChange} value={card.validThruYear} placeholder="Valid thru year" />
            <label className="descriptor">Valid thru Month</label>
            <input type="text" name="validThruMonth" onChange={handleOnChange} value={card.validThruMonth} placeholder="Valid thru month" />
            <label className="descriptor">CVV</label>
            <input type="text" name="CVV" onChange={handleOnChange} value={card.CVV} placeholder="CVV" />
            <button>Submit</button>
        </form>
    )
}