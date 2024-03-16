<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut" %>

<c:set var="pageTitle" value="마이" />
<%@include file="../common/head.jspf"%>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <table border="1">
        <tbody>
        <colgroup>
          <col width="200" />
        </colgroup>
        <tr>
          <th>로그인아이디</th>
          <td>${rq.loginedMember.loginId}</td>
        </tr>
        <tr>
          <th>이름</th>
          <td>${rq.loginedMember.name}</td>
        </tr>
        <tr>
          <th>닉네임</th>
          <td>${rq.loginedMember.nickname}</td>
        </tr>
        <tr>
          <th>이메일</th>
          <td>${rq.loginedMember.email}</td>
        </tr>
        <tr>
          <th>휴대폰</th>
          <td>${rq.loginedMember.cellphoneNo}</td>
        </tr>
        <tr>
          <th>비고</th>
          <td>
            <a href="../member/checkPassword?replaceUri=${Ut.getUriEncoded('../member/modify')}" type="submit" class="btn btn-primary">회원정보 수정</a>
            <button type="button" class="btn btn-outline btn-error" onclick="history.back();">뒤로가기</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</section>

<%@include file="../common/foot.jspf"%>
