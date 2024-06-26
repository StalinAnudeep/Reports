<%@page import="javafx.scene.shape.Circle"%>
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
					var currentYear = (new Date()).getFullYear();
					var currnetMonth = (new Date()).getMonth() + 1;
					for (var j = currentYear; j > 2015; j--) {
						$("#fyear").append(
								"<option value="+j+">" + j + "</option>");

					}
					$('#fmonth option:eq(' + currnetMonth + ')').prop(
							'selected', true);
					$('#fyear option[value="' + currentYear + '"]').prop(
							'selected', true);

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
	<form class="card" action="HtDCBCollectionSplitMonthlyWise"
		method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT97D </span> -HT DIV, SD
					, SEC Wise DCB Collection Split Monthly</strong>
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
						<label for="firstInput">From</label>
						<div>
							<select class="form-control firstInput" name="fmonth"
								required="required" id="fmonth" ng-model="obj.month">
								<option value="">Month</option>
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
							</select> <select class="form-control secondInput" name="fyear"
								required="required" id="fyear" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div>


				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Get DCB Collection Split Monthly
						</label>
						<button type="submit" class="btn btn-success">Get DCB
							Collection Split Monthly</button>
					</div>
				</div>
			</div>
		</div>
	</form>

	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(tp_sale)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">${title}</h2>
				<form name="frm" style="overflow: auto;">

					<c:if test="${CIR eq 'ALL'}">
						<div class="bg-info text-white text-center"
							onclick="exportThisWithParameter('multiLevelTable', '${title}')"
							style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>

						<table id="multiLevelTable"
							class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
							style="width: 100%;">
							<thead>
								<tr>
									<th class="bg-primary text-white text-center" colspan="12">${title}</th>
								</tr>
								<tr class="bg-primary text-white text-center">
									<th>LDT</th>
									<th>CIRNAME</th>
									<th>Division</th>
									<th>SubDivision</th>
									<th>Section</th>
									<th>OB</th>
									<th>DEMAND</th>
									<th>COLLECTION ARREAR</th>
									<th>COLLECTION DEMAND</th>
									<th>COLLECTION</th>
									<th>CB</th>
									<th>SD</th>
								</tr>

							</thead>
							<tbody>
								<%
								int flag = 0;
								String cricle = "S";
								String circletype = "S";
								%>
								<c:forEach var="mtrblc" items="${tp_sale}" varStatus="tagStatus">
									<c:set var="cirl" value="${mtrblc.LDT}" scope="request" />
									<tr class="${mtrblc.CIRNAME}" style="font-weight: 500;">
										<%
										if (!cricle.equals((String) request.getAttribute("cirl"))) {
										%>
										<td rowspan="${CIRCOUNT[cirl]}">${mtrblc.LDT}</td>
										<%
										}
										cricle = (String) request.getAttribute("cirl");
										%><c:if test="${mtrblc.CIRNAME eq 'TOTAL'}">
											<td class="text-left TOTAL bg-primary"
												style="padding-left: 5px;">${mtrblc.CIRNAME}</td>
											<td class="text-left TOTAL bg-primary"
												style="padding-left: 5px;">${mtrblc.DIVNAME}</td>
											<td class="text-left TOTAL bg-primary"
												style="padding-left: 5px;">${mtrblc.SUBNAME}</td>
											<td class="text-left TOTAL bg-primary"
												style="padding-left: 5px;">${mtrblc.SECNAME}</td>
											<td class="text-right TOTAL bg-primary format"
												style="padding-left: 5px;">${mtrblc.TOB}</td>
											<td class="text-right TOTAL bg-primary format"
												style="padding-left: 5px;">${mtrblc.DEMAND}</td>
											<td class="text-right TOTAL bg-primary format"
												style="padding-left: 5px;">${mtrblc.COLL_ARREAR}</td>
											<td class="text-right TOTAL bg-primary format"
												style="padding-left: 5px;">${mtrblc.COLL_DEMAND}</td>
											<td class="text-right TOTAL bg-primary format"
												style="padding-left: 5px;">${mtrblc.COLLECTION}</td>
											<td class="text-right TOTAL bg-primary format"
												style="padding-left: 5px;">${mtrblc.CB}</td>
											<td class="text-right TOTAL bg-primary format"
												style="padding-left: 5px;">${mtrblc.SD}</td>
										</c:if>


										<c:if test="${mtrblc.CIRNAME ne 'TOTAL'}">
											<td class="text-left" style="padding-left: 5px;">${mtrblc.CIRNAME}</td>
											<td class="text-left" style="padding-left: 5px;">${mtrblc.DIVNAME}</td>
											<td class="text-left" style="padding-left: 5px;">${mtrblc.SUBNAME}</td>
											<td class="text-left" style="padding-left: 5px;">${mtrblc.SECNAME}</td>
											<td class="text-right format" style="padding-left: 5px;">${mtrblc.TOB}</td>
											<td class="text-right format" style="padding-left: 5px;">${mtrblc.DEMAND}</td>
											<td class="text-right format" style="padding-left: 5px;">${mtrblc.COLL_ARREAR}</td>
											<td class="text-right format" style="padding-left: 5px;">${mtrblc.COLL_DEMAND}</td>
											<td class="text-right format" style="padding-left: 5px;">${mtrblc.COLLECTION}</td>
											<td class="text-right format" style="padding-left: 5px;">${mtrblc.CB}</td>
											<td class="text-right format" style="padding-left: 5px;">${mtrblc.SD}</td>
										</c:if>

									</tr>

								</c:forEach>
							</tbody>



						</table>
					</c:if>

					<c:if test="${CIR ne 'ALL'}">
						<table id="multiLevelTable"
							class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
							style="width: 100%;">
							<thead>
								<tr>
									<th class="bg-primary text-white text-center" colspan="13">${title}</th>
								</tr>
								<tr class="bg-primary text-white text-center">
									<th>LDT</th>
									<th>Circle</th>
									<th>Division</th>
									<th>SubDivision</th>
									<th>Section</th>
									<th>OB</th>
									<th>DEMAND</th>
									<th>COLLECTION ARREAR</th>
									<th>COLLECTION DEMAND</th>
									<th>COLLECTION</th>
									<th>CB</th>
								</tr>

							</thead>

							<tbody>
								<%
								int flag = 0;
								String cricle = "S";
								String circletype = "S";
								%>
								<c:forEach var="mtrblc" items="${tp_sale}" varStatus="tagStatus">
									<c:set var="cirl" value="${mtrblc.LDT}" scope="request" />
									<tr class="${mtrblc.CIRNAME}" style="font-weight: 500;">
										<%
										if (!cricle.equals((String) request.getAttribute("cirl"))) {
										%>
										<td rowspan="${CIRCOUNT[cirl]}">${mtrblc.LDT}</td>
										<%
										}
										cricle = (String) request.getAttribute("cirl");
										%>
										<td class="text-left" style="padding-left: 5px;">${mtrblc.CIRNAME}</td>
										<td class="text-left" style="padding-left: 5px;">${mtrblc.DIVNAME}</td>
										<td class="text-left" style="padding-left: 5px;">${mtrblc.SUBNAME}</td>
										<td class="text-left" style="padding-left: 5px;">${mtrblc.SECNAME}</td>
										<td class="text-right format" style="padding-left: 5px;">${mtrblc.TOB}</td>
										<td class="text-right format" style="padding-left: 5px;">${mtrblc.DEMAND}</td>
										<td class="text-right format" style="padding-left: 5px;">${mtrblc.COLL_ARREAR}</td>
										<td class="text-right format" style="padding-left: 5px;">${mtrblc.COLL_DEMAND}</td>
										<td class="text-right format" style="padding-left: 5px;">${mtrblc.COLLECTION}</td>
										<td class="text-right format" style="padding-left: 5px;">${mtrblc.CB}</td>


									</tr>

								</c:forEach>
							</tbody>
							<tfoot>
								<tr style="font-weight: 500;">
									<th class="text-center" colspan="5">Grand Total</th>
									<th class="text-right format">${tp_sale.stream().map(mtrblc -> mtrblc.TOB).sum()}</th>
									<th class="text-right format">${tp_sale.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
									<th class="text-right format">${tp_sale.stream().map(mtrblc -> mtrblc.COLL_ARREAR).sum()}</th>
									<th class="text-right format">${tp_sale.stream().map(mtrblc -> mtrblc.COLL_DEMAND).sum()}</th>
									<th class="text-right format">${tp_sale.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th>
									<th class="text-right format">${tp_sale.stream().map(mtrblc -> mtrblc.CB).sum()}</th>
								</tr>
							</tfoot>
						</table>


					</c:if>
				</form>
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

	});
</script>

<script>
	require([ 'jquery', 'datatables.net', 'datatables.net-jszip',
			'datatables.net-buttons', 'datatables.net-buttons-flash',
			'datatables.net-buttons-html5' ], function($, datatable, jszip) {
		window.JSZip = jszip;
		$('.datatable').DataTable({
			dom : 'Bfrltip',
			"scrollX" : true,
			"paging" : false,
			"ordering" : false,
			buttons : {
				buttons : [ {
					extend : 'csv',
					className : 'btn btn-xs btn-primary',
					title : 'Services Wise ledger Closing Balance',
					footer : true
				}, {
					extend : 'excel',
					className : 'btn btn-xs btn-primary',
					title : 'Services Wise ledger Closing Balance',
					footer : true
				} ]
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