const rightSlideButton = document.querySelector(".slide__btn-right");
const leftSlideButton = document.querySelector(".slide__btn-left");
const slides = document.querySelector(".slides");
const slideIndex = document.querySelector(".wrap_slide__index");

let slideNumber = 0;
const MAX = slides.childElementCount - 1;
const MIN = 0;

function setTransform() {
  slides.setAttribute(
    "style",
    `transform: translateX(${-960 * slideNumber}px)`
  );
  const currentEmphasis = document.querySelector(".emphasis");
  currentEmphasis.classList.remove("emphasis");
  slideIndex.children[slideNumber].classList.add("emphasis");

  if (slideNumber == MAX) {
    rightSlideButton.classList.add("slide__btn-disappear");
    leftSlideButton.classList.remove("slide__btn-disappear");
  } else if (slideNumber == MIN) {
    leftSlideButton.classList.add("slide__btn-disappear");
    rightSlideButton.classList.remove("slide__btn-disappear");
  } else {
    leftSlideButton.classList.remove("slide__btn-disappear");
    rightSlideButton.classList.remove("slide__btn-disappear");
  }
}

function rightSlide() {
  if (slideNumber < MAX) {
    slideNumber += 1;
    setTransform();
  }
}

function leftSlide() {
  if (slideNumber > MIN) {
    slideNumber -= 1;
    setTransform();
  }
}

function indexEmphasis() {
  const currentEmphasis = document.querySelector(".emphasis");
  currentEmphasis.classList.remove("emphasis");
  this.classList.add("emphasis");

  slideNumber = parseInt(this.getAttribute("value"));
  setTransform();
}

for (let i = 0; i <= MAX; i++) {
  slideIndex.children[i].addEventListener("click", indexEmphasis);
}

rightSlideButton.addEventListener("click", rightSlide);
leftSlideButton.addEventListener("click", leftSlide);
