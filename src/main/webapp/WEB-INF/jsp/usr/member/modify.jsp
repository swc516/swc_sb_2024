<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="회원정보수정 " />
<%@include file="../common/head.jspf"%>

<script>
  let MemberModify__submitFormDone = false;
  function MemberModify__submitForm(form) {
    if (MemberModify__submitFormDone) {
      return;
    }

	    form.loginPw.value = form.loginPw.value.trim();
	    form.loginPwConfirm.value = form.loginPwConfirm.value.trim();
    
	if ( form.loginPw.value.length > 0) {
	    if (form.loginPwConfirm.value.length == 0) {
	      alert('비밀번호 확인을 입력해주세요');
	      form.loginPwConfirm.focus();
	      return;
	    }
	}
	
	if ( form.loginPwConfirm.value.length > 0) {
	    if (form.loginPw.value.length == 0) {
	      alert('비밀번호를 입력해주세요');
	      form.loginPwConfirm.focus();
	      return;
	    }
	}
	

    if (form.loginPw.value != form.loginPwConfirm.value) {
      alert('비밀번호가 일치하지 않습니다');
      form.loginPwConfirm.focus();
      return;
    }

    form.name.value = form.name.value.trim();

    if (form.name.value.length == 0) {
      alert('이름을 입력해주세요');
      form.name.focus();
      return;
    }

    form.nickname.value = form.nickname.value.trim();

    if (form.nickname.value.length == 0) {
      alert('닉네임을 입력해주세요');
      form.nickname.focus();
      return;
    }
    
    form.email.value = form.email.value.trim();

    if (form.email.value.length == 0) {
      alert('이메일을 입력해주세요');
      form.email.focus();
      return;
    }
    
    form.cellphoneNo.value = form.cellphoneNo.value.trim();

    if (form.cellphoneNo.value.length == 0) {
      alert('휴대전화번호를 입력해주세요');
      form.cellphoneNo.focus();
      return;
    }


    MemberModify__submitFormDone = true;
    form.submit();
  }
</script>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <form class="table-box-type-1" method="post" action="../member/doModify" onsubmit="MemberModify__submitForm(this); return false;">
        <input type="hidden" name="memberModifyAuthKey" value="${param.memberModifyAuthKey}" />
        <table>
          <tbody>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>로그인아이디</th>
            <td>${rq.loginedMember.loginId}</td>
          </tr>
          <tr>
            <th>새 비밀번호</th>
            <td>
              <input class="input input-bordered" name="loginPw" placeholder="새 비밀번호를 입력해주세요." type="password"/>
            </td>
          </tr>
          <tr>
            <th>새 비밀번호 확인</th>
            <td>
              <input class="input input-bordered" name="loginPwConfirm" placeholder="새 비밀번호를 입력해주세요." type="password" />
            </td>
          </tr>
          <tr>
            <th>이름</th>
            <td>
              <input class="input input-bordered" name="name" placeholder="이름을 입력해주세요." type="text" value="${rq.loginedMember.name}" />
            </td>
          </tr>
          <tr>
            <th>닉네임</th>
            <td>
              <input class="input input-bordered" name="nickname" placeholder="닉네임을 입력해주세요." type="text"  value="${rq.loginedMember.nickname}"/>
            </td>
          </tr>
          <tr>
            <th>이메일</th>
            <td>
              <input class="input input-bordered" name="email" placeholder="이메일을 입력해주세요." type="text"  value="${rq.loginedMember.email}"/>
            </td>
          </tr>
          <tr>
            <th>휴대전화번호</th>
            <td>
              <input class="input input-bordered" name="cellphoneNo" placeholder="휴대전화번호를 입력해주세요." type="text"  value="${rq.loginedMember.cellphoneNo}"/>
            </td>
          </tr>
          <tr>
            <th>회원정보수정</th>
            <td>
              <button type="submit" class="btn btn-primary">회원정보 수정</button>
              <button type="button" class="btn btn-outline btn-error" onclick="history.back();">뒤로가기</button>
            </td>
          </tr>
          </tbody>
        </table>
      </form>
    </div>
  </div>
</section>

<%@include file="../common/foot.jspf"%>
