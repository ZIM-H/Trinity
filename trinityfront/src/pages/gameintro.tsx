import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logoImage from "../assets/trinitylogo.png"

const GameIntro: React.FC = () => {
  const navigate = useNavigate();
  const Rule = () => {
      navigate("/rule");
  };

    
    return (
      <div style={{display:'flex', flexDirection: 'column', alignItems:'center', justifyContent:'center'}}>
        <img src={logoImage} alt="logo" />
        <h1 style={{fontSize:'120px'}}>Trinity</h1>
        <p style={{fontSize:'80px'}}>우주 탐사를 마치고 귀환 도중</p>
        <p style={{fontSize:'80px'}}>Trinity호에 우주 기생충이 침략했다!</p>
        <p style={{fontSize:'80px'}}>우주정거장 까지 걸리는 시간은 12일..!</p>
        <p style={{fontSize:'80px'}}>접촉을 차단하고 각자 협력하여 생존하라..!</p>
        <Link to="/rule">
          <button>
            <p style={{fontSize:'40px'}} onClick={Rule}>Go to Rule</p>
          </button>
        </Link>
      </div>
    );
}
export default GameIntro;
