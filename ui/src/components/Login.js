import React, { useState } from "react";

export default function Login({navigate, login, setLogin}) {
    const [formData, setFormData] = useState({
        email: "",
        password: "",
        name: "",
        isSignUp: false
    })

    const handleOnChange = event => {
        const {name, value, type, checked} = event.target;
        setFormData(prevFormData => { 
            return {
                ...prevFormData,
                [name]: type === "checkbox" ? checked : value           
            }
        })
    }

    const handleSubmit = async event => {
        event.preventDefault();

        if (login) return;

        let url = "http://localhost:8889";
        if (formData.isSignUp) {
            url += "/registration/user";
        } else {
            url += "/auth";
        }

        localStorage.setItem("token", "123");
        setLogin(true);
        navigate("/");

        // fetch(url, {
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body: JSON.stringify(formData) 
        // })
        // .then(response => {
        //     if (!response.ok) {
        //         throw new Error(response.statusText);
        //     }
        //     return response.text();
        // })
        // .then(token => {
        //     console.log(token);
        //     localStorage.setItem("token", token);
        //     setLogin(true);
        //     navigate("/");
        // })
        // .catch(err => {
        //     console.log(err.statusText);
        // });
    }

    return (
        <div className="login-wrapper">
            <input 
                type="checkbox" 
                id="isSignUp" 
                onChange={handleOnChange} 
                name="isSignUp" 
                checked={formData.isSignUp}
            />
            <label htmlFor="isSignUp">Create account</label>
            <form onSubmit={handleSubmit}>
                {formData.isSignUp && 
                <input 
                    type="text" 
                    onChange={handleOnChange} 
                    name="name" 
                    value={formData.name}
                    placeholder="User Name"
                />}
                <input 
                    type="email" 
                    onChange={handleOnChange} 
                    name="email" 
                    value={formData.email}
                    placeholder="Email"
                />
                <input 
                    type="password" 
                    onChange={handleOnChange} 
                    name="password" 
                    value={formData.password}
                    placeholder="Password"
                />
                <button>Submit</button>
            </form>
        </div>
    );
}
