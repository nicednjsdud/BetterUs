/* =========================================================
   로그인 화면 클라이언트 검증 (M000000004)
   - 빈 값·이메일 형식을 1차로 확인하고, 통과 시 서버로 전송한다.
   - 중복 제출을 막는다.
   ========================================================= */
(function () {
  const form = document.getElementById("login");
  if (!form) return;

  const emailInput = form.querySelector("#email");
  const passwordInput = form.querySelector("#password");
  const submitButton = form.querySelector(".auth-card__submit");
  const errorBox = document.querySelector(".auth-card__error");

  const emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,}$/i;

  function showError(message) {
    if (errorBox) {
      errorBox.textContent = message;
      errorBox.setAttribute("role", "alert");
    } else {
      alert(message);
    }
  }

  form.addEventListener("submit", function (event) {
    const email = (emailInput.value || "").trim();
    const password = passwordInput.value || "";

    if (!email && !password) {
      event.preventDefault();
      showError("이메일과 비밀번호를 모두 입력해 주세요.");
      emailInput.focus();
      return;
    }

    if (!emailRegExp.test(email)) {
      event.preventDefault();
      showError("이메일 형식이 올바르지 않습니다.");
      emailInput.focus();
      return;
    }

    if (!password) {
      event.preventDefault();
      showError("비밀번호를 입력해 주세요.");
      passwordInput.focus();
      return;
    }

    // 중복 제출 방지
    if (submitButton) {
      submitButton.disabled = true;
    }
  });
})();
