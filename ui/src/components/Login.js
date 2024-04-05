import React, { useState } from "react";

const formDataTemplate = {
    email: "",
    password: "",
    name: "",
    isSignUp: false
}

export default function Login({ navigate, login, setLogin }) {
    const [formData, setFormData] = useState(formDataTemplate)

    const handleOnChange = event => {
        const { name, value, type, checked } = event.target;
        setFormData(prevFormData => {
            return {
                ...prevFormData,
                [name]: type === "checkbox" ? checked : value
            }
        })
    }

    const handleSubmit = async event => {
        event.preventDefault();

        let url = "http://localhost:8889";
        if (formData.isSignUp) {
            url += "/registration/user";
        } else {
            url += "/auth";
        }

        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            if (response.status === 400) {
                alert("Username or password not correct");
                setFormData(prev => {
                    return {
                        ...formDataTemplate,
                        isSignUp: prev.isSignUp
                    }
                })
            } else {
                throw new Error(response.statusText);
            }
        } else {
            const token = await response.text();
            localStorage.setItem("token", token);
            setLogin(token);
            navigate("/");
        }
    }

    return (
        <div className="login-wrapper">
            {login ? "You are logged in" :
                <div>
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
            }
        </div>
    );
}
