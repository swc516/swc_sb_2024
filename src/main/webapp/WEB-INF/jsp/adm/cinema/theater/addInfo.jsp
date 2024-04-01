<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="관리자페이지 - 상영관 추가 " />
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.20/lodash.min.js"></script>

<%@include file="../../../common/head.jspf"%>

<script>
	let TheaterAdd__submitFormDone = false;

	function TheaterAdd__submitForm(form) {
		if (TheaterAdd__submitFormDone) {
			alert('처리중입니다.');
			return;
		}

		form.region.value = form.region.value.trim();

		if (form.region.value.length == 0) {
			alert('영화관 명을 입력해주세요');
			form.region.focus();
			return;
		}

		TheaterAdd__submitFormDone = true;
		form.submit();
	}
</script>

<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">

      <form class="table-box-type-1" method="post" enctype="multipart/form-data" action="../theater/doAddInfo"
        onsubmit="TheaterAdd__submitForm(this); return false;">
        <input type="hidden" name="cinemaId" value="${cinema.id}" />
        <table>
          <tbody>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>지역_지점</th>
            <td>${cinema.region}_${cinema.branch}</td>
          </tr>
          <tr>
            <th>상영관 명</th>
            <td>
              <input class="w-96 input input-bordered w-full max-w-xs" type="text" name="theater"
                placeholder="1관 (***관) / 괄호는 선택" />
            </td>
          </tr>
          <tr>
            <th>열</th>
            <td>
              A ~
              <input class="w-20 input input-bordered max-w-xs" type="text" name="seatRow" maxlength="1"/>
              열까지 (대문자)
            </td>
          </tr>
          <tr>
            <th>행</th>
            <td>
              1 ~ 
              <input class="w-20 input input-bordered max-w-xs" type="text" name="seatCol">
              행까지 (숫자)
            </td>
          </tr>
          <tr>
            <th>상영관 추가</th>
            <td>
              <button type="submit" class="btn btn-primary">상영관 추가</button>
              <button type="button" class="btn btn-outline btn-error" onclick="history.back();">뒤로가기</button>
            </td>
          </tr>
          </tbody>
        </table>
      </form>
    </div>
  </div>
</section>

<%@include file="../../../common/foot.jspf"%>
