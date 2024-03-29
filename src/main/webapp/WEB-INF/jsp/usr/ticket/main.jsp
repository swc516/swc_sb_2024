<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>


<c:set var="pageTitle" value="영화 예매(테스트) " />
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
  ${movie.title} <br>
      </c:forEach>
    </div>
    <div style="width: 20%;">
      <c:forEach var="cinema" items="${cinemas}">
        <c:if test="${cinema.region == param.region}">
          <input type="radio" name="region" value="${cinema.region}" onclick="document.getElementById('frm').submit();"
            checked>
        </c:if>
        <c:if test="${cinema.region != param.region }">
          <input type="radio" name="region" value="${cinema.region}" onclick="document.getElementById('frm').submit();">
        </c:if>
  ${cinema.region} <br>
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
              <c:if test="${theaterTime.extra__sellSeatCount == theaterTime.extra__maxSeatCount}">
                <input type="radio" name="ticketing"
                  value="${param.movieId}__${theaterTime.relTypeCode}__${theaterTime.theaterName}__${theaterTime.date}__${theaterTime.time}"
                  disabled>
              </c:if>
              <c:if test="${theaterTime.extra__sellSeatCount != theaterTime.extra__maxSeatCount}">
                <input type="radio" name="ticketing"
                  value="${param.movieId}__${theaterTime.relTypeCode}__${theaterTime.theaterName}__${theaterTime.date}__${theaterTime.time}">
              </c:if>
            </td>
            <td>${theaterTime.theaterName})</td>
            <td>
              [${theaterTime.time}회차] ${theaterTime.getForPrintType1StartTime()} ~
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
      <input type="hidden" id="movieId" name="movieId" value="">
      <input type="hidden" id="region" name="region" value="">
      <input type="hidden" id="theaterName" name="theaterName" value="">
      <input type="hidden" id="date" name="date" value="">
      <input type="hidden" id="time" name="time" value="">
      <input type="submit" value="예매" class="btn btn-success">
    </form>
  </div>
</section>

<script>
	$("input:radio[name=ticketing]").click(
			function() {
				var result = $(":input:radio[name=ticketing]:checked").val()
						.split('__');
				var movieId = result[0];
				var region = result[1];
				var theaterName = result[2];
				var date = result[3];
				var time = result[4];

				document.getElementById("movieId").value = movieId;
				document.getElementById("region").value = region;
				document.getElementById("theaterName").value = theaterName;
				document.getElementById("date").value = date;
				document.getElementById("time").value = time;
			});
</script>

<%@include file="../../common/foot.jspf"%>
