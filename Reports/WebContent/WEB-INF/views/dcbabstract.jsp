<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="dcbabstract" method="post">
		<div class="card-body">
			<h3 class="card-title">Section Wise , Category Wise DCB Split
				Report</h3>
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
				<div class="col-md-3">
					<div class="form-group">
						<label for="inputZip">Select Year</label> <select
							class="form-control" name="year" id="year">
							<option value="">--Select Year--</option>
						</select>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">GET Abstract</label>
						<button type="submit" class="btn btn-success">GET
							Abstract</button>
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
				<table
					class="table table-sm card-table table-vcenter text-nowrap datatable display">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>CIRCLE</th>
							<th>DIVISION</th>
							<th>ERO</th>
							<th>SUB-DIVISION</th>
							<th>SECTIOM</th>
							<th>CAT</th>
							<th>SCAT</th>
							<th>STATUS</th>
							<th>NOS</th>
							<th>UNITS</th>
							<th>TOT_OB</th>
							<th>DEMAND_NET</th>
							<th>DEMAND_GROSS</th>
							<th>DR_TDA</th>
							<th>DR_DRC</th>
							<th>DR_OTH</th>
							<th>ARREARS_COLL</th>
							<th>CURR_MON_DEM_COLL</th>
							<th>CR_TCA</th>
							<th>CR_DWC</th>
							<th>CR_DWC_SCS</th>
							<th>CR_OTH</th>
							<th>TOTAL_CB</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${acd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRNAME}</td>
								<td>${mtrblc.DIVNAME}</td>
								<td>${mtrblc.ERONAME}</td>
								<td>${mtrblc.SUBNAME}</td>
								<td>${mtrblc.SECNAME}</td>
								<td>${mtrblc.CAT}</td>
								<td>${mtrblc.SCAT}</td>
								<td>${mtrblc.STATUS}</td>
								<td>${mtrblc.NOS}</td>
								<td class="text-right">${mtrblc.UNITS}</td>
								<td class="text-right">${mtrblc.TOT_OB}</td>
								<td class="text-right">${mtrblc.DEMAND_NET}</td>
								<td class="text-right">${mtrblc.DEMAND_GROSS}</td>
								<td class="text-right">${mtrblc.DR_TDA}</td>
								<td class="text-right">${mtrblc.DR_DRC}</td>
								<td class="text-right">${mtrblc.DR_OTH}</td>
								<td class="text-right">${mtrblc.ARREARS_COLL}</td>
								<td class="text-right">${mtrblc.CURR_MON_DEM_COLL}</td>
								<td class="text-right">${mtrblc.CR_TCA}</td>
								<td class="text-right">${mtrblc.CR_DWC}</td>
								<td class="text-right">${mtrblc.CR_DWC_SCS}</td>
								<td class="text-right">${mtrblc.CR_OTH}</td>
								<td class="text-right">${mtrblc.TOTAL_CB}</td>
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