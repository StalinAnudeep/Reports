<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="hoddemandsplit" method="post">
		<div class="card-body">
			<h3 class="card-title">Department Wise, Service Wise Demand Split Report</h3>
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
						<label class="form-label">HOD Department</label> <select
							class="form-control" name="hoddept" id="hoddept"
							required="required">
							<option value="">Select Department</option>
						</select>
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label for="inputState">Ledger Month</label> <select id="mon"
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
				<div class="col-md-2">
					<div class="form-group">
						<label for="inputZip">Select Year</label> <select
							class="form-control" name="year" id="year">
							<option value="">--Select Year--</option>
						</select>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">GET HOD Demand Report</label>
						<button type="submit" class="btn btn-success">GET HOD Demand Report</button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="hmon" name="hmon"> <input
			type="hidden" id="hyear" name="hyear">
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>


	<c:if test="${ not empty fn:trim(acd)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">${title}</h2>
				<table class="table table-sm card-table table-vcenter text-nowrap datatable display">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>CIRCLE</th>
							<th>DIVISION</th>
							<th>SUB DIVISION</th>
							<th>SECTION</th>
							<th>USCNO</th>
							<th>NAME</th>
							<th>DEPTCODE</th>
							<th>DEPT NAME</th>
							<th>HOD NAME</th>
							<th>CAT</th>
							<th>SUB CAT</th>
							<th>HOD TYPE</th>
							<th>STATUS</th>
							<th>SALES</th>
							<th>EC CHARGE</th>
							<th>ED CHARGE</th>
							<th>EDINT CHARGE</th>
							<th>SURCHARGE</th>
							<th>DEMAND</th>


						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${acd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRCLE}</td>
								<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUB_DIVISION}</td>
								<td>${mtrblc.SECTION}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.CTNAME}</td>
								<td>${mtrblc.DEPTCODE}</td>
								<td>${mtrblc.DEPT_NAME}</td>
								<td>${mtrblc.HOD_NAME}</td>
								<td>${mtrblc.CAT}</td>
								<td>${mtrblc.CTSUBCAT}</td>
								<td>${mtrblc.HOD_TYPE}</td>
								<td>${mtrblc.STATUS}</td>
								<td class="text-right">${mtrblc.SALES}</td>
								<td class="text-right">${mtrblc.EC_CHG}</td>
								<td class="text-right">${mtrblc.ED_CHG}</td>
								<td class="text-right">${mtrblc.EDINT_CHG}</td>
								<td  class="text-right">${mtrblc.SURCHARGE}</td>
								<td  class="text-right">${mtrblc.DEMAND}</td>

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
		

			$(document).ready(
					function() {
						$.ajax({
							type : "POST",
							url : "getHODDepts",
							success : function(data) {
								var saptype = jQuery.parseJSON(data);
								$.each(saptype, function(k, v) {
									$("#hoddept").append(
											"<option value="+k+">" + v
													+ "</option>");

								});
							}

						});
					});
			$("#hoddept").append("<option value=ALL>ALL</option>");

		var currentYear = (new Date()).getFullYear();
		var currnetMonth = (new Date()).getMonth() + 1;
		$("#hyear").val(currentYear);
		$("#hmon").val($("#mon").val());

		for (var j = currentYear; j > 2015; j--) {
			$("#year").append("<option value="+j+">" + j + "</option>");

		}
		$('#mon option:eq(' + currnetMonth + ')').prop('selected', true);
		$('#year option[value="' + currentYear + '"]').prop('selected', true);

	});
</script>
<script>
	require([ 'jquery', 'datatables.net', 'datatables.net-jszip',
			'datatables.net-buttons', 'datatables.net-buttons-flash',
			'datatables.net-buttons-html5' ], function($, datatable, jszip) {
		window.JSZip = jszip;
		$('.datatable').DataTable({
			dom : 'Bfrltip',
			"scrollX" : true,
			buttons : {
				buttons : [ {
					extend : 'csv',
					className : 'btn btn-xs btn-primary',
					title : 'Services Wise ledger Closing Balance',
					footer : true
				}, {
					extend : 'excel',
					className : 'btn btn-xs btn-primary',
					title : 'Services Wise ledger Closing Balance',
					footer : true
				} ]
			}
		});
	});
</script>