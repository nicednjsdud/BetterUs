const emailInput = document.querySelector(".section .center .input #email");
const nicknameInput = document.querySelector(".section .center .input #nickname");
const pwInput = document.querySelector(".section .center .input #password");
const pwCheckInput = document.querySelector(".section .center .input #pwCheck");
const signUpButton = document.querySelector(".section .center .login-button #signUp");

console.log(emailInput.getAttribute( 'check_result' ));
 

let emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
let passwordRegExp =/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;



function loginLink(event){
  event.preventDefault();
  let isValid_email=emailRegExp.test(emailInput.value);
//  if(!isValid_email){
//    alert("유효하지 않은 이메일입니다.");
//    return;
//  }

  let isValid_pwCond=passwordRegExp.test(pwInput.value);
  if(!isValid_pwCond){
    alert("문자, 숫자, 특수문자를 포함하여 8~15자리로 설정해주세요.");
    return;
  } 
  
  if(!nicknameInput.value){
    alert("닉네임을 입력해주세요");
    return;
  } 

  let isValid_pwCheck= ((pwInput.value) == (pwCheckInput.value))
  if(!isValid_pwCheck){
    alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    return;
  } 

//  let isDuplicate_email = (emailInput.getAttribute( 'check_result' ) == "success");
//  if(!isDuplicate_email){
//    alert("이메일 중복 확인을 진행해주십시오.");
//    return;
//  }
//
//  let isCertificate_email = (emailInput.getAttribute( 'certification' ) == "success");
//  if(!isCertificate_email){
//    alert("이메일 인증을 진행해주십시오.");
//    return;
//  }

  let isDuplicate_nickname = (nicknameInput.getAttribute( 'check_result2' ) == "success");
  if(!isDuplicate_nickname){
    alert("닉네임 중복 확인을 진행해주십시오.");
    return;
  } 

//  if ((isVaild_email) && (isVaild_pwCond) && (isVaild_pwCheck)
//     && (isDuplicate_email) && (isCertificate_email) && (isDuplicate_nickname)){
//    location.href = "loginSuccess";
//  }
    else{
        const form = document.querySelector('#form');
        form.submit();
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

