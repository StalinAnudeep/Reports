<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="courtcases" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT114</span> - Service
					Wise Court Case Report</strong>
			</h3>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div>
				<div class="col-md-3">
					<label class="form-label">Month</label> <select id="mon"
						class="form-control" name="month" required="required">
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
						<label class="form-label">Year</label> <select
							class="form-control" name="year" required="required" id="year">
							<option value="">--Select Year--</option>
						</select>
					</div>
				</div>

				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">GET COURT CASE REPORT</label>
						<button type="submit" class="btn btn-success">GET COURT
							CASE REPORT</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(arrears)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<table class="table card-table table-vcenter text-nowrap datatable"
					style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="2" class="align-middle">S.NO</th>
							<th rowspan="2" class="align-middle">USCNO</th>
							<th rowspan="2" class="align-middle">NAME</th>
							<th rowspan="2" class="align-middle">DIVISION</th>
							<th rowspan="2" class="align-middle">SUBDIVISION</th>
							<th rowspan="2" class="align-middle">SECTION</th>
							<th rowspan="2" class="align-middle">COURT CB</th>
							<th rowspan="2" class="align-middle">COURT CUMULATIVE LPC</th>
							<th colspan="2" class="text-center">REALIZATION RJ COURT</th>
						</tr>


						<tr>
							<th class="text-right">COURT CASE</th>
							<th class="text-right">LPC</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${arrears}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.NAM}</td>
								<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUBDIVISION}</td>
								<td>${mtrblc.SECTION}</td>
								<td class="text-right">${mtrblc.CB_OTH}</td>
								<td class="text-right"> ${mtrblc.CB_CCLPC}</td>
								<td class="text-right">${mtrblc.RJ_OTH}</td>
								<td class="text-right">${mtrblc.RJ_CCLPC}</td>
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
		 var currnetMonth=(new Date()).getMonth();
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:' MONTH WISE ARREARS ABOVE 50000 Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'MONTH WISE ARREARS ABOVE 50000 Report'}
	            ]
	        }
	    });
	});
</script>
<script> 
	requirejs([ 'jquery' ], function($) {
			$("td,th").each(function() { 
				if ($.isNumeric( $(this).text())) {
				    // It isn't a number	
				    $(this).html(parseFloat($(this).text()).toLocaleString('en-IN', {style: 'decimal', currency: 'INR'})); 
				}
			}
				
				
			)
			
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>

