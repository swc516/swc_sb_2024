<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.swc.exam.demo.util.Ut"%>

<c:set var="pageTitle" value="영화 예매(테스트) " />
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.20/lodash.min.js"></script>

<%@include file="../../common/head.jspf"%>

<section>
  <div>
    <form class="table-box-type-1" method="post" enctype="multipart/form-data" action="../ticket/ticketing">
      <div class="container mx-auto px-3">
      영화관:<input type="text" name="region" class="w-96 input input-bordered w-full max-w-xs" value="서울_방학" > <br>
      상영관:<input type="text" name="theaterName" class="w-96 input input-bordered w-full max-w-xs" value="1관"> <br>
      상영날짜:<input type="text" name="date" class="w-96 input input-bordered w-full max-w-xs" value="2024-03-29"><br>
      회차:<input type="text" name="time" class="w-96 input input-bordered w-full max-w-xs" value="1"> <br>
      
      </div>
        <button type="submit" class="btn btn-primary">예매하기</button>
    </form>
  </div>
</section>


<section>
  <div>
  asd
  
  </div>

</section>

<%@include file="../../common/foot.jspf"%>
