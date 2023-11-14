import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logoImage from "../assets/trinitylogo.png"
import ModelViewer from '../components/3dmodelviewer'


const MainHome: React.FC = () => {
    const navigate = useNavigate();
    const policy = () => {
        navigate("/privatepolicy");
    };
    const intro = () => {
      navigate("/intro");
  };
  
    
    return (
      <div style={{ position: 'relative' }}>
         <div style={{ marginTop: '20px' }}>
          <ModelViewer modelPath="/assets/spaceship_colored_joint.fbx" fileType="fbx" />
        </div>
        <div style={{ position: 'absolute', top: '20px', left: '565px', zIndex: 1 }}>
          <div style={{display:'flex', flexDirection: 'column', alignItems:'center', justifyContent:'center'}}>
            <Link to="/intro">
              <img src={logoImage} alt="logo" />
            </Link>
            <h2>made by unity</h2>
              <h1 onClick={intro} style={{cursor: 'pointer', color:'red'}}>Trinity Story</h1>
            <p onClick={policy} style={{fontSize:'55px', cursor: 'pointer', marginTop:'50px'}}>개인정보 처리 방침</p>
          </div>
        </div>
      </div>
    );
}
export default MainHome;
