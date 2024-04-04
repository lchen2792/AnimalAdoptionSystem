import React, { useState } from 'react';
import {
  Routes,
  Route,
  useNavigate
} from 'react-router-dom';
import './css/App.css';
import Navbar from './components/Navbar';
import Home from './components/Home';
import UserProfile from './components/userProfile/UserProfile';
import Login from './components/Login';
import Animals from './components/animals/Animals';
import AnimalProfile from './components/animals/AnimalProfile';
import Application from './components/application/Application';


export default function App() {
  const navigate = useNavigate();
  const [login, setLogin] = useState(localStorage.getItem("token"));

  return (<div>
        <Navbar navigate={navigate} login={login} setLogin={setLogin} />
        <Routes>
          <Route path='/' element={<Home />} />
          <Route exact path='/user-profile' element={<UserProfile login={login} setLogin={setLogin} navigate={navigate} />} />
          <Route path='/application/:animalId' element={<Application login={login} setLogin={setLogin} navigate={navigate} />} />
          <Route exact path='/animals' element={<Animals />} />
          <Route path='/animals/:id' element={<AnimalProfile login={login} navigate={navigate} />} />
          <Route path='/login' element={<Login navigate={navigate} login={login} setLogin={setLogin} />} />
        </Routes>
    </div>);
}

