/* =========================================================
   회원정보 수정 화면 (M000000008)
   - 비밀번호를 바꾸면 현재 비밀번호를 요구한다.
   - 탈퇴는 현재 비밀번호를 다시 한 번 확인한다.
   ========================================================= */
(function () {
  const editForm = document.getElementById("editForm");
  const newPassword = document.getElementById("newPassword");
  const currentPassword = document.getElementById("currentPassword");
  const pwMsg = document.getElementById("pwMsg");

  function setMsg(text, kind) {
    if (!pwMsg) return;
    pwMsg.textContent = text || "";
    pwMsg.classList.remove("is-ok", "is-error");
    if (kind) pwMsg.classList.add(kind);
  }

  if (editForm) {
    editForm.addEventListener("submit", function (event) {
      if (newPassword && newPassword.value) {
        if (newPassword.value.length < 8) {
          event.preventDefault();
          setMsg("새 비밀번호는 8자 이상이어야 합니다.", "is-error");
          return;
        }
        if (!currentPassword || !currentPassword.value) {
          event.preventDefault();
          setMsg("비밀번호를 바꾸려면 현재 비밀번호를 입력해 주세요.", "is-error");
          currentPassword && currentPassword.focus();
        }
      }
    });
  }

  const deleteForm = document.getElementById("deleteForm");
  const deletePassword = document.getElementById("deletePassword");
  if (deleteForm) {
    deleteForm.addEventListener("submit", function (event) {
      if (!deletePassword || !deletePassword.value) {
        event.preventDefault();
        alert("탈퇴하려면 현재 비밀번호를 입력해 주세요.");
        deletePassword && deletePassword.focus();
        return;
      }
      if (!confirm("정말 탈퇴하시겠습니까? 되돌릴 수 없습니다.")) {
        event.preventDefault();
      }
    });
  }
})();
