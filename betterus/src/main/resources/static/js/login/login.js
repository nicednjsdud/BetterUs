const loginInput = document.querySelector(".section .input #login #email-inputbox");
const pwInput = document.querySelector(".section .input #login #pw-inputbox");
const loginButton = document.querySelector(".section button");

let emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

<<<<<<< HEAD
console.log(pwInput);
=======
>>>>>>> JWYBrc

function loginLink(event){


  event.preventDefault();
  let isVaildId=emailRegExp.test(loginInput.value);
  if(!isVaildId){
    alert("유효하지 않은 이메일입니다.");
    return;
<<<<<<< HEAD
  } 

  if(!pwInput.value){
    alert("비밀번호를 입력해주세요");
    return;
  } 
  
  if ((isVaildId == true) && (pwInput.value)){
    location.href = "confirm";
  }
}  
=======
  }

 let form = document.getElementById("login");
 form.submit();
}

>>>>>>> JWYBrc
loginButton.addEventListener("click", loginLink);
