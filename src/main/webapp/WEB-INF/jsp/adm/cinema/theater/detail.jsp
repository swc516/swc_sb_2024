<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${region}_${branch} / ${theaterInfos.get(0).getTheater()} 정보" />
<%@include file="../../../common/head.jspf"%>

<section>
  <div>
    <form action="../theater/doModify" method="post" name="cb">
      <div class="container mx-auto px-3">

        <a class="btn btn-success" href="../../cinema/detail?id=${theaterInfos.get(0).getCinemaId()}">상영관 목록으로</a>

        <div class="divider divider-info">Screen</div>
        <table style="margin-left: auto; margin-right: auto;">
          <tr>
            <td>＃</td>
            <c:forEach var="seatCol" items="${seatColArr}">
              <td style="text-align: center">${seatCol}</td>
            </c:forEach>
          </tr>
          <c:forEach var="theaterInfo" items="${theaterInfos}">
            <c:if test="${theaterInfo.seatCol == 1}">
              <tr>
                <td style="text-align: center">${theaterInfo.seatRow}</td>
            </c:if>
            <td>
              <c:if test="${theaterInfo.seatStatus == '일반'}">
                <input class="checkbox seatRow-${theaterInfo.seatRow} seatCol-${theaterInfo.seatCol}" type="checkbox" name="seats"
                  value=" ${theaterInfo.seatRow}-${theaterInfo.seatCol}-${theaterInfo.seatStatus}">
              </c:if>
              <c:if test="${theaterInfo.seatStatus == '장애'}">
                <input class="checkbox checkbox-success seatRow-${theaterInfo.seatRow} seatCol-${theaterInfo.seatCol}"
                  type="checkbox" name="seats" value=" ${theaterInfo.seatRow}-${theaterInfo.seatCol}-${theaterInfo.seatStatus}">
              </c:if>
              <c:if test="${theaterInfo.seatStatus == '없음'}">
                <input class="checkbox checkbox-error seatRow-${theaterInfo.seatRow} seatCol-${theaterInfo.seatCol}"
                  type="checkbox" name="seats" value=" ${theaterInfo.seatRow}-${theaterInfo.seatCol}-${theaterInfo.seatStatus}">
              </c:if>
            </td>
          </c:forEach>
        </table>

        <br>

        <label>
          일반
          <input type="radio" name="seatStatus" value="일반" class="radio">
        </label>
        <br>
        <label>
          장애
          <input type="radio" name="seatStatus" value="장애" class="radio radio-success">
        </label>
        <br>
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
          <input class="checkbox empty" type="checkbox" name="empty" />
        </label>
        <br>
        <c:forEach var="seatRow" items="${seatRowArr}">
          <label>
            &nbsp;${seatRow}
            <input class="checkbox-${seatRow} checkbox checkbox-xs" type="checkbox" id="${seatRow}" />
           </label>
          <script>
											$('.checkbox-${seatRow}')
													.change(
															function() {
																const $all = $(this);
																const allChecked = $all
																		.prop('checked');

																$(
																		'.seatRow-${seatRow}')
																		.prop(
																				'checked',
																				allChecked);

															})

											$('.seatRow-${seatRow}')
													.change(
															function() {
																const checkboxSeatRowCount = $('.seatRow-${seatRow}').length;
																const checkboxSeatRowCheckedCount = $('.seatRow-${seatRow}:checked').length;

																const allChecked = checkboxSeatRowCount == checkboxSeatRowCheckedCount;

																$(
																		'.checkbox-${seatRow}')
																		.prop(
																				'checked',
																				allChecked);
															})
										</script>
        </c:forEach>
        <br>
        <br>
        <c:forEach var="seatCol" items="${seatColArr}">
          <label>
            &nbsp;${seatCol}
            <input class="checkbox-${seatCol} checkbox checkbox-xs" type="checkbox" id="${seatCol}" />
          </label>
          <script>
											$('.checkbox-${seatCol}')
													.change(
															function() {
																const $all = $(this);
																const allChecked = $all
																		.prop('checked');

																$(
																		'.seatCol-${seatCol}')
																		.prop(
																				'checked',
																				allChecked);

															})

											$('.seatCol-${seatCol}')
													.change(
															function() {
																const checkboxSeatColCount = $('.seatCol-${seatCol}').length;
																const checkboxSeatColCheckedCount = $('.seatCol-${seatCol}:checked').length;

																const allChecked = checkboxSeatColCount == checkboxSeatColCheckedCount;

																$(
																		'.checkbox-${seatCol}')
																		.prop(
																				'checked',
																				allChecked);
															})
										</script>
        </c:forEach>
        <div>
          <button class="btn btn-primary" type="submit">좌석정보 수정</button>
          <a class="btn btn-error"
            href="../theater/doTheaterInfoDelete?theaterInfoId=${theaterInfos.get(0).getTheaterInfoId()}">상영관 삭제</a>
        </div>
        <br>
        선택된 좌석 :
        <span id="multiPrint"></span>
        <br>
      </div>
      <input type="hidden" name="cinemaId" value="${theaterInfos.get(0).getCinemaId()}">
      <input type="hidden" name="theaterInfoId" value="${theaterInfos.get(0).getTheaterInfoId()}">
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

<%@include file="../../../common/foot.jspf"%>



