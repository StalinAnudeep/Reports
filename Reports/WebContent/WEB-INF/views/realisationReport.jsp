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
	background-color: #cef4ff;
	font-weight: bold;
}

.VJA {
	background-color: #fff0dd;
	font-weight: bold;
}

.GNT {
	background-color: #fff0dd;
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
	<form class="card" action="realisationReport" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT142</span> -Circle
					wise, Average Rate Of Realisation Report </strong>
			</h3>
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
				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Year</label> <select id="year"
							class="form-control" name="year" required="required">
							<option value="">Select Financial Year</option>
						</select>
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">GET Average Rate Of Realisation
							Report</label>
						<button type="submit" class="btn btn-success">GET Average
							Rate Of Realisation Report</button>
					</div>
				</div>
			</div>
		</div>
	</form>

	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(list)}">
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
						<tr class="bg-primary text-white text-center">
							<th class="text-center" rowspan="2">CIRCLE</th>
							<th class="text-center" rowspan="2">CATEGORY</th>
							<th class="text-center bg-info" colspan="4">FY ${year } (CY)</th>
							<th class="text-center bg-success" colspan="4">FY ${previousYear } (PY)</th>

						</tr>
						<tr>
							<th class="text-center bg-info">NOS</th>
							<th class="text-center bg-info">SALES</th>
							<th class="text-center bg-info">REVENUE</th>
							<th class="text-center bg-info">AVG_Realisation<br> Per Unit
							</th>
							<th class="text-center bg-success">NOS</th>
							<th class="text-center bg-success">SALES</th>
							<th class="text-center bg-success">REVENUE</th>
							<th class="text-center bg-success">AVG_Realisation<br> Per Unit
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="frc" items="${list}" varStatus="tagStatus">
							<tr style="font-weight: 500;">
								<td class="text-center">${frc.circle}</td>
								<td class="text-center">${frc.cyCategory}</td>
								<td class="text-right" style="background-color: #cef4ff;">${frc.cyNos}</td>
								<td class="text-right" style="background-color: #cef4ff;">${frc.cySales}</td>
								<td class="text-right" style="background-color: #cef4ff;">${frc.cyRevenue}</td>
								<td class="text-right format" style="background-color: #cef4ff;">${frc.cyRevenue/frc.cySales}</td>
								<td class="text-right" style="background-color: #fff0dd;">${frc.pyNos}</td>
								<td class="text-right" style="background-color: #fff0dd;">${frc.pySales}</td>
								<td class="text-right" style="background-color: #fff0dd;">${frc.pyRevenue}</td>
								<td class="text-right format" style="background-color: #fff0dd;">${frc.pyRevenue/frc.pySales}</td>
							</tr>
						</c:forEach>
					</tbody>

					<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right" style="background-color: #cef4ff;">${list.stream().map(mtrblc -> mtrblc.cyNos).sum()}</th>
							<th class="text-right" style="background-color: #cef4ff;">${list.stream().map(mtrblc -> mtrblc.cySales).sum()}</th>
							<th class="text-right" style="background-color: #cef4ff;">${list.stream().map(mtrblc -> mtrblc.cyRevenue).sum()}</th>
							<th class="text-right format" style="background-color: #cef4ff;">${list.stream().map(mtrblc -> mtrblc.cyRevenue).sum()
								/ list.stream().map(mtrblc -> mtrblc.cySales).sum()}</th>
							<th class="text-right" style="background-color: #fff0dd;">${list.stream().map(mtrblc -> mtrblc.pyNos).sum()}</th>
							<th class="text-right" style="background-color: #fff0dd;">${list.stream().map(mtrblc -> mtrblc.pySales).sum()}</th>
							<th class="text-right" style="background-color: #fff0dd;">${list.stream().map(mtrblc -> mtrblc.pyRevenue).sum()}</th>
							<th class="text-right format" style="background-color: #fff0dd;">${list.stream().map(mtrblc -> mtrblc.pyRevenue).sum()
								/ list.stream().map(mtrblc -> mtrblc.pySales).sum()}</th>
						</tr>
					</tfoot>

				</table>
			</div>
		</div>
	</c:if>


	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(billingDetails)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">CIRCLE WISE BILLING UNITS REPORT
					${year}</h2>
				<div class="bg-info text-white text-center"
					onclick="exportThisWithParameter('multiLevelTable', '${title}')"
					style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>

				<table id="multiLevelTable"
					class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
					style="width: 100%;">
					<thead>
						<tr class="bg-primary text-white text-center">
							<th class="text-center">Month_Year</th>
							<th class="text-center">Circle</th>
							<th class="text-center">HT1</th>
							<th class="text-center">HT2</th>
							<th class="text-center">HT3</th>
							<th class="text-center">HT4</th>
							<th class="text-center">HT5</th>
							<th class="text-center">TOTAL</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach var="frc" items="${billingDetails}"
							varStatus="tagStatus">
							<tr class="${frc.CIRCLE}">
								<td class="text-center "><fmt:formatDate pattern="MMM-YYYY"
										value="${frc.BTBLDT}" /></td>
								<td class="text-center">${frc.CIRCLE}</td>
								<td class="text-right">${frc.HT1}</td>
								<td class="text-right">${frc.HT2}</td>
								<td class="text-right">${frc.HT3}</td>
								<td class="text-right">${frc.HT4}</td>
								<td class="text-right">${frc.HT5}</td>
								<td class="text-right">${frc.HT1 + frc.HT2 + frc.HT3 +frc.HT4 + frc.HT5}</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right">${billingDetails.stream().map(mtrblc -> mtrblc.HT1).sum()}</th>
							<th class="text-right">${billingDetails.stream().map(mtrblc -> mtrblc.HT2).sum()}</th>
							<th class="text-right">${billingDetails.stream().map(mtrblc -> mtrblc.HT3).sum()}</th>
							<th class="text-right">${billingDetails.stream().map(mtrblc -> mtrblc.HT4).sum()}</th>
							<th class="text-right">${billingDetails.stream().map(mtrblc -> mtrblc.HT5).sum()}</th>
							<th class="text-right">${billingDetails.stream().map(mtrblc -> mtrblc.HT1).sum() + billingDetails.stream().map(mtrblc -> mtrblc.HT2).sum() + billingDetails.stream().map(mtrblc -> mtrblc.HT3).sum() + billingDetails.stream().map(mtrblc -> mtrblc.HT4).sum() + billingDetails.stream().map(mtrblc -> mtrblc.HT5).sum() }</th>
						</tr>
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