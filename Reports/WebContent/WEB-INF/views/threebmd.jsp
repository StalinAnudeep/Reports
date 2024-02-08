	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="threebmd" method="post">
                <div class="card-body">
                  <h3 class="card-title">Three Months BMD Exceeded Services report</h3>
                  <div class="row">
                     <div class="col-md-3">
					      <label for="inputState">Month</label>
					      <select id="mon" class="form-control" name="month" required="required">
					        <option value="">Select</option>
								<option value="JAN">JANUARY</option>
								<option value="FEB">FEBRUARY</option>
								<option value="MAR">MARCH</option>
								<option value="APR">APRIL</option>
								<option value="MAY">MAY</option>
								<option value="JUN">JUNE</option>
								<option value="JUL">JULY</option>
								<option value="AUG">AUGUST</option>
								<option value="SEP">SEPTEMBER</option>
								<option value="OCT">OCTOBER</option>
								<option value="NOV">NOVEMBER</option>
								<option value="DEC">DECEMBER</option>
					      </select>
					    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Year</label>
                       <select class="form-control" name="year" required="required" id="year">
					      	<option value="">--Select Year-- </option>
					    </select>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Get Details</label>
                        <button type="submit" class="btn btn-success">Get Details</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(circle)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>CIRCLE<br>NAME</th>
						<th>SCS</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${circle}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td><a href="threecircleall?cir=${mtrblc.CIRCLE}&mon_year=${mon_year}&type=cir&div=0&sub=0&sec=0">${mtrblc.CIRCLE}</a></td>
							<td><a href="threescsall?cir=${mtrblc.CIRCLE}&mon_year=${mon_year}&type=cir&div=0&sub=0&sec=0">${mtrblc.SCS}</a></td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right"><a href="allthreebmd?cir=${mtrblc.CIRCLE}&mon_year=${mon_year}&type=cir&div=0&sub=0&sec=0">${circle.stream().map(mtrblc -> mtrblc.SCS).sum()}</a></th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>


<c:if test="${ not empty fn:trim(division)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>DIVISION<br>NAME</th>
						<th>SCS</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${division}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td><a href="threecircleall?cir=${circlereturn}&div=${mtrblc.DIVNAME}&mon_year=${mon_year}&type=div&sub=0&sec=0">${mtrblc.DIVNAME}</a></td>
							<td><a href="threescsall?cir=${circlereturn}&div=${mtrblc.DIVNAME}&mon_year=${mon_year}&type=div&sub=0&sec=0">${mtrblc.SCS}</a></td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right"><a href="allthreebmd?cir=${circlereturn}&div=0&mon_year=${mon_year}&type=div&sub=0&sec=0">${division.stream().map(mtrblc -> mtrblc.SCS).sum()}</a></th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>


<c:if test="${ not empty fn:trim(subdivision)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>SUBDIVISION<br>NAME</th>
						<th>SCS</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${subdivision}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td><a href="threecircleall?cir=${circlereturn}&div=${divisionreturn}&mon_year=${mon_year}&type=sub&sub=${mtrblc.SUBNAME}&sec=0">${mtrblc.SUBNAME}</a></td>
							<td><a href="threescsall?cir=${circlereturn}&div=${divisionreturn}&mon_year=${mon_year}&type=sub&sub=${mtrblc.SUBNAME}&sec=0">${mtrblc.SCS}</a></td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right"><a href="allthreebmd?cir=${circlereturn}&div=${divisionreturn}&mon_year=${mon_year}&type=sub&sub=${mtrblc.SUBNAME}&sec=0">${subdivision.stream().map(mtrblc -> mtrblc.SCS).sum()}</a></th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>


<c:if test="${ not empty fn:trim(section)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>SECTION<br>NAME</th>
						<th>SCS</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${section}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.SECNAME}</td>
							<td><a href="threescsall?cir=${circlereturn}&div=${divisionreturn}&mon_year=${mon_year}&type=sec&sub=${subreturn}&sec=${mtrblc.SECNAME}">${mtrblc.SCS}</a></td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right"><a href="allthreebmd?cir=${circlereturn}&div=${divisionreturn}&mon_year=${mon_year}&type=sec&sub=${subreturn}&sec=0">${section.stream().map(mtrblc -> mtrblc.SCS).sum()}</a></th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>


<c:if test="${ not empty fn:trim(scs)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>CIRCLE</th>
						<th>DIVISION<BR>NAME</th>
						<th>SUBDIVISION<BR>NAME</th>
						<th>SECTION<BR>NAME</th>
						<th>USCNO</th>
						<th>NAME</th>
						<th>CAT</th>
						<th>CMD</th>
						<th>BMD<br>(KVA)<br>${opre}${year}</th>
						<th>BMD<br>(KVA)<br>${pre}${year}</th>
						<th>BMD<br>(KVA)<br>${p}${year}</th>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${scs}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.CIRCLE}</td>
							<td>${mtrblc.DIVNAME}</td>
							<td>${mtrblc.SUBNAME}</td>
							<td>${mtrblc.SECNAME}</td>
							<td>${mtrblc.USCNO}</td>
							<td>${mtrblc.NAME}</td>
							<td>${mtrblc.CATEGORY}</td>
							<td>${mtrblc.CMD}</td>
							<td>${mtrblc.BMD_1}</td>
							<td>${mtrblc.BMD_2}</td>
							<td>${mtrblc.BMD_3}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</c:if>










</div>
<script>
	requirejs([ 'jquery' ], function($) {
		$(document).ready(
				function() {
					$.ajax({
						type : "POST",
						url : "getCircles",
						success : function(data) {
							var saptype = jQuery.parseJSON(data);
							$.each(saptype, function(k, v) {
								$("#circle").append(
										"<option value="+k+">" + v
												+ "</option>");

							});
						}
					  
					});
				});
		$("#circle").append("<option value=ALL>ALL</option>");
		 var currentYear = (new Date()).getFullYear();
		 var currnetMonth=(new Date()).getMonth()+1;
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#year").append("<option value="+j+">"+j+"</option>");
		 }
		 $('#mon option:eq('+currnetMonth+')').prop('selected', true);
		 $('#year option[value="'+currentYear+'"]').prop('selected', true);

	});
</script>
<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
	
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'Three Months BMD Exceeded Services report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Three Months BMD Exceeded Services report'}
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>