<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="CBAndDemand" method="post">
		<div class="card-body">
			<h3 class="card-title">CB Split (Arrear, Demand Part)  Service Wise Report</h3>
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
					<label for="inputState">Month</label> <select id="mon"
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
						<label class="form-label">GET Report</label>
						<button type="submit" class="btn btn-success">GET Report</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(cdmd)}">
		    <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="2">S NO</th>
							
							
							<th rowspan="2" class="text-center">CIRCLE	</th>
							<th rowspan="2" class="text-center">DIVISION	</th>
							<th rowspan="2" class="text-center">SUB DIVISION	</th>
							<th rowspan="2" class="text-center">SECTION</th>
							<th rowspan="2" class="text-center">SCNO	</th>
							<th rowspan="2" class="text-center">NAME	</th>
							<th rowspan="2" class="text-center">STATUS	</th>
							<th rowspan="2" class="text-center">TYPE	</th>
							<th rowspan="2" class="text-center">OPENING BALANCE</th>
							<th colspan="2" class="text-center">DURING MONTH </th>

							
							<th  rowspan="2" class="text-center">CB AS ON MONTH ${month}</th>
							<th colspan="2" class="text-center">CB AS ON MONTH  ${month} SPLIT</th>
		
							
						</tr>
						<tr>
						<th class="text-center">DEMAND	</th>
							<th class="text-center">COLLECTION</th>
							<th class="text-center">DEMAND_PART	</th>
							<th class="text-center">ARREAR_PART</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${cdmd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								
								
								<td class="text-center">${mtrblc.CIRNAME}</td>
								<td  class="text-center">${mtrblc.DIVNAME}	</td>
								<td  class="text-center">${mtrblc.SUBNAME}	</td>
								<td  class="text-center">${mtrblc.SECNAME}	</td>
								<td class="text-center">${mtrblc.CTUSCNO}</td>
								<td>${mtrblc.CTNAME}</td>
								<td>${mtrblc.CTSTATUS}</td>
								<td class="text-center">${mtrblc.TYPE}</td>
								<td class="text-right">${mtrblc.TOB}</td>
								<td class="text-right">${mtrblc.TDEM}</td>
								<td class="text-right">${mtrblc.TCOLL}</td>
								<td class="text-right">${mtrblc.CB}</td>
								<td class="text-right">${mtrblc.DEMAND_PART}</td>
								<td class="text-right">${mtrblc.ARREAR_PART}</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="7" class="text-right">Grand Total</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.TOB).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.TDEM).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.TCOLL).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.CB).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.DEMAND_PART).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.ARREAR_PART).sum()}</th>
						</tr>
					</tfoot>
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
		$("#circle").append("<option value='ALL'>ALL</option>");
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'HT CB FOR THE MONTH OF ${month}  SPLIT(AREAR PART AND DEMAND PART)',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'HT CB FOR THE MONTH OF ${month}  SPLIT(AREAR PART AND DEMAND PART)',footer: true }
	            ]
	        }
	    });
	});
</script>


<jsp:include page="footer.jsp"></jsp:include>