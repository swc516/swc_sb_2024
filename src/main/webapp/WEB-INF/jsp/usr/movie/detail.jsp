<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="영화 정보" />
<%@include file="../../common/head.jspf"%>



<section class="mt-5">

  <div class="container mx-auto px-3">
    <div class="table-box-type-1">
      <table>
        <tbody>
        <colgroup>
          <col width="200" />
        </colgroup>
        <tr>
          <th>영화명</th>
          <td>${movie.id}</td>
        </tr>
        <tr>
          <th>작성날짜</th>
          <td>${movie.title}</td>
        </tr>
        <tr>
          <th>수정날짜</th>
          <td>${movie.body}</td>
        </tr>

        </tbody>
      </table>
    </div>
  </div>
</section>






<%@include file="../../common/foot.jspf"%>
