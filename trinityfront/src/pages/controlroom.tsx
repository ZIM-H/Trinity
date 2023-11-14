import React from 'react';
import { Link } from 'react-router-dom';
import logoImage from "../assets/trinitylogo.png"

const ControlRoom: React.FC = () => {
    
    return (
      <div style={{display:'flex', flexDirection: 'column', alignItems:'center', justifyContent:'center'}}>
        <Link to="/rule">
          <img src={logoImage} alt="logo" />
        </Link>
        <img src="assets/Trinity_ctrlroom_UI.png" alt="logo" />
        <p style={{fontSize:'55px'}}>Trinity의 조타실입니다.</p>
        <p style={{fontSize:'55px'}}>이 곳에서 우주를 관측할 수 있습니다!</p>
        <p style={{fontSize:'55px'}}>관측기를 누르면 소행성과 블랙홀이 관측됩니다.</p>
        <p style={{fontSize:'55px'}}>소행성이 관측되고 파괴하지 않는다면</p>
        <p style={{fontSize:'55px'}}>중앙정원이 파괴되고 투입한 비료수가 0이 됩니다!</p>
        <p style={{fontSize:'55px'}}>보호막을 업그레이드하여 활성화할 수 있습니다!</p>
        <p style={{fontSize:'55px'}}>보호막이 활성화되면 소행성을 막을 수 있습니다.</p>
      </div>
    );
}
export default ControlRoom;
