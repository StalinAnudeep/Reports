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

.bg-light {
	background-color: #d8e4f154 !important;
}

.CellWithComment {
	position: relative;
}

.CellComment {
	display: none;
	position: absolute;
	z-index: 100;
	border: 1px;
	background-color: white;
	border-style: solid;
	border-width: 1px;
	border-color: #e81a40;
	padding: 3px;
	color: #e81a40;
	top: 20px;
	left: 20px;
}

.CellWithComment:hover span.CellComment {
	display: block;
}

.CRD {
	background-color: #cef4ff;
	font-weight: bold;
}

.ONG {
	background-color: #fff0dd;
	font-weight: bold;
}
.TOTAL {
	color: #fff !important;
	font-weight: bold !important;
}
.VJA {
	background-color: #fff0dd;
	font-weight: bold;
}

.GNT {
	background-color: #cef4ff;
	font-weight: bold;
}
</style>
<div class="row row-cards row-deck">
	<form class="card" action="arrearsStatusReport" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT136</span> - Break up
					Arrears Status Wise And Govt/Pvt Report</strong>
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
				<div class="col">
					<label for="inputState">CB Amount</label>
					<div>
						<select id='dropdown' class="form-control firstInput"
							name="dropdown">
							<option value="">Select CB Amount</option>
							<option value="ALL">All Amounts</option>
							<option value="CA">Greater Than 50000</option>
						</select> <input type="number" class="form-control  secondInput"
							id="cbamount" name="cbamount" disabled="true" />
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
						<label class="form-label">GET Break up Arrears Report </label>
						<button type="submit" class="btn btn-success">GET Break
							up Arrears Report</button>
					</div>
				</div>
			</div>
		</div>

	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>


	<c:if test="${ not empty fn:trim(arrearsDetails)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">${title}</h2>
				<div class="bg-info text-white text-center"
					onclick="exportThisWithParameter('multiLevelTable', '${title}')"
					style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>
				<table id="multiLevelTable"
					class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
					style="width: 100%;">
					<thead>
						<tr>
							<th class="bg-primary text-white text-center" colspan="23">${title}</th>
						</tr>
						<tr class="bg-primary text-center">
							
							<th rowspan="4" style="vertical-align: middle;"
								class="text-white">CIRCLE</th>
							<th rowspan="4" style="vertical-align: middle;"
								class="text-white">DIVISION</th>
							<th rowspan="4" style="vertical-align: middle;"
								class="text-white">SUBDIVISION</th>
							<th rowspan="4" style="vertical-align: middle;"
								class="text-white">SECTION</th>
							<th colspan="14" style="vertical-align: middle;"
								class=" text-white">Total Arrears Upto ${monthYear}</th>

						</tr>
						<tr class="bg-primary text-center">
							<th rowspan="3" style="vertical-align: middle;"
								class="text-white">SCS</th>
							<th rowspan="3" style="vertical-align: middle;"
								class="text-white">AMT</th>
							<th colspan="6" style="vertical-align: middle;"
								class="text-white">Status Wise</th>
							<th colspan="6" style="vertical-align: middle;"
								class="text-white">Type</th>
						</tr>

						<tr class="bg-primary text-center">
							<th colspan="2" style="vertical-align: middle;"
								class="text-white">Live</th>
							<th colspan="2" style="vertical-align: middle;"
								class="text-white">UDC</th>
							<th colspan="2" style="vertical-align: middle;"
								class="text-white">BillStop</th>
							<th colspan="2" style="vertical-align: middle;"
								class="text-white">Govt</th>
							<th colspan="2" style="vertical-align: middle;"
								class="text-white">PVT</th>
						</tr>
						<tr class="bg-primary text-center">
							<th style="vertical-align: middle;" class="text-white">SCS</th>
							<th style="vertical-align: middle;" class="text-white">AMT</th>
							<th style="vertical-align: middle;" class="text-white">SCS</th>
							<th style="vertical-align: middle;" class="text-white">AMT</th>
							<th style="vertical-align: middle;" class="text-white">SCS</th>
							<th style="vertical-align: middle;" class="text-white">AMT</th>
							<th style="vertical-align: middle;" class="text-white">SCS</th>
							<th style="vertical-align: middle;" class="text-white">AMT</th>
							<th style="vertical-align: middle;" class="text-white">SCS</th>
							<th style="vertical-align: middle;" class="text-white">AMT</th>
						</tr>

					</thead>
					<tbody>
						<%
						int flag = 0;
						String cricle = "S";
						%>
						<c:forEach var="mtrblc" items="${arrearsDetails}"
							varStatus="tagStatus">
							<c:set var="cirl" value="${mtrblc.CIRCLE}" scope="request" />
							<tr class="${mtrblc.CIRCLE}">
								<%
								if (!cricle.equals((String) request.getAttribute("cirl"))) {
								%>
								<td rowspan="${CIRCOUNT[cirl]}">${mtrblc.CIRCLE}</td>
								<%
								}
								cricle = (String) request.getAttribute("cirl");
								%><c:if test="${mtrblc.SECTION eq 'TOTAL'}">
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.DIVISION}</td>
									<td class="text-left TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.SUBDIVISION}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.SECTION}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.TOT_SCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.TOT_ARREARS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.LIVE_SCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.LIVE_ARREARS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.UDC_SCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.UDC_ARREARS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.BS_SCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.BS_ARREARS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.GOVT_SCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.GOVT_ARREARS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.PVT_SCS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${mtrblc.PVT_ARREARS}</td>

								</c:if>

								<c:if test="${mtrblc.SECTION ne 'TOTAL'}">
									<td>${mtrblc.DIVISION}</td>
									<td>${mtrblc.SUBDIVISION}</td>
									<td>${mtrblc.SECTION}</td>
									<td>${mtrblc.TOT_SCS}</td>
									<td>${mtrblc.TOT_ARREARS}</td>
									<td>${mtrblc.LIVE_SCS}</td>
									<td>${mtrblc.LIVE_ARREARS}</td>
									<td>${mtrblc.UDC_SCS}</td>
									<td>${mtrblc.UDC_ARREARS}</td>
									<td>${mtrblc.BS_SCS}</td>
									<td>${mtrblc.BS_ARREARS}</td>
									<td>${mtrblc.GOVT_SCS}</td>
									<td>${mtrblc.GOVT_ARREARS}</td>
									<td>${mtrblc.PVT_SCS}</td>
									<td>${mtrblc.PVT_ARREARS}</td>
								</c:if>
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
		$('#dropdown').change(function() {
			if ($(this).val() == 'ALL') {
				$('#cbamount').prop("disabled", false);
			} else {
				$('#cbamount').prop("disabled", true);
			}
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
