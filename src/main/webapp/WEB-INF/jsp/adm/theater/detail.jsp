<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${param.relTypeCode} / ${theaterName} 정보" />
<%@include file="../../common/head.jspf"%>
<%@include file="../../common/toastUiEditorLib.jspf"%>

<section>
  <div>
    <form action="../theater/doModify" method="post" name="cb">
      <div class="container mx-auto px-3">
      
       <a class="btn btn-success" href="../theater/addTime?relTypeCode=${param.relTypeCode}&theaterName=${theaterName}">상영회차 추가</a>
      
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
              <c:if test="${theater.seatStatus == '일반'}">
                <input class="checkbox seatId-${theater.seatId} seatNo-${theater.seatNo}" type="checkbox" name="seats"
                  value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
              </c:if>
              <c:if test="${theater.seatStatus == '장애'}">
                <input class="checkbox checkbox-success seatId-${theater.seatId} seatNo-${theater.seatNo}"
                  type="checkbox" name="seats" value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
              </c:if>
              <c:if test="${theater.seatStatus == '없음'}">
                <input class="checkbox checkbox-error seatId-${theater.seatId} seatNo-${theater.seatNo}" type="checkbox"
                  name="seats" value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
              </c:if>
            </td>
          </c:forEach>
        </table>

        <br>

        <label>
          일반
          <input type="radio" name="seatStatus" value="일반" class="radio">
        </label><br>
        <label>
          장애
          <input type="radio" name="seatStatus" value="장애" class="radio radio-success">
        </label><br>
        <label>
          없음
          <input type="radio" name="seatStatus" value="없음" class="radio radio-error">
        </label>
        <br>
        <br>
        <label>
          전체선택
          <input class="checkbox-all checkbox checkbox-primary" type="checkbox" />
        </label>
        <label>
          선택좌석 출력 갱신(일괄선택 후 한번 눌러주세요)
          <input class="checkbox empty" type="checkbox" name="empty"/>
        </label>
        <br>
        <c:forEach var="seatIdArr" items="${seatIdArr}">
          <label>
            &nbsp;${seatIdArr}
            <input class="checkbox-${seatIdArr} checkbox checkbox-xs" type="checkbox" id="${seatIdArr}" />
          </label>
          <script>
											$('.checkbox-${seatIdArr}')
													.change(
															function() {
																const $all = $(this);
																const allChecked = $all
																		.prop('checked');

																$(
																		'.seatId-${seatIdArr}')
																		.prop(
																				'checked',
																				allChecked);

															})

											$('.seatId-${seatIdArr}')
													.change(
															function() {
																const checkboxSeatIdCount = $('.seatId-${seatIdArr}').length;
																const checkboxSeatIdCheckedCount = $('.seatId-${seatIdArr}:checked').length;

																const allChecked = checkboxSeatIdCount == checkboxSeatIdCheckedCount;

																$(
																		'.checkbox-${seatIdArr}')
																		.prop(
																				'checked',
																				allChecked);
															})
										</script>
        </c:forEach>
        <br>
        <br>
        <c:forEach var="seatNoArr" items="${seatNoArr}">
          <label>
            &nbsp;${seatNoArr}
            <input class="checkbox-${seatNoArr} checkbox checkbox-xs" type="checkbox" id="${seatNoArr}" />
          </label>
          <script>
											$('.checkbox-${seatNoArr}')
													.change(
															function() {
																const $all = $(this);
																const allChecked = $all
																		.prop('checked');

																$(
																		'.seatNo-${seatNoArr}')
																		.prop(
																				'checked',
																				allChecked);

															})

											$('.seatId-${seatNoArr}')
													.change(
															function() {
																const checkboxSeatNoCount = $('.seatNo-${seatNoArr}').length;
																const checkboxSeatNoCheckedCount = $('.seatNo-${seatNoArr}:checked').length;

																const allChecked = checkboxSeatNoCount == checkboxSeatNoCheckedCount;

																$(
																		'.checkbox-${seatNoArr}')
																		.prop(
																				'checked',
																				allChecked);
															})
										</script>
        </c:forEach>
        <div>
          <button class="btn btn-primary" type="submit">좌석정보 수정</button>
          <a class="btn btn-error" href="../theater/doDelete?relTypeCode=${param.relTypeCode}&theaterName=${theaterName}">상영관 삭제</a>
        </div>
        <br>
      선택된 좌석 :
      <span id="multiPrint"></span>
      <br>
      </div>

      <input type="hidden" name="replaceUri" value="${rq.currentUri}">
      <input type="hidden" name="theaterName" value="${theaterName}">
      <input type="hidden" name="relTypeCode" value="${param.relTypeCode}">
    </form>
  </div>
</section>

<script>
	$('.checkbox-all').change(function() {
		const $all = $(this);
		const allChecked = $all.prop('checked');

		$('.checkbox').prop('checked', allChecked);

	})

	$('.checkbox').change(function() {
		const checkboxSeatCount = $('.checkbox').length;
		const checkboxSeatCheckedCount = $('.checkbox:checked').length;

		const allChecked = checkboxSeatCount == checkboxSeatCheckedCount;

		$('.checkbox-all').prop('checked', allChecked);
	})

	$(document).ready(function() {
		// .check 클래스 중 어떤 원소가 체크되었을 때 발생하는 이벤트
		$(".checkbox").click(function() { // 여기서 .click은 체크박스의 체크를 뜻한다.
			var str = ""; // 여러개가 눌렸을 때 전부 출력이 될 수 있게 하나의 객체에 담는다.
			$(".checkbox").each(function() { // .each()는 forEach를 뜻한다.
				if ($(this).is(":checked")) // ":checked"를 이용하여 체크가 되어있는지 아닌지 확인한다.
					str += $(this).val().slice(-7, -3) + " "; // 체크된 객체를 str에 저장한다.

			});
			$("#multiPrint").text(str); // #multiPrint에 체크된 원소를 출력한다.
		});

	});
</script>

<%@include file="../../common/foot.jspf"%>



