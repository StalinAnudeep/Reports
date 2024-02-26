<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="fyConsumption" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT125 </span> - Financial Year Consumption Report</strong>
			</h3>
			<div class="row">
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Year</label> <select id="inputyear"
							class="form-control" name="year" required="required">
							<option value="">Select Financial Year</option>
						</select>
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">GET Consumption Report</label>
						<button type="submit" class="btn btn-success">GET
							Consumption Report</button>
					</div>
				</div>
			</div>
		</div>
	</form>

	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(fyConsumptionReport)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center text-primary">${title}</h2>
				<table class="table card-table table-vcenter text-nowrap datatable"
					style="width: 100%;">
					<thead>
						<tr>

							<th class="text-center" rowspan="2">Circle</th>
							<th class="text-center" colspan="6">HT1</th>
							<th class="text-center" colspan="6">HT2</th>
							<th class="text-center" colspan="6">HT3</th>
							<th class="text-center" colspan="6">HT4</th>
							<th class="text-center" colspan="6">HT5B</th>
							<th class="text-center" colspan="6">HTE</th>

						</tr>


						<tr>

							<th class="text-center">SCS</th>
							<th class="text-center">UNITS</th>
							<th class="text-center">DEMAND</th>
							<th class="text-center">EC</th>
							<th class="text-center">SPC_CON</th>
							<th class="text-center">SPECIFIC_REVENUE</th>

							<th class="text-center">SCS</th>
							<th class="text-center">UNITS</th>
							<th class="text-center">DEMAND</th>
							<th class="text-center">EC</th>
							<th class="text-center">SPC_CON</th>
							<th class="text-center">SPC_REV</th>

							<th class="text-center">SCS</th>
							<th class="text-center">UNITS</th>
							<th class="text-center">DEMAND</th>
							<th class="text-center">EC</th>
							<th class="text-center">SPC_CON</th>
							<th class="text-center">SPC_REV</th>

							<th class="text-center">SCS</th>
							<th class="text-center">UNITS</th>
							<th class="text-center">DEMAND</th>
							<th class="text-center">EC</th>
							<th class="text-center">SPC_CON</th>
							<th class="text-center">SPC_REV</th>

							<th class="text-center">SCS</th>
							<th class="text-center">UNITS</th>
							<th class="text-center">DEMAND</th>
							<th class="text-center">EC</th>
							<th class="text-center">SPC_CON</th>
							<th class="text-center">SPC_REV</th>

							<th class="text-center">SCS</th>
							<th class="text-center">UNITS</th>
							<th class="text-center">DEMAND</th>
							<th class="text-center">EC</th>
							<th class="text-center">SPC_CON</th>
							<th class="text-center">SPC_REV</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach var="frc" items="${fyConsumptionReport}">
							<tr>

								<td>${ frc.CIRCLE}</td>
								<td>${frc.HT1SCS}</td>
								<td>${frc.HT1UNITS}</td>
								<td>${frc.HT1DEMAND}</td>
								<td>${frc.HT1EC}</td>
								<td>${frc.HT1SPECIFIC_CONSUMPTION}</td>
								<td>${frc.HT1SPECIFIC_REVENUE}</td>
								
								<td class="text-center">${frc.HT2SCS}</td>
								<td class="text-center">${frc.HT2UNITS}</td>
								<td class="text-center">${frc.HT2DEMAND}</td>
								<td class="text-center">${frc.HT2EC}</td>
								<td class="text-center">${frc.HT2SPECIFIC_CONSUMPTION}</td>
								<td class="text-center">${frc.HT2SPECIFIC_REVENUE}</td>
								
								<td class="text-center">${frc.HT3SCS}</td>
								<td class="text-center">${frc.HT3UNITS}</td>
								<td class="text-center">${frc.HT3DEMAND}</td>
								<td class="text-center">${frc.HT3EC}</td>
								<td class="text-center">${frc.HT3SPECIFIC_CONSUMPTION}</td>
								<td class="text-center">${frc.HT3SPECIFIC_REVENUE}</td>
								
								<td class="text-center">${frc.HT4SCS}</td>
								<td class="text-center">${frc.HT4UNITS}</td>
								<td class="text-center">${frc.HT4DEMAND}</td>
								<td class="text-center">${frc.HT4EC}</td>
								<td class="text-center">${frc.HT4SPECIFIC_CONSUMPTION}</td>
								<td class="text-center">${frc.HT4SPECIFIC_REVENUE}</td>
								
								<td class="text-center">${frc.HT5BSCS}</td>
								<td class="text-center">${frc.HT5BUNITS}</td>
								<td class="text-center">${frc.HT5BDEMAND}</td>
								<td class="text-center">${frc.HT5BEC}</td>
								<td class="text-center">${frc.HT5BSPECIFIC_CONSUMPTION}</td>
								<td class="text-center">${frc.HT5BSPECIFIC_REVENUE}</td>
								
								<td class="text-center">${frc.HT5ESCS}</td>
								<td class="text-center">${frc.HT5EUNITS}</td>
								<td class="text-center">${frc.HT5EDEMAND}</td>
								<td class="text-center">${frc.HT5EEC}</td>
								<td class="text-center">${frc.HT5ESPECIFIC_CONSUMPTION}</td>
								<td class="text-center">${frc.HT5ESPECIFIC_REVENUE}</td>

							</tr>
						</c:forEach>
						
						</tbody>
						
						
						<c:if test="${type eq 'ALL'}">
						<tfoot>
						
						<tr>
							<td class="text-center">Grand Total</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT1SCS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT1UNITS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT1DEMAND).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT1EC).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT1SPECIFIC_CONSUMPTION).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT1SPECIFIC_REVENUE).sum()}</td>
							
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT2SCS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT2UNITS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT2DEMAND).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT2EC).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT2SPECIFIC_CONSUMPTION).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT2SPECIFIC_REVENUE).sum()}</td>
							
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT3SCS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT3UNITS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT3DEMAND).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT3EC).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT3SPECIFIC_CONSUMPTION).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT3SPECIFIC_REVENUE).sum()}</td>
							
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT4SCS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT4UNITS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT4DEMAND).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT4EC).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT4SPECIFIC_CONSUMPTION).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT4SPECIFIC_REVENUE).sum()}</td>
							
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5BSCS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5BUNITS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5BDEMAND).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5BEC).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5BSPECIFIC_CONSUMPTION).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5BSPECIFIC_REVENUE).sum()}</td>
							
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5ESCS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5EUNITS).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5EDEMAND).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5EEC).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5ESPECIFIC_CONSUMPTION).sum()}</td>
							<td class="text-center">${fyConsumptionReport.stream().map(frc -> frc.HT5ESPECIFIC_REVENUE).sum()}</td>
							
						</tr>
						</c:if>
					</tfoot>
					
				</table>
			</div>
		</div>
	</c:if>

	
</div>
<script>
	requirejs([ 'jquery' ], function($) {
		$("#circle").append("<option value=ALL>ALL</option>");
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
					var currentYear = (new Date()).getFullYear();
					for (var j = currentYear; j > 2015; j--) {
						var jj = j - 1 + "-" + j;
						$("#inputyear").append(
								"<option value="+jj+">" + jj + "</option>");
					}
					$('#mon option:eq(' + currnetMonth + ')').prop('selected',
							true);
					$('#year option[value="' + currentYear + '"]').prop(
							'selected', true);
				});

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
					title : '${title}',
					footer : true
				}, {
					extend : 'excel',
					className : 'btn btn-xs btn-primary',
					title : '${title}',
					footer : true
				} ]
			}
		});
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>