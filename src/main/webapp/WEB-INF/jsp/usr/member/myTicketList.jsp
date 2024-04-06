<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="예매 목록 " />
<%@include file="../../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="mt-3">
      <c:forEach var="list" items="${lists}">
              ${list.cinema}
              ${list.theater}
              ${list.time}회
              ${list.movieTitle}
              ${list.buyDate}
              ${list.playingTime}
              ${list.extra__seatInfo}
                <a
          href="/usr/ticket/seatLocation?&cinema=${list.cinema}&theater=${list.theater}&mySeats=${list.extra__seatInfo}"
          onclick="window.open(this.href, '_blank', 'width=500, height=500'); return false;">위치보기</a>

        <a href="../member/doTicketCancel?id=${list.id}&seatIds=${list.extra__seatId}" class="btn btn-error">취소하기</a>
        <br>
      </c:forEach>
    </div>
</section>

<%@include file="../../common/foot.jspf"%>
