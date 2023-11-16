import React from 'react';
// import { useNavigate } from 'react-router-dom';
import logoImage from "../assets/trinitylogo.png"
import AnimatedNeonButtons from '../components/neonbtn';

const GameIntro: React.FC = () => {
  // const navigate = useNavigate();
  // const Rule = () => {
  //     window.scrollTo(0, 0);
  //     navigate("/rule");
  // };
  // const Home = () => {
  //   window.scrollTo(0, 0);
  //   navigate("/");
  // }

    return (
      <div style={{display:'flex', flexDirection: 'column', alignItems:'center', justifyContent:'center'}}>
        <img src={logoImage} alt="logo" />
        <h1 style={{fontSize:'120px'}}>Trinity</h1>
        <p style={{fontSize:'80px'}}>우주 탐사를 마치고 귀환 도중</p>
        <p style={{fontSize:'80px'}}>Trinity호에 우주 기생충이 침략했다!</p>
        <p style={{fontSize:'80px'}}>우주정거장 까지 걸리는 시간은 12일..!</p>
        <p style={{fontSize:'80px'}}>접촉을 차단하고 각자 협력하여 생존하라..!</p>
        <div>
          <AnimatedNeonButtons href='/'>
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            HOME
          </AnimatedNeonButtons>
          <AnimatedNeonButtons href='/rule'>
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            RULE
          </AnimatedNeonButtons>
        </div>
      </div>
    );
}
export default GameIntro;
