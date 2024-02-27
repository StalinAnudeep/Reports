<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="monthWiseTariffReport" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT123 </span> - Month Wise Tariff Report</strong>
			</h3>
            <div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label for="inputState">Month</label> <select id="mon"
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
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Year</label> <select id="year"
							class="form-control" name="year" required="required">
							<option value="">Select Year</option>
						</select>
					</div>
				</div>

				<div class="col-md-6">
					<div class="form-group">
						<label class="form-label"> Get Month Wise Tariff
							</label>
						<button type="submit" class="btn btn-primary"> Get Month Wise Tariff </button>
					</div>
				</div>
			</div>
		</div>
	</form>


	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>

	

		<c:if test="${ not empty fn:trim(monthWiseTariff)}">
			<div class="card ">
				<div
					class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
					<h2 class="text-center text-primary">${title}</h2>
					<table class="table card-table table-vcenter text-nowrap datatable"
						style="width: 100%;">
						<thead class="bg-primary">

							<tr>
								<th class="text-center text-light">CAT</th>
								<th class="text-center text-light">SUBCAT</th>
								<th class="text-center text-light">VOLTAGE</th>
								<th class="text-center text-light">DESCRIPT</th>
								<th class="text-center text-light">NOS</th>
								<th class="text-center text-light">BILLED_UNITS</th>
								<th class="text-center text-light"></th>
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
							<c:forEach var="mwt" items="${monthWiseTariff}"
								varStatus="tagStatus">

								<tr>
									<td>${mwt.CAT}</td>
									<td>${mwt.SUBCAT}</td>
									<td>${mwt.VOLTAGE}</td>
									<td>${mwt.DESCRIPT}</td>
									<td>${mwt.NOS}</td>
									<td>${mwt.BILLED_UNITS}</td>
									<td></td>
									<td>${mwt.OFF_PEAK_UNITS}</td>
									<td>${mwt.PEAK_UNITS}</td>
									<td>${mwt.COLONY_UNITS}</td>
									<td>${mwt.ECHG}</td>
									<td>${mwt.FCHG}</td>
									<td>${mwt.CCHG}</td>
									<td>${mwt.OTHER_CHGS}</td>
									<td>${mwt.COLL}</td>
									<td>${mwt.INCENTIVE_AMT}</td>
									<td>${mwt.CMD}</td>
									<td>${mwt.RMD}</td>
									
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
$(document).ready(function() {
	 var currentYear = (new Date()).getFullYear();
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#year").append("<option value="+j+">"+j+"</option>");
	     }
		 $('#mon option:eq('+currnetMonth+')').prop('selected', true);
		 $('#year option[value="'+currentYear+'"]').prop('selected', true);
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