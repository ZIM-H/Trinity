import React from 'react';
// import logo from './logo.svg';
import './App.css';
import { Route, BrowserRouter, Routes } from "react-router-dom";

import MainHome from "./pages/mainhome";

import SignUp from "./pages/account/signup";
import Login from "./pages/account/login";
import LocalLogin from "./pages/account/locallogin";
import SocialSignUp from "./pages/account/socialsignup";
import MyPage from "./pages/account/mypage";
import FindPassWord from "./pages/account/findpassword";
import SocialLogin from "./pages/account/sociallogin";
import GameIntro from './pages/gameintro';
import GameRule from './pages/gamerule';
import BiryoRoom from './pages/biryoroom';
import ControlRoom from './pages/controlroom';
import MedicineRoom from './pages/medicineroom';
import PrivacyPolicy from './pages/privatepolicy';

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainHome />} />
        {/* Login status */}
        <Route path="/login" element={<Login />} />
        <Route path="/locallogin" element={<LocalLogin />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/socialsignup" element={<SocialSignUp />} />
        <Route path="/sociallogin" element={<SocialLogin />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/findpw" element={<FindPassWord />} />
        {/* game introduce */}
        <Route path="/intro" element={<GameIntro />} />
        <Route path="/rule" element={<GameRule />} />
        {/* game Room */}
        <Route path="/biryoroom" element={<BiryoRoom />} />
        <Route path="/ctrlroom" element={<ControlRoom />} />
        <Route path="/mediroom" element={<MedicineRoom />} />
        <Route path="/privatepolicy" element={<PrivacyPolicy />} />
      </Routes>
    </BrowserRouter>

  );
}

export default App;
