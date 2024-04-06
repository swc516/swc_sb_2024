<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="관리자페이지 - 영화리스트 " />
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
            <c:when test="${param.searchKeywordTypeCode eq 'director'}">
              <option value="director" selected>감독</option>
            </c:when>
            <c:otherwise>
              <option value="director">감독</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'actor'}">
              <option value="actor" selected>배우</option>
            </c:when>
            <c:otherwise>
              <option value="actor">배우</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'genre'}">
              <option value="genre" selected>장르</option>
            </c:when>
            <c:otherwise>
              <option value="genre">장르</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'country'}">
              <option value="country" selected>제작국가</option>
            </c:when>
            <c:otherwise>
              <option value="country">제작국가</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'title, body, director, actor, genre, country'}">
              <option value="title, body, director, actor, genre, country" selected>전체</option>
            </c:when>
            <c:otherwise>
              <option value="title, body, director, actor, genre, country">전체</option>
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
          <col width="50" />
          <col width="50" />
          <col width="100" />
          <col width="150" />
          <col width="500" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
        </colgroup>
        <thead>
          <tr>
            <th>
              <input class="checkbox-all-movie-id" type="checkbox" />
            </th>
            <th>번호</th>
            <th>포스터</th>
            <th>영화명</th>
            <th>영화소개</th>
            <th>감독</th>
            <th>배우</th>
            <th>제작국가</th>
            <th>장르</th>
            <th>개봉일자</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="movie" items="${movies}">
            <tr class="hover">
              <th>
                <input class="checkbox-movie-id" value="${movie.id}" type="checkbox" />
              </th>
              <td>${movie.id}</td>
              <td>
                <img width="200" height="300" class="ml-1 mr-1" src="${rq.getMoviePosterImgUri(movie.id)}" alt=""
                  onerror="${rq.moviePosterFallbackImgOnErrorHtml}" />
              </td>
   
              <td>
                <a class="btn-text-link block w-full truncate" href="/adm/movie/modify?id=${movie.id}">${movie.title}
                </a>
              </td>
              <td>${movie.body}</td>
              <td>${movie.director}</td>
              <td>${movie.actor}</td>
              <td>${movie.country}</td>
              <td>${movie.genre}</td>
              <td>${movie.releaseDate}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <script>
      $('.checkbox-all-movie-id').change(function() {
      	const $all = $(this);
      	const allChecked = $all.prop('checked');
      
      	$('.checkbox-movie-id').prop('checked', allChecked);
      
      })
      
      $('.checkbox-movie-id').change(function() {
		const checkboxMovieIdCount = $('.checkbox-movie-id').length;
		const checkboxMovieIdCheckedCount = $('.checkbox-movie-id:checked').length;

		const allChecked = checkboxMovieIdCount == checkboxMovieIdCheckedCount;

		$('.checkbox-all-movie-id').prop(
				'checked', allChecked);
		})
	</script>

    <div>
      <button class="btn btn-error btn-delete-selected-movies">선택삭제</button>
    </div>

    <form method="post" name="do-delete-movies-form" action="../movie/doDelete">
      <input type="hidden" name="ids" value="" />
      <input type="hidden" name="replaceUri" value="${rq.currentUri}" />
    </form>
    <div>
      <a class="btn btn-success" href="/adm/movie/add">영화추가</a>
    </div>

    <script>
     $('.btn-delete-selected-movies').click(function(){
    	 const values = $('.checkbox-movie-id:checked').map((index, el) => el.value).toArray();
    	 
    	 if(values.length == 0) {
    		 alert('삭제할 영화을 선택해주세요.');
    		 return;
    	 }
    	 
    	 if(confirm('정말 삭제하시겠습니까?') == false){
    		 return;
    	 }
    	 
    	 document['do-delete-movies-form'].ids.value = values.join(',');     
    	 document['do-delete-movies-form'].submit();   
     })
    </script>

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
