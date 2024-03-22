<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="게시물 내용" />
<%@include file="../../common/head.jspf"%>
<%@include file="../../common/toastUiEditorLib.jspf"%>


<script>
	const params = {};
	params.id = parseInt('${param.id}');
</script>

<script>
	function ArticleDetail__increaseHitCount() {
		const localStorageKey = 'article__' + params.id + '__viewDone';

		if (localStorage.getItem(localStorageKey)) {
			return;
		}

		localStorage.setItem(localStorageKey, true);

		$.get('../article/doIncreaseHitCountRd', {
			id : params.id,
			ajaxMode : 'Y'
		}, function(data) {
			$('.article-detail__hit-count').empty().html(data.data1);
		}, 'json');
	}
	$(function() {
		//실전코드
		ArticleDetail__increaseHitCount();

		//임시코드
		//setTimeout(ArticleDetail__increaseHitCount, 1000);
	})
</script>

<script>
	let ReplyWrite__submitFormDone = false;
	function ReplyWrite__submitForm(form) {
		if (ReplyWrite__submitFormDone) {
			alert('처리중입니다.');
			return;
		}

		//좌우 공백 제거
		form.body.value = form.body.value.trim();

		if (form.body.value.length == 0) {
			alert('댓글을 입력해주세요');
			form.body.focus();
			return;
		}

		if (form.body.value.length < 2) {
			alert('댓글을 2자 이상 입력해주세요.');
			form.body.focus();
			return;
		}

		ReplyWrite__submitFormDone = true;
		form.submit();
	}
</script>
<section class="mt-5">

  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <table>
        <tbody>
        <colgroup>
          <col width="200" />
        </colgroup>
        <tr>
          <th>번호</th>
          <td>${article.id}</td>
        </tr>
        <tr>
          <th>작성날짜</th>
          <td>${article.forPrintType2RegDate}</td>
        </tr>
        <tr>
          <th>수정날짜</th>
          <td>${article.forPrintType2UpdateDate}</td>
        </tr>
        <tr>
          <th>작성자</th>
          <td colspan="1">
            <img style="float: left" class="w-6 h-6 ml-1 mr-1 rounded-full object-cover"
              src="${rq.getProfileImgUri(rq.loginedMember.id)}" alt="" onerror="${rq.profileFallbackImgOnErrorHtml}" />
            <span>${article.extra__writerName}</span>
          </td>
        </tr>
        <tr>
          <th>조회수</th>
          <td>
            <span class="text-blue-700 article-detail__hit-count">${article.hitCount}</span>
          </td>
        </tr>
        <tr>
          <th>추천</th>
          <td>
            <div class="flex items-center">
              <span class="text-blue-700">${article.goodReactionPoint}</span>
              <span>&nbsp;</span>
              <c:if test="${actorCanMakeReaction}">
                <a
                  href="/usr/reactionPoint/doGoodReaction?relTypeCode=article&relId=${param.id}&replaceUri=${rq.encodedCurrentUri}"
                  class="btn btn-xs btn-primary btn-outline"> 좋아요👍 </a>
                <span>&nbsp;</span>
                <a
                  href="/usr/reactionPoint/doBadReaction?relTypeCode=article&relId=${param.id}&replaceUri=${rq.encodedCurrentUri}"
                  class="btn btn-xs btn-secondary btn-outline"> 싫어요👎 </a>
              </c:if>

              <c:if test="${actorCanCancelGoodReaction}">
                <a
                  href="/usr/reactionPoint/doCencelGoodReaction?relTypeCode=article&relId=${param.id}&replaceUri=${rq.encodedCurrentUri}"
                  class="btn btn-xs btn-primary"> 좋아요👍 </a>
                <span>&nbsp;</span>
                <a onclick="alert(this.title); return false;" href="#" title="먼저 좋아요를 취소해주세요."
                  class="btn btn-xs btn-secondary btn-outline"> 싫어요👎 </a>
              </c:if>

              <c:if test="${actorCanCancelBadReaction}">
                <a onclick="alert(this.title); return false;" href="#" title="먼저 싫어요를 취소해주세요."
                  class="btn btn-xs btn-primary btn-outline"> 좋아요👍 </a>
                <span>&nbsp;</span>
                <a
                  href="/usr/reactionPoint/doCencelBadReaction?relTypeCode=article&relId=${param.id}&replaceUri=${rq.encodedCurrentUri}"
                  class="btn btn-xs btn-secondary"> 싫어요👎 </a>
              </c:if>
              <span>&nbsp;</span>
              <span class="text-red-700">${article.badReactionPoint}</span>
            </div>
          </td>
        </tr>
        <tr>
          <th>제목</th>
          <td>${article.title}</td>
        </tr>
        <tr>
          <th>내용</th>
          <td>
            <div class="toast-ui-viewer">
              <script type="text/x-template">
               ${article.body} 
                </script>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="btns">
      <c:if test="${empty param.listUri}">
        <button class="btn btn-link" type="button" onclick="history.back();">뒤로가기</button>
      </c:if>
      <c:if test="${not empty param.listUri}">
        <a class="btn btn-link" href="${param.listUri}">뒤로가기</a>
      </c:if>

      <c:if test="${article.extra__actorCanModify || rq.admin}">
        <a class="btn btn-link" href="../article/modify?id=${article.id}">게시물 수정</a>
      </c:if>
      <c:if test="${article.extra__actorCanDelete || rq.admin}">
        <a class="btn btn-link" onclick="if ( confirm('정말 삭제하시겠습니까?') == false) return false;"
          href="../article/doDelete?id=${article.id}">게시물 삭제</a>
      </c:if>
    </div>
  </div>
</section>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <h1>댓글 작성</h1>
    <c:if test="${rq.logined}">
      <form class="table-box-type-1" method="post" action="../reply/doWrite"
        onsubmit="ReplyWrite__submitForm(this); return false;">
        <input type="hidden" name="replaceUri" value="${rq.currentUri}">
        <input type="hidden" name="relTypeCode" value="article">
        <input type="hidden" name="relId" value="${article.id}">
        <table>
          <tbody>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>작성자</th>
            <td>
              <img style="float: left" class="w-6 h-6 ml-1 mr-1 rounded-full object-cover"
                src="${rq.getProfileImgUri(rq.loginedMember.id)}" alt="" onerror="${rq.profileFallbackImgOnErrorHtml}" />
              ${rq.loginedMember.nickname}
            </td>
          </tr>
          <tr>
            <th>내용</th>
            <td>
              <textarea class="w-full textarea textarea-bordered" name="body" rows="5" placeholder="내용"></textarea>
            </td>
          </tr>
          <tr>
            <th>댓글작성</th>
            <td>
              <button type="submit" class="btn btn-primary">댓글작성</button>
            </td>
          </tr>
          </tbody>
        </table>
      </form>
    </c:if>
    <c:if test="${rq.notLogined}">
      <a class="btn btn-link" href="${rq.loginUri}">로그인</a> 후 이용해주세요.
    </c:if>
  </div>
</section>

<script>
	function ReplyList_deleteReply(btn) {
		const $clicked = $(btn);
		const $target = $clicked.closest('[data-id]');
		const id = $target.attr('data-id');

		$clicked.html('삭제중...');

		$.post('../reply/doDeleteAjax', {
			id : id
		}, function(data) {
			if (data.success) {
				$target.remove();
			} else {
				if (data.msg) {
					alert(data.msg);
				}

				$clicked.html('삭제실패');
			}

		}, 'json');
	}
</script>

<section class="mt-5">
  <div class="container mx-auto px-3">
    <h1>댓글 리스트(${replies.size()})</h1>
    <table class="table table-fixed w-full">
      <colgroup>
        <col width="50" />
        <col width="150" />
        <col width="150" />
        <col />
        <col width="200" />
        <col width="50" />
        <col width="50" />
        <col width="200" />
      </colgroup>
      <thead>
        <tr>
          <th>번호</th>
          <th>작성날짜</th>
          <th>수정날짜</th>
          <th>내용</th>
          <th>작성자</th>
          <th>좋아요</th>
          <th>싫어요</th>
          <th>비고</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="reply" items="${replies}">
          <tr data-id="${reply.id}" class="hover">
            <td>${reply.id}</td>
            <td>${reply.forPrintType1RegDate}</td>
            <td>${reply.forPrintType1UpdateDate}</td>
            <td>${reply.forPrintBody}</td>
            <td>
              <img style="float: left" class="w-6 h-6 ml-1 mr-1 rounded-full object-cover"
                src="${rq.getProfileImgUri(reply.memberId)}" alt="" onerror="${rq.profileFallbackImgOnErrorHtml}" />
              ${reply.extra__writerName}
            </td>
            <td>
              <span class="text-blue-700">${reply.goodReactionPoint}</span>
            </td>
            <td>
              <span class="text-red-700">${reply.badReactionPoint}</span>
            </td>
            <td>
              <c:if test="${reply.extra__actorCanModify || rq.admin}">
                <a class="btn btn-link" href="../reply/modify?id=${reply.id}&replaceUri=${rq.encodedCurrentUri}">수정</a>
              </c:if>
              <c:if test="${reply.extra__actorCanDelete || rq.admin}">
                <a class="btn btn-link"
                  onclick="if ( confirm('정말 삭제하시겠습니까?') ) {ReplyList_deleteReply(this); } return false;">삭제</a>
              </c:if>
            </td>
            <td>
            
            <div class="flex items-center">
              <span class="text-blue-700">${reply.goodReactionPoint}</span>
              <span>&nbsp;</span>
              
              <c:if test="${reply.extra__actorCanMakeReactionPoint}">
                <a
                  href="/usr/reactionPoint/doGoodReaction?relTypeCode=reply&relId=${reply.id}&replaceUri=${rq.encodedCurrentUri}"
                  class="btn btn-xs btn-primary btn-outline"> 좋아요👍 </a>
                <span>&nbsp;</span>
                <a
                  href="/usr/reactionPoint/doBadReaction?relTypeCode=reply&relId=${reply.id}&replaceUri=${rq.encodedCurrentUri}"
                  class="btn btn-xs btn-secondary btn-outline"> 싫어요👎 </a>
              </c:if>

              <c:if test="${reply.extra__actorCanCancelGoodReaction}">
                <a
                  href="/usr/reactionPoint/doCencelGoodReaction?relTypeCode=reply&relId=${reply.id}&replaceUri=${rq.encodedCurrentUri}"
                  class="btn btn-xs btn-primary"> 좋아요👍 </a>
                <span>&nbsp;</span>
                <a onclick="alert(this.title); return false;" href="#" title="먼저 좋아요를 취소해주세요."
                  class="btn btn-xs btn-secondary btn-outline"> 싫어요👎 </a>
              </c:if>

              <c:if test="${reply.extra__actorCanCancelBadReaction}">
                <a onclick="alert(this.title); return false;" href="#" title="먼저 싫어요를 취소해주세요."
                  class="btn btn-xs btn-primary btn-outline"> 좋아요👍 </a>
                <span>&nbsp;</span>
                <a
                  href="/usr/reactionPoint/doCencelBadReaction?relTypeCode=reply&relId=${reply.id}&replaceUri=${rq.encodedCurrentUri}"
                  class="btn btn-xs btn-secondary"> 싫어요👎 </a>
              </c:if>
              
              
              <span>&nbsp;</span>
              <span class="text-red-700">${reply.badReactionPoint}</span>
            </div>
            
          
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</section>



<%@include file="../../common/foot.jspf"%>
