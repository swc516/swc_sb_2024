<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="상영중인 영화" />
<%@include file="../../common/head.jspf"%>

<section class="mt-5">
  <div class="container mx-auto px-3 ">
    <c:forEach var="movie" items="${movieList}">
      <div class="card card-compact w-80 bg-base-100 shadow-xl" style="float: left; margin:20px">
        <figure>
          <a href="../ticket/main?movieId=${movie.id}&cinemaId="><img src="${rq.getMoviePosterImgUri(movie.id)}" onerror="${rq.moviePosterFallbackImgOnErrorHtml}" /></a>
        </figure>
        <div class="card-body">
          <h2 class="card-title">${movie.title}</h2>
          <p>${movie.body}</p>
          <div class="card-actions justify-end">
            <a class="btn btn-primary" href="../movie/detail?movieId=${movie.id}">자세히</a>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</section>




<%@include file="../../common/foot.jspf"%>
