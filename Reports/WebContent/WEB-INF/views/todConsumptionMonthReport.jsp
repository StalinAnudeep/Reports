<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<style>
.11 {
	background-color: #fff0dd;
	font-weight: bold;
}

.33 {
	background-color: #fff0dd;
	font-weight: bold;
}

.132 {
	background-color: #cef4ff;
	font-weight: bold;
}
</style>
<div class="row row-cards row-deck">
	<form class="card" action="todConsumptionMonthReport" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT128 </span> -Voltage
					Wise,Category Wise,TOD Consumption Month Report </strong>
			</h3>
			<div class="row">
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Year</label> <select id="year"
							class="form-control" name="year" required="required">
							<option value="">Select Financial Year</option>
						</select>
					</div>
				</div>


				<div class="col-md-6">
					<div class="form-group">
						<label class="form-label"> Get TOD Month Consumption
							Report </label>
						<button type="submit" class="btn btn-primary">Get TOD
							Month Consumption Report</button>
					</div>
				</div>
			</div>
		</div>
	</form>


	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>



	<c:if test="${ not empty fn:trim(todDetails)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">${title}</h2>
				<form name="frm" style="overflow: auto;">

					<div class="bg-info text-white text-center"
						onclick="exportThisWithParameter('multiLevelTable', '${title}')"
						style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>
					<table id="multiLevelTable"
						class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
						style="width: 100%;">
						<thead class="bg-primary">

							<tr style="font-weight: 500;">
								<th class="text-center text-light" rowspan="2">MONTH_YEAR</th>
								<th class="text-center text-light" rowspan="2">CAT</th>
								<th class="text-center text-light" colspan="7">11</th>
								<th class="text-center text-light" colspan="7">33</th>
								<th class="text-center text-light" colspan="7">132</th>
							</tr>
							<tr class="bg-primary text-light">
								<c:forEach var="i" begin="1" end="3">
									<th class="text-center text-light">SUBCAT</th>
									<th class="text-center text-light">SCS</th>
									<th class="text-center text-light">LOAD</th>
									<th class="text-center text-light">PEAK</th>
									<th class="text-center text-light">OFFPEAK</th>
									<th class="text-center text-light">NORMAL</th>
									<th class="text-center text-light">COLONY</th>
								</c:forEach>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="td" items="${list}">
								<tr style="font-weight: 600;">
									<td class="text-center">${td.MONTH_YEAR_11}</td>
									<td class="text-center">${td.CAT_11}</td>
									<td class="text-center">${td.SUBCAT_11}</td>
									<td class="text-center">${td.SCS_11}</td>
									<td class="text-center">${td.LOAD_11}</td>
									<td class="text-center">${td.PEAK_11}</td>
									<td class="text-center">${td.OFFPEAK_11}</td>
									<td class="text-center">${td.NORMAL_11}</td>
									<td class="text-center">${td.COLONY_11}</td>

									<td class="text-center">${td.SUBCAT_33}</td>
									<td class="text-center">${td.SCS_33}</td>
									<td class="text-center">${td.LOAD_33}</td>
									<td class="text-center">${td.PEAK_33}</td>
									<td class="text-center">${td.OFFPEAK_33}</td>
									<td class="text-center">${td.NORMAL_33}</td>
									<td class="text-center">${td.COLONY_33}</td>

									<td class="text-center">${td.SUBCAT_132}</td>
									<td class="text-center">${td.SCS_132}</td>
									<td class="text-center">${td.LOAD_132}</td>
									<td class="text-center">${td.PEAK_132}</td>
									<td class="text-center">${td.OFFPEAK_132}</td>
									<td class="text-center">${td.NORMAL_132}</td>
									<td class="text-center">${td.COLONY_132}</td>
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
		$(document).ready(
				function() {
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