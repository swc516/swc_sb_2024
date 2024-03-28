<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="영화 예매(테스트) " />
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.20/lodash.min.js"></script>

<%@include file="../../common/head.jspf"%>

<section>
  <div>
    <form class="table-box-type-1" method="post" enctype="multipart/form-data" action="../ticketing/doTicketing">
      <div class="container mx-auto px-3">
        <div class="divider divider-info">Screen</div>
        <table style="margin-left: auto; margin-right: auto;">
          <tr>
            <td>＃</td>
            <c:forEach var="seatNo" items="${seatNoArr}">
              <td style="text-align: center">${seatNo}</td>
            </c:forEach>
          </tr>
          <c:forEach var="theaterTime" items="${theaterTime}">
            <c:if test="${theaterTime.seatNo == 1}">
              <tr>
                <td style="text-align: center">${theaterTime.seatId}</td>
            </c:if>
            <td>
              <c:choose>
                <c:when test="${theaterTime.seatSell == true }">
                  <input class="checkbox seatId-${theaterTime.seatId} seatNo-${theaterTime.seatNo}" type="checkbox"
                    name="seats" value=" ${theaterTime.seatId}-${theaterTime.seatNo}-${theaterTime.seatStatus}" disabled checked>
                </c:when>
                <c:otherwise>
                  <c:if test="${theaterTime.seatStatus == '일반'}">
                    <input class="checkbox seatId-${theaterTime.seatId} seatNo-${theaterTime.seatNo}" type="checkbox"
                      name="seats" value=" ${theaterTime.seatId}-${theaterTime.seatNo}-${theaterTime.seatStatus}">
                  </c:if>
                  <c:if test="${theaterTime.seatStatus == '장애'}">
                    <input class="checkbox checkbox-success seatId-${theaterTime.seatId} seatNo-${theaterTime.seatNo}"
                      type="checkbox" name="seats"
                      value=" ${theaterTime.seatId}-${theaterTime.seatNo}-${theaterTime.seatStatus}">
                  </c:if>
                  <c:if test="${theaterTime.seatStatus == '없음'}">
                  
                    <input class="checkbox checkbox-error seatId-${theaterTime.seatId} seatNo-${theaterTime.seatNo}"
                      type="checkbox" name="seats"
                      value=" ${theaterTime.seatId}-${theaterTime.seatNo}-${theaterTime.seatStatus}" disabled>
                  </c:if>
                </c:otherwise>
              </c:choose>

            </td>
          </c:forEach>
        </table>


        <input type="hidden" name="region" value="${param.region}">
        <input type="hidden" name="theaterName" value="${param.theaterName}">
        <input type="hidden" name="date" value="${param.date}">
        <input type="hidden" name="time" value="${param.time}">
        <button type="submit" class="btn btn-primary">예매하기</button>
    </form>
  </div>
</section>

<%@include file="../../common/foot.jspf"%>
