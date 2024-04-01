<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="관리자페이지 - 상영회차 추가 " />
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.20/lodash.min.js"></script>

<%@include file="../../../common/head.jspf"%>

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

      <form id="frm" class="table-box-type-1" method="post" action="../theater/addTime">
        <table>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>지역</th>
            <td>
              <select class="select select-bordered" name="cinemaId">
                <option value="0"></option>
                <c:forEach var="cinema" items="${cinemas}">
                  <c:choose>
                    <c:when test="${cinema.id == param.cinemaId}">
                      <option value="${cinema.id}" selected>${cinema.region}_${cinema.branch}</option>
                    </c:when>
                    <c:when test="${cinema.id != param.cinemaId}">
                      <option value="${cinema.id}">${cinema.region}_${cinema.branch}</option>
                    </c:when>
                  </c:choose>
                </c:forEach>
              </select>
              <button class="btn btn-primary">조회</button>
            </td>
          </tr>
        </table>
      </form>


      <form class="table-box-type-1" method="post" action="../theater/doAddTime">
        <input type="hidden" name="cinemaId" value="${param.cinemaId}">
        <table>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>상영관</th>
            <td>
              <select class="select select-bordered" name="theaterInfoId">
                <c:forEach var="theaterInfo" items="${theaterInfos}">
                  <option value="${theaterInfo.theaterInfoId}">${theaterInfo.theater}</option>
                </c:forEach>
              </select>
            </td>
          </tr>
          <tr>
          </tr>
          <tr>
            <th>영화</th>
            <td>
              <select class="select select-bordered" name="movieId">
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
              <input class="input input-bordered" name="theaterTime" type="text">
              회차
            </td>
          </tr>
          <tr>
            <th>상영 시간</th>
            <td>
              <input class="input input-bordered" name="startTime" type="datetime-local">
              ~
              <input class="input input-bordered" name="endTime" type="datetime-local">
            </td>
          </tr>
          </tbody>
        </table>
        <div style="float: right">
          <input type="submit" value="예매" class="btn btn-success">
        </div>
      </form>
    </div>
  </div>
</section>



<%@include file="../../../common/foot.jspf"%>
