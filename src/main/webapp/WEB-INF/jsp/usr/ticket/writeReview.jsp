<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 노말라이즈, 라이브러리까지 한번에 해결 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tailwindcss/2.2.7/tailwind.min.css" />
<!-- 테일윈드 불러오기 -->
<link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2/dist/tailwind.min.css" rel="stylesheet" type="text/css" />
<!-- 데이지 UI -->
<link href="https://cdn.jsdelivr.net/npm/daisyui@4.7.3/dist/full.min.css" rel="stylesheet" type="text/css" />
<!-- 폰트어썸 불러오기 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" />
<!--  사이트 공통 CSS -->
<link rel="stylesheet" href="/resource/common.css" />
<!--  사이트 공통 JS -->
<script src="/resource/common.js" defer="defer"></script>
</head>
<body>
  <div style="width: 500px;">
  <form action="/usr/ticket/writeReview" method="post">
  영화 제목 : ${param.movieTitle} <br>
    <input type="hidden" name="movieTitle" value="${param.movieTitle}">
    <input type="text" name="body" placeholder="리뷰를 남겨주세요." class="w-96 input input-bordered w-full max-w-xs" > <br>
    <input type="radio" name="rate" value="1" class="radio"> 1점
    <input type="radio" name="rate" value="2" class="radio"> 2점
    <input type="radio" name="rate" value="3" class="radio"> 3점
    <input type="radio" name="rate" value="4" class="radio"> 4점
    <input type="radio" name="rate" value="5" class="radio"> 5점
    <br>
    <input type="submit" value="작성">
    </form>
  </div>
</body>
</html>