<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="회원정보수정 " />
<%@include file="../common/head.jspf"%>

<script>
	let MemberModify__submitFormDone = false;
	function MemberModify__submitForm(form) {
		if (MemberModify__submitFormDone) {
			alert('처리중입니다.');
			return;
		}

		form.loginPw.value = form.loginPw.value.trim();
		form.loginPwConfirm.value = form.loginPwConfirm.value.trim();

		if (form.loginPw.value.length > 0) {
			if (form.loginPwConfirm.value.length == 0) {
				alert('비밀번호 확인을 입력해주세요');
				form.loginPwConfirm.focus();
				return;
			}
		}

		if (form.loginPwConfirm.value.length > 0) {
			if (form.loginPw.value.length == 0) {
				alert('비밀번호를 입력해주세요');
				form.loginPw.focus();
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

		const maxSizeMb = 10;
		const maxSize = maxSizeMb * 1024 * 1024

		const profileImgFileInput = form["file__member__0__extra__profileImg__1"];

		if (profileImgFileInput.value) {
			if (profileImgFileInput.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
				profileImgFileInput.focus();

				return;
			}
		}

		MemberModify__submitFormDone = true;
		form.submit();
	}
</script>


<section class="mt-5">
  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <form class="table-box-type-1" method="post" enctype="multipart/form-data" action="../member/doModify"
        onsubmit="MemberModify__submitForm(this); return false;">
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
              <input class="input input-bordered" name="loginPw" placeholder="새 비밀번호를 입력해주세요." type="password" />
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
              <input class="input input-bordered" name="name" placeholder="이름을 입력해주세요." type="text"
                value="${rq.loginedMember.name}" />
            </td>
          </tr>
          <tr>
            <th>닉네임</th>
            <td>
              <input class="input input-bordered" name="nickname" placeholder="닉네임을 입력해주세요." type="text"
                value="${rq.loginedMember.nickname}" />
            </td>
          </tr>
          <tr>
            <th>프로필 이미지</th>
            <td>
              <img class="w-40 h-40 object-cover rounded-full" src="${rq.getProfileImgUri(rq.loginedMember.id)}" alt="" onerror="${rq.removeProfileImgIfNotExitOnErrorHtmlAttr}" />
              <input accept="image/gif, image/jpeg, image/png" name="file__member__0__extra__profileImg__1"
                placeholder="프로필 이미지를 선택해주세요." type="file" />
                
              <div class="mt-2">
                <label class="cursor-pointer inline-flex">
                  <span class="label-text mr-2 mt-1">이미지 삭제</span>
                  <div>
                    <input type="checkbox"  name="deleteFile__member__0__extra__profileImg__1" value="Y" class="checkbox"/>
                    <span class="checkbox-mark"></span>
                  </div>
                </label>
              </div>
            
            </td>
          </tr>
          <tr>
            <th>이메일</th>
            <td>
              <input class="input input-bordered" name="email" placeholder="이메일을 입력해주세요." type="text"
                value="${rq.loginedMember.email}" />
            </td>
          </tr>
          <tr>
            <th>휴대전화번호</th>
            <td>
              <input class="input input-bordered" name="cellphoneNo" placeholder="휴대전화번호를 입력해주세요." type="text"
                value="${rq.loginedMember.cellphoneNo}" />
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
