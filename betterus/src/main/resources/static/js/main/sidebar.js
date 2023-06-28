const sideBar = document.querySelector(".side-bar");
const bar = document.querySelectorAll(".header__content-left i");

console.log(bar);

function showSideBar() {
  sideBar.setAttribute("style", "left: 0");
  console.log("hi");
}

function hideSideBar(e) {
  if (e.clientX > 260) {
    sideBar.setAttribute("style", "left: -260px");
  }
}

bar.forEach((item) => {
  item.addEventListener("click", showSideBar);
});
// bar.addEventListener("click", showSideBar);
window.addEventListener("click", hideSideBar);
