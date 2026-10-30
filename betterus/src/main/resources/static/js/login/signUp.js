/* =========================================================
   회원가입 화면 (M000000005)
   이메일 인증 → 닉네임 중복확인 → 비밀번호 확인 → 약관 동의 후 가입
   ========================================================= */
(function () {
  const form = document.getElementById("signup");
  if (!form) return;

  const emailInput = document.getElementById("email");
  const sendCodeBtn = document.getElementById("sendCodeBtn");
  const emailMsg = document.getElementById("emailMsg");
  const authCodeInput = document.getElementById("authCode");
  const verifyCodeBtn = document.getElementById("verifyCodeBtn");
  const codeMsg = document.getElementById("codeMsg");
  const nicknameInput = document.getElementById("nickname");
  const checkNickBtn = document.getElementById("checkNickBtn");
  const nickMsg = document.getElementById("nickMsg");
  const passwordInput = document.getElementById("password");
  const strengthBar = document.getElementById("strength");
  const pwMsg = document.getElementById("pwMsg");
  const pwConfirmInput = document.getElementById("passwordConfirm");
  const pwConfirmMsg = document.getElementById("pwConfirmMsg");
  const submitBtn = document.getElementById("signupSubmit");
  const agreeAll = document.getElementById("agreeAll");
  const agrees = document.querySelectorAll(".agree");

  const emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,}$/i;

  let emailVerified = false;
  let verifiedEmail = "";
  let nickChecked = false;
  let checkedNickValue = "";
  let cooldownTimer = null;

  function setMsg(el, text, kind) {
    el.textContent = text || "";
    el.classList.remove("is-ok", "is-error");
    if (kind) el.classList.add(kind);
  }

  function emailValid() {
    return emailRegExp.test((emailInput.value || "").trim());
  }

  function refreshSendBtn() {
    sendCodeBtn.disabled = !emailValid() || cooldownTimer !== null || emailVerified;
  }

  // ---- 이메일 인증코드 발송 ----
  sendCodeBtn.addEventListener("click", function () {
    const email = emailInput.value.trim();
    if (!emailValid()) {
      setMsg(emailMsg, "이메일 형식을 확인해 주세요.", "is-error");
      return;
    }
    sendCodeBtn.disabled = true;
    fetch("/api/email_auth_codes", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({ email: email }),
    })
      .then((r) => r.json())
      .then((data) => {
        if (data.sent) {
          setMsg(emailMsg, "인증코드를 발송했습니다. 메일을 확인해 주세요.", "is-ok");
          if (data.devCode) {
            setMsg(emailMsg, "인증코드를 발송했습니다. (개발 모드 코드: " + data.devCode + ")", "is-ok");
          }
          startCooldown(data.cooldownSeconds || 60);
        } else {
          setMsg(emailMsg, "발송에 실패했습니다. 잠시 후 다시 시도해 주세요.", "is-error");
        }
      })
      .catch(() => setMsg(emailMsg, "발송에 실패했습니다. 잠시 후 다시 시도해 주세요.", "is-error"));
  });

  function startCooldown(seconds) {
    let remain = seconds;
    sendCodeBtn.disabled = true;
    const original = "인증코드 발송";
    sendCodeBtn.textContent = "재발송 (" + remain + "s)";
    cooldownTimer = setInterval(function () {
      remain -= 1;
      if (remain <= 0) {
        clearInterval(cooldownTimer);
        cooldownTimer = null;
        sendCodeBtn.textContent = original;
        refreshSendBtn();
      } else {
        sendCodeBtn.textContent = "재발송 (" + remain + "s)";
      }
    }, 1000);
  }

  // 이메일 값을 바꾸면 인증 상태를 초기화한다.
  emailInput.addEventListener("input", function () {
    if (emailVerified && emailInput.value.trim() !== verifiedEmail) {
      emailVerified = false;
      verifiedEmail = "";
      authCodeInput.readOnly = false;
      authCodeInput.value = "";
      authCodeInput.classList.remove("is-invalid");
      setMsg(codeMsg, "", null);
      setMsg(emailMsg, "", null);
    }
    refreshSendBtn();
  });

  // ---- 인증코드 입력/확인 ----
  authCodeInput.addEventListener("input", function () {
    authCodeInput.value = authCodeInput.value.replace(/[^0-9]/g, "").slice(0, 6);
  });

  verifyCodeBtn.addEventListener("click", function () {
    const email = emailInput.value.trim();
    const code = authCodeInput.value.trim();
    if (!emailVerified && code.length !== 6) {
      setMsg(codeMsg, "6자리 숫자를 입력해 주세요.", "is-error");
      return;
    }
    fetch("/api/email_auth_code_verifications", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({ email: email, authCode: code }),
    })
      .then((r) => r.json())
      .then((data) => {
        if (data.verified) {
          emailVerified = true;
          verifiedEmail = email;
          emailInput.readOnly = true;
          authCodeInput.readOnly = true;
          setMsg(codeMsg, "인증되었습니다.", "is-ok");
          refreshSendBtn();
        } else {
          setMsg(codeMsg, "인증코드가 일치하지 않습니다.", "is-error");
        }
      })
      .catch(() => setMsg(codeMsg, "확인에 실패했습니다. 다시 시도해 주세요.", "is-error"));
  });

  // ---- 닉네임 중복확인 ----
  nicknameInput.addEventListener("input", function () {
    if (nickChecked && nicknameInput.value.trim() !== checkedNickValue) {
      nickChecked = false;
      checkedNickValue = "";
      setMsg(nickMsg, "중복확인을 해주세요.", null);
    }
  });

  checkNickBtn.addEventListener("click", function () {
    const nickName = nicknameInput.value.trim();
    if (nickName.length < 2 || nickName.length > 12) {
      setMsg(nickMsg, "닉네임은 2~12자로 입력해 주세요.", "is-error");
      return;
    }
    fetch("/api/member_nicknames", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({ nickName: nickName }),
    })
      .then((r) => r.json())
      .then((data) => {
        if (data.available) {
          nickChecked = true;
          checkedNickValue = nickName;
          setMsg(nickMsg, "사용 가능한 닉네임입니다.", "is-ok");
        } else {
          nickChecked = false;
          setMsg(nickMsg, "이미 사용 중인 닉네임입니다.", "is-error");
        }
      })
      .catch(() => setMsg(nickMsg, "확인에 실패했습니다. 다시 시도해 주세요.", "is-error"));
  });

  // ---- 비밀번호 강도/일치 ----
  function pwValid() {
    const v = passwordInput.value || "";
    return v.length >= 8 && /[A-Za-z]/.test(v) && /[0-9]/.test(v);
  }

  passwordInput.addEventListener("input", function () {
    const v = passwordInput.value || "";
    let level = 0;
    if (v.length >= 8) level = 1;
    if (level === 1 && /[A-Za-z]/.test(v) && /[0-9]/.test(v)) level = 2;
    if (level === 2 && /[^A-Za-z0-9]/.test(v)) level = 3;
    strengthBar.setAttribute("data-level", String(level));
    if (v && !pwValid()) {
      setMsg(pwMsg, "8자 이상 영문+숫자 조합이어야 합니다.", "is-error");
    } else {
      setMsg(pwMsg, "", null);
    }
    checkConfirm();
  });

  pwConfirmInput.addEventListener("input", checkConfirm);

  function checkConfirm() {
    if (!pwConfirmInput.value) {
      setMsg(pwConfirmMsg, "", null);
      return;
    }
    if (passwordInput.value === pwConfirmInput.value) {
      setMsg(pwConfirmMsg, "비밀번호가 일치합니다.", "is-ok");
    } else {
      setMsg(pwConfirmMsg, "비밀번호가 일치하지 않습니다.", "is-error");
    }
  }

  // ---- 약관 ----
  agreeAll.addEventListener("change", function () {
    agrees.forEach((c) => (c.checked = agreeAll.checked));
  });

  // ---- 제출 ----
  form.addEventListener("submit", function (event) {
    if (!emailVerified) {
      event.preventDefault();
      setMsg(emailMsg, "이메일 인증을 완료해 주세요.", "is-error");
      return;
    }
    if (!nickChecked || nicknameInput.value.trim() !== checkedNickValue) {
      event.preventDefault();
      setMsg(nickMsg, "닉네임 중복확인을 해주세요.", "is-error");
      return;
    }
    if (!pwValid()) {
      event.preventDefault();
      setMsg(pwMsg, "비밀번호는 8자 이상 영문+숫자 조합이어야 합니다.", "is-error");
      return;
    }
    if (passwordInput.value !== pwConfirmInput.value) {
      event.preventDefault();
      setMsg(pwConfirmMsg, "비밀번호가 일치하지 않습니다.", "is-error");
      return;
    }
    const required = [document.getElementById("agreeTerms"), document.getElementById("agreePrivacy")];
    if (required.some((c) => !c.checked)) {
      event.preventDefault();
      alert("필수 약관에 동의해 주세요.");
      return;
    }
    submitBtn.disabled = true;
    submitBtn.textContent = "처리 중…";
  });

  refreshSendBtn();
})();
