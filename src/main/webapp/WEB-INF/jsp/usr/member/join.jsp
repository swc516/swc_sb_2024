<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="회원가입 " />
<%@include file="../common/head.jspf"%>

<script>
	let MemberJoin__submitFormDone = false;
	let validLoginId = "";

	function MemberJoin__submitForm(form) {
		if (MemberJoin__submitFormDone) {
			alert('처리중입니다.');
			return;
		}

		form.loginId.value = form.loginId.value.trim();

		if (form.loginId.value.length == 0) {
			alert('로그인 아이디를 입력해주세요');
			form.loginId.focus();
			return;
		}

		if (form.loginId.value != validLoginId) {
			alert('해당 로그인 아이디는 올바르지 않습니다. 다른 로그인아이디를 입력해주세요.');
			form.loginId.focus();
			return;
		}

		form.loginPw.value = form.loginPw.value.trim();
		form.loginPwConfirm.value = form.loginPwConfirm.value.trim();

		if (form.loginPw.value.length == 0) {
			alert('비밀번호를 입력해주세요');
			form.loginPw.focus();
			return;
		}

		if (form.loginPwConfirm.value.length == 0) {
			alert('비밀번호 확인을 입력해주세요');
			form.loginPwConfirm.focus();
			return;
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

		MemberJoin__submitFormDone = true;
		form.submit();
	}

	var checkLoginIdDup = _.debounce(function(form) {

		<!--
		$('.loginId-message').html('<div class="mt-2">아이디를 입력해주세요</div>');
		-->

		$.get('../member/getLoginIdDup', {
			isAjax : 'Y',
			loginId : form.loginId.value,
		}, function(data) {
			var $message = $(form.loginId).next();

			if (data.resultCode.substr(0, 2) == 'S-') {
				$message.empty().append('<div class="mt-2 text-green-500">'+ data.msg +'</div>');
			} else {
				$message.empty().append('<div class="mt-2 text-red-500">'+ data.msg +'</div>');
			}

			if (data.success) {
				validLoginId = data.data1;
			} else {
				validLoginId = '';
			}

		}, 'json');

	}, 300);

	function JoinForm_checkLoginIdDup(input) {
		var form = input.form;
		form.loginId.value = form.loginId.value.trim();

		var $message = $(form.loginId).next();

		if (form.loginId.value.length == 0) {
			$message.empty();
			return;
		}
		checkLoginIdDup(form);
	}
</script>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <form class="table-box-type-1" method="post" action="../member/doJoin"
        onsubmit="MemberJoin__submitForm(this); return false;">
        <input type="hidden" name="afterJoinUri" value="${param.afterJoinUri}" />
        <table>
          <tbody>
          <colgroup>
            <col width="200" />
          </colgroup>
          <tr>
            <th>로그인아이디</th>
            <td>
              <input class="input input-bordered" name="loginId" placeholder="로그인아이디를 입력해주세요." type="text"
                onkeyup="JoinForm_checkLoginIdDup(this)" autocomplete="off" />
              <div class="message-msg"></div>
            </td>
          </tr>
          <tr>
            <th>비밀번호</th>
            <td>
              <input class="input input-bordered" name="loginPw" placeholder="비밀번호를 입력해주세요." type="password" />
            </td>
          </tr>
          <tr>
            <th>비밀번호 확인</th>
            <td>
              <input class="input input-bordered" name="loginPwConfirm" placeholder="비밀번호를 입력해주세요." type="password" />
            </td>
          </tr>
          <tr>
            <th>이름</th>
            <td>
              <input class="input input-bordered" name="name" placeholder="이름" type="text" />
            </td>
          </tr>
          <tr>
            <th>닉네임</th>
            <td>
              <input class="input input-bordered" name="nickname" placeholder="닉네임" type="text" />
            </td>
          </tr>
          <tr>
            <th>이메일</th>
            <td>
              <input class="input input-bordered" name="email" placeholder="이메일" type="text" />
            </td>
          </tr>
          <tr>
            <th>휴대전화번호</th>
            <td>
              <input class="input input-bordered" name="cellphoneNo" placeholder="휴대전화번호" type="text"
                value="${rq.loginedMember.cellphoneNo}" />
            </td>
          </tr>
          <tr>
            <th>회원가입</th>
            <td>
              <button type="submit" class="btn btn-primary">회원가입</button>
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
