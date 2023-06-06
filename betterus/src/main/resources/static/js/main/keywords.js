keywords = [
  "지구한바퀴<br>세계여행",
  "그림·웹툰",
  "시사·이슈",
  "IT<br>트랜트",
  "사진·촬영",
  "취향저격<br>영화 리뷰",
  "오늘은<br>이런 책",
  "뮤직 인사이드",
  "글쓰기<br>코치",
  "직장인<br>현실 조언",
  "스타트업<br>경험담",
  "육아<br>이야기",
  "요리·레시피",
  "건강·운동",
  "멘탈 관리<br>심리 탐구",
  "디자인<br>스토리",
  "문화·예술",
  "건출·설계",
  "인문학·철학",
  "쉽게 읽는<br>역사",
  "우리집<br>반려동물",
  "멋진<br>캘리그래피",
  "사랑·이별",
  "감성<br>에세이",
];
sideLength = 120;

const keywordsBox = document.querySelector(".keyword_big-box");

let keyword = "";
for (let i = 0; i < keywords.length; i++) {
  keyword += `<a class="keyword" style="top: ${parseInt(i / 8) * sideLength}px; 
              left: ${(i % 8) * sideLength}px">
                <span>${keywords[i]}</span>
              </a>`;
}
keywordsBox.innerHTML = keyword;

console.log(keywordsBox);
