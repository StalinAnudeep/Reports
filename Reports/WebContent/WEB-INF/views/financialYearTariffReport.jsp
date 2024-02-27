<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="financialYearTariffReport" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT124 </span> - Financial
					Year Tariff Report</strong>
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
						<label class="form-label"> Get Financial Year Tariff </label>
						<button type="submit" class="btn btn-primary">Get
							Financial Year Tariff</button>
					</div>
				</div>
			</div>
		</div>
	</form>


	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>



	<c:if test="${ not empty fn:trim(financialYearTariff)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center text-primary">${title}</h2>
				<table class="table card-table table-vcenter text-nowrap datatable"
					style="width: 100%;">
					<thead class="bg-primary">

						<tr>
							<th class="text-center text-light">MONTH</th>
							<th class="text-center text-light">CAT</th>
							<th class="text-center text-light">SUBCAT</th>
							<th class="text-center text-light">VOLTAGE</th>
							<th class="text-center text-light">DESCRIPT</th>
							<th class="text-center text-light">NOS</th>
							<th class="text-center text-light">BILLED_UNITS</th>
							<th class="text-center text-light">OFF_PEAK_UNITS</th>
							<th class="text-center text-light">PEAK UNITS</th>
							<th class="text-center text-light">COLONY</th>
							<th class="text-center text-light">ECHG</th>
							<th class="text-center text-light">FCHG</th>
							<th class="text-center text-light">CCHG</th>
							<th class="text-center text-light">OTHER_CHGS</th>
							<th class="text-center text-light">COLL</th>
							<th class="text-center text-light">INCENTIVE_AMT</th>
							<th class="text-center text-light">CMD</th>
							<th class="text-center text-light">RMD</th>
						</tr>

					</thead>
					<tbody>
						<c:forEach var="fwt" items="${financialYearTariff}">

							<tr>
								<td><fmt:formatDate pattern="dd-MMM-yyyy"
										value="${fwt.BMONTH}" /></td>
								<td>${fwt.CAT}</td>
								<td>${fwt.SUBCAT}</td>
								<td>${fwt.VOLTAGE}</td>
								<td>${fwt.DESCRIPT}</td>
								<td>${fwt.NOS}</td>
								<td>${fwt.BILLED_UNITS}</td>
								<td>${fwt.OFF_PEAK_UNITS}</td>
								<td>${fwt.PEAK_UNITS}</td>
								<td>${fwt.COLONY_UNITS}</td>
								<td>${fwt.ECHG}</td>
								<td>${fwt.FCHG}</td>
								<td>${fwt.CCHG}</td>
								<td>${fwt.OTHER_CHGS}</td>
								<td>${fwt.COLL}</td>
								<td>${fwt.INCENTIVE_AMT}</td>
								<td>${fwt.CMD}</td>
								<td>${fwt.RMD}</td>

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

					footer : true
				}, {
					extend : 'excel',
					className : 'btn btn-xs btn-primary',
					footer : true
				} ]
			}
		});
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>