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
						$("#tyear").append(
								"<option value="+j+">" + j + "</option>");
					}
					$('#fmonth option:eq(' + currnetMonth + ')').prop(
							'selected', true);
					$('#fyear option[value="' + currentYear + '"]').prop(
							'selected', true);

					/*  $('#tmonth option:eq('+currnetMonth+')').prop('selected', true);
					 $('#tyear option[value="'+currentYear+'"]').prop('selected', true); */
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
</style>

<div class="row row-cards row-deck">
	<form class="card" action="pvtcoll" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT122</span> - Private
					Department Collection</strong>
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

				<!-- <div class="col-md-3">
					<div class="form-group">
						<label for="firstInput">To</label>
						<div>
							<select class="form-control firstInput" name="tmonth"
								required="required" id="tmonth" ng-model="obj.month">
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
							</select> <select class="form-control secondInput" name="tyear"
								required="required" id="tyear" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div> -->
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">GET Collection</label>
						<button type="submit" class="btn btn-success">GET
							Collection</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(account)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<table
					class="table  card-table table-vcenter text-nowrap datatable display"
					style="width: 100%;">
					<thead>
						<tr>
							<th>SR.NO</th>
							<th class="text-center">CIRCLE</th>
							<th class="text-center">DIVISION</th>
							<th class="text-center">ERO NAME</th>
							<th class="text-center">SUB_DIVISION</th>
							<th class="text-center">SECTION</th>
							<th class="text-center">USCNO</th>
							<th class="text-center">OB</th>
							<th class="text-center">OB COLLECTION</th>
							<th class="text-right">DEMAND</th>
							<th class="text-right">DEMAND COLLECTION</th>
							<th class="text-right">TOTAL ARREARS</th>
							<th class="text-right">TOTAL COLLECTION</th>
							<th class="text-right">MONTH YEAR</th>
							<th class="text-right">STATUS</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${account}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRNAME}</td>
								<td>${mtrblc.DIVNAME}</td>
								<td>${mtrblc.ERONAME}</td>
								<td>${mtrblc.SUBNAME}</td>
								<td>${mtrblc.SECNAME}</td>
								<td>${mtrblc.CTUSCNO}</td>
								<td class="text-right">${mtrblc.OB}</td>
								<td class="text-right">${mtrblc.COLL_OB}</td>
								<td class="text-right">${mtrblc.CMD}</td>
								<td class="text-right">${mtrblc.COLL_CMD}</td>
								<td class="text-right">${mtrblc.TOTAL_ARREARS}</td>
								<td class="text-right">${mtrblc.TOTAL_COLLECTION}</td>
								<td class="text-right">${mtrblc.MON_YEAR}</td>
								<td class="text-right">${mtrblc.STATUS}</td>

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
		var currentYear = (new Date()).getFullYear();
		var currnetMonth = (new Date()).getMonth() + 1;
		for (var j = currentYear; j > 2015; j--) {
			$("#year").append("<option value="+j+">" + j + "</option>");
		}

		$('#mon option:eq(' + currnetMonth + ')').prop('selected', true);
		$('#year option[value="' + currentYear + '"]').prop('selected', true);

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
			buttons : {
				buttons : [ {
					extend : 'csv',
					className : 'btn btn-xs btn-primary',
					title : 'Private Department Collection',
					footer : true
				}, {
					extend : 'excel',
					className : 'btn btn-xs btn-primary',
					title : 'Private Department Collection',
					footer : true
				} ]
			}
		});
	});
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
<jsp:include page="footer.jsp"></jsp:include>