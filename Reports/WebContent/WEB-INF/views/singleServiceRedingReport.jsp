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

.LOW_GRID {
	background-color: #cef4ff;
	font-weight: bold;
}

.HIGH_GRID {
	background-color: #fff0dd;
	font-weight: bold;
}
</style>
<div class="row row-cards row-deck">
	<form class="card" action="singleServiceRedingReport" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT141</span> - Single
					Service Month wise Reading Details Report</strong>
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
				<div class="col-md-2">
					<div class="form-group">
						<label for="inputZip">Select Year</label> <select
							class="form-control" name="year" id="year">
							<option value="">--Select Year--</option>
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


	<c:if test="${ not empty fn:trim(readingDetails)}">
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
							<th class="bg-primary text-white text-center" colspan="53">${title}</th>
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
							<th>TYPE</th>
							<th>LOAD</th>
							<th>SERVICE_TYPE</th>
							<th>VOLTAGE</th>
							<th>STATUS</th>
							<th>OP_KWH</th>
							<th>CL_KWH</th>
							<th>RKWH</th>
							<th>MF</th>
							<th>KWH_UNITS</th>
							<th>OP_KVAH</th>
							<th>CL_KVAH</th>
							<th>RKVAH</th>
							<th>RKVAH_UNITS</th>
							<th>TOD1_OP</th>
							<th>TOD1_CL</th>
							<th>TOD1_UNITS</th>
							<th>TOD2_OP</th>
							<th>TOD2_CL</th>
							<th>TOD2_UNITS</th>
							<th>TOD3_OP</th>
							<th>TOD3_CL</th>
							<th>TOD3_UNITS</th>
							<th>TOD4_OP</th>
							<th>TOD4_CL</th>
							<th>TOD4_UNITS</th>
							<th>TOD5_OP</th>
							<th>TOD5_CL</th>
							<th>TOD5_UNITS</th>
							<th>TOD6_OP</th>
							<th>TOD6_CL</th>
							<th>TOD6_UNITS</th>
							<th>TOD TOTAL_UNITS</th>
							<th>TOD_PEAK</th>
							<th>TOD_OFFPEAK</th>
							<th>COLOPNRDG</th>
							<th>COLCLRDG</th>
							<th>COLMTRMF</th>
							<th>COLMETKVAH</th>
							<th>SKWH_OPN</th>
							<th>SKWH_CLS</th>
							<th>SRECKWH</th>
							<th>OA_KVAH_ADJ_ENG</th>
							<th>OA_PEAK</th>
							<th>OA_OFFPEAK</th>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${readingDetails}"
							varStatus="tagStatus">
							<tr style="font-weight: 500;" class="${mtrblc.HGD_LGD_NOR}">
								<td class="text-left">${mtrblc.CIRCLE}</td>
								<td class="text-left">${mtrblc.DIVNAME}</td>
								<td class="text-left">${mtrblc.SUBNAME}</td>
								<td class="text-left">${mtrblc.SECNAME}</td>
								<td class="text-left">${mtrblc.USCNO}</td>
								<td class="text-left">${mtrblc.NAME}</td>
								<td class="text-right">${mtrblc.CAT}</td>
								<td class="text-right">${mtrblc.SCAT}</td>
								<td class="text-right">${mtrblc.TYPE}</td>
								<td class="text-right">${mtrblc.LOAD}</td>
								<td class="text-right">${mtrblc.SERVICETYPE}</td>
								<td class="text-right">${mtrblc.VOLTAGE}</td>
								<td class="text-right">${mtrblc.STATUS}</td>
								<td class="text-right">${mtrblc.MDOPNKWH_HT}</td>
								<td class="text-right">${mtrblc.MDCLKWH_HT}</td>
								<td class="text-right">${mtrblc.MDRKWH_HT}</td>
								<td class="text-right">${mtrblc.MDMF_HT}</td>
								<td class="text-right">${mtrblc.MDRECKWH_HT}</td>
								<td class="text-right">${mtrblc.MDOPNKVAH_HT}</td>
								<td class="text-right">${mtrblc.MDCLKVAH_HT}</td>
								<td class="text-right">${mtrblc.MDRKVAH_HT}</td>
								<td class="text-right">${mtrblc.MDRECKVAH_HT}</td>
								<td class="text-right">${mtrblc.MDTOD1_OPN}</td>
								<td class="text-right">${mtrblc.MDTOD1_CLS}</td>
								<td class="text-right">${mtrblc.MDTOD1_RECKVAH}</td>
								<td class="text-right">${mtrblc.MDTOD2_OPN}</td>
								<td class="text-right">${mtrblc.MDTOD2_CLS}</td>
								<td class="text-right">${mtrblc.MDTOD2_RECKVAH}</td>
								<td class="text-right">${mtrblc.MDTOD3_OPN}</td>
								<td class="text-right">${mtrblc.MDTOD3_CLS}</td>
								<td class="text-right">${mtrblc.MDTOD3_RECKVAH}</td>
								<td class="text-right">${mtrblc.MDTOD4_OPN}</td>
								<td class="text-right">${mtrblc.MDTOD4_CLS}</td>
								<td class="text-right">${mtrblc.MDTOD4_RECKVAH}</td>
								<td class="text-right">${mtrblc.MDTOD5_OPN}</td>
								<td class="text-right">${mtrblc.MDTOD5_CLS}</td>
								<td class="text-right">${mtrblc.MDTOD5_RECKVAH}</td>
								<td class="text-right">${mtrblc.MDTOD6_OPN}</td>
								<td class="text-right">${mtrblc.MDTOD6_CLS}</td>
								<td class="text-right">${mtrblc.MDTOD6_RECKVAH}</td>
								<td class="text-right">${mtrblc.TOD_TOTAL_UNITS}</td>
								<td class="text-right">${mtrblc.TOD_PEAK}</td>
								<td class="text-right">${mtrblc.TOD_OFFPEAK}</td>
								<td class="text-right">${mtrblc.COLOPNRDG}</td>
								<td class="text-right">${mtrblc.COLCLRDG}</td>
								<td class="text-right">${mtrblc.COLMTRMF}</td>
								<td class="text-right">${mtrblc.COLMETKVAH}</td>
								<td class="text-right">${mtrblc.SKWH_OPN}</td>
								<td class="text-right">${mtrblc.SKWH_CLS}</td>
								<td class="text-right">${mtrblc.SRECKWH}</td>
								<td class="text-right">${mtrblc.OA_KVAH_ADJ_ENG}</td>
								<td class="text-right">${mtrblc.OA_PEAK}</td>
								<td class="text-right">${mtrblc.OA_OFFPEAK}</td>
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
		var currnetMonth = (new Date()).getMonth() + 1;
		$("#hyear").val(currentYear);
		$("#hmon").val($("#mon").val());

		for (var j = currentYear; j > 2015; j--) {
			$("#year").append("<option value="+j+">" + j + "</option>");

		}
		$('#mon option:eq(' + currnetMonth + ')').prop('selected', true);
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
