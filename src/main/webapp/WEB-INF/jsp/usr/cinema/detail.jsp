<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${cinema.region}_${cinema.branch} 정보" />
<%@include file="../../common/head.jspf"%>
<%@include file="../../common/toastUiEditorLib.jspf"%>

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
          <td>${cinema.id}</td>
        </tr>
        <tr>
          <th>추가날짜</th>
          <td>${cinema.forPrintType2RegDate}</td>
        </tr>
        <tr>
          <th>수정날짜</th>
          <td>${cinema.forPrintType2UpdateDate}</td>
        </tr>
        <tr>
          <th>지역_지점</th>
          <td>${cinema.region}_${cinema.branch}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</section>


<%@include file="../../common/foot.jspf"%>
