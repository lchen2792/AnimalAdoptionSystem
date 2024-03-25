import React, { useEffect, useState } from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route
} from 'react-router-dom';
import './css/App.css';
import Navbar from './components/Navbar';
import Home from './components/Home';
import Animal from './components/Animal';
import UserProfile from './components/UserProfile';
import Login from './components/Login';


export default function App() {
  const [token, setToken] = useState(null);

  useEffect(()=> {
    sessionStorage.setItem("token", token);
  }, [token]);

  return (<div>
      <Router>
        <Navbar />
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/user' element={<UserProfile token={token} />} />
          <Route path='/animal' element={<Animal token={token} />} />
          <Route path='/login' element={<Login setToken={setToken} />} />
        </Routes>
      </Router>
    </div>);
}

