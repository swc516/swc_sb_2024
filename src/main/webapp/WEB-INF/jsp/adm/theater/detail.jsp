<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${param.relTypeCode} / ${theaterName} 정보" />
<%@include file="../../common/head.jspf"%>
<%@include file="../../common/toastUiEditorLib.jspf"%>

<section>
  <div>
    <form action="../theater/doModify" method="post">
      <div class="container mx-auto px-3" style="text-align: center">
        <c:set var="i" value="${0}" />
        <div class="divider divider-info">Screen</div>
        <c:forEach var="theater" items="${theaters}" varStatus="status">
          <c:choose>
            <c:when test="${theater.seatId == 65+i}">
              <c:if test="${theater.seatStatus eq '일반'}">
                <input class="checkbox seatId-${theater.seatId} seatNo-${theater.seatNo}" type="checkbox" name="seats"
                  value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
              </c:if>
              <c:if test="${theater.seatStatus eq '장애'}">
                <input class="checkbox checkbox-success seatId-${theater.seatId} seatNo-${theater.seatNo}" type="checkbox" name="seats"
                  value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
              </c:if>
              <c:if test="${theater.seatStatus eq '없음'}">
                <input class="checkbox checkbox-warning seatId-${theater.seatId} seatNo-${theater.seatNo}" type="checkbox" name="seats"
                  value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
              </c:if>
            </c:when>
            <c:otherwise>
              <c:set var="i" value="${i+1}" />
              <br>
              <c:if test="${theater.seatId == 65+i}">
                <c:if test="${theater.seatStatus eq '일반'}">
                  <input class="checkbox seatId-${theater.seatId} seatNo-${theater.seatNo}" type="checkbox" name="seats"
                    value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
                </c:if>
                <c:if test="${theater.seatStatus eq '장애'}">
                  <input class="checkbox checkbox-success seatId-${theater.seatId} seatNo-${theater.seatNo}" type="checkbox" name="seats"
                    value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
                </c:if>
                <c:if test="${theater.seatStatus eq '없음'}">
                  <input class="checkbox checkbox-warning seatId-${theater.seatId} seatNo-${theater.seatNo}" type="checkbox" name="seats"
                    value=" ${theater.seatId}-${theater.seatNo}-${theater.seatStatus}">
                </c:if>
              </c:if>
            </c:otherwise>
          </c:choose>
        </c:forEach>
        <br>
        <input type="hidden" name="replaceUri" value="${rq.currentUri}">
        <input type="hidden" name="theaterName" value="${theaterName}">
        <input type="hidden" name="relTypeCode" value="${param.relTypeCode}">
        <input type="radio" name="seatStatus" value="일반">
        일반
        <input type="radio" name="seatStatus" value="장애">
        장애
        <input type="radio" name="seatStatus" value="없음">
        없음
        <br>
        <input class="checkbox-all checkbox checkbox-primary" type="checkbox" />
        전체선택
        <br>
        <c:forEach var="seatIdArr" items="${seatIdArr}">
          <input class="checkbox-${seatIdArr} checkbox checkbox-xs" type="checkbox" />&nbsp;${seatIdArr}열
        &nbsp;&nbsp;
        <script>
			$('.checkbox-${seatIdArr}').change(
					function() {
						const $all = $(this);
						const allChecked = $all.prop('checked');

						$('.seatId-${seatIdArr}').prop('checked', allChecked);

					})

			$('.seatId-${seatIdArr}')
					.change(
							function() {
								const checkboxSeatIdCount = $('.seatId-${seatIdArr}').length;
								const checkboxSeatIdCheckedCount = $('.seatId-${seatIdArr}:checked').length;

								const allChecked = checkboxSeatIdCount == checkboxSeatIdCheckedCount;

								$('.checkbox-${seatIdArr}')
										.prop('checked', allChecked);
							})
							
		</script>
        </c:forEach>
        <br><br>
         <c:forEach var="seatNoArr" items="${seatNoArr}">
          <input class="checkbox-${seatNoArr} checkbox checkbox-xs" type="checkbox" />&nbsp;${seatNoArr}행
        &nbsp;&nbsp;
        <script>
      $('.checkbox-${seatNoArr}').change(
          function() {
            const $all = $(this);
            const allChecked = $all.prop('checked');

            $('.seatNo-${seatNoArr}').prop('checked', allChecked);

          })

      $('.seatId-${seatNoArr}')
          .change(
              function() {
                const checkboxSeatNoCount = $('.seatNo-${seatNoArr}').length;
                const checkboxSeatNoCheckedCount = $('.seatNo-${seatNoArr}:checked').length;

                const allChecked = checkboxSeatNoCount == checkboxSeatNoCheckedCount;

                $('.checkbox-${seatNoArr}')
                    .prop('checked', allChecked);
              })
              
    </script>
        </c:forEach>

        <button class="btn btn-primary" type="submit">수정</button>
      </div>

    </form>
  </div>
</section>

<script>
	$('.checkbox-all').change(function() {
		const $all = $(this);
		const allChecked = $all.prop('checked');

		$('.checkbox').prop('checked', allChecked);

	})
	

	$('.checkbox')
			.change(
					function() {
						const checkboxSeatCount = $('.checkbox').length;
						const checkboxSeatCheckedCount = $('.checkbox:checked').length;

						const allChecked = checkboxSeatCount == checkboxSeatCheckedCount;

						$('.checkbox-all')
								.prop('checked', allChecked);
					})
					
					

</script>

<%@include file="../../common/foot.jspf"%>



