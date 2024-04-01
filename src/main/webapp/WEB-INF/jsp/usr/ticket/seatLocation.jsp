<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 노말라이즈, 라이브러리까지 한번에 해결 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tailwindcss/2.2.7/tailwind.min.css" />
<!-- 테일윈드 불러오기 -->
<link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2/dist/tailwind.min.css" rel="stylesheet" type="text/css" />
<!-- 데이지 UI -->
<link href="https://cdn.jsdelivr.net/npm/daisyui@4.7.3/dist/full.min.css" rel="stylesheet" type="text/css" />
<!-- 폰트어썸 불러오기 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" />
<!--  사이트 공통 CSS -->
<link rel="stylesheet" href="/resource/common.css" />
<!--  사이트 공통 JS -->
<script src="/resource/common.js" defer="defer"></script>
</head>
<body>
  <div style="width: 500px;">
    <div class="divider divider-info">Screen</div>
    <table style="margin-left: auto; margin-right: auto;">
      <tr>
        <td>＃</td>
        <c:forEach var="seatNo" items="${seatNoArr}">
          <td style="text-align: center">${seatNo}</td>
        </c:forEach>
      </tr>
      <c:forEach var="theater" items="${theaters}">
        <c:if test="${theater.seatNo == 1}">
          <tr>
            <td style="text-align: center">${theater.seatId}</td>
        </c:if>
        <td>
          <c:if test="${theater.seatStatus != '없음'}">
            <c:if test="${theater.extra__seat != mySeat}">
              <input class="checkbox" type="checkbox" disabled>
            </c:if>
            <c:if test="${theater.extra__seat == mySeat}">
              <input class="checkbox checkbox-error" type="checkbox" checked>
            </c:if>
          </c:if>
          <c:if test="${theater.seatStatus == '없음'}">

          </c:if>
        </td>
      </c:forEach>
    </table>
  </div>
</body>
</html>