<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="관리자페이지 - 영화수정 " />
<%@include file="../../common/head.jspf"%>

<script>
	let MovieModify__submitFormDone = false;

	function MovieModify__submitForm(form) {
		if (MovieModify__submitFormDone) {
			alert('처리중입니다.');
			return;
		}

		form.title.value = form.title.value.trim();

		if (form.title.value.length == 0) {
			alert('영화명을 입력해주세요');
			form.title.focus();
			return;
		}

		form.body.value = form.body.value.trim();

		if (form.body.value.length == 0) {
			alert('영화소개를 입력해주세요');
			form.body.focus();
			return;
		}

		form.runDate.value = form.runDate.value.trim();

		if (form.runDate.value.length == 0) {
			alert('상영마감일자를 입력해주세요');
			form.runDate.focus();
			return;
		}


		const maxSizeMb = 10;
		const maxSize = maxSizeMb * 1024 * 1024

		const moviePosterImgFileInput = form["file__movie__0__extra__moviePosterImg__1"];

		if (moviePosterImgFileInput.value) {
			if (moviePosterImgFileInput.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
				moviePosterImgFileInput.focus();

				return;
			}
		}

		MovieModify__submitFormDone = true;
		form.submit();
	}
</script>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <form class="table-box-type-1" method="post" enctype="multipart/form-data" action="../movie/doModify"
        onsubmit="MovieModify__submitForm(this); return false;">
        <input type="hidden" name="id" value="${movie.id}" />
        <table>
          <tbody>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>영화명</th>
            <td>
              <input class="input input-bordered" name="title" placeholder="영화명" type="text" value="${movie.title}" />
            </td>
          </tr>
          <tr>
            <th>영화소개</th>
            <td>
              <input class="input input-bordered" name="body" placeholder="영화소개" type="text" value="${movie.body}" />
            </td>
          </tr>
          <tr>
            <th>국가</th>
            <td>
              <input class="input input-bordered" name="country" placeholder="국가" type="text" value="${movie.country}" />
            </td>
          </tr>
          <tr>
            <th>상영시간</th>
            <td>
              <input class="input input-bordered" name="runningTime" placeholder="상영시간" type="text" value="${movie.runningTime}" />분
            </td>
          </tr>
          <tr>
            <th>감독</th>
            <td>
              <input class="input input-bordered" name="director" placeholder="감독" type="text" value="${movie.director}" />
            </td>
          </tr>
          <tr>
            <th>배우</th>
            <td>
              <input class="input input-bordered" name="actor" placeholder="배우" type="text" value="${movie.actor}" />
            </td>
          </tr>
          <tr>
            <th>장르</th>
            <td>
              <input class="input input-bordered" name="genre" placeholder="장르" type="text" value="${movie.genre}" />
            </td>
          </tr>
          <tr>
            <th>개봉날짜</th>
            <td>
              <input class="input input-bordered" name="releaseDate" type="date" value="${movie.releaseDate}" />
            </td>
          </tr>
          <tr>
            <th>트레일러 주소</th>
            <td>
              <input class="input input-bordered" name="trailer" placeholder="uri" type="text" value="${movie.trailer}" />
            </td>
          </tr>
          <tr>
            <th>포스터</th>
            <td>
              <img class="w-40 h-40 ml-1 mr-1 object-cover" src="${rq.getMoviePosterImgUri(movie.id)}" alt=""
                onerror="${rq.removeMoviePosterImgIfNotExitOnErrorHtmlAttr}" />
              <input accept="image/gif, image/jpeg, image/png" name="file__movie__0__extra__moviePosterImg__1"
                type="file" />

              <c:if test="${hasImg}">
                <div class="mt-2">
                  <label class="cursor-pointer inline-flex">
                    <span class="label-text mr-2 mt-1">이미지 삭제</span>
                    <div>
                      <input type="checkbox" name="deleteFileMovieExtraMoviePosterImg" value="Y"
                        class="checkbox" />
                      <span class="checkbox-mark"></span>
                    </div>
                  </label>
                </div>
              </c:if>
            </td>
          </tr>
          <tr>
            <th>영화추가</th>
            <td>
              <button type="submit" class="btn btn-primary">영화수정</button>
              <button type="button" class="btn btn-outline btn-error" onclick="history.back();">뒤로가기</button>
            </td>
          </tr>
          </tbody>
        </table>
      </form>
    </div>
  </div>
</section>

<%@include file="../../common/foot.jspf"%>
