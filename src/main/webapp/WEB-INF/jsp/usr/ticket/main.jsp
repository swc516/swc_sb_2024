<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>


<c:set var="pageTitle" value="영화 예매" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.20/lodash.min.js"></script>


<%@include file="../../common/head.jspf"%>

<form id="frm" action="../ticket/main" method="post">
  <section class="container mx-auto px-3" style="display: flex; width: 100%; border: 1px solid #003458;">

    <div style="width: 20%;">
      <c:forEach var="movie" items="${movies}">
        <c:if test="${movie.id == param.movieId }">
          <input type="radio" name="movieId" value="${movie.id}" onclick="document.getElementById('frm').submit();"
            checked>
        </c:if>
        <c:if test="${movie.id != param.movieId }">
          <input type="radio" name="movieId" value="${movie.id}" onclick="document.getElementById('frm').submit();">
        </c:if>
  <a href="/usr/movie/detail?id=${movie.id}">${movie.title}</a> <br>
      </c:forEach>
    </div>
    <div style="width: 20%;">
      <c:forEach var="cinema" items="${cinemas}">
        <c:if test="${cinema.id == param.cinemaId}">
          <input type="radio" name="cinemaId" value="${cinema.id}" onclick="document.getElementById('frm').submit();"
            checked>
        </c:if>
        <c:if test="${cinema.id != param.cinemaId }">
          <input type="radio" name="cinemaId" value="${cinema.id}" onclick="document.getElementById('frm').submit();">
        </c:if>
  ${cinema.region}_${cinema.branch} <br>
      </c:forEach>
    </div>
    <div style="width: 20%;">
      <c:forEach var="week" items="${week}">
        <c:if test="${week == param.date}">
          <input type="radio" name="date" value="${week}" onclick="document.getElementById('frm').submit();" checked>
        </c:if>
        <c:if test="${week != param.date }">
          <input type="radio" name="date" value="${week}" onclick="document.getElementById('frm').submit();">
        </c:if>
  ${week} <br>
      </c:forEach>
    </div>
    <div>
      <table>
        <c:forEach var="theaterTime" items="${theaterTimes}">
          <tr>
            <td>
              <c:if
                test="${theaterTime.extra__sellSeatCount == theaterTime.extra__maxSeatCount || theaterTime.startTime <= beforeFiveMinutes}">
                <input type="radio" name="ticketing" id="radio"
                  value="${param.movieId}__${theaterTime.cinemaId}__${theaterTime.theaterInfoId}__${theaterTime.theaterTimeId}"
                  disabled>
              </c:if>
              <c:if
                test="${theaterTime.extra__sellSeatCount != theaterTime.extra__maxSeatCount && theaterTime.startTime > beforeFiveMinutes}">
                <input type="radio" name="ticketing" id="radio"
                  value="${param.movieId}__${theaterTime.cinemaId}__${theaterTime.theaterInfoId}__${theaterTime.theaterTimeId}">
              </c:if>
            </td>
            <td>${theaterTime.extra__theater}</td>
            <td>
              [${theaterTime.theaterTime}회차] ${theaterTime.getForPrintType1StartTime()} ~
              ${theaterTime.getForPrintType1EndTime()}
              (${theaterTime.extra__sellSeatCount}/${theaterTime.extra__maxSeatCount})
              <br>
            </td>
            <c:if test="${theaterTime.extra__sellSeatCount == theaterTime.extra__maxSeatCount}">

            </c:if>
          </tr>
        </c:forEach>
      </table>
    </div>
  </section>
</form>
<section class="container mx-auto px-3">
  <div style="float: right">
    <form action="../ticket/ticketing" method="post">
      <input type="hidden" id="movieId" name="movieId" value="0">
      <input type="hidden" id="cinemaId" name="cinemaId" value="0">
      <input type="hidden" id="theaterInfoId" name="theaterInfoId" value="0">
      <input type="hidden" id="theaterTimeId" name="theaterTimeId" value="0">
      <input type="submit" value="예매" class="btn btn-success" onclick="send(); return false;">
    </form>
  </div>
</section>
<script>
	function send() {
		var check = document.getElementById("movieId").value;
		if (check == 0) {
			alert('상영회차를 선택해주세요');
			return;
		} else{
			
			form.submit();
		}
	}

	$("input:radio[name=ticketing]").click(
			function() {
				var result = $(":input:radio[name=ticketing]:checked").val()
						.split('__');
				var movieId = result[0];
				var cinemaId = result[1];
				var theaterInfoId = result[2];
				var theaterTimeId = result[3];

				document.getElementById("movieId").value = movieId;
				document.getElementById("cinemaId").value = cinemaId;
				document.getElementById("theaterInfoId").value = theaterInfoId;
				document.getElementById("theaterTimeId").value = theaterTimeId;

			});
</script>



<%@include file="../../common/foot.jspf"%>
