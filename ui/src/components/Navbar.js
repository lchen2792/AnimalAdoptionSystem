import React from 'react';
import "../css/Navbar.css";
import { Link } from 'react-router-dom';

export default function Navbar({ navigate, login, setLogin }) {

    const handleLogout = () => {
        localStorage.removeItem("token");
        setLogin(prev => !prev);
        navigate("/");
    }

    return (<nav className='nav-bar'>
        <div className='nav-bar-home'><Link to="/">Home</Link></div>
        <div className='nav-bar-user'><Link to="/user-profile">User</Link></div>
        <div className='nav-bar-animal'><Link to="/animals">Animal</Link></div>
        {login ?
            <div className='nav-bar-logout' onClick={handleLogout}>Log out</div> :
            <div className='nav-bar-login'><Link to="/login">Log in</Link></div>
        }
    </nav>);
}