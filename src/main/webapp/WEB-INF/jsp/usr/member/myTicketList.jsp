<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="예매 목록 " />
<%@include file="../../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="mt-3">
      <c:forEach var="list" items="${lists}">
        <div class="stats bg-warning text-primary-content">

          <div class="stat">
            <div class="stat-title">${list.cinema}${list.theater},(${list.time}회)
              ${list.startTime.substring(0,10)}</div>
            <div class="stat-value">${list.playingTime}</div>
            <div class="stat-title">
              <a
                href="/usr/ticket/seatLocation?&cinema=${list.cinema}&theater=${list.theater}&mySeats=${list.extra__seatInfo}"
                onclick="window.open(this.href, '_blank', 'width=500, height=500'); return false;">좌석위치</a>
            </div>
            <div class="stat-value">${list.extra__seatInfo}</div>
          </div>

          <div class="stat">
            <div class="stat-title">영화제목</div>
            <div class="stat-value">${list.movieTitle }</div>
          </div>

          <div class="stat">
            <div class="stat-title">예매날짜</div>
            <div class="stat-value">${list.buyDate}</div>
          </div>

          <c:choose>
            <c:when test="${list.startTime > beforeThirthMinutes}">
              <div class="stat">
                <div class="stat-title"></div>
                <div class="stat-value">
                  <a href="../member/doTicketCancel?id=${list.id}&seatIds=${list.extra__seatId}" class="btn btn-error">취소하기</a>
                </div>
              </div>
            </c:when>
            <c:when test="${list.startTime < beforeThirthMinutes}">
              <div class="stat">
                <div class="stat-title"></div>
                <div class="stat-value">
                  <a href="#" class="btn btn-error">취소불가</a>
                </div>
              </div>
            </c:when>
          </c:choose>
            <a
                href="/usr/ticket/writeReview?movieTitle=${list.movieTitle}"
                onclick="window.open(this.href, '_blank', 'width=1000, height=300'); return false;">리뷰 작성</a>
            </div>

        </div>
        <br>
        <br>
      </c:forEach>
    </div>
</section>

<%@include file="../../common/foot.jspf"%>
