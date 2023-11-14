import React from 'react';
import { Link } from 'react-router-dom';
import logoImage from "../assets/trinitylogo.png"

const BiryoRoom: React.FC = () => {
    
    return (
      <div style={{display:'flex', flexDirection: 'column', alignItems:'center', justifyContent:'center'}}>
        <Link to="/rule">
          <img src={logoImage} alt="logo" />
        </Link>
        <img src="assets/Trinity_Biryoroom_UI.png" alt="logo" />
        <p style={{fontSize:'55px'}}>Trinity의 비료방입니다.</p>
        <p style={{fontSize:'55px'}}>이 곳에서 비료를 업그레이드 할 수 있습니다!</p>
        <p style={{fontSize:'55px'}}>정수 시스템이 오염되면 수리를 해야 살 수있습니다!</p>
        <p style={{fontSize:'55px'}}>비료를 생산해야 비료를 투입하여 식량 생산이 가능합니다!</p>
      </div>
    );
}
export default BiryoRoom;
