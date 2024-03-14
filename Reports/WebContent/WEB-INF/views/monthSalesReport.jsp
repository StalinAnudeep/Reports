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
	<form class="card" action="monthSalesReport" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT130 </span> - Month
					Wise,Category Wise,Sales Report </strong>
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
						<label class="form-label">Year</label> <select id="year"
							class="form-control" name="year" required="required">
							<option value="">Select Year</option>
						</select>
					</div>
				</div>


				<div class="col-md-6">
					<div class="form-group">
						<label class="form-label"> Get Month Sales Report </label>
						<button type="submit" class="btn btn-primary">Get Month
							Sales Report</button>
					</div>
				</div>
			</div>
		</div>
	</form>


	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(monthSalesDetails)}">
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
							<tr class="bg-primary text-white text-center">
								<th>CIRCLE</th>
								<th>MON_YEAR</th>
								<th>CTCAT</th>
								<th>SCS</th>
								<th>CAPACITY</th>
								<th>SALES_MU</th>
								<th>DEMAND_LAKHS</th>
								<th>COLLECTION_LAKHS</th>
								<th>CB_LAKHS</th>
							</tr>
						</thead>
						<tbody>
							<%
							int flag = 0;
							String cricle = "S";
							%>
							<c:forEach var="mtrblc" items="${monthSalesDetails}"
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
									%>
									<c:if test="${mtrblc.CTCAT eq 'TOTAL'}">
									<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.MON_YEAR}</td>
										<td class="text-left TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.CTCAT}</td>
										<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.SCS}</td>
										<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.CAPACITY}</td>
										<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.SALES_MU}</td>
										<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.DEMAND_LAKHS}</td>
										<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.COLLECTION_LAKHS}</td>
										<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.CB_LAKHS}</td>

									</c:if>

									<c:if test="${mtrblc.CTCAT ne 'TOTAL'}">
									<td class="text-right" style="padding-left: 5px;">
											${mtrblc.MON_YEAR}</td>
										<td class="text-right" style="padding-left: 5px;">
											${mtrblc.CTCAT}</td>
										<td class="text-right" style="padding-left: 5px;">
											${mtrblc.SCS}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.CAPACITY}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.SALES_MU}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.DEMAND_LAKHS}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.COLLECTION_LAKHS}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.CB_LAKHS}</td>
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