import React from 'react';
import { Link } from 'react-router-dom';
import logoImage from "../assets/trinitylogo.png"

const MedicineRoom: React.FC = () => {
    
    return (
      <div style={{display:'flex', flexDirection: 'column', alignItems:'center', justifyContent:'center'}}>
        <Link to="/rule">
          <img src={logoImage} alt="logo" />
        </Link>
        <img src="assets/Trinity_ctrlroom_UI.png" alt="logo" />
        <p style={{fontSize:'55px'}}>Trinity의 약제실입니다.</p>
        <p style={{fontSize:'55px'}}>이 곳에서 타우린을 제조할 수 있습니다!</p>
        <p style={{fontSize:'55px'}}>이산화탄소 포집기가 고장나면 수리해야합니다.</p>
        <p style={{fontSize:'55px'}}>소행성으로인해 중앙정원이 파괴된다면 수리해야 합니다.</p>
      </div>
    );
}
export default MedicineRoom;
