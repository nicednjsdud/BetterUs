const editorPicRightSlideButton = document.querySelector(
  ".slide__btn-right.editor__pic-btn"
);
const editorPicLeftSlideButton = document.querySelector(
  ".slide__btn-left.editor__pic-btn"
);
const slides = document.querySelector(".slides");
const slideIndex = document.querySelector(".wrap_slide__index");
const recmmndedArticlesSlides = document.querySelector(
  ".recmmnded-articles-slides"
);
const articlesRightSlideButton = document.querySelector(
  ".slide__btn-right.recmmnded-articles-btn"
);
const articlesLeftSlideButton = document.querySelector(
  ".slide__btn-left.recmmnded-articles-btn"
);

let slideNumber = 0;
const MAX = slides.childElementCount - 1;
const MIN = 0;
let movedLength = 0;

function editorPicSetTransform() {
  slides.setAttribute(
    "style",
    `transform: translateX(${-960 * slideNumber}px)`
  );
  const currentEmphasis = document.querySelector(".emphasis");
  currentEmphasis.classList.remove("emphasis");
  slideIndex.children[slideNumber].classList.add("emphasis");

  if (slideNumber == MAX) {
    editorPicRightSlideButton.classList.add("slide__btn-disappear");
    editorPicLeftSlideButton.classList.remove("slide__btn-disappear");
  } else if (slideNumber == MIN) {
    editorPicLeftSlideButton.classList.add("slide__btn-disappear");
    editorPicRightSlideButton.classList.remove("slide__btn-disappear");
  } else {
    editorPicLeftSlideButton.classList.remove("slide__btn-disappear");
    editorPicRightSlideButton.classList.remove("slide__btn-disappear");
  }
}

function editorPicRightSlide() {
  if (slideNumber < MAX) {
    slideNumber += 1;
    editorPicSetTransform();
  }
  console.log("hi");
}

function editorPicLeftSlide() {
  if (slideNumber > MIN) {
    slideNumber -= 1;
    editorPicSetTransform();
  }
}

function indexEmphasis() {
  const currentEmphasis = document.querySelector(".emphasis");
  currentEmphasis.classList.remove("emphasis");
  this.classList.add("emphasis");

  slideNumber = parseInt(this.getAttribute("value"));
  editorPicSetTransform();
}

function articlesSetTransform() {
  recmmndedArticlesSlides.setAttribute(
    "style",
    `transform: translateX(${movedLength}px)`
  );

  if (movedLength == -4800) {
    articlesRightSlideButton.classList.add("slide__btn-disappear");
    articlesLeftSlideButton.classList.remove("slide__btn-disappear");
  } else if (movedLength == 0) {
    articlesLeftSlideButton.classList.add("slide__btn-disappear");
    articlesRightSlideButton.classList.remove("slide__btn-disappear");
  } else {
    articlesLeftSlideButton.classList.remove("slide__btn-disappear");
    articlesRightSlideButton.classList.remove("slide__btn-disappear");
  }
}

function articlesRightSlide() {
  if (movedLength > -4800) {
    movedLength -= 960;
    articlesSetTransform();
  }
}

function articlesLeftSlide() {
  if (movedLength < 0) {
    movedLength += 960;
    articlesSetTransform();
  }
}

for (let i = 0; i <= MAX; i++) {
  slideIndex.children[i].addEventListener("click", indexEmphasis);
}

editorPicRightSlideButton.addEventListener("click", editorPicRightSlide);
editorPicLeftSlideButton.addEventListener("click", editorPicLeftSlide);

articlesRightSlideButton.addEventListener("click", articlesRightSlide);
articlesLeftSlideButton.addEventListener("click", articlesLeftSlide);
