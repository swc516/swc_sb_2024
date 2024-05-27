<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="영화 예매" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.20/lodash.min.js"></script>
<!-- 포트원 결제 -->
<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<!-- jQuery -->
<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<!-- iamport.payment.js -->
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
<!-- 포트원 결제 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.20/lodash.min.js"></script>


<%@include file="../../common/head.jspf"%>

<section>
  <div>
    <form class="table-box-type-1" method="post" action="../ticket/doTicketing" name="frm">
      <div class="container mx-auto px-3">
        <input type="checkbox" class="checkbox">
        : 일반 좌석
        <br>
        <input type="checkbox" class="checkbox checkbox-success">
        : 장애인 배려 좌석
        <br>
        <input type="checkbox" class="checkbox" disabled>
        : 예매가 완료된 좌석
        <br>
        <hr>
        ${movieTitle} / ${cinemaRegion}_${cinemaBranch} / ${theater} / 상영날짜 : ${theaterTimes.get(0).date} / 상영시간 :
        (${theaterTimes.get(0).theaterTime}회차), ${playingTime}
        <div class="divider divider-info">Screen</div>
        <table style="margin-left: auto; margin-right: auto;">
          <tr>
            <td>＃</td>
            <c:forEach var="seatCol" items="${seatColArr}">
              <td style="text-align: center">${seatCol}</td>
            </c:forEach>
          </tr>
          <c:forEach var="theaterTime" items="${theaterTimes}">
            <c:if test="${theaterTime.seatCol == 1}">
              <tr>
                <td style="text-align: center">${theaterTime.seatRow}</td>
            </c:if>
            <td>
              <c:choose>
                <c:when test="${theaterTime.seatSell == true }">
                  <input class="checkbox seatRow-${theaterTime.seatRow} seatCol-${theaterTime.seatCol}" type="checkbox"
                    name="seats"
                    value="${theaterTime.id}_${theaterTime.seatRow}-${theaterTime.seatCol}-${theaterTime.seatStatus}"
                    disabled>
                </c:when>
                <c:otherwise>
                  <c:if test="${theaterTime.seatStatus == '일반'}">
                    <input class="checkbox seatRow-${theaterTime.seatRow} seatCol-${theaterTime.seatCol}"
                      type="checkbox" onclick="CountChecked(this)" name="seats"
                      value="${theaterTime.id}_${theaterTime.seatRow}-${theaterTime.seatCol}-${theaterTime.seatStatus}">
                  </c:if>
                  <c:if test="${theaterTime.seatStatus == '장애'}">
                    <input
                      class="checkbox checkbox-success seatRow-${theaterTime.seatRow} seatCol-${theaterTime.seatCol}"
                      onclick="CountChecked(this)" type="checkbox" name="seats"
                      value="${theaterTime.id}_${theaterTime.seatRow}-${theaterTime.seatCol}-${theaterTime.seatStatus}">
                  </c:if>
                  <c:if test="${theaterTime.seatStatus == '없음'}">

                  </c:if>
                </c:otherwise>
              </c:choose>

            </td>
          </c:forEach>
        </table>
        <br>
        <hr>
        <br>
        선택된 좌석 : <input type="text" id="selectedSeat" readonly> <br>
        총 가격 : <input type="text" id="totalAmount" readonly> <br>
  




        <input type="hidden" name="movieTitle" value="${movieTitle}">
        <input type="hidden" name="cinema" value="${cinemaRegion}_${cinemaBranch}">
        <input type="hidden" name="theater" value="${theater}">
        <input type="hidden" name="time" value="${theaterTimes.get(0).theaterTime}">
        <input type="hidden" name="startTime" value="${theaterTimes.get(0).startTime}">
        <input type="hidden" name="endTime" value="${theaterTimes.get(0).endTime}">
        <input type="hidden" name="theaterInfoId" value="${param.theaterInfoId}">
        <input type="hidden" name="theaterTimeId" value="${param.theaterTimeId}">
        <button type="button" class="btn btn-primary" onclick="requestPay(); return false;">예매하기</button>
        <input type="submit" value="">
    </form>
  </div>





</section>


<script type="text/javascript">
	var maxChecked = 4; //선택가능한 체크박스 갯수
	var totalChecked = 0; // 설정 끝

	function CountChecked(field) {
		if (field.checked){ // input check 박스가 체크되면 
			totalChecked += 1; // totalChecked 증가
			document.getElementById("selectedSeat").value = totalChecked;
			document.getElementById("totalAmount").value = totalChecked * 8000;
		}
		else{
			// 체크가 아니면
			totalChecked -= 1; // totalChecked 감소
			document.getElementById("selectedSeat").value = totalChecked;
			document.getElementById("totalAmount").value = totalChecked * 8000;
		}
		
		if (totalChecked > maxChecked) { // totalchecked 수가 maxchecked수보다 크다면
			alert("최대 " + maxChecked + "석 까지만 가능합니다."); // 팝업창 띄움
			field.checked = false;
			totalChecked -= 1;
			document.getElementById("selectedSeat").value = totalChecked;
			document.getElementById("totalAmount").value = totalChecked * 8000;
		}

	}
</script>









<script>
	var IMP = window.IMP;
	IMP.init("imp30576558");

	var today = new Date();
	var hours = today.getHours(); // 시
	var minutes = today.getMinutes(); // 분
	var seconds = today.getSeconds(); // 초
	var milliseconds = today.getMilliseconds();
	var makeMerchantUid = hours + minutes + seconds + milliseconds;

	function requestPay() {
		var seatsCount = $('input:checkbox[name=seats]:checked').length;
		if (seatsCount > 0) {
			IMP.request_pay({
				pg : 'kakaopay',
				merchant_uid : "IMP" + makeMerchantUid,
				name : '[CGW] ${movieTitle} '+ seatsCount +'매',
				amount : document.getElementById("totalAmount").value,
				buyer_email : '${email}',
				buyer_name : '${name}',
				buyer_tel : '${cellphoneNo}'
			}, function(rsp) { // callback
				if (rsp.success) {
					console.log(rsp);
					frm.submit();
				} else {
					console.log(rsp);
					alert('오류가 발생했습니다. 다시 시도해주세요.');
				}
			});
		} else {
			alert('좌석을 선택해주세요.');
			return;
		}
	}
</script>

<%@include file="../../common/foot.jspf"%>
