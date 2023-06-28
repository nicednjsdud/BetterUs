const startOpenBtn = document.querySelector(".header__content-right p");
const searchOpenBtn = document.querySelector(".header__content-right i");
const startModal = document.querySelector(".start");
const startExitBtn = document.querySelector(".start-exit i");
const searchModal = document.querySelector(".search");
const searchExitBtn = document.querySelector(".search-exit i");

function startOpen() {
  startModal.removeAttribute("style");
}
function startExit() {
  startModal.setAttribute("style", "display: none");
}
function searchOpen() {
  searchModal.removeAttribute("style");
}
function searchExit() {
  searchModal.setAttribute("style", "display: none");
}

startOpenBtn.addEventListener("click", startOpen);
startExitBtn.addEventListener("click", startExit);
searchOpenBtn.addEventListener("click", searchOpen);
searchExitBtn.addEventListener("click", searchExit);
