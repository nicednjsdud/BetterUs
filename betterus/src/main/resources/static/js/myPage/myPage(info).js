const currnePwInput = document.querySelector(".section .center .input-total .input-box .input #currentPassword");
const pwInput = document.querySelector(".section .center .input-total .input-box .input #chagePassword");
const pwCheckInput = document.querySelector(".section .center .input-total .input-box .input #checkChagePassword");
// const nicknameCorrectButton = document.querySelector(".section .right .nickname-button #nicknameCorrect");
// const introCorrectButton = document.querySelector(".section .right #introCorrect");
const pwCorrectcButton = document.querySelector(".section .right .pw-button #pwCorrect");

let emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
let passwordRegExp =/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;



function pwCheckLink(event){
  event.preventDefault();
  // let isVaild1=passwordRegExp.test(pwInput.value);
  // 현재 비밀번호와 같은지 확인해야 함

  let isValid_currentPw = (currnePwInput.getAttribute( 'check_result' ) == "success");
  if(!isValid_currentPw){
    alert("현재비밀번호를 확인해주세요");
    return;
  } 

  let isVaild_pwCond=passwordRegExp.test(pwInput.value);
  if(!isVaild_pwCond){
    alert("문자, 숫자, 특수문자를 포함하여 8~15자리로 설정해주세요.");
    return;
  } 
  
  let isVaild_pwCheck= ((pwInput.value) == (pwCheckInput.value))
  if(!isVaild_pwCheck){
    alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    return;
  } 

  if ((isVaild_pwCond == true) && (isVaild_pwCheck == true) && (isValid_currentPw)){
    location.href = "#";
  }
}
  
pwCorrectcButton.addEventListener("click", pwCheckLink);