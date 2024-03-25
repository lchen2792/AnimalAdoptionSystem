import React, {useState, useEffect} from 'react';
import {Link} from 'react-router-dom';

export default function Navbar() {
   
    return (<nav className='nav-bar'>
        <ul>
            <li className='nav-bar-home'><Link to="/">Home</Link></li>
            <li className='nav-bar-user'><Link to="/user">User</Link></li>
            <li className='nav-bar-animal'><Link to="/animal">Animal</Link></li>
            <li className='nav-bar-login'><Link to="/login">Log in</Link></li>
        </ul>       
    </nav>);
}