<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="acdbalancereport" method="post">
		<div class="card-body">
			<h3 class="card-title">ACD Balance Report</h3>
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
					<div class="form-group">
						<label for="inputState">LEVI MONTH</label> <select id="mon"
							class="form-control" name="month">
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
				</div>
				<div class="col-md-3">
					<div class="form-group">
					      <label for="inputZip">Select Year</label>
					      <select  class="form-control" name="year" id="year">
					      <option value="">--Select Year--</option>
					      </select>
			    </div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">GET ACD Balance Report</label>
						<button type="submit" class="btn btn-success">GETACD Balance Report</button>
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
	
	
	<c:if test="${ not empty fn:trim(acd)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>USCNO</th>
							<th>GOVT/PVT</th>
							<th class="text-center">acd<br>Demand</th>
							<th class="text-center">Pay<br>ACD</th>
							<th class="text-center">Rj<br>ACD</th>
							<th class="text-center">Closing<br>Balance<br>ACD</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${acd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.CTGOVT_PVT}</td>
								<td>${mtrblc.ACDDEMAND}</td>
								<td>${mtrblc.PAYACD}</td>
								<td>${mtrblc.ACD_RJ}</td>
								<td>${mtrblc.CLOSING_ACD}</td>
							</tr>
						</c:forEach>
					</tbody>
        			  <tfoot>
						<tr>
							<th colspan="3" class="text-right">Grand Total</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.ACDDEMAND).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.PAYACD).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.ACD_RJ).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.CLOSING_ACD).sum()}</th>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'ACD_Balance_Report',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'ACD_Balance_Report',footer: true }
	            ]
	        }
	    });
	});
</script>