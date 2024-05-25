<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="pageTitle" value="${movie.title}" />
<%@include file="../../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <table>
        <tbody>
        <colgroup>
          <col width="200" />
        </colgroup>
        <tr>
          <th>줄거리</th>
          <td>${movie.body}</td>
        </tr>
        <tr>
          <th>상영시간</th>
          <td>${movie.runningTime}분</td>
        </tr>
        <tr>
          <th>감독</th>
          <td>
            <c:set var="key" value="1" />
            <c:set var="director" value="${fn:split(movie.director,',')}" />
            <c:forEach var="directorValue" items="${director}" varStatus="varStatus">
              <c:if test="${varStatus.count eq key }">
                <a class="btn-text-link w-full truncate" href="../movie/list?searchKeywordTypeCode=director&searchKeyword=${directorValue}">${directorValue}</a>
                <c:if test="${fn:length(director) != key}">
                  ,&nbsp;
                  </c:if>
              </c:if>
              <c:set var="key" value="${key + 1 }" />
            </c:forEach>
          </td>
        </tr>
        <tr>
          <th>배우</th>
          <td>
            <c:set var="key" value="1" />
            <c:set var="actor" value="${fn:split(movie.actor,',')}" />
            <c:forEach var="actorValue" items="${actor}" varStatus="varStatus">
              <c:if test="${varStatus.count eq key }">
                <a class="btn-text-link w-full truncate" href="../movie/list?searchKeywordTypeCode=actor&searchKeyword=${actorValue}">${actorValue}</a>
                <c:if test="${fn:length(actor) != key}">
                  ,&nbsp;
                  </c:if>
              </c:if>
              <c:set var="key" value="${key + 1 }" />
            </c:forEach>
          </td>
        </tr>
        <tr>
          <th>장르</th>
          <td>
            <c:set var="key" value="1" />
            <c:set var="genre" value="${fn:split(movie.genre,',')}" />
            <c:forEach var="genreValue" items="${genre}" varStatus="varStatus">
              <c:if test="${varStatus.count eq key }">
                <a class="btn-text-link w-full truncate" href="../movie/list?searchKeywordTypeCode=genre&searchKeyword=${genreValue}">${genreValue}</a>
                <c:if test="${fn:length(genre) != key}">
                  ,&nbsp;
                  </c:if>
              </c:if>
              <c:set var="key" value="${key + 1 }" />
            </c:forEach></td>
        </tr>
        <tr>
          <th>제작국가</th>
          <td>
          <c:set var="key" value="1" />
            <c:set var="country" value="${fn:split(movie.country,',')}" />
            <c:forEach var="countryValue" items="${country}" varStatus="varStatus">
              <c:if test="${varStatus.count eq key }">
                <a class="btn-text-link w-full truncate" href="../movie/list?searchKeywordTypeCode=country&searchKeyword=${countryValue}">${countryValue}</a>
                <c:if test="${fn:length(country) != key}">
                  ,&nbsp;
                  </c:if>
              </c:if>
              <c:set var="key" value="${key + 1 }" />
            </c:forEach>
          </td>
        </tr>
        <tr>
          <th>개봉일</th>
          <td>${movie.releaseDate}</td>
        </tr>
        <tr>
          <th>예고편</th>
          <td>
            <iframe width="1280" height="720" src="${movie.trailer}" title="YouTube video player" frameborder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
              referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
          </td>
        </tr>
        <tr>
          <th>포스터</th>
          <td>
            <img src="${rq.getMoviePosterImgUri(movie.id)}" onerror="${rq.moviePosterFallbackImgOnErrorHtml}" />
          </td>
        </tr>
        <tr>
          <th>평점</th>
          <td>${movie.rateAvg}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</section>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <table>
        <tbody>
        <colgroup>
          <col width="200" />
        </colgroup>
        <tr>
          <th>평점 및 리뷰</th>
        </tr>
        <tr>
        <c:forEach var="review" items="${reviews}">
          <td width="8%">${review.rate}</td>
          <td width="88%">${review.body}</td>
          <td width="10%">${review.extra__writerName}</td>
        </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</section>

<br>



<%@include file="../../common/foot.jspf"%>
