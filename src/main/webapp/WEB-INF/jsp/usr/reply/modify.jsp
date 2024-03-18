<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="댓글 수정" />
<%@include file="../common/head.jspf"%>

<script>
	let ReplyModify__submitFormDone = false;
	function ReplyModify__submitForm(form) {
		if (ReplyModify__submitFormDone) {
			alert('처리중입니다.');
			return;
		}

		//좌우 공백 제거
		form.body.value = form.body.value.trim();

		if (form.body.value.length == 0) {
			alert('내용을 입력해주세요');
			form.body.focus();
			return;
		}

		if (form.body.value.length < 2) {
			alert('댓글 내용을 2자 이상 입력해주세요.');
			form.body.focus();
			return;
		}

		ReplyModify__submitFormDone = true;
		form.submit();
	}
</script>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <form class="table-box-type-1" method="POST" action="../reply/doModify"
      onsubmit="ReplyModify__submitForm(this); return false;">
      <input type="hidden" name="id" value="${reply.id}" />
      <input type="hidden" name="replaceUri" value="${param.replaceUri}">
      <table>
        <colgroup>
          <col width="200" />
        </colgroup>
        <tbody>
          <tr>
            <th>게시물 번호</th>
            <td>
              <div>${reply.relId}</div>
            </td>
          </tr>
          <tr>
            <th>게시물 제목</th>
            <td>
              <div>${relDataTitle}</div>
            </td>
          </tr>
          <tr>
            <th>번호</th>
            <td>
              <div class="badge badge-primary">${reply.id}</div>
            </td>
          </tr>
          <tr>
            <th>댓글작성날짜</th>
            <td>${reply.forPrintType2RegDate}</td>
          </tr>
          <tr>
            <th>댓글수정날짜</th>
            <td>${reply.forPrintType2RegDate}</td>
          </tr>
          <tr>
            <th>댓글작성자</th>
            <td>${reply.extra__writerName}</td>
          </tr>
          <tr>
            <th>추천</th>
            <td>
              <span class="text-blue-700">${reply.goodReactionPoint}</span>
            </td>
          </tr>
          <tr>
            <th>댓글내용</th>
            <td>
              <textarea class="w-full textarea textarea-bordered" name="body" rows="5" placeholder="내용">${reply.body}</textarea>
            </td>
          </tr>
          <tr>
            <th>댓글수정</th>
            <td>
              <input type="submit" class="btn btn-primary" value="댓글수정" />
              <a class="btn btn-outline btn-success" href="${param.replaceUri}">뒤로가기</a>
            </td>
          </tr>
        </tbody>
      </table>
    </form>
  </div>
</section>

<%@include file="../common/foot.jspf"%>
