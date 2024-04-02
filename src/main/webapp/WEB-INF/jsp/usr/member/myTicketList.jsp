<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="예매 목록 " />
<%@include file="../../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="mt-3">
      <table class="table table-fixed w-full">
        <colgroup>
        </colgroup>
        <thead>
          <tr>
            <th>영화관</th>
            <th>상영관</th>
            <th>영화제목</th>
            <th>예매날짜</th>
            <th>상영날짜</th>
            <th>상영시간</th>
            <th>좌석</th>
            <th>위치</th>
            <th>좌석정보</th>
            <th>비고</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="list" items="${lists}">
            <tr class="hover">
              <td>${list.extra__cinemaRegion}_${list.extra__cinemaBranch}</td>
              <td>${list.extra__theater}</td>
              <td>
                <a class="btn-text-link block w-full truncate" href="/usr/movie/detail?id=${list.movieId}">${list.extra__movieTitle}</a>
              </td>
              <td>${list.buyDate}</td>
              <td>${list.date}</td>
              <td>${list.getForPrintType1StartTime()} ~${list.getForPrintType1EndTime()}</td>
              <td>${list.seatRow}-${list.seatCol}</td>
              <td>
                <a
                  href="/usr/ticket/seatLocation?&cinemaId=${list.cinemaId}&theaterInfoId=${list.theaterInfoId}&mySeatRow=${list.seatRow}&mySeatCol=${list.seatCol}"
                  onclick="window.open(this.href, '_blank', 'width=500, height=500'); return false;">위치보기</a>
              </td>
              <td>${list.seatStatus}</td>
              <td>
                <a href="../member/doTicketCancel?id=${list.id}" class="btn btn-error">취소하기</a>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
</section>

<%@include file="../../common/foot.jspf"%>
