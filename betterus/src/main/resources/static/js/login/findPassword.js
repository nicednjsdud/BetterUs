/* =========================================================
   닉네임·비밀번호 찾기 화면 (M000000006)
   이메일 인증코드로 본인 확인 후 닉네임 조회 또는 임시 비밀번호 발급
   ========================================================= */
(function () {
  // 이메일 인증 건너뛰기 — 서버 설정이 켜져 있으면 인증 단계를 통과시킨다.
  const skipMailAuth = (document.getElementById("mailAuthSkip") || {}).value === "true";

  const emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,}$/i;

  function setMsg(el, text, kind) {
    if (!el) return;
    el.textContent = text || "";
    el.classList.remove("is-ok", "is-error");
    if (kind) el.classList.add(kind);
  }

  function post(url, params) {
    return fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams(params),
    }).then((r) => r.json());
  }

  // ---- 탭 전환 ----
  const tabButtons = document.querySelectorAll(".find-tab");
  const panels = { id: document.getElementById("panel-id"), pw: document.getElementById("panel-pw") };
  tabButtons.forEach(function (btn) {
    btn.addEventListener("click", function () {
      const tab = btn.getAttribute("data-tab");
      tabButtons.forEach(function (b) {
        const active = b === btn;
        b.classList.toggle("is-active", active);
        b.setAttribute("aria-selected", active ? "true" : "false");
      });
      Object.keys(panels).forEach(function (k) {
        panels[k].hidden = k !== tab;
      });
    });
  });

  /**
   * 탭 하나를 구성한다.
   * @param prefix  요소 id 접두사 (findId / findPw)
   * @param mode    'id' | 'pw'
   */
  function setupTab(prefix, mode) {
    const email = document.getElementById(prefix + "Email");
    const sendBtn = document.getElementById(prefix + "SendBtn");
    const emailMsg = document.getElementById(prefix + "EmailMsg");
    const code = document.getElementById(prefix + "Code");
    const verifyBtn = document.getElementById(prefix + "VerifyBtn");
    const codeMsg = document.getElementById(prefix + "CodeMsg");
    const submitBtn = document.getElementById(prefix + "SubmitBtn");
    const result = document.getElementById(prefix + "Result");

    let verified = skipMailAuth;
    let cooldown = null;

    function emailValid() {
      return emailRegExp.test((email.value || "").trim());
    }

    sendBtn.addEventListener("click", function () {
      const addr = email.value.trim();
      if (!emailValid()) {
        setMsg(emailMsg, "이메일 형식을 확인해 주세요.", "is-error");
        return;
      }
      sendBtn.disabled = true;
      post("/api/email_auth_codes", { email: addr })
        .then(function (data) {
          if (data.sent) {
            code.disabled = false;
            verifyBtn.disabled = false;
            setMsg(emailMsg, data.devCode
              ? "인증코드를 발송했습니다. (개발 모드 코드: " + data.devCode + ")"
              : "인증코드를 발송했습니다. 메일을 확인해 주세요.", "is-ok");
            startCooldown(data.cooldownSeconds || 60);
          } else {
            setMsg(emailMsg, "발송에 실패했습니다. 잠시 후 다시 시도해 주세요.", "is-error");
          }
        })
        .catch(function () {
          setMsg(emailMsg, "발송에 실패했습니다. 잠시 후 다시 시도해 주세요.", "is-error");
        });
    });

    function startCooldown(seconds) {
      let remain = seconds;
      sendBtn.textContent = "재발송 (" + remain + "s)";
      cooldown = setInterval(function () {
        remain -= 1;
        if (remain <= 0) {
          clearInterval(cooldown);
          cooldown = null;
          sendBtn.textContent = "인증코드 발송";
          sendBtn.disabled = false;
        } else {
          sendBtn.textContent = "재발송 (" + remain + "s)";
        }
      }, 1000);
    }

    code.addEventListener("input", function () {
      code.value = code.value.replace(/[^0-9]/g, "").slice(0, 6);
    });

    verifyBtn.addEventListener("click", function () {
      const c = code.value.trim();
      if (c.length !== 6) {
        setMsg(codeMsg, "6자리 숫자를 입력해 주세요.", "is-error");
        return;
      }
      post("/api/email_auth_code_verifications", { email: email.value.trim(), authCode: c })
        .then(function (data) {
          if (data.verified) {
            verified = true;
            submitBtn.disabled = false;
            setMsg(codeMsg, "인증되었습니다.", "is-ok");
          } else {
            setMsg(codeMsg, "인증코드가 올바르지 않거나 만료되었습니다.", "is-error");
          }
        })
        .catch(function () {
          setMsg(codeMsg, "확인에 실패했습니다. 다시 시도해 주세요.", "is-error");
        });
    });

    submitBtn.addEventListener("click", function () {
      if (!verified) {
        setMsg(codeMsg, "이메일 인증을 먼저 완료해 주세요.", "is-error");
        return;
      }
      const addr = email.value.trim();
      const c = code.value.trim();
      submitBtn.disabled = true;
      submitBtn.textContent = "처리 중…";

      if (mode === "id") {
        post("/api/member_id_inquiries", { email: addr, authCode: c })
          .then(function (data) {
            if (data.ok) {
              document.getElementById("findIdValue").textContent = data.nickName;
              result.hidden = false;
              setMsg(codeMsg, "", null);
            } else {
              setMsg(codeMsg, data.message || "닉네임을 찾을 수 없습니다.", "is-error");
            }
          })
          .catch(function () {
            setMsg(codeMsg, "처리 중 오류가 발생했습니다.", "is-error");
          })
          .finally(function () {
            submitBtn.disabled = false;
            submitBtn.textContent = "닉네임 확인";
          });
      } else {
        post("/api/password_resets", { email: addr, authCode: c })
          .then(function (data) {
            if (data.ok) {
              result.hidden = false;
              setMsg(codeMsg, "", null);
            } else {
              setMsg(codeMsg, data.message || "처리 중 오류가 발생했습니다.", "is-error");
            }
          })
          .catch(function () {
            setMsg(codeMsg, "처리 중 오류가 발생했습니다.", "is-error");
          })
          .finally(function () {
            submitBtn.disabled = false;
            submitBtn.textContent = "임시 비밀번호 받기";
          });
      }
    });
  }

  setupTab("findId", "id");
  setupTab("findPw", "pw");
})();
