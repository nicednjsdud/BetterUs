const emailInput = document.querySelector(".section .center .input #email");
const pwInput = document.querySelector(".section .center .input #password");
const pwCheckInput = document.querySelector(".section .center .input #pwCheck");
const signUpButton = document.querySelector(".section .center .login-button #signUp");


let emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
let passwordRegExp =/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;

function loginLink(event){
  event.preventDefault();
  let isVaild=emailRegExp.test(emailInput.value);
  if(!isVaild){
    alert("유효하지 않은 이메일입니다.");
    return;
  } 

  let isVaild1=passwordRegExp.test(pwInput.value);
  if(!isVaild1){
    alert("문자, 숫자, 특수문자를 포함하여 8~15자리로 설정해주세요.");
    return;
  } 
  
  let isVaild2= ((pwInput.value) == (pwCheckInput.value))
  if(!isVaild2){
    alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    return;
  } 

  if ((isVaild == true) && (isVaild1 == true) && (isVaild2 == true)){
    location.href = "loginSuccess";
  }
}
  

signUpButton.addEventListener("click", loginLink);


// function pwLink(event){
//   event.preventDefault();
//   let isVaild=passwordRegExp.test(pwInput.value);
//   if(!isVaild){
//     alert("문자, 숫자, 특수문자를 포함하여 8~15자리로 설정해주세요.");
//     return;
//   } 
// }

// function pwCheckLink(event){
//   event.preventDefault();
//   let isVaild= ((pwInput.value) == (pwCheckInput.value))
//   if(!isVaild){
//     alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
//     return;
//   } 
//   // location.href = "loginSuccess";
// }

