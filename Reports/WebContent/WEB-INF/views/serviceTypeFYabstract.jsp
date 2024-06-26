<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="serviceTypeFYabstract" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT138</span> - Service
					Type wise Division,Sub-Division Wise DCB FY Abstract</strong>
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

				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">GET Service Type, Cat Wise
							Abstract</label>
						<button type="submit" class="btn btn-success">GET Service
							Type, Cat Wise Abstract</button>
					</div>
				</div>

			</div>
		</div>

	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>


	<c:if test="${ not empty fn:trim(acd)}">
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
							<th class="text-center" rowspan="2"
								style="vertical-align: middle;">MON_YEAR</th>
							<th class="text-center" rowspan="2"
								style="vertical-align: middle;">Circle</th>
							<th class="text-center" rowspan="2"
								style="vertical-align: middle;">Division</th>
							<th class="text-center" rowspan="2"
								style="vertical-align: middle;">SubDivision</th>

							<th rowspan="2" style="vertical-align: middle;">SERVICE TYPE
								NAME</th>
							<th rowspan="2" style="vertical-align: middle;">CATEGOERY</th>
							<th rowspan="2" style="vertical-align: middle;">NOS</th>
							<th rowspan="2" style="vertical-align: middle;">LOAD</th>
							<th rowspan="2" style="vertical-align: middle;">REC MD</th>
							<th rowspan="2" style="vertical-align: middle;">SALES</th>
							<th rowspan="2" style="vertical-align: middle;">OB</th>
							<th colspan="3" class="text-center">DEMAND</th>

							<th colspan="4" class="text-center">COLLECTION</th>
							<!-- <th  rowspan="2">DRJ</th> -->

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
						<c:forEach var="mtrblc" items="${acd}" varStatus="tagStatus">
							<c:set var="cirl" value="${mtrblc.MON_YEAR}" scope="request" />
							<tr style="font-weight: 500;">
								<%
								if (!cricle.equals((String) request.getAttribute("cirl"))) {
								%>
								<td rowspan="${CIRCOUNT[cirl]}" class="text-center">${mtrblc.MON_YEAR}</td>
								<%
								}
								cricle = (String) request.getAttribute("cirl");
								%>
								<td>${mtrblc.CIRCLE}</td>
								<td>${mtrblc.DIVNAME}</td>
								<td>${mtrblc.SUBNAME}</td>
								<td>${mtrblc.STDESC}</td>
								<td>${mtrblc.CTCAT}</td>
								<td class="text-right"><a
									href="serviceTypeFYabstractForNOS?cir=${mtrblc.CIRCLE}&division=${mtrblc.DIVCD}&subdivision=${mtrblc.SUBCD}&mon_year=${mtrblc.MON_YEAR}
										&service=${mtrblc.SERVTYPE}&fcir=${circle}&year=${year}&month=${month}&fservice=${service}">${mtrblc.NOS}</a></td>
								<td class="text-right">${mtrblc.LOAD}</td>
								<td class="text-right">${mtrblc.REC_MD}</td>
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

							</tr>


						</c:forEach>
					</tbody>
					<tfoot>
						<tr>

							<th colspan="5" class="text-right">Grand Total</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.NOS).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.LOAD).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.REC_MD).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.SALES).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.OB).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.DEMAND).sum()  + acd.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLL_ARREAR).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLL_DEMAND).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
							<%-- 		<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th> --%>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLLECTION).sum() + acd.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.CB).sum()}</th>
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
				$("#servicetype").append("<option value=ALL>ALL</option>");

				var currentYear = (new Date()).getFullYear();
				for (var j = currentYear; j > 2015; j--) {
					var jj = j - 1 + "-" + j;
					$("#year").append(
							"<option value="+jj+">" + jj + "</option>");
				}
				$('#year option[value="' + currentYear + '"]').prop('selected',
						true);

				$("#subdivision")
						.change(
								function() {
									var subdiv = $(this).val();
									$
											.ajax({
												type : "POST",
												url : "getSubDivFeeders/"
														+ subdiv,
												success : function(data) {
													var slctSubcat = $('#feeder');
													slctSubcat.empty();
													$("#feeder")
															.append(
																	"<option value='0' selected> Select Feeder </option>");

													var saptype = jQuery
															.parseJSON(data);
													$
															.each(
																	saptype,
																	function(k,
																			v) {
																		$(
																				"#feeder")
																				.append(
																						"<option value="+k+">"
																								+ v
																								+ "</option>");

																	});
												}

											});
								});

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
<!-- <script>
	require([ 'jquery', 'datatables.net', 'datatables.net-jszip',
			'datatables.net-buttons', 'datatables.net-buttons-flash',
			'datatables.net-buttons-html5' ], function($, datatable, jszip) {
		window.JSZip = jszip;
		$('.datatable').DataTable({
			dom : 'Bfrltip',
			"scrollX" : true,
			"bPaginate": false,
			buttons : {
				buttons : [ ]
			}
		});
	});
</script>  -->

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
<!-- <script>
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
</script> -->