<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="daily" method="post">
		<div class="card-body">
			<h3 class="card-title">Total  Daily Collection Report</h3>
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
					<label for="inputState">From Date</label>
					<input class="form-control" id="fromdate" name="fromdate"  type="text" placeholder="--Select From Date--" autocomplete="off">
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">To Date</label>
						<input type="text" class="form-control" id="todate" name="todate" placeholder="--Select To Date---" autocomplete="off">
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">GET     TotalDailyCollection </label>
						<button type="submit" class="btn btn-success">GET TotalDailyCollection</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(daily)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>SNO</th>
							<th>SCNO</th>
							<th>COLL DATE</th>
							<th>SAP DOC DATE</th>
							<th>SAP DOC NO</th>
							<th>BILL AMOUNT</th>
							<th>COLLECTION AGAINST DEMAND</th>
							<th>RC</th>
							<th>ACD</th>
							<th>TOTAL AMOUNT</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${daily}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.PAY_DATE}</td>
								<td>${mtrblc.SAP_NO}</td>
								<td>${mtrblc.SAP_DT}</td>
								<td class="text-right">${mtrblc.BTCURDEM}</td>
								<td class="text-right">${mtrblc.PCMD}</td>
								<td class="text-right">${mtrblc.PRC}</td>
								<td class="text-right">${mtrblc.PACD}</td>
								<td class="text-right">${mtrblc.TOTAL_AMT}</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="5" class="text-right">Grand Total</th>
							<th class="text-right">${daily.stream().map(mtrblc -> mtrblc.BTCURDEM).sum()}</th>
							<th class="text-right">${daily.stream().map(mtrblc -> mtrblc.PCMD).sum()}</th>
							<th class="text-right">${daily.stream().map(mtrblc -> mtrblc.PRC).sum()}</th>
							<th class="text-right">${daily.stream().map(mtrblc -> mtrblc.PACD).sum()}</th>
							<th class="text-right">${daily.stream().map(mtrblc -> mtrblc.TOTAL_AMT).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	
</div>


<script>
	requirejs([ 'jquery','moment','datepicker'], function($,moment,datepicker) {
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
		 
		 $( "#fromdate" ).datepicker({
			   format: 'dd-mm-yyyy',
			   endDate: new Date()
			});
		 $( "#todate" ).datepicker({
			   format: 'dd-mm-yyyy',
			   endDate: new Date()
			});

	});
</script>
<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip,moment,datepicker) {
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',  title :'Daily Collection Report',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Daily Collection Report',footer: true }
	            ]
	        }
	    });
	});
</script>