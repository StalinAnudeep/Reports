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
	<c:if test="${ not empty fn:trim(sectionSalesDetails)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">${title}</h2>
				<form name="frm" style="overflow: auto;">

					<div class="bg-info text-white text-center"
						onclick="exportThisWithParameter('multiLevelTable', '${title}')"
						style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>
					<div class="text-right">
						<a href="fySalesReport" class="btn btn-primary">Back</a>
					</div>
					<table id="multiLevelTable"
						class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
						style="width: 100%;">
						<thead class="bg-primary">
							<tr class="bg-primary text-light">
								<th class="text-center text-light">SECTION</th>
								<th class="text-center text-light">CTCAT</th>
								<th class="text-center text-light">SCS</th>
								<th class="text-center text-light">CAPACITY</th>
								<th class="text-center text-light">SALES_MU</th>
								<th class="text-center text-light">DEMAND_LAKHS</th>
								<th class="text-center text-light">COLLECTION_LAKHS</th>
								<th class="text-center text-light">CB_LAKHS</th>
							</tr>
						</thead>
						<tbody>
							<%
							int flag = 0;
							String cricle = "S";
							String circletype = "S";
							%>
							<c:forEach var="sd" items="${sectionSalesDetails}"
								varStatus="tagStatus">
								<c:set var="cirl" value="${sd.SECNAME}" scope="request" />
								<tr style="font-weight: 500;">
									<%
									if (!cricle.equals((String) request.getAttribute("cirl"))) {
									%>
									<td rowspan="${CIRCOUNT[cirl]}" class="text-center">${sd.SECNAME}</td>
									<%
									}
									cricle = (String) request.getAttribute("cirl");
									%><c:if test="${ sd.CTCAT eq 'TOTAL'}">
										<td class="text-right TOTAL bg-primary format"
											style="padding-left: 5px;">${sd.CTCAT}</td>
										<td class="text-right TOTAL bg-primary format"
											style="padding-left: 5px;">${sd.SCS}</td>
										<td class="text-right TOTAL bg-primary format"
											style="padding-left: 5px;">${sd.CAPACITY}</td>
										<td class="text-right TOTAL bg-primary format"
											style="padding-left: 5px;">${sd.SALES_MU}</td>
										<td class="text-right TOTAL bg-primary format"
											style="padding-left: 5px;">${sd.DEMAND_LAKHS}</td>
										<td class="text-right TOTAL bg-primary format"
											style="padding-left: 5px;">${sd.COLLECTION_LAKHS}</td>
										<td class="text-right TOTAL bg-primary format"
											style="padding-left: 5px;">${sd.CB_LAKHS}</td>
									</c:if>
									<c:if test="${ sd.CTCAT ne 'TOTAL'}">
										<td class="text-center">${sd.CTCAT}</td>
										<td class="text-right format">${sd.SCS}</td>
										<td class="text-right format">${sd.CAPACITY}</td>
										<td class="text-right format">${sd.SALES_MU}</td>
										<td class="text-right format">${sd.DEMAND_LAKHS}</td>
										<td class="text-right format">${sd.COLLECTION_LAKHS}</td>
										<td class="text-right format">${sd.CB_LAKHS}</td>
									</c:if>
								</tr>
							</c:forEach>
						</tbody>
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