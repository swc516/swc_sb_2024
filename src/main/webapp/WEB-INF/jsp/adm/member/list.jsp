<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="관리자페이지 - 회원리스트 " />
<%@include file="../../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="flex">
      <div>
        회원 수 :
        <span class="text-blue-700">${membersCount}</span>
        명
      </div>
      <div class="flex-grow"></div>
      <form class="flex">
        <select data-value="${authLevel}" name="authLevel" class="select select-bordered">
          <option disabled>회원종류</option>
          <c:choose>
            <c:when test="${authLevel == 3}">
              <option value="3" selected>일반회원</option>
            </c:when>
            <c:otherwise>
              <option value="3">일반회원</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${authLevel == 7}">
              <option value="7" selected>관리자</option>
            </c:when>
            <c:otherwise>
              <option value="7">관리자</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${authLevel == 0}">
              <option value="0" selected>일반회원, 관리자</option>
            </c:when>
            <c:otherwise>
              <option value="0">일반회원, 관리자</option>
            </c:otherwise>
          </c:choose>
        </select>
        <select data-value="${param.searchKeywordTypeCode}" name="searchKeywordTypeCode" class="select select-bordered">
          <option disabled>검색타입</option>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'loginId'}">
              <option value="loginId" selected>아이디</option>
            </c:when>
            <c:otherwise>
              <option value="loginId">아이디</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'name'}">
              <option value="name" selected>이름</option>
            </c:when>
            <c:otherwise>
              <option value="name">이름</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'nickname'}">
              <option value="nickname" selected>닉네임</option>
            </c:when>
            <c:otherwise>
              <option value="nickname">닉네임</option>
            </c:otherwise>
          </c:choose>
          <c:choose>
            <c:when test="${param.searchKeywordTypeCode eq 'loginId, name, nickname'}">
              <option value="loginId, name, nickname" selected>아이디, 이름, 닉네임</option>
            </c:when>
            <c:otherwise>
              <option value="loginId, name, nickname">아이디, 이름, 닉네임</option>
            </c:otherwise>
          </c:choose>
        </select>
        <c:if test="${param.searchKeywordTypeCode eq 'name'}">
        </c:if>
        <input name="searchKeyword" type="text" class="ml-2 w-72 input input-bordered" placeholder="검색어" maxlength="20"
          value="${param.searchKeyword}" />
        <button type="submit" class="ml-2 btn btn-primary">검색</button>
      </form>
    </div>
    <div class="mt-3">
      <table class="table table-fixed w-full">
        <colgroup>
          <col width="50" />
          <col width="80" />
          <col width="80" />
          <col width="130" />
          <col width="130" />
          <col />
          <col />
        </colgroup>
        <thead>
          <tr>
            <th>
              <input class="checkbox-all-member-id" type="checkbox" />
            </th>
            <th>번호</th>
            <th>가입날짜</th>
            <th>갱신날짜</th>
            <th>아이디</th>
            <th>이름</th>
            <th>닉네임</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="member" items="${members}">
            <tr class="hover">
              <th>
                <input class="checkbox-member-id" value="${member.id}" type="checkbox" />
              </th>
              <td>${member.id}</td>
              <td>${member.forPrintType1RegDate}</td>
              <td>${member.forPrintType1UpdateDate}</td>
              <td>${member.loginId}</td>
              <td>${member.name}</td>
              <td>${member.nickname}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <script>
      $('.checkbox-all-member-id').change(function() {
      	const $all = $(this);
      	const allChecked = $all.prop('checked');
      
      	$('.checkbox-member-id').prop('checked', allChecked);
      
      })
      
      $('.checkbox-member-id').change(function() {
		const checkboxMemberIdCount = $('.checkbox-member-id').length;
		const checkboxMemberIdCheckedCount = $('.checkbox-member-id:checked').length;

		const allChecked = checkboxMemberIdCount == checkboxMemberIdCheckedCount;

		$('.checkbox-all-member-id').prop(
				'checked', allChecked);
		})
	</script>
  
    <div>
      <button class="btn btn-error btn-delete-selected-members">선택삭제</button>
    </div>

    <form method="post" name="do-delete-members-form" action="../member/doDeleteMembers">
      <input type="hidden" name="ids" value=""/>
      <input type="hidden" name="replaceUri" value="${rq.currentUri}"/>
    </form>

    <script>
     $('.btn-delete-selected-members').click(function(){
    	 const values = $('.checkbox-member-id:checked').map((index, el) => el.value).toArray();
    	 
    	 if(values.length == 0) {
    		 alert('삭제할 회원을 선택해주세요.');
    		 return;
    	 }
    	 
    	 if(confirm('정말 삭제하시겠습니까?') == false){
    		 return;
    	 }
    	 
    	 document['do-delete-members-form'].ids.value = values.join(',');     
    	 document['do-delete-members-form'].submit();   
     })
    </script>

    <div class="page-menu mt-4" align="center">
      <c:set var="pageMenuArmLen" value="4" />
      <c:set var="startPage" value="${page - pageMenuArmLen >= 1 ? page - pageMenuArmLen : 1}" />
      <c:set var="endPage" value="${page + pageMenuArmLen <= pagesCount ? page + pageMenuArmLen : pagesCount}" />

      <c:set var="pageBaseUri" value="?authLevel=${authLevel}" />
      <c:set var="pageBaseUri" value="${pageBaseUri}&searchKeyword=${param.searchKeyword}" />
      <c:set var="pageBaseUri" value="${pageBaseUri}&searchKeywordTypeCode=${param.searchKeywordTypeCode}" />

      <c:if test="${startPage > 1}">
        <a class="btn btn-sm" href="${pageBaseUri}&page=1">1</a>
        <c:if test="${startPage > 2}">
          <a class="btn btn-sm btn-disabled">...</a>
        </c:if>
      </c:if>
      <c:forEach begin="${startPage}" end="${endPage}" var="i">
        <a class="btn btn-sm ${page == i ? 'btn-active' : '' }" href="${pageBaseUri}&page=${i}">${i}</a>
      </c:forEach>

      <c:if test="${endPage < pagesCount}">
        <c:if test="${endPage < pagesCount - 1}">
          <a class="btn btn-sm btn-disabled">...</a>
        </c:if>
        <a class="btn btn-sm" href="${pageBaseUri}&page=${pagesCount}">${pagesCount}</a>
      </c:if>
    </div>
  </div>

</section>

<%@include file="../../common/foot.jspf"%>
