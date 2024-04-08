<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="관리자 " />
<%@include file="../../common/head.jspf"%>

<div class="container mx-auto px-3">
  <section class="mt-5">
    <span style="font-size:30px">▶게시글 관련</span><br>
    <span class="container mx-auto px-3">
      <a href="/usr/article/write?boardId=1" type="submit" class="btn btn-primary">공지사항 작성</a>
    </span>
    <span class="container mx-auto px-3">
      <a href="/adm/article/list" type="submit" class="btn btn-primary">전체 게시글 관리</a>
    </span>
    <span class="container mx-auto px-3">
      <a href="/adm/reply/list" type="submit" class="btn btn-primary">전체 댓글 관리</a>
    </span>
  </section>
</div>

<div class="container mx-auto px-3">
  <section class="mt-5">
    <span style="font-size:30px">▶회원 관련</span><br>
    <span class="container mx-auto px-3">
      <a href="/adm/member/list" type="submit" class="btn btn-primary">회원리스트</a>
    </span>
  </section>
</div>

<div class="container mx-auto px-3">
  <section class="mt-5">
    <span style="font-size:30px">▶영화관 관련</span><br>
    <span class="container mx-auto px-3">
      <a href="/adm/movie/list" type="submit" class="btn btn-primary">영화 관리</a>
    </span>
    <span class="container mx-auto px-3">
      <a href="/adm/cinema/list" type="submit" class="btn btn-primary">영화관 관리</a>
    </span>
    <span class="container mx-auto px-3">
      <a href="/adm/cinema/theater/addTime" type="submit" class="btn btn-primary">상영회차 관리</a>
    </span>
  </section>
</div>

<div class="container mx-auto px-3">
  <section class="mt-5">
    <span style="font-size:30px">▶TEST</span><br>
    <span class="container mx-auto px-3">
      <a href="#" type="submit" class="btn btn-primary"></a>
    </span>
  </section>
</div>

<%@include file="../../common/foot.jspf"%>
