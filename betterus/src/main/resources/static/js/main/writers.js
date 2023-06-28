const categories = document.querySelector(".writers-categories");
const profileBoxes = document.querySelector(".writer-profile-boxes");

for (let i = 0; i < 3; i++) {
  categories.children[i].addEventListener("click", () => {
    const currentcategory = document.querySelector(".current-category");
    const currentbox = document.querySelector(".current-box");
    currentcategory.classList.remove("current-category");
    currentbox.classList.remove("current-box");

    categories.children[i].classList.add("current-category");
    profileBoxes.children[i].classList.add("current-box");
  });
}
