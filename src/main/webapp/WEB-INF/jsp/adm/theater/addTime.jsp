<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="관리자페이지 - 상영회차 추가 " />
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.20/lodash.min.js"></script>

<%@include file="../../common/head.jspf"%>

<script>
	let TheaterAddTime__submitFormDone = false;

	function TheaterAddTime__submitForm(form) {
		if (TheaterAddTime__submitFormDone) {
			alert('처리중입니다.');
			return;
		}

		form.title.value = form.title.value.trim();

		if (form.title.value.length == 0) {
			alert('영화를 선택해주세요');
			form.title.focus();
			return;
		}

		form.date.value = form.date.value.trim();

		if (form.date.value.length == 0) {
			alert('상영날짜를 선택해주세요');
			form.date.focus();
			return;
		}
		
		form.time.value = form.time.value.trim();

		if (form.time.value.length == 0) {
			alert('상영회차를 입력해주세요');
			form.time.focus();
			return;
		}
		
		form.startTime.value = form.startTime.value.trim();

		if (form.startTime.value.length == 0) {
			alert('상영시작 시간을 선택해주세요');
			form.startTime.focus();
			return;
		}
		
		form.endTime.value = form.endTime.value.trim();

		if (form.endTime.value.length == 0) {
			alert('상영종료 시간을 선택해주세요');
			form.endTime.focus();
			return;
		}
		

		TheaterAddTime__submitFormDone = true;
		form.submit();
	}
</script>

<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">

      <form class="table-box-type-1" method="post" enctype="multipart/form-data" action="../theater/doAddTime"
        onsubmit="TheaterAddTime__submitForm(this); return false;">
        <input type="hidden" name="theaterName" value="${theaterName}">
        <input type="hidden" name="region" value="${region}">
        <table>
          <tbody>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>상영관 명</th>
            <td>${theaterName}</td>
          </tr>
          <tr>
            <th>영화</th>
            <td>
              <select name="movieId">
                <c:forEach var="movies" items="${movies}">
                  <option value="${movies.id}">${movies.title}</option>
                </c:forEach>
              </select>
            </td>
          </tr>
          <tr>
            <th>상영 날짜</th>
            <td>
              <input class="input input-bordered" name="date" type="date">
            </td>
          </tr>
          <tr>
            <th>회차</th>
            <td>
              <input class="input input-bordered" name="time" type="text">
              회차
            </td>
          </tr>
          <tr>
            <th>상영 시간</th>
            <td>
              <input class="input input-bordered" name="startTime" type="time">
              ~
              <input class="input input-bordered" name="endTime" type="time">
            </td>
          </tr>
          <tr>
            <th>상영관 추가</th>
            <td>
              <button type="submit" class="btn btn-primary">상영관 추가</button>
              <button type="button" class="btn btn-outline btn-error" onclick="history.back();">뒤로가기</button>
            </td>
          </tr>
          </tbody>
        </table>
      </form>
    </div>
  </div>
</section>

<%@include file="../../common/foot.jspf"%>
