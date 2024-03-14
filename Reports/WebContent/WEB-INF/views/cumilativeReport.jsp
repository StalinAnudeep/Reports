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

					var currentYear = (new Date()).getFullYear();
					var currnetMonth = (new Date()).getMonth() + 1;
					for (var j = currentYear; j > 2015; j--) {
						$("#fyear").append(
								"<option value="+j+">" + j + "</option>");
						$("#tyear").append(
								"<option value="+j+">" + j + "</option>");
					}
					$('#fmonth option:eq(' + currnetMonth + ')').prop(
							'selected', true);
					$('#tmonth option:eq(' + currnetMonth + ')').prop(
							'selected', true);
					$('#fyear option[value="' + currentYear + '"]').prop(
							'selected', true);
					$('#tyear option[value="' + currentYear + '"]').prop(
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

	<form class="card" action="cumilativeReport" method="post" id="form">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT135</span> -
					Circle/Div/SD/SEC wise CUM Dem Collection Report</strong>
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
						<label for="firstInput">From</label>
						<div>
							<select class="form-control firstInput" name="fmonth"
								required="required" id="fmonth" ng-model="obj.month">
								<option value="">Month</option>
								<option value="01">JANUARY</option>
								<option value="02">FEBRUARY</option>
								<option value="03">MARCH</option>
								<option value="04">APRIL</option>
								<option value="05">MAY</option>
								<option value="06">JUNE</option>
								<option value="07">JULY</option>
								<option value="08">AUGUST</option>
								<option value="09">SEPTEMBER</option>
								<option value="10">OCTOBER</option>
								<option value="11">NOVEMBER</option>
								<option value="12">DECEMBER</option>
							</select> <select class="form-control secondInput" name="fyear"
								required="required" id="fyear" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label for="firstInput">To</label>
						<div>
							<select class="form-control firstInput" name="tmonth"
								required="required" id="tmonth" ng-model="obj.month">
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
							</select> <select class="form-control secondInput" name="tyear"
								required="required" id="tyear" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Get Cumilative Demand Collection
							Report</label>
						<button type="submit" class="btn btn-success">Get
							Cumilative Demand Collection Report</button>
					</div>
				</div>
			</div>
		</div>
	</form>



	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(collectionDetails)}">
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
								<th class="text-center text-light">CIRCLE</th>
								<th class="text-center text-light">DIVNAME</th>
								<th class="text-center text-light">SUBNAME</th>
								<th class="text-center text-light">SECNAME</th>
								<th class="text-center text-light">TYPE</th>
								<th class="text-center text-light">OB</th>
								<th class="text-center text-light">DEMAND</th>
								<th class="text-center text-light">COLLECTION</th>
								<th class="text-center text-light">CB</th>

							</tr>
						</thead>
						<tbody>
							<%
							int flag = 0;
							String cricle = "S";
							%>
							<c:forEach var="mtrblc" items="${collectionDetails}"
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
									<td class="text-right">${mtrblc.DIVNAME}</td>
									<td class="text-right">${mtrblc.SUBNAME}</td>
									<td class="text-right">${mtrblc.SECNAME}</td>
									<td class="text-right">${mtrblc.TYPE}</td>
									<td class="text-right">${mtrblc.OB}</td>
									<td class="text-right">${mtrblc.DEMAND}</td>
									<td class="text-right">${mtrblc.COLLECTION}</td>
									<td class="text-right">${mtrblc.CB}</td>
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
<jsp:include page="footer.jsp"></jsp:include>

