<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name} 게시물 리스트" />
<%@include file="../../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="flex">
      <div>
        게시물 개수 :
        <span class="text-blue-700">${articlesCount}</span>
        개
      </div>
      <div class="flex-grow"></div>
      <form class="flex">
        <input type="hidden" name="boardId" value="${param.boardId}" />
        <select data-value="${param.searchKeywordTypeCode}" name="searchKeywordTypeCode" id=""
          class="select select-bordered">
          <option disabled>검색타입</option>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'title'}">
              <option value="title" selected>제목</option>
            </c:when>
            <c:otherwise>
              <option value="title">제목</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'body'}">
              <option value="body" selected>내용</option>
            </c:when>
            <c:otherwise>
              <option value="body">내용</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'title, body'}">
              <option value="title, body" selected>제목, 내용</option>
            </c:when>
            <c:otherwise>
              <option value="title, body">제목, 내용</option>
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
          <col width="100" />
          <col />
          <col width="150" />
          <col width="50" />
          <col width="50" />
          <col width="150" />
        </colgroup>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>조회</th>
            <th>추천</th>
            <th>작성날짜</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="article" items="${articles}">
            <tr class="hover">
              <td>${article.id}</td>
              <td>
                <a class="btn-text-link block w-full truncate" href="${rq.getArticleDetailUriFromArticleList(article)}">${article.title}
                </a>
              </td>
              <td>
                <img style="float: left" class="w-6 h-6 ml-1 mr-1 rounded-full object-cover"
                  src="${rq.getProfileImgUri(article.memberId)}" alt="" onerror="${rq.profileFallbackImgOnErrorHtml}" />
                ${article.extra__writerName}
              </td>
              <td>${article.hitCount}</td>
              <td>${article.goodReactionPoint}</td>
              <td>${article.forPrintType1RegDate}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <div class="page-menu mt-4" align="center">
      <c:set var="pageMenuArmLen" value="4" />
      <c:set var="startPage" value="${page - pageMenuArmLen >= 1 ? page - pageMenuArmLen : 1}" />
      <c:set var="endPage" value="${page + pageMenuArmLen <= pagesCount ? page + pageMenuArmLen : pagesCount}" />

      <c:set var="pageBaseUri" value="?boardId=${boardId}" />
      <c:set var="pageBaseUri" value="${pageBaseUri}&searchKeyword=${param.searchKeyword}" />
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
    <c:if test="${param.boardId ne 1}">
      <form action="/usr/article/write" method="post">
        <input type="hidden" name="boardId" value="${param.boardId}" />
        <button type="submit" class="btn btn-primary">게시물 작성</button>
      </form>
    </c:if>
  </div>
</section>

<%@include file="../../common/foot.jspf"%>
