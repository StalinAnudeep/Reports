<%@page import="javafx.scene.shape.Circle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>

<style>
.firstInput {
	width: 50%;
	display: inline-block;
	float: left;
	border-bottom-right-radius: 0;
	border-top-right-radius: 0;
}

.secondInput {
	width: 50%;
	display: inline-block;
	float: left;
	border-bottom-left-radius: 0;
	border-top-left-radius: 0;
	border-left: 0px;
}

.null {
	font-weight: bold;
}

.CRD {
	background-color: #cef4ff;
	font-weight: bold;
}

.ONG {
	background-color: #fff0dd;
	font-weight: bold;
}

.VJA {
	background-color: #fff0dd;
	font-weight: bold;
}

.GNT {
	background-color: #cef4ff;
	font-weight: bold;
}

.APCPDCL {
	background-color: #fff0dd;
	font-weight: bold;
}

.NOSTAT {
	color: #fff !important;
	font-weight: bold !important;
}

.NULLCIR {
	font-weight: bold !important;
	background-color: #4ff1f1;
}

.TOTAL {
	color: #fff !important;
	font-weight: bold !important;
}

thead>tr>th {
	color: #fff !important;
	font-weight: bold !important;
}
</style>
<div class="row row-cards row-deck">
	<form class="card" action="fyConsumption" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT125</span>
					-FYConsumption </strong>
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
						<label class="form-label">Year</label> <select id="year"
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
				<h2 class="text-center">${title}</h2>
				<form name="frm" style="overflow: auto;">

					<c:if test="${circle eq 'ALL'}">
						<div class="bg-info text-white text-center"
							onclick="exportThisWithParameter('multiLevelTable', '${title}')"
							style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>

						<table id="multiLevelTable"
							class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
							style="width: 100%;">
							<thead>
								<tr class="bg-primary text-white text-center">
									<th class="text-center" rowspan="2">Month_Year</th>
									<th class="text-center" rowspan="2">Circle</th>
									<th class="text-center" colspan="6">HT1</th>
									<th class="text-center" colspan="6">HT2</th>
									<th class="text-center" colspan="6">HT3</th>
									<th class="text-center" colspan="6">HT4</th>
									<th class="text-center" colspan="6">HT5B</th>
									<th class="text-center" colspan="6">HT5E</th>

								</tr>


								<tr class="bg-primary text-white text-center">

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
								<c:if test="${frc.CIRCLE eq 'TOTAL'}">
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.CIRCLE}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT1SCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT1UNITS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT1DEMAND}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT1EC}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT1SPECIFIC_CONSUMPTION}</td>
									<td class="text-center TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT1EC/frc.HT1UNITS}</td>

									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT2SCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT2UNITS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT2DEMAND}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT2EC}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT2SPECIFIC_CONSUMPTION}</td>
									<td class="text-center TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT2EC/frc.HT2UNITS}</td>

									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT3SCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT3UNITS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT3DEMAND}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT3EC}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT3SPECIFIC_CONSUMPTION}</td>
									<td class="text-center TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT3EC/frc.HT3UNITS}</td>

									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT4SCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT4UNITS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT4DEMAND}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT4EC}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT4SPECIFIC_CONSUMPTION}</td>
									<td class="text-center TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT4EC/frc.HT4UNITS}</td>

									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5BSCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5BUNITS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5BDEMAND}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5BEC}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5BSPECIFIC_CONSUMPTION}</td>
									<td class="text-center TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5BEC/frc.HT5BUNITS}</td>

									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5ESCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5EUNITS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5EDEMAND}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5EEC}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5ESPECIFIC_CONSUMPTION}</td>
									<td class="text-center TOTAL bg-primary"
										style="padding-left: 5px;">${frc.HT5EEC/frc.HT5EUNITS}</td>
								</c:if>

								<c:if test="${frc.CIRCLE ne 'TOTAL'}">
									<td class="text-center ">${frc.CIRCLE}</td>
									<td class="text-center">${frc.HT1SCS}</td>
									<td class="text-center">${frc.HT1UNITS}</td>
									<td class="text-center">${frc.HT1DEMAND}</td>
									<td class="text-center">${frc.HT1EC}</td>
									<td class="text-center">${frc.HT1SPECIFIC_CONSUMPTION}</td>
									<td class="text-center">${frc.HT1SPECIFIC_REVENUE}</td>

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
								</c:if>


							</tbody>

						</table>
					</c:if>

					<c:if test="${circle ne 'ALL'}">
						<table id="multiLevelTable"
							class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
							style="width: 100%;">
							<thead>

								<tr class="bg-primary text-white text-center">
									<th class="text-center" rowspan="2">Month_Year</th>
									<th class="text-center" rowspan="2">Circle</th>
									<th class="text-center" colspan="6">HT1</th>
									<th class="text-center" colspan="6">HT2</th>
									<th class="text-center" colspan="6">HT3</th>
									<th class="text-center" colspan="6">HT4</th>
									<th class="text-center" colspan="6">HT5B</th>
									<th class="text-center" colspan="6">HT5E</th>

								</tr>


								<tr class="bg-primary text-white text-center">

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

								<c:forEach var="frc" items="${fyConsumptionReport}"
									varStatus="tagStatus">
									<c:if test="${frc.CIRCLE ne 'TOTAL'}">
										<tr style="font-weight: 500;">
											<td class="text-center">${frc.MON_YEAR}</td>
											<td class="text-center">${frc.CIRCLE}</td>
											<td class="text-center">${frc.HT1SCS}</td>
											<td class="text-center format">${frc.HT1UNITS}</td>
											<td class="text-center format">${frc.HT1DEMAND}</td>
											<td class="text-center format">${frc.HT1EC}</td>
											<td class="text-center format">${frc.HT1SPECIFIC_CONSUMPTION}</td>
											<td class="text-center format">${frc.HT1SPECIFIC_REVENUE}</td>

											<td class="text-center">${frc.HT2SCS}</td>
											<td class="text-center format">${frc.HT2UNITS}</td>
											<td class="text-center format">${frc.HT2DEMAND}</td>
											<td class="text-center format">${frc.HT2EC}</td>
											<td class="text-center format">${frc.HT2SPECIFIC_CONSUMPTION}</td>
											<td class="text-center format">${frc.HT2SPECIFIC_REVENUE}</td>

											<td class="text-center">${frc.HT3SCS}</td>
											<td class="text-center format">${frc.HT3UNITS}</td>
											<td class="text-center format">${frc.HT3DEMAND}</td>
											<td class="text-center format">${frc.HT3EC}</td>
											<td class="text-center  format">${frc.HT3SPECIFIC_CONSUMPTION}</td>
											<td class="text-center  format">${frc.HT3SPECIFIC_REVENUE}</td>

											<td class="text-center">${frc.HT4SCS}</td>
											<td class="text-center  format">${frc.HT4UNITS}</td>
											<td class="text-center  format">${frc.HT4DEMAND}</td>
											<td class="text-center  format">${frc.HT4EC}</td>
											<td class="text-center  format">${frc.HT4SPECIFIC_CONSUMPTION}</td>
											<td class="text-center  format">${frc.HT4SPECIFIC_REVENUE}</td>

											<td class="text-center">${frc.HT5BSCS}</td>
											<td class="text-center  format">${frc.HT5BUNITS}</td>
											<td class="text-center  format">${frc.HT5BDEMAND}</td>
											<td class="text-center  format">${frc.HT5BEC}</td>
											<td class="text-center  format">${frc.HT5BSPECIFIC_CONSUMPTION}</td>
											<td class="text-center  format">${frc.HT5BSPECIFIC_REVENUE}</td>

											<td class="text-center">${frc.HT5ESCS}</td>
											<td class="text-center  format">${frc.HT5EUNITS}</td>
											<td class="text-center  format">${frc.HT5EDEMAND}</td>
											<td class="text-center  format">${frc.HT5EEC}</td>
											<td class="text-center  format">${frc.HT5ESPECIFIC_CONSUMPTION}</td>
											<td class="text-center  format">${frc.HT5ESPECIFIC_REVENUE}</td>
										</tr>
									</c:if>
								</c:forEach>
							</tbody>

						</table>


					</c:if>
				</form>
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
						$("#year").append(
								"<option value="+jj+">" + jj + "</option>");
					}
					$('#year option[value="' + currentYear + '"]').prop(
							'selected', true);
				});

	});
</script>
<script type="text/javascript">
	var exportThisWithParameter = (function() {
		var uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"  xmlns="http://www.w3.org/TR/REC-html40"><head> <!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets> <x:ExcelWorksheet><x:Name>{worksheet}</x:Name> <x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions> </x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook> </xml><![endif]--></head><body> <table>{table}</table></body></html>', base64 = function(
				s) {
			return window.btoa(unescape(encodeURIComponent(s)))
		}, format = function(s, c) {
			return s.replace(/{(\w+)}/g, function(m, p) {
				return c[p];
			})
		}
		return function(tableID, excelName) {

			tableID = document.getElementById(tableID)
			var ctx = {
				worksheet : excelName || 'Worksheet',
				table : tableID.innerHTML
			}
			var link = document.createElement("a");
			link.download = "${title}.xls";
			link.href = uri + base64(format(template, ctx));
			link.click();

		}
	})()
</script>
<script>
	function getNext(circle, agewise) {
		alert(circle)
		document.frm.action = "AgeWiseServiceBalance?circle=" + circle
				+ "&agewise=" + agewise;
		document.frm.method = "post";
		document.frm.submit();
		window.open(url, '_blank').focus();
	}
</script>

<script>
	requirejs([ 'jquery' ], function($) {
		$(".format").each(
				function() {
					if ($.isNumeric($(this).text())) {
						// It isn't a number	
						$(this).html(
								parseFloat($(this).text()).toLocaleString(
										'en-IN', {
											style : 'decimal',
											currency : 'INR'
										}));
					}
				}

		)

	});
</script>

<jsp:include page="footer.jsp"></jsp:include>