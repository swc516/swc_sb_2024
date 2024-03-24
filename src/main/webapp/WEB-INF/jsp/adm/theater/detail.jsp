<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${param.relTypeCode} / ${theaterName} 정보" />
<%@include file="../../common/head.jspf"%>
<%@include file="../../common/toastUiEditorLib.jspf"%>
<section>
  <div>
    <form action="../theater/doModify" method="post">
      <div class="container mx-auto px-3">
        <c:set var="i" value="${0}" />
        <c:forEach var="theater" items="${theaters}" varStatus="status">
          <c:choose>
            <c:when test="${theater.seatId == 65+i}">
              <c:if test="${theater.seatStatus eq '일반'}">
                <input class="checkbox" type="checkbox" name="seats"
                  value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
              </c:if>
              <c:if test="${theater.seatStatus eq '장애'}">
                <input class="checkbox checkbox-success" type="checkbox" name="seats"
                  value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
              </c:if>
              <c:if test="${theater.seatStatus eq '없음'}">
                <input class="checkbox checkbox-warning" type="checkbox" name="seats" 
                  value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
              </c:if>
            </c:when>
            <c:otherwise>
              <c:set var="i" value="${i+1}" />
              <br>
              <c:if test="${theater.seatId == 65+i}">
                <c:if test="${theater.seatStatus eq '일반'}">
                  <input class="checkbox" type="checkbox" name="seats"
                    value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
                </c:if>
                <c:if test="${theater.seatStatus eq '장애'}">
                  <input class="checkbox checkbox-success" type="checkbox" name="seats"
                    value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
                </c:if>
                <c:if test="${theater.seatStatus eq '없음'}">
                  <input class="checkbox checkbox-warning" type="checkbox" name="seats" 
                    value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
                </c:if>
              </c:if>
            </c:otherwise>
          </c:choose>
        </c:forEach>
        <br>
        <input type="hidden" name="replaceUri" value="${rq.currentUri}">
        <input type="hidden" name="theaterName" value="${theaterName}">
        <input type="hidden" name="relTypeCode" value="${param.relTypeCode}">
        <input type="radio" name="seatStatus" value="일반">
        일반
        <input type="radio" name="seatStatus" value="장애">
        장애
        <input type="radio" name="seatStatus" value="없음">
        없음
        <br>
        <button type="submit">수정</button>
      </div>
    </form>
  </div>
</section>



<%@include file="../../common/foot.jspf"%>



