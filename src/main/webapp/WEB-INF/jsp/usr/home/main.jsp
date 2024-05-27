<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="상영중인 영화" />
<%@include file="../../common/head.jspf"%>

<section class="mt-5">
  <div class="container mx-auto px-3 ">
    <c:forEach var="movie" items="${movieList}">
      <div class="card card-compact w-80 bg-base-100 shadow-xl" style="float: left; margin: 20px">
        <figure>
          <a href="../movie/detail?id=${movie.id}">
            <img style="width:300px; height:450px;"src="${rq.getMoviePosterImgUri(movie.id)}" onerror="${rq.moviePosterFallbackImgOnErrorHtml}" />
          </a>
        </figure>
        <div class="card-body">
          <h2 class="card-title">${movie.title}</h2>
          <div
            style="text-overflow: ellipsis; overflow: hidden; word-break: break-all; display: -webkit-box; -webkit-line-clamp: 5; -webkit-box-orient: vertical">
            <p>${movie.body}</p>
          </div>
          <div class="card-actions justify-end">
          
         <c:choose>
           <c:when test="${movie.rateAvg == 5}">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
           </c:when>
           <c:when test="${movie.rateAvg >= 4.5}">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/harf.png" width="17">
           </c:when>
           <c:when test="${movie.rateAvg >= 4}">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/empty.png" width="17">
           </c:when>
           <c:when test="${movie.rateAvg >= 3.5}">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/harf.png" width="17">
             <img src="/rate/empty.png" width="17">
           </c:when>
           <c:when test="${movie.rateAvg >= 3}">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
           </c:when>
           <c:when test="${movie.rateAvg >= 2.5}">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/harf.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
           </c:when>
           <c:when test="${movie.rateAvg >= 2}">
             <img src="/rate/full.png" width="17">
             <img src="/rate/full.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
           </c:when>
           <c:when test="${movie.rateAvg >= 1.5}">
             <img src="/rate/full.png" width="17">
             <img src="/rate/harf.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
           </c:when>
           <c:when test="${movie.rateAvg >= 1}">
             <img src="/rate/full.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
           </c:when>
           <c:when test="${movie.rateAvg >= 0.5}">
             <img src="/rate/harf.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
           </c:when>
           <c:when test="${movie.rateAvg >= 0}">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
             <img src="/rate/empty.png" width="17">
           </c:when>
         </c:choose>
         
         (${movie.rateAvg})
          </div>
          <div class="card-actions justify-end">
            <a class="btn btn-primary"
              href="../ticket/main?movieId=${movie.id}&cinemaId=${rq.loginedMemberFavoriteCinema}&date=${rq.today}">예매</a>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</section>


<%@include file="../../common/foot.jspf"%>
