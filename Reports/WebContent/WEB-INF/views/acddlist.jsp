<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="acddlist" method="post">
		<div class="card-body">
			<h3 class="card-title">ADC-D- List Report</h3>
			<div class="row">
				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Govt/Pvt</label> <select
							class="form-control" name="type" id="type"
							required="required">
							<option value="">Select Status</option>
							<option value="All">ALL</option>
							<option value="GOVT">GOVT</option>
							<option value="PVT">PVT</option>
						</select>
					</div>
				</div>
				
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">GET ACD D-LIST</label>
						<button type="submit" class="btn btn-success">GET ACD D-LIST</button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="hmon" name="hmon">
		<input type="hidden" id="hyear" name="hyear">
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(dlist)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Division</th>
							<th>Sub<br>Division</th>
							<th>Section</th>
							<th>SC.NO</th>
							<th>NAME</th>
							<th>CAT</th>
							<th>STATUS</th>
							<th>GOVT<br>PVT</th>
							<th>RKVAH</th>
							<th>ACD<br>Demand</th>
							<th>ACD BALANCE <br> TO BE PAY</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${dlist}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUB_DIV}</td>
								<td>${mtrblc.SECNAME}</td>
								<td>${mtrblc.SCNO}</td>
								<td>${mtrblc.NAME}</td>
								<td>${mtrblc.CAT}</td>
								<td>${mtrblc.STATUS}</td>
								<td>${mtrblc.GOV_PVT}</td>
								<td class="text-right">${mtrblc.RKVAH}</td>
								<td class="text-right">${mtrblc.ACD_DEMAND}</td>
								<td class="text-right">${mtrblc.ACD_BAL}</td>
							</tr>
						</c:forEach>
					</tbody>
					  <tfoot>
						<tr>
							<th colspan="10" class="text-right">Grand Total</th>
							<th colspan="2" class="text-right">ACD BAL:${dlist.stream().map(mtrblc -> mtrblc.ACD_BAL).sum()}</th>
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
		 $("#circle").append("<option value=ALL>ALL</option>");
		 var currentYear = (new Date()).getFullYear();
		 var currnetMonth=(new Date()).getMonth()+1;
		 $("#hyear").val(currentYear);
		 $("#hmon").val($("#mon").val());
		 
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'ACD_D_List_Report',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'ACD_D_List_Report',footer: true }
	            ]
	        }
	    });
	});
</script>