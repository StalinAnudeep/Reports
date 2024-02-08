<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
		<form class="card" action="Tp_Sales2" method="post">
			<div class="card-body">
				<h3 class="card-title">Voltage wise 3rd  party sales </h3>
				<div class="row">
					<div class="col-md-4">
						<label for="inputState">Month</label> <select id="mon"
							class="form-control" name="month" required="required">
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
					<div class="col-md-4">
						<div class="form-group">
							<label class="form-label">Year</label> <select
								class="form-control" name="year" required="required" id="year">
								<option value="">--Select Year--</option>
							</select>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">
							<label class="form-label">GET Voltage wise 3rd  party sales </label>
							<button type="submit" class="btn btn-success">GET Voltage wise 3rd  party sales </button>
						</div>
					</div>
				</div>
			</div>
		</form>

		<c:if test="${ not empty fn:trim(fail)}">
			<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
		</c:if>
		<c:if test="${ not empty fn:trim(tp)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
						<thead>
							<tr>
								<th>S.NO</th>
								<th>DEV_CODE</th>
								<th>USCNO</th>
								<th>CAT</th>
								<th>NAME</th>
								<th>CMD <br>in<br> KVA</th>
								<th>VOLT <br>KV</th>
								<th>DEV_NAME</th>
								<th>WHEEL_LOSS</th>
								<th>REC_KVAH</th>
								<th>ADJ<br>ENG<br>GROSS</th>
								<th>WHEELING<br>LOSS<br>KVAH</th>
								<th>ADJ<br>ENG<br>NET</th>
								<th>WHEELING<br>CASH</th>
								<th>UNUTILIZED<br>UNITS</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${tp}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.DEV_CODE}</td>
									<td>${mtrblc.TPUSCNO}</td>
									<td>${mtrblc.CTCAT}</td>
									<td>${mtrblc.CTNAME}</td>
									<td class="text-right">${mtrblc.CTCMD_HT}</td>
									<td class="text-right">${mtrblc.CTACTUAL_KV}</td>
									<td class="text-right">${mtrblc.DEV_NAME}</td>
									<td class="text-right">${mtrblc.WHEEL_PER}</td>
									<td class="text-right">${mtrblc.BTRKVAH_HT}</td>
									<td class="text-right">${mtrblc.ADJ_ENG_GROSS}</td>
									<td class="text-right">${mtrblc.WHEELING_LOSS_KVAH}</td>
									<td class="text-right">${mtrblc.TP_ADJ_ENG}</td>
									<td class="text-right">${mtrblc.TP_WHEEL_CHGS}</td>
									<td class="text-right">${mtrblc.UNUTILIZED_UNITS}</td>
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
			 var currnetMonth=(new Date()).getMonth()+1;
				 for (var j = currentYear; j > 2015; j--) {
			     	$("#year").append("<option value="+j+">"+j+"</option>");
			     }
				 $('#mon option:eq('+currnetMonth+')').prop('selected', true);
				 $('#year option[value="'+currentYear+'"]').prop('selected', true);
		});

	});
</script>

<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
	
		window.JSZip = jszip;
		$('.datatable').DataTable({
			"scrollX": true,
	        dom: 'Bfrltip',
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'Tp_Details' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'Tp_Details' }
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>