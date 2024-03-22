<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="게시물 작성" />
<%@include file="../../common/head.jspf"%>
<%@include file="../../common/toastUiEditorLib.jspf"%>


<script>
	let ArticleWrite__submitFormDone = false;
	function ArticleWrite__submitForm(form) {
		if (ArticleWrite__submitFormDone) {
			alert('처리중입니다.');
			return;
		}

		form.boardId.value = form.boardId.value.trim();

		if (form.boardId.value == '게시판을 선택해주세요.') {
			alert('게시판을 선택해주세요.');
			form.boardId.focus();
			return;
		}

		form.title.value = form.title.value.trim();

		if (form.title.value.length == 0) {
			alert('제목을 입력해주세요');
			form.title.focus();
			return;
		}

		const editor = $(form).find('.toast-ui-editor').data(
				'data-toast-editor');
		const markdown = editor.getMarkdown().trim();

		if (markdown.length == 0) {
			alert('내용을 입력해주세요');
			editor.focus();
			return;
		}

		form.body.value = markdown;

		ArticleWrite__submitFormDone = true;
		form.submit();
	}
</script>

<section class="mt-5">
  <div class="container mx-auto px-3">
    <form class="table-box-type-1" method="POST" action="../article/doWrite"
      onsubmit="ArticleWrite__submitForm(this); return false;">
      <input type="hidden" name="body" />
      <table>
        <colgroup>
          <col width="200" />
        </colgroup>
        <tbody>
          <tr>
            <th>작성자</th>
            <td>
              <img style="float: left" class="w-6 h-6 ml-1 mr-1 rounded-full object-cover"
                src="${rq.getProfileImgUri(rq.loginedMember.id)}" alt="" onerror="${rq.profileFallbackImgOnErrorHtml}" />
              ${rq.loginedMember.nickname}
            </td>
          </tr>
          <tr>
            <th>게시판$</th>
            <td>
              <select class="select select-bordered" name="boardId">
                <option disabled selected>게시판을 선택해주세요.</option>
                <option value="1">공지</option>
                <option value="2">자유</option>
              </select>
            </td>
          </tr>
          <tr>
            <th>제목</th>
            <td>
              <input name="title" type="text" placeholder="제목" class="w-96 input input-bordered w-full max-w-xs" />
            </td>
          </tr>
          <tr>
            <th>내용</th>
            <td>
              <div class="toast-ui-editor">
                <script type="text/x-template">
                
                </script>
              </div>
            </td>
          </tr>
          <tr>
            <th>작성</th>
            <td>
              <input type="submit" class="btn btn-primary" value="작성" />
              <button type="button" class="btn btn-outline btn-success" onclick="history.back();">뒤로가기</button>
            </td>
          </tr>
        </tbody>
      </table>
    </form>
  </div>
</section>

<%@include file="../../common/foot.jspf"%>
