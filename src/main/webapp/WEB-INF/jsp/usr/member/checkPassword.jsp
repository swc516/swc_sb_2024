<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="비밀번호 확인" />
<%@include file="../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <form class="table-box-type-1" method="post" action="../member/doCheckPassword">
    <input type="hidden" name="replaceUri" value="${param.replaceUri}"/>
      <table>
        <tbody>
        <colgroup>
          <col width="200" />
        </colgroup>
        <tr>
          <th>로그인아이디</th>
          <td>${rq.loginedMember.loginId}
          </td>
        </tr>
        <tr>
          <th>로그인비밀번호</th>
          <td>
            <input name="loginPw" type="password" placeholder="로그인비밀번호" class="w-96 input input-bordered w-full max-w-xs" required/>
          </td>
        </tr>
        <tr>
          <th>비밀번호 확인</th>
          <td>
            <button type="submit" class="btn btn-primary">비밀번호 확인</button>
            <button type="button" class="btn btn-outline btn-error" onclick="history.back();">뒤로가기</button>
          </td>
        </tr>
        </tbody>
      </table>
    </form>
  </div>

</section>

<%@include file="../common/foot.jspf"%>