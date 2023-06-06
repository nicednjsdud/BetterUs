const sideBar = document.querySelector(".side-bar");
const bar = document.querySelector(".header__top i");
const body = document.querySelector("body");

function showSideBar() {
  sideBar.setAttribute("style", "left: 0");
  console.log("hi");
}

function hideSideBar(e) {
  if (e.clientX > 260) {
    sideBar.setAttribute("style", "left: -260px");
  }
}

bar.addEventListener("click", showSideBar);
window.addEventListener("click", hideSideBar);
