<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="energysales" method="post">
		<div class="card-body">
			<h3 class="card-title"><strong><span class="text-danger">HT93</span> -Energy Audit Sales Report</strong></h3>
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
							<th>USCNO</th>
							<th>NAME</th>
							<th>CIRCLE</th>
							<th>DIVNAME</th>
							<th>SUBNAME</th>
							<th>SECNAME</th>
							<th>REC_KWH</th>
							<th>REC_KVAH</th>
							<th>COLONY</th>
							<th>BILLED_KVAH</th>
							<th>THIRD_PARTY</th>
							<th>OPEN_ACCESS</th>
							<th>SOLAR</th>
							<th>TOD_PEAK</th>
							<th>TOD_OFFPEAK</th>							
							<th>BILL_DATE</th>
							<th>LOAD</th>
							<th>CAT</th>
							<th>SUB_CAT</th>
							<th>VOLTAGE</th>
							<th>STATUS</th>
							<th>FEEDER_CODE</th>
							<th>FEEDER_NAME</th>
							<th>SERVICE TYPE</th>
							<th>OPEN ACCESS (70%)</th>
							<th>OPEN ACCESS (30%)</th> 
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${daily}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.NAME}</td>
								<td>${mtrblc.CIRCLE}</td>
								<td>${mtrblc.DIVNAME}</td>
								<td>${mtrblc.SUBNAME}</td>
								<td>${mtrblc.SECNAME}</td>
								<td>${mtrblc.REC_KWH}</td>
								<td>${mtrblc.REC_KVAH}</td>
								<td>${mtrblc.COLONY}</td>
								<td>${mtrblc.BILLED_KVAH}</td>
								<td>${mtrblc.TIRD_PARTY}</td>
								<td>${mtrblc.OPEN_ACCESS}</td>
								<td>${mtrblc.SOLAR}</td>
								<td>${mtrblc.MDTOD_PEAK}</td>
								<td>${mtrblc.MDTOD_OFFPEAK}</td>
								<td>${mtrblc.BILL_DATE}</td>
								<td>${mtrblc.CTCMD_HT}</td>
								<td>${mtrblc.CAT}</td>
								<td>${mtrblc.SUB_CAT}</td>
								<td>${mtrblc.VOLTAGE}</td>
								<td>${mtrblc.CTSTATUS}</td>
								<td>${mtrblc.FEEDER_CODE}</td>
								<td>${mtrblc.FEEDER_NAME}</td>
								<td>${mtrblc.STDESC}</td>
								<td class="text-right">${mtrblc.BTOA_KVAH}</td>
								<td class="text-right">${mtrblc.KVAH_UNITS}</td>
							</tr>
						</c:forEach>
					</tbody>
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
			   format: 'dd-mm-yy',
			   endDate: new Date()
			});
		 $( "#todate" ).datepicker({
			   format: 'dd-mm-yy',
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',  title :'Energy Audit Sales Report',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Energy Audit Sales Report',footer: true }
	            ]
	        }
	    });
	});
</script>