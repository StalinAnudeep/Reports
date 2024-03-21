<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
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
					$("#circle").append("<option value=ALL>ALL</option>");

				});

	});
</script>
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

.TOTAL {
	color: #fff !important;
	font-weight: bold !important;
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

	<form class="card" action="edCourtCasesReport" method="post" id="form">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT139</span> - ED Court
					Cases Report</strong>
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

				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Get ED Court Cases Report</label>
						<button type="submit" class="btn btn-success">Get ED
							Court Cases Report</button>
					</div>
				</div>
			</div>
		</div>
	</form>



	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(caseDetails)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">${title}</h2>
				<div class="bg-info text-white text-center"
					onclick="exportThisWithParameter('multiLevelTable', '${title}')"
					style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>
				<form name="frm" style="overflow: auto;">
					<table id="multiLevelTable"
						class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
						style="width: 100%;">
						<thead>
							<tr>
								<th class="bg-primary text-white text-center" colspan="23">${title}</th>
							</tr>
							<tr class="bg-primary text-center">
								<th class="text-center text-light">S.NO</th>
								<th class="text-center text-light">CIRCLE</th>
								<th class="text-center text-light">HT2_EDRATE(0.06)</th>
								<th class="text-center text-light">HT2_EDRATE(1)</th>
								<th class="text-center text-light">HT3_EDRATE(0.06)</th>
								<th class="text-center text-light">HT3_EDRATE(1)</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${caseDetails}"
								varStatus="tagStatus">
								<tr class="${mtrblc.CIRCLE}">
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.CIRCLE}</td>
									<td class="text-right"><a
										href="edCourtCasesForHT2MinEDRate?circle=${mtrblc.CIRCLE}">${mtrblc.HT2_MIN_EDRATE}</a></td>
									<td class="text-right"><a
										href="edCourtCasesForHT2MaxEDRate?cir=${mtrblc.CIRCLE}">${mtrblc.HT2_MAX_EDRATE}</a></td>
									<td class="text-right"><a
										href="edCourtCasesForHT3MinEDRate?cir=${mtrblc.CIRCLE}">${mtrblc.HT3_MIN_EDRATE}</a></td>
									<td class="text-right"><a
										href="edCourtCasesForHT3MaxEDRate?cir=${mtrblc.CIRCLE}">${mtrblc.HT3_MAX_EDRATE}</a></td>

								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
							<c:if test="${circle.equals('ALL') }">
								<tr class="bg-primary text-light">
									<th class="text-right text-light" colspan = "2">Grand Total</th>
									<td class="text-right format">${caseDetails.stream().map(cd -> cd.HT2_MIN_EDRATE).sum()}</td>
									<td class="text-right format">${caseDetails.stream().map(cd -> cd.HT2_MAX_EDRATE).sum()}</td>
									<td class="text-right format">${caseDetails.stream().map(cd -> cd.HT3_MIN_EDRATE).sum()}</td>
									<td class="text-right format">${caseDetails.stream().map(cd -> cd.HT3_MAX_EDRATE).sum()}</td>
								</tr>
							</c:if>
					</table>
				</form>
			</div>
		</div>
	</c:if>
</div>



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
<jsp:include page="footer.jsp"></jsp:include>

