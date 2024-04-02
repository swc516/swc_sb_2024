<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="관리자페이지 - 영화 추가 " />
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.20/lodash.min.js"></script>

<%@include file="../../common/head.jspf"%>

<script>
	let MovieAdd__submitFormDone = false;
	let validLogined = "";

	function MovieAdd__submitForm(form) {
		if (MovieAdd__submitFormDone) {
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

		form.file__movie__0__extra__posterImg__1.value = form.file__movie__0__extra__posterImg__1.value.trim();

		if (form.file__movie__0__extra__posterImg__1.value.length == 0) {
			alert('영화포스터를 추가해주세요');
			form.file__movie__0__extra__posterImg__1.focus();
			return;
		}


		
		const maxSizeMb = 10;
		const maxSize = maxSizeMb * 1024 * 1024 
		
		const profileImgFileInput = form["file__movie__0__extra__moviePosterImg__1"];
		
		if( profileImgFileInput.value ) {
			if ( profileImgFileInput.files[0].size > maxSize ) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
				profileImgFileInput.focus();
				
				return;
			}
		}
		
		MovieAdd__submitFormDone = true;
		form.submit();
	}

</script>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <form class="table-box-type-1" method="post" enctype="multipart/form-data" action="../movie/doAdd"
        onsubmit="MovieAdd__submitForm(this); return false;">
        <input type="hidden" name="afterJoinUri" value="${param.afterJoinUri}" />
        <table>
          <tbody>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>영화명</th>
            <td>
              <input class="input input-bordered" name="title" placeholder="영화명" type="text"/>
            </td>
          </tr>
          <tr>
            <th>영화소개</th>
            <td>
              <input class="input input-bordered" name="body" placeholder="영화소개" type="text" />
            </td>
          </tr>
          <tr>
            <th>상영마감일자</th>
            <td>
              <input class="input input-bordered" name="runDate" type="datetime-local" />
            </td>
          </tr>
          <tr>
            <th>포스터</th>
            <td>
              <input accept="image/gif, image/jpeg, image/png" name="file__movie__0__extra__moviePosterImg__1" type="file" />
            </td>
          </tr>
          <tr>
            <th>영화추가</th>
            <td>
              <button type="submit" class="btn btn-primary">영화추가</button>
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
