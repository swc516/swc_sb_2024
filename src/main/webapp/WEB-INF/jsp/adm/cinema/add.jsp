<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="관리자페이지 - 영화관 추가 " />
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.20/lodash.min.js"></script>

<%@include file="../../common/head.jspf"%>

<script>
	let CinemaAdd__submitFormDone = false;

	function CinemaAdd__submitForm(form) {
		form.region.value = form.region.value.trim();
		if (form.region.value.length == 0) {
			alert('지역을 입력해주세요');
			form.region.focus();
			return;
		}

		form.branch.value = form.branch.value.trim();
		if (form.branch.value.length == 0) {
			alert('지점명을 입력해주세요');
			branch.region.focus();
			return;
		}
		
		CinemaAdd__submitFormDone = true;
		form.submit();
	}

</script>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <form class="table-box-type-1" method="post" enctype="multipart/form-data" action="../cinema/doAdd"
        onsubmit="CinemaAdd__submitForm(this); return false;">
        <input type="hidden" name="afterJoinUri" value="${param.afterJoinUri}" />
        <table>
          <tbody>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>지역<br> ex)서울</th>
            <td>
              <input class="input input-bordered" name="region" placeholder="지역" type="text"/>
            </td>
          </tr>
          <tr>
            <th>지점<br> ex)도봉</th>
            <td>
              <input class="input input-bordered" name="branch" placeholder="지점" type="text"/>
            </td>
          </tr>
          <tr>
            <th>영화관 추가</th>
            <td>
              <button type="submit" class="btn btn-primary">영화관 추가</button>
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
