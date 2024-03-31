import React from 'react';
import { Link } from 'react-router-dom';

export default function Navbar({navigate, login, setLogin}) {
    
    const handleLogout = ()=>{
        localStorage.removeItem("token");
        setLogin(prev=>!prev);
        navigate("/");
    }

    return (<nav className='nav-bar'>
        <ul>
            <li className='nav-bar-home'><Link to="/">Home</Link></li>
            <li className='nav-bar-user'><Link to="/user-profile">User</Link></li>
            <li className='nav-bar-animal'><Link to="/animals">Animal</Link></li>
            {login ?
                <li className='nav-bar-logout' onClick={handleLogout}>Log out</li> :
                <li className='nav-bar-login'><Link to="/login">Log in</Link></li>
            }
        </ul>
    </nav>);
}