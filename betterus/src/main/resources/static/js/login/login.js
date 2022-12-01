const loginInput = document.querySelector(".section .input #login input");
const loginButton = document.querySelector(".section button");

let emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;


function loginLink(event){


  event.preventDefault();
  let isVaild=emailRegExp.test(loginInput.value);
  if(!isVaild){
    alert("유효하지 않은 이메일입니다.");
    return;
  }

 let form = document.getElementById("login");
 form.submit();
}

loginButton.addEventListener("click", loginLink);
