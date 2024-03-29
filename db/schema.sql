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
SELECT FLOOR(RAND() * 3) + 1;

SELECT *FROM article;

# 게시물 테이블 hitCount 컬럼을 추가
ALTER TABLE article
ADD COLUMN hitCount INT(10) UNSIGNED NOT NULL DEFAULT 0;

DESC article;


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

# 리액션포인트 테스트 데이터
## 1번 회원이 1번 article에 대해서 싫어요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
relTypeCode = 'article',
relId = 1,
`point` = -1;

## 1번 회원이 2번 article에 대해서 좋아요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
relTypeCode = 'article',
relId = 2,
`point` = 1;

## 2번 회원이 1번 article에 대해서 싫어요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
relTypeCode = 'article',
relId = 1,
`point` = -1;

## 2번 회원이 2번 article에 대해서 좋아요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
relTypeCode = 'article',
relId = 2,
`point` = 1;

## 3번 회원이 1번 article에 대해서 좋아요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 3,
relTypeCode = 'article',
relId = 1,
`point` = 1;

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

SELECT * FROM article;

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

EXPLAIN SELECT R.*,
M.nickname AS extra__writerName
FROM reply AS R
LEFT JOIN `member` AS M
ON R.memberId = M.id
WHERE R.relTypeCode = 'article'
AND R.relId = 3
ORDER BY R.id DESC;


# 댓글 좋아요 수, 싫어요 수 칼럼 추가
ALTER TABLE reply
ADD COLUMN goodReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE reply
ADD COLUMN badReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0;

# 댓글 테이블에 인덱스 걸기
ALTER TABLE reply ADD INDEX (relTypeCode, relId);

SELECT * FROM `member`;

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

SELECT * FROM `reactionPoint`;
SELECT * FROM `reply`;
SELECT * FROM `member`;


#영화 테이블 생성

CREATE TABLE movie (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title CHAR(30) NOT NULL,
    `body`  CHAR(100) NOT NULL,
    rate DOUBLE(5,3) NOT NULL DEFAULT 0,
    `count` INT(10) NOT NULL DEFAULT 0,
    runDate DATETIME NOT NULL,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    delDate DATETIME
    );
    
    
#영화, 테스트 데이터 생성
INSERT INTO movie
SET regDate = NOW(),
updateDate = NOW(),
title = "영화1",
`body` = "영화내용1",
runDate = '2024-05-01 06:00:00';

INSERT INTO movie
SET regDate = NOW(),
updateDate = NOW(),
title = "영화2",
`body` = "영화내용2",
rate = 0.0,
`count` = 0,
runDate = '2024-03-24 06:00:00';


SELECT * FROM movie;

#영화관 테이블 생성
CREATE TABLE cinema (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    region CHAR(30) NOT NULL,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    delDate DATETIME
    );
    
INSERT INTO cinema
SET regDate = NOW(),
updateDate = NOW(),
region = "서울_방학";
    
    #상영관 테이블 생성
CREATE TABLE theater (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    relTypeCode CHAR(30) NOT NULL,
    relId INT(10) UNSIGNED NOT NULL,
    theaterName CHAR(20) NOT NULL,
    seatId CHAR(10) NOT NULL,
    seatNo INT(5) UNSIGNED NOT NULL,
    seatStatus CHAR(20) NOT NULL
);


SELECT * FROM cinema; 

SELECT * FROM theater; 
SELECT * FROM genFile;
SELECT * FROM movie; 
SELECT * FROM `member`; 

