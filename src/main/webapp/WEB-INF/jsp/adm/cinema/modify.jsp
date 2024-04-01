<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="관리자페이지 - 영화관 수정 " />
<%@include file="../../common/head.jspf"%>

<script>
	let CinemaModify__submitFormDone = false;

	function CinemaModify__submitForm(form) {

		form.region.value = form.region.value.trim();
		if (form.region.value.length == 0) {
			alert('지역 명을 입력해주세요');
			form.region.focus();
			return;
		}

		form.branch.value = form.branch.value.trim();
		if (form.branch.value.length == 0) {
			alert('지점 명을 입력해주세요');
			form.branch.focus();
			return;
		}


		CinemaModify__submitFormDone = true;
		form.submit();
	}
</script>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <form class="table-box-type-1" method="post" enctype="multipart/form-data" action="../cinema/doModify"
        onsubmit="CinemaModify__submitForm(this); return false;">
        <input type="hidden" name="id" value="${cinema.id}" />
        <table>
          <tbody>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>지역 명</th>
            <td>
              <input class="input input-bordered" name="region" placeholder="영화관 명" type="text" value="${cinema.region}" />
            </td>
          </tr>
          <tr>
            <th>지점 명</th>
            <td>
              <input class="input input-bordered" name="branch" placeholder="영화관 명" type="text" value="${cinema.branch}" />
            </td>
          </tr>
          <tr>
            <th>영화관 수정</th>
            <td>
              <button type="submit" class="btn btn-primary">영화관 수정</button>
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
