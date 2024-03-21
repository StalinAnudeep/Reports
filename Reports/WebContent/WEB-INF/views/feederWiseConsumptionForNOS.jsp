<%@page import="javafx.scene.shape.Circle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<style>
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
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>


	<c:if test="${ not empty fn:trim(NOSfeederDetails)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">${title}</h2>
				<div class="row">
					<div class="col-md-10">
						<div class="bg-info text-white text-center"
							onclick="exportThisWithParameter('multiLevelTable', '${title}')"
							style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>
					</div>
					<%-- <div class="col-md-2">
						<div class="text-right">
							<form class="card" action="feederwiseFYConsumption" method="post"
								id="form">
								<input type="text" value='${circle}' name="circle" id="circle">
								<input type="text" value='${division}' name="division" id="division">
								<input type="text" value='${subdivision}' name="subdivision" id="subdivision">
								<input type="text" value='${feeder}' id="feeder" name="feeder">
								<input type="text" value='${year}' id="year" name="year">
								<button type="submit" class="btn btn-link">
									<i class="fa fa-arrow-left" aria-hidden="true"></i> Back
								</button>

							</form>
						</div>
					</div> --%>
				</div>
				<table id="multiLevelTable"
					class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
					style="width: 100%;">
					<thead>
						<tr>
							<th class="bg-primary text-white text-center" colspan="17">${title}</th>
						</tr>
						<tr class="bg-primary text-white text-center">
							<th class="text-center align-middle" rowspan="2">SNO</th>
							<th rowspan="2" style="vertical-align: middle;">USCNO</th>
							<th rowspan="2" style="vertical-align: middle;">SALES</th>
							<th rowspan="2" style="vertical-align: middle;">OB</th>
							<th colspan="3" class="text-center">DEMAND</th>

							<th colspan="4" class="text-center">COLLECTION</th>
							<!-- <th  rowspan="2">DRJ</th> -->

							<th rowspan="2" class="text-center"
								style="vertical-align: middle;">CB</th>
						</tr>
						<tr class="bg-primary text-white text-center">
							<th>DEMAND</th>
							<th>DRJ</th>
							<th>TOTAL</th>
							<th>COLL ARREAR</th>
							<th>COLL DEMAND</th>
							<th>CRJ</th>
							<th>TOTAL</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${NOSfeederDetails}"
							varStatus="tagStatus">
							<tr style="font-weight: 500;">
								<td class="text-right">${tagStatus.index + 1}</td>
								<td class="text-right">${mtrblc.CTUSCNO}</td>
								<td class="text-right">${mtrblc.SALES}</td>
								<td class="text-right">${mtrblc.OB}</td>
								<td class="text-right">${mtrblc.DEMAND}</td>
								<td class="text-right">${mtrblc.DRJ}</td>
								<td class="text-right">${mtrblc.DEMAND+mtrblc.DRJ}</td>
								<td class="text-right">${mtrblc.COLL_ARREAR}</td>
								<td class="text-right">${mtrblc.COLL_DEMAND}</td>
								<td class="text-right">${mtrblc.CRJ}</td>
								<td class="text-right">${mtrblc.COLLECTION + mtrblc.CRJ}</td>
								<td class="text-right">${mtrblc.CB}</td>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>

							<th colspan="1" class="text-right">Grand Total</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.SALES).sum()}</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.OB).sum()}</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.DEMAND).sum()  + NOSfeederDetails.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.COLL_ARREAR).sum()}</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.COLL_DEMAND).sum()}</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.COLLECTION).sum() + NOSfeederDetails.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
							<th class="text-right">${NOSfeederDetails.stream().map(mtrblc -> mtrblc.CB).sum()}</th>
						</tr>
					</tfoot>
				</table>
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