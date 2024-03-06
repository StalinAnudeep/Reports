<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<style>
.TOTAL {
	color: #fff !important;
	font-weight: bold !important;
}
</style>
<div class="row row-cards row-deck">
	<form class="card" action="voltagewiseFinancialYearAbstract"
		method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT102A</span> - Voltage
					Wise FinancialYear DCB Abstract</strong>
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
						<label class="form-label">Division</label> <select
							class="form-control" name="division" id="division"
							required="required">
							<option value="">Select Division</option>
							<option value="ALL">ALL</option>
						</select>
					</div>
				</div>

				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">SubDivision</label> <select
							class="form-control" name="subdivision" id="subdivision"
							required="required">
							<option value="">Select SubDivision</option>
						</select>
					</div>
				</div>


				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Voltage</label> <select
							class="form-control" id="voltage" name="voltage"
							required="required">
							<option value="">Select Voltage</option>
							<option value="ALL">ALL</option>
							<option value="11">11</option>
							<option value="33">33</option>
							<option value="132">132</option>
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


				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">GET Voltage Wise Abstract</label>
						<button type="submit" class="btn btn-success">GET Voltage
							Wise Abstract</button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="hmon" name="hmon"> <input
			type="hidden" id="hyear" name="hyear">
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>


	<c:if test="${ not empty fn:trim(voltageDetails)}">
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
							<th class="bg-primary text-white text-center" colspan="16">${title}</th>
						</tr>
						<tr>
							<th rowspan="2" style="vertical-align: middle;">MON_YEAR</th>
							<th rowspan="2" style="vertical-align: middle;">Circle</th>
							<th rowspan="2" style="vertical-align: middle;">DIVISION</th>
							<th rowspan="2" style="vertical-align: middle;">SUBDIVISION</th>
							<th rowspan="2" style="vertical-align: middle;">Voltage</th>
							<th rowspan="2" style="vertical-align: middle;">NOS</th>
							<th rowspan="2" style="vertical-align: middle;">SALES</th>
							<th rowspan="2" style="vertical-align: middle;">OB</th>
							<th colspan="3" class="text-center">DEMAND</th>
							<th colspan="4" class="text-center">COLLECTION</th>
							<th rowspan="2" class="text-center"
								style="vertical-align: middle;">CB</th>
						</tr>
						<tr>
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
						<%
						int flag = 0;
						String cricle = "S";
						String circletype = "S";
						%>
						<c:forEach var="vd" items="${voltageDetails}"
							varStatus="tagStatus">
							<c:set var="cirl" value="${vd.MON_YEAR}" scope="request" />
							<tr style="font-weight: 500;">
								<%
								if (!cricle.equals((String) request.getAttribute("cirl"))) {
								%>
								<td rowspan="${CIRCOUNT[cirl]}" class="text-center">${vd.MON_YEAR}</td>
								<%
								}
								cricle = (String) request.getAttribute("cirl");
								%><c:if test="${vd.CTACTUAL_KV ne 'TOTAL'}">
									<td class="text-right">${vd.Circle}</td>
									<td class="text-right">${vd.DIVNAME}</td>
									<td class="text-right">${vd.SUBNAME}</td>
									<td class="text-right">${vd.CTACTUAL_KV}</td>
									<td class="text-right">${vd.NOS}</td>
									<td class="text-right">${vd.SALES}</td>
									<td class="text-right">${vd.OB}</td>
									<td class="text-right">${vd.DEMAND}</td>
									<td class="text-right">${vd.DRJ}</td>
									<td class="text-right">${vd.DEMAND+vd.DRJ}</td>
									<td class="text-right">${vd.COLL_ARREAR}</td>
									<td class="text-right">${vd.COLL_DEMAND}</td>
									<td class="text-right">${vd.CRJ}</td>
									<td class="text-right">${vd.COLLECTION + vd.CRJ}</td>
									<td class="text-right">${vd.CB}</td>
								</c:if>


								<c:if test="${vd.CTACTUAL_KV eq 'TOTAL'}">
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.Circle}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.DIVNAME}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.SUBNAME}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.CTACTUAL_KV}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.NOS}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.SALES}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.OB}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.DEMAND}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.DRJ}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.DEMAND+vd.DRJ}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.COLL_ARREAR}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.COLL_DEMAND}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.CRJ}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.COLLECTION + vd.CRJ}</td>
									<td class="text-right TOTAL bg-primary"
										style="padding-left: 5px;">${vd.CB}</td>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>

							<th colspan="5" class="text-right">Grand Total</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.NOS).sum()}</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.SALES).sum()}</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.OB).sum()}</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.DEMAND).sum()  + voltageDetails.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.COLL_ARREAR).sum()}</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.COLL_DEMAND).sum()}</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.COLLECTION).sum() + voltageDetails.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
							<th class="text-right">${voltageDetails.stream().map(mtrblc -> mtrblc.CB).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
</div>


<script>
	requirejs(
			[ 'jquery' ],
			function($) {
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
				var currentYear = (new Date()).getFullYear();
				for (var j = currentYear; j > 2015; j--) {
					var jj = j - 1 + "-" + j;
					$("#year").append(
							"<option value="+jj+">" + jj + "</option>");
				}
				$('#year option[value="' + currentYear + '"]').prop('selected',
						true);

				$("#circle")
						.change(
								function() {
									var circle = $(this).val();
									$
											.ajax({
												type : 'GET',
												url : "${pageContext.request.contextPath}/getdivision/"
														+ circle,
												contentType : "application/json;charset=utf-8",
												success : function(data) {
													var slctSubcat = $('#division'), option = "<option value='0' label='select'/>";

													slctSubcat.empty();
													var obj = jQuery
															.parseJSON(data);
													for ( var prop in obj) {
														option = option
																+ "<option value='"+prop + "'>"
																+ obj[prop]
																+ "</option>";
													}
													slctSubcat.append(option);
												},
												error : function() {
													alert("error");
												}
											});
								});
				$("#division")
						.change(
								function() {
									var division = $(this).val();
									$
											.ajax({
												type : 'GET',
												url : "${pageContext.request.contextPath}/getsubdivision/"
														+ division,
												contentType : "application/json;charset=utf-8",
												success : function(data) {
													var slctSubcat = $('#subdivision'), option = " <option value='0' label='select'/>";
													slctSubcat.empty();

													var obj = jQuery
															.parseJSON(data);
													for ( var prop in obj) {
														option = option
																+ "<option value='"+prop + "'>"
																+ obj[prop]
																+ "</option>";
													}
													slctSubcat.append(option);
												},
												error : function() {
													alert("error");
												}
											});
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