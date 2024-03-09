<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="로그인" />
<%@include file="../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <form class="table-box-type-1" method="post" action="../member/doLogin">
      <table border="1">
        <tbody>
        <colgroup>
          <col width="200" />
        </colgroup>
        <tr>
          <th>로그인아이디</th>
          <td>
            <input name="loginId" class="w-96" type="text" placeholder="로그인아이디">
          </td>
        </tr>
        <tr>
          <th>로그인비밀번호</th>
          <td>
            <input name="loginPw" class="w-96" type="password" placeholder="로그인비밀번호">
          </td>
        </tr>
        <tr>
          <th>로그인</th>
          <td>
            <input type="submit" value="로그인">
            <button type="button" onclick="history.back();">뒤로가기</button>
          </td>
        </tr>
        </tbody>
      </table>
    </form>
  </div>

</section>

<%@include file="../common/foot.jspf"%>
