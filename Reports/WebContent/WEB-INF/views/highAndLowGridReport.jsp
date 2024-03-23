<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<style>
.null{
font-weight: bold;
}
.LOW_GRID{
background-color: #cef4ff;
font-weight: bold;
}
.HIGH_GRID{
background-color: #fff0dd;
font-weight: bold;
}


</style>
<div class="row row-cards row-deck">
	<form class="card" action="highAndLowGridReport" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT140</span> - Single
					Service High Grid _Low Grid Month wise consumption Report</strong>
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
						<label class="form-label">Service Type</label> <select
							class="form-control" id="servicetype" name="servicetype"
							required="required">
							<option value="">Select Service Type</option>
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
						<label class="form-label">GET Single service High&Low Grid
							Report </label>
						<button type="submit" class="btn btn-primary">GET Single
							service High&Low Grid Report</button>
					</div>
				</div>

			</div>
		</div>

	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>


	<c:if test="${ not empty fn:trim(serviceDetails)}">
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
							<th class="bg-primary text-white text-center" colspan="19">${title}</th>
						</tr>
						<tr>
							<th>CIRCLE</th>
							<th>DIVISION</th>
							<th>SUBDIVISION</th>
							<th>SECTION</th>
							<th>USCNO</th>
							<th>NAME</th>
							<th>CAT</th>
							<th>SUBCAT</th>
							<th>LOAD</th>
							<th>PURPOSE_OF_SUPPLY</th>
							<th>HGD_LGD_NOR</th>
							<th>MON_YEAR</th>
							<th>PEAK</th>
							<th>OFFPEAK</th>
							<th>NORMAL</th>
							<th>REC_KVAH</th>
							<th>BILLED_KVAH</th>
							<th>COL_UNITS</th>
							<th>SOLAR_UNITS</th>
							<th>OA_UNITS</th>
							<th>OA_PEAK</th>
							<th>OA_OFFPEAK</th>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${serviceDetails}"
							varStatus="tagStatus">
							<tr style="font-weight: 500;" class = "${mtrblc.HGD_LGD_NOR}">
								<td class="text-left">${mtrblc.CIRCLE}</td>
								<td class="text-left">${mtrblc.DIVNAME}</td>
								<td class="text-left">${mtrblc.SUBNAME}</td>
								<td class="text-left">${mtrblc.SECNAME}</td>
								<td class="text-left">${mtrblc.USCNO}</td>
								<td class="text-left">${mtrblc.NAME}</td>
								<td class="text-left">${mtrblc.CAT}</td>
								<td class="text-center">${mtrblc.SUBCAT}</td>
								<td class="text-right">${mtrblc.LOAD}</td>
								<td class="text-left">${mtrblc.PURPOSE_OF_SUPPLY}</td>
								<td class="text-left">${mtrblc.HGD_LGD_NOR}</td>
								<td class="text-left">${mtrblc.MON_YEAR}</td>
								<td class="text-right">${mtrblc.PEAK}</td>
								<td class="text-right">${mtrblc.OFFPEAK}</td>
								<td class="text-right">${mtrblc.NOR}</td>
								<td class="text-right">${mtrblc.REC_KVAH}</td>
								<td class="text-right">${mtrblc.BILLED_KVAH}</td>
								<td class="text-right">${mtrblc.COL_UNITS}</td>
								<td class="text-right">${mtrblc.SOLAR_UNITS}</td>
								<td class="text-right">${mtrblc.OA_UNITS}</td>
								<td class="text-right">${mtrblc.OA_PEAK}</td>
								<td class="text-right">${mtrblc.OA_OFFPEAK}</td>
							</tr>


						</c:forEach>
					</tbody>
					<%-- <tfoot>
						<tr>

							<th colspan="5" class="text-right">Grand Total</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.NOS).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.LOAD).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.REC_MD).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.OB).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.SALES).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.DEMAND).sum()  + acd.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLL_ARREAR).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLL_DEMAND).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
									<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLLECTION).sum() + acd.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.CB).sum()}</th>
						</tr>
					</tfoot> --%>
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
				});

		$(document).ready(
				function() {
					$.ajax({
						type : "POST",
						url : "getServiceTypes",
						success : function(data) {
							var saptype = jQuery.parseJSON(data);
							$.each(saptype, function(k, v) {
								$("#servicetype").append(
										"<option value="+k+">" + v
												+ "</option>");

							});
						}

					});
				});

		var currentYear = (new Date()).getFullYear();
		for (var j = currentYear; j > 2015; j--) {
			var jj = j - 1 + "-" + j;
			$("#year").append("<option value="+jj+">" + jj + "</option>");
		}
		$('#year option[value="' + currentYear + '"]').prop('selected', true);

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
		$("td,th").each(
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