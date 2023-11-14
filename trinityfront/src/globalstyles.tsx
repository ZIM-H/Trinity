import { createGlobalStyle } from 'styled-components';
import backgroundImage1 from './assets/backgroundgifimg.gif';

const GlobalStyles = createGlobalStyle`

  body {
    background-image: url(${backgroundImage1});
    background-size: cover;
    // background-size: contain;
    background-repeat: no-repeat, no-repeat;
    background-attachment: fixed;
    background-position: left, right;
    // background-position: left top, right top;
    margin: 0;
    font-family: Ftstardust;
    color: white;
    background-color: black;
  }



  ::placeholder {
    font-family: Ftstardust;
    
    /* 다른 스타일 옵션 추가 가능 */
  }

  /* 추가: input 요소의 value 스타일 변경 */
  input {
    font-family: Ftstardust;
    padding-top: 8px;
    /* 다른 스타일 옵션 추가 가능 */
  }

  button {
    font-family: Ftstardust;
    /* 다른 스타일 옵션 추가 가능 */
  }
`;

export default GlobalStyles;