<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="영화리스트 " />
<%@include file="../../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="flex">
      <div>
        영화 수 :
        <span class="text-blue-700">${moviesCount}</span>
        개
      </div>
      <div class="flex-grow"></div>
      <form class="flex">
        <select data-value="${param.searchKeywordTypeCode}" name="searchKeywordTypeCode" class="select select-bordered">
          <option disabled>검색타입</option>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'title'}">
              <option value="title" selected>영화명</option>
            </c:when>
            <c:otherwise>
              <option value="title">영화명</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'body'}">
              <option value="body" selected>영화소개</option>
            </c:when>
            <c:otherwise>
              <option value="body">영화소개</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'title, body'}">
              <option value="title, body" selected>영화명, 영화소개</option>
            </c:when>
            <c:otherwise>
              <option value="title, body">영화명, 영화소개</option>
            </c:otherwise>
          </c:choose>
        </select>

        <input name="searchKeyword" type="text" class="ml-2 w-72 input input-bordered" placeholder="검색어" maxlength="20"
          value="${param.searchKeyword}" />
        <button type="submit" class="ml-2 btn btn-primary">검색</button>
      </form>
    </div>
    <div class="mt-3">
      <table class="table table-fixed w-full">
        <colgroup>
          <col width="80" />
          <col width="130" />
          <col width="150" />
          <col width="150" />
          <col width="150" />
          <col width="300" />
          <col width="150" />
        </colgroup>
        <thead>
          <tr>
            <th>번호</th>
            <th>포스터</th>
            <th>추가날짜</th>
            <th>갱신날짜</th>
            <th>영화명</th>
            <th>영화소개</th>
            <th>상영마감일자</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="movie" items="${movies}">
            <tr class="hover">
              <td>${movie.id}</td>
              <td>
                <img class="w-20 h-40 ml-1 mr-1 object-cover" src="${rq.getMoviePosterImgUri(movie.id)}" alt=""
                  onerror="${rq.moviePosterFallbackImgOnErrorHtml}" />
              </td>
              <td>${movie.forPrintType1RegDate}</td>
              <td>${movie.forPrintType1UpdateDate}</td>
              <td>
                ${movie.title}
              </td>
              <td>${movie.body}</td>
              <td>${movie.runDate}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>


    <div class="page-menu mt-4" align="center">
      <c:set var="pageMenuArmLen" value="4" />
      <c:set var="startPage" value="${page - pageMenuArmLen >= 1 ? page - pageMenuArmLen : 1}" />
      <c:set var="endPage" value="${page + pageMenuArmLen <= pagesCount ? page + pageMenuArmLen : pagesCount}" />

      <c:set var="pageBaseUri" value="?searchKeyword=${param.searchKeyword}" />
      <c:set var="pageBaseUri" value="${pageBaseUri}&searchKeywordTypeCode=${param.searchKeywordTypeCode}" />

      <c:if test="${startPage > 1}">
        <a class="btn btn-sm" href="${pageBaseUri}&page=1">1</a>
        <c:if test="${startPage > 2}">
          <a class="btn btn-sm btn-disabled">...</a>
        </c:if>
      </c:if>
      <c:forEach begin="${startPage}" end="${endPage}" var="i">
        <a class="btn btn-sm ${page == i ? 'btn-active' : '' }" href="${pageBaseUri}&page=${i}">${i}</a>
      </c:forEach>

      <c:if test="${endPage < pagesCount}">
        <c:if test="${endPage < pagesCount - 1}">
          <a class="btn btn-sm btn-disabled">...</a>
        </c:if>
        <a class="btn btn-sm" href="${pageBaseUri}&page=${pagesCount}">${pagesCount}</a>
      </c:if>
    </div>
  </div>

</section>


<%@include file="../../common/foot.jspf"%>
