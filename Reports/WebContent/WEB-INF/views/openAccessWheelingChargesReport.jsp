<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>

<div class="row row-cards row-deck">
	<form class="card" action="openAccessWheelingChargesReport" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT134</span> - HT Circle wise,Month Wise Open Access Wheeling Charges Report </strong>
			</h3>
			<div class="row">


				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Year</label> <select id="inputyear"
							class="form-control" name="year" required="required">
							<option value="">Select Financial Year</option>
						</select>
					</div>
				</div>
				<div class=" col-md-4">
					<div class="form-group">
						<label class="form-label">Get Open Access Wheeling
							Charges Report</label>
						<button type="submit" class="btn btn-success">Get Open
							Access Wheeling Charges Report</button>
					</div>
				</div>
			</div>

		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>

	<c:if test="${ not empty fn:trim(wheelingChargesDetails)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<table
					class="table card-table table-vcenter text-nowrap datatable display"
					style="width: 100%;">
					<thead>
						<tr>
							<th class="bg-primary text-white text-center" colspan="14">${title}</th>
						</tr>
						<tr>
							<th>S.NO</th>
							<th>CIRCLE</th>
							<th class="text-right">APR-${FI}</th>
							<th class="text-right">MAY-${FI}</th>
							<th class="text-right">JUN-${FI}</th>
							<th class="text-right">JUL-${FI}</th>
							<th class="text-right">AUG-${FI}</th>
							<th class="text-right">SEP-${FI}</th>
							<th class="text-right">OCT-${FI}</th>
							<th class="text-right">NOV-${FI}</th>
							<th class="text-right">DEC-${FI}</th>
							<th class="text-right">JAN-${SI}</th>
							<th class="text-right">FEB-${SI}</th>
							<th class="text-right">MAR-${SI}</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${wheelingChargesDetails}"
							varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<%
								int s = 0;
								%>
								<c:forEach items="${mtrblc}" var="entry">
									<%
									if (s == 0) {
									%>
									<td class="text-right"><a
										href="openAccessWheelingChargesReportForDivision?cir=${mtrblc.CIRCLE}&fyear=${year}">${entry.value}</a></td>
									<%
									s++;
									} else {
									%>
									<td class="text-right">${entry.value}</td>
									<%
									}
									%>

								</c:forEach>
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
					$("#circle").append("<option value='ALL'>ALL</option>");

					$("#monthyearonly").hide();

					$("#inputyear").change(function() {
						var datevalue = $("#inputyear").val().split("-");
						var value = parseInt(datevalue[1]);
						if (value > 2019) {
							$("#monthyearonly").show();
							$("#mon").attr('required', true);
							$("#yearonly").attr('required', true);
						} else {
							$("#monthyearonly").hide();
							$("#mon").attr('required', false);
							$("#yearonly").attr('required', false);
						}
					});
					var currentYear = (new Date()).getFullYear();
					for (var j = currentYear; j > 2015; j--) {
						$("#yearonly").append(
								"<option value="+j+">" + j + "</option>");
					}

				});

		var currentYear = (new Date()).getFullYear();
		for (var j = currentYear; j > 2015; j--) {
			var jj = j - 1 + "-" + j;
			$("#inputyear").append("<option value="+jj+">" + jj + "</option>");
		}

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
					title : 'ACD_REPORT'
				}, {
					extend : 'excel',
					className : 'btn btn-xs btn-primary',
					title : 'ACD_REPORT'
				} ]
			}
		});
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>