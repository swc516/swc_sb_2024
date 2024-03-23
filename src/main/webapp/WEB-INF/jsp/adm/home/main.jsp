<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="관리자 " />
<%@include file="../../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <a href="/usr/article/write?boardId=1" type="submit" class="btn btn-primary">공지사항 작성</a>
    </div>
  </div>
</section>
<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <a href="/adm/member/list" type="submit" class="btn btn-primary">회원리스트</a>
    </div>
  </div>
</section>
<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <a href="/adm/member/list" type="submit" class="btn btn-primary">전체 게시글 관리</a>
    </div>
  </div>
</section>
<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <a href="/adm/member/list" type="submit" class="btn btn-primary">전체 댓글 관리</a>
    </div>
  </div>
</section>
<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <a href="/adm/movie/list" type="submit" class="btn btn-primary">영화 관리</a>
    </div>
  </div>
</section>
<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <a href="/adm/cinema/list" type="submit" class="btn btn-primary">영화관 관리</a>
    </div>
  </div>
</section>

<%@include file="../../common/foot.jspf"%>
