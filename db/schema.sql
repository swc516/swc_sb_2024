#DB 생성
DROP DATABASE IF EXISTS swc_sb_2024;
CREATE DATABASE swc_sb_2024;
USE swc_sb_2024;

#게시물 테이블 생성
CREATE TABLE article (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title CHAR(100),
    `body` TEXT NOT NULL 
);

#게시물, 테스트 데이터 생성
INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = "제목1",
`body` = "내용1";

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = "제목2",
`body` = "내용2";

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = "제목3",
`body` = "내용3";


#회원 테이블 생성
CREATE TABLE `member` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(20) NOT NULL,
    loginPw CHAR(60) NOT NULL,
    `authLevel` SMALLINT(2) UNSIGNED DEFAULT 3 COMMENT '권한레벨(3= 일반, 7=관리자)',
    `name` CHAR(20) NOT NULL,
    nickname  CHAR(20) NOT NULL,
    cellphoneNo CHAR(20) NOT NULL,
    email CHAR(50) NOT NULL,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '탈퇴여부(0=탈퇴전, 1=탈퇴)',
    delDate DATETIME COMMENT '탈퇴날짜'
);

# 멤버 테이블에 즐겨찾는 영화관 추가
ALTER TABLE `member` ADD COLUMN favoriteCinema INT(10) UNSIGNED NOT NULL AFTER email;

#회원, 테스트 데이터 생성
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = "admin",
loginPw = "admin",
authLevel = 7,
`name` = "관리자",
nickname = "관리자",
cellphoneNo = "01011112222",
email = "admin@gmail.com";

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = "user1",
loginPw = "user1",
`name` = "사용자1",
nickname = "사용자1",
cellphoneNo = "01011112222",
email = "test1@gmail.com";


INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = "user2",
loginPw = "user2",
`name` = "사용자2",
nickname = "사용자2",
cellphoneNo = "01011112222",
email = "test2@gmail.com";

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = "user3",
loginPw = "user3",
`name` = "사용자3",
nickname = "사용자3",
cellphoneNo = "01011112222",
email = "test3@gmail.com";


# 게시물 테이블에 회원정보 추가
ALTER TABLE article ADD COLUMN memberId INT(10) UNSIGNED NOT NULL AFTER `updateDate`;

# 기존 게시물의 작성자를 2번 으로 지정 
UPDATE article 
SET memberId = 2
WHERE memberId = 0;

#회원 테이블 생성
CREATE TABLE board (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    `code` CHAR(50) NOT NULL UNIQUE COMMENT 'notice(공지사항), free1(자유게시판), free2(자유게시판2), ...',
    `name` CHAR(60) NOT NULL UNIQUE COMMENT '게시판 이름',
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '삭제여부(0=삭제전, 1=삭제)',
    delDate DATETIME COMMENT '삭제날짜'
);

# 기본 게시판 생성
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'notice',
`name` = '공지사항';

INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'free',
`name` = '자유게시판';

# 게시판 테이블에 boardId 컬럼 추가
ALTER TABLE article ADD COLUMN boardId INT(10) UNSIGNED NOT NULL AFTER memberId;

# 1, 2번 게시물을 공지사항 게시물로 지정
UPDATE article
SET boardId = 1
WHERE id IN(1,2);

# 3번 게시물을 자유게시판 게시물로 지정
UPDATE article
SET boardId = 2
WHERE id IN(3);

/*
# 게시물 개수 늘리기
insert into article(
regDate, updateDate, memberId, boardId, title, `body`
)

select now(), now(), FLOOR(RAND() * 2) + 1, FLOOR(RAND() * 2) + 1, CONCAT('제목_', RAND()), CONCAT('내용_', RAND())
from article; 
*/

# 게시물 테이블 hitCount 컬럼을 추가
ALTER TABLE article
ADD COLUMN hitCount INT(10) UNSIGNED NOT NULL DEFAULT 0;


# 리액션 포인트 테이블 
CREATE TABLE reactionPoint (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    relTypeCode CHAR(30) NOT NULL COMMENT '관련데이터타입코드',
    relId INT(10) UNSIGNED NOT NULL COMMENT '관련데이터번호',
    `point` SMALLINT(2) NOT NULL
    
);


# 게시물 테이블 goodReactionPoint 컬럼을 추가
ALTER TABLE article
ADD COLUMN goodReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0;

# 게시물 테이블 badReactionPoint 컬럼을 추가
ALTER TABLE article
ADD COLUMN badReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0;

SELECT Rp.relTypeCode,
Rp.relId,
SUM(IF(Rp.point > 0, Rp.point, 0)) AS goodReactionPoint,
SUM(IF(Rp.point < 0, Rp.point * 1, 0)) AS badReactionPoint
FROM reactionPoint AS Rp
GROUP BY Rp.relTypeCode, Rp.relId;

#각 게시물별, 좋아요, 싫어요 총합
SELECT Rp.relId,
SUM(IF(Rp.point > 0, Rp.point, 0)) AS goodReactionPoint,
SUM(IF(Rp.point < 0, Rp.point * -1, 0)) AS badReactionPoint
FROM reactionPoint AS Rp
WHERE relTypeCode = 'article'
GROUP BY Rp.relTypeCode, Rp.relId;


UPDATE article AS A
INNER JOIN(
	SELECT RP.relId,
	SUM(IF(RP.point > 0, RP.point, 0)) AS goodReactionPoint,
	SUM(IF(RP.point < 0, RP.point * -1, 0)) AS badReactionPoint
	FROM reactionPoint AS RP
	WHERE relTypeCode = 'article'
	GROUP BY RP.relTypeCode, RP.relId
) AS RP_SUM
ON A.id = RP_SUM.relId
SET A.goodReactionPoint = RP_SUM.goodReactionPoint,
A.badReactionPoint = RP_SUM.badReactionPoint;

# 댓글 테이블
CREATE TABLE reply (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    relTypeCode CHAR(30) NOT NULL COMMENT '관련데이터타입코드',
    relId INT(10) UNSIGNED NOT NULL COMMENT '관련데이터번호',
    `body` TEXT NOT NULL
);

## 댓글 테스트 데이터
INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
relTypeCode = 'article',
relId = 1,
`body` = '댓글 1';

INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
relTypeCode = 'article',
relId = 1,
`body` = '댓글 2';

INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
relTypeCode = 'article',
relId = 1,
`body` = '댓글 3';

INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
memberId = 3,
relTypeCode = 'article',
relId = 2,
`body` = '댓글 4';



# 댓글 좋아요 수, 싫어요 수 칼럼 추가
ALTER TABLE reply
ADD COLUMN goodReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE reply
ADD COLUMN badReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0;

# 댓글 테이블에 인덱스 걸기
ALTER TABLE reply ADD INDEX (relTypeCode, relId);

# 부가정보테이블
# 댓글 테이블 추가
CREATE TABLE attr (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    relTypeCode CHAR(20) NOT NULL,
    relId INT(10) UNSIGNED NOT NULL,
	typeCode CHAR(30) NOT NULL,
	type2Code CHAR(70) NOT NULL,
    `value` TEXT NOT NULL
);

# attr 유니크 인덱스 걸기
## 중복변수 생성금지
## 변수찾는 속도 최적화
ALTER TABLE attr ADD UNIQUE INDEX(relTypeCode, relId, typeCode, type2Code);

## 특정 조건을 만족하는 회원 또는 게시물(기타 데이터)를 빠르게 찾기 위해서
ALTER TABLE attr ADD INDEX (relTypeCode, typeCode, type2Code);

# attr에 만료날짜 추가
ALTER TABLE attr ADD COLUMN expireDate DATETIME NULL AFTER `value`;

# 로그인 비밀번호의 컬럼 길이를 100으로 늘림
ALTER TABLE `member` MODIFY COLUMN loginPw VARCHAR(100) NOT NULL;





CREATE TABLE genFile (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  regDate DATETIME DEFAULT NULL,
  updateDate DATETIME DEFAULT NULL,
  delDate DATETIME DEFAULT NULL,
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  relTypeCode CHAR(50) NOT NULL,
  relId INT(10) UNSIGNED NOT NULL,
  originFileName VARCHAR(100) NOT NULL,
  fileExt CHAR(10) NOT NULL,
  typeCode CHAR(20) NOT NULL,
  type2Code CHAR(20) NOT NULL,
  fileSize INT(10) UNSIGNED NOT NULL,
  fileExtTypeCode CHAR(10) NOT NULL,
  fileExtType2Code CHAR(10) NOT NULL,
  fileNo SMALLINT(2) UNSIGNED NOT NULL,
  fileDir CHAR(20) NOT NULL,
  PRIMARY KEY (id),
  KEY relId (relTypeCode,relId,typeCode,type2Code,fileNo)
);


#회원 생성용
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'user3',
loginPw = 'user3',
authLevel = 3,
`name` = '사용자3',
`nickname` = '사용자3',
cellphoneNo = '01011112222',
email = 'user3@gmail.com';


# 기존 회원의 비밀번호를 암호화 해서 저장
UPDATE `member` 
SET loginPw = SHA2(loginPw, 256);


#영화 테이블 생성
CREATE TABLE movie (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title CHAR(30) NOT NULL,
    `body`  TEXT NOT NULL,
	runningTime INT(10) NOT NULL,
	country CHAR(20) NOT NULL,
	director CHAR(30) NOT NULL,
    actor CHAR(50) NOT NULL,
	genre CHAR(20) NOT NULL, 
    releaseDate DATE NOT NULL,
    trailer CHAR(255),
    rate DOUBLE(5,3) NOT NULL DEFAULT 0,
    sellCount INT(10) NOT NULL DEFAULT 0,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    delDate DATETIME
    );


#영화관 테이블 생성
CREATE TABLE cinema (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    region CHAR(30) NOT NULL,
    branch CHAR(30) NOT NULL,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    delDate DATETIME
    );
    
#상영관 테이블 생성
CREATE TABLE theaterInfo (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	cinemaId INT(10) UNSIGNED NOT NULL,
    theaterInfoId INT(10) UNSIGNED NOT NULL DEFAULT 0,
    theater CHAR(20) NOT NULL,
    seatRow CHAR(5) NOT NULL,
    seatCol INT(5) NOT NULL,
    seatStatus CHAR(20) NOT NULL
);

#상영회차 테이블 생성
CREATE TABLE theaterTime (
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	cinemaId INT(10) UNSIGNED NOT NULL,
	theaterInfoId INT(10) UNSIGNED NOT NULL,
	theaterTimeId INT(10) UNSIGNED NOT NULL DEFAULT 0,
	theaterTime INT(10) UNSIGNED NOT NULL,
	`date` DATE NOT NULL,
	startTime DATETIME NOT NULL,
	endTime DATETIME NOT NULL,
	movieId INT(10) UNSIGNED NOT NULL,
	seatRow CHAR(5) NOT NULL,
	seatCol INT(5) NOT NULL,
	seatStatus CHAR(20) NOT NULL,
	seatSell TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
	buyMemberId INT(10) DEFAULT 0,
	buyDate DATETIME
);
SELECT * FROM movie;
UPDATE movie
SET runDate = DATE_ADD(NOW(),INTERVAL 1 DAY);
SELECT DATE_ADD(NOW(),INTERVAL 1 DAY) FROM DUAL;

INSERT INTO movie
SET regDate = NOW(),
updateDate = NOW(),
title = '댓글부대',
`body` = '실력 있지만 허세 가득한 사회부 기자 ‘임상진’ 대기업 ‘만전’의 비리를 취재하지만 오보로 판명되며 정직당한다. “기자님 기사 오보 아니었어요. 다 저희들이 만든 수법이에요” 그러던 어느 날, 의문의 제보자가 찾아온다. 자신을 온라인 여론 조작을 주도하는 댓글부대, 일명 ‘팀알렙’의 멤버라고 소개한 제보자는 돈만 주면 진실도 거짓으로, 거짓도 진실로 만들 수 있다고 하는데… “불법은 아니에요. 합법인지는 모르겠지만” 이 제보, 어디부터 진실이고, 어디까지 거짓인가?',
country = '대한민국',
runningTime = 109,
director = '안국진',
actor = '손석구, 김성철, 김동휘, 홍경',
genre = '범죄',
releaseDate = '24-03-27',
trailer = 'https://www.youtube.com/embed/6kjApj4YXgY';

INSERT INTO movie
SET regDate = NOW(),
updateDate = NOW(),
title = '고질라 X 콩: 뉴 엠파이어',
`body` = '고질라 X 콩, 이번에는 한 팀이다! ‘고질라’ VS ‘콩’, 두 타이탄의 전설적인 대결 이후 할로우 어스에 남은 ‘콩’은 드디어 애타게 찾던 동족을 발견하지만 그 뒤에 도사리고 있는 예상치 못한 위협에 맞닥뜨린다. 한편, 깊은 동면에 빠진 ‘고질라’는 알 수 없는 신호로 인해 깨어나고 푸른 눈의 폭군 ‘스카 킹’의 지배 아래 위기에 처한 할로우 어스를 마주하게 된다. 할로우 어스는 물론, 지구상에도 출몰해 전세계를 초토화시키는 타이탄들의 도발 속에서 ‘고질라’와 ‘콩’은 사상 처음으로 한 팀을 이뤄 반격에 나서기로 하는데…',
country = '미국, 중국',
runningTime = 115,
director = '애덤 윈가드',
actor = '댄 스티븐스, 레베카 홀',
genre = '애니메이션',
releaseDate = '24-03-27',
trailer = 'https://www.youtube.com/embed/6pfdNc20Eqs';

INSERT INTO movie
SET regDate = NOW(),
updateDate = NOW(),
title = '파묘',
`body` = '미국 LA, 거액의 의뢰를 받은 무당 ‘화림’(김고은)과 ‘봉길’(이도현)은 기이한 병이 대물림되는 집안의 장손을 만난다. 조상의 묫자리가 화근임을 알아챈 ‘화림’은 이장을 권하고, 돈 냄새를 맡은 최고의 풍수사 ‘상덕’(최민식)과 장의사 ‘영근’(유해진)이 합류한다. “전부 잘 알 거야… 묘 하나 잘못 건들면 어떻게 되는지” 절대 사람이 묻힐 수 없는 악지에 자리한 기이한 묘. ‘상덕’은 불길한 기운을 느끼고 제안을 거절하지만, ‘화림’의 설득으로 결국 파묘가 시작되고… 나와서는 안될 것이 나왔다.',
country = '대한민국',
runningTime = 134,
director = '장재현',
actor = '최민식, 김고은, 유해진, 이도현',
genre = '미스터리',
releaseDate = '24-02-22',
trailer = 'https://www.youtube.com/embed/PnLp7HaN1ao';

INSERT INTO movie
SET regDate = NOW(),
updateDate = NOW(),
title = '듄: 파트2',
`body` = '황제의 모략으로 멸문한 가문의 유일한 후계자 폴.(티모시 샬라메) 어머니 레이디 제시카(레베카 퍼거슨)와 간신히 목숨만 부지한 채 사막으로 도망친다. 그곳에서 만난 반란군들과 숨어 지내다 그들과 함께 황제의 모든 것을 파괴할 전투를 준비한다. 한편 반란군들의 기세가 높아질수록 불안해진 황제와 귀족 가문은 잔혹한 암살자 페이드 로타(오스틴 버틀러)를 보내 반란군을 몰살하려 하는데… 운명의 반격이 시작된다!',
country = '미국, 캐나다',
runningTime = 166,
director = '드니 뵐니브',
actor = '티모시 샬라메, 젠데이아 콜먼',
genre = '액션',
releaseDate = '24-02-28',
trailer = 'https://www.youtube.com/embed/81JOj5-xNGc';

INSERT INTO movie
SET regDate = NOW(),
updateDate = NOW(),
title = '1980',
`body` = '12·12 군사반란 불과 5개월 후 세상이 무너지고 있었다! 평생 중국 음식점 수타면을 뽑던 철수 할아버지는 1980년 5월 17일 드디어 자기 음식점을 오픈한다. 철수와 엄마, 아빠, 이모, 새신랑이 될 둘째 아들과 예비 신부까지 대가족은 이제 행복한 꿈만 꾸면 된다고 생각하는데',
country = '대한민국',
runningTime = 99,
director = '강승용',
actor = '강신일, 김규리, 백성현, 한구연',
genre = '드라마',
releaseDate = '24-03-27',
trailer = 'https://www.youtube.com/embed/F6Q_TFe8HHU';

INSERT INTO movie
SET regDate = NOW(),
updateDate = NOW(),
title = '드림쏭3',
`body` = '진정한 음악의 힘을 찾아서! 이번엔 오디션 프로다! 앵거스가 코치로 활약하는 TV쇼 ‘리듬 대전’이라는 음악 경연 프로그램을 접하게 된 버디, 음악보다는 코치들의 입담으로 말장난이 주가 되는 이 방송이 마음에 들지 않는다. 하지만 앵거스가 갑작스레 사라지고 새로 나온 경연 참가자들은 앵거스가 누군지도 모른다고 하자, 버디는 예정에 없던 앵거스의 후임으로 앉게 된다. 로큰롤이 최고라고 생각하는 버디와 팝스타가 되고 싶은 걸그룹 친구들은 과연 힘을 모아 경연을 잘 준비할 수 있을까? 우리 함께 꿈을 노래해!',
country = '미국, 중국',
runningTime = 87,
director = '안소니 벨',
actor = '',
genre = '애니메이션',
releaseDate = '24-03-28',
trailer = 'https://www.youtube.com/embed/I4JnzWicBHE';





INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = '서울',
branch = '도봉';

INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = '서울',
branch = '용산';


INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = '경기',
branch = '의정부';


INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = '충청',
branch = '세종';

INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = '전북',
branch = '순창';

INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = '전남',
branch = '광주';

INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = '경북',
branch = '경주';


INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = '부산',
branch = '해운대';


INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = '경남',
branch = '김해';


INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = '강원',
branch = '동해';

