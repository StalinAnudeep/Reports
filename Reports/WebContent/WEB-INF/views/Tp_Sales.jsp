<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
		<form class="card" action="Tp_Sales" method="post">
			<div class="card-body">
				<h3 class="card-title">ThirdParty Sales Details</h3>
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
							<label class="form-label">GET ThirdParty Sales  Report</label>
							<button type="submit" class="btn btn-success">GET ThirdParty Sales Report</button>
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
								<th>CMD</th>
								<th>VOLT</th>
								<th>WHEEL_LOSS</th>
								<th>REC_KVAH</th>
								<th>REC_KVA</th>
								<th>REC_PEAK_TOD</th>
								<th>REC_OFFPEAK_TOD</th>
								<th>PF</th>
								<th>ALLOC_ENG_GROSS</th>
								<th>ADJ_ENG_GROSS</th>
								<th>ADJ_ENG_NET</th>
								<th>ADJ_TOD_NET</th>
								<th>BKVAH</th>
								<th>BMD</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${tp}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.DEV_CODE}</td>
									<td>${mtrblc.TPUSCNO}</td>
									<td>${mtrblc.CTCAT}</td>
									<td class="text-right">${mtrblc.CTCMD_HT}</td>
									<td class="text-right">${mtrblc.CTACTUAL_KV}</td>
									<td class="text-right">${mtrblc.WHEEL_PER}</td>
									<td class="text-right">${mtrblc.BTRKVAH_HT}</td>
									<td class="text-right">${mtrblc.BTRKVA_HT}</td>
									<td class="text-right">${mtrblc.REC_PEAK_TOD}</td>
									<td class="text-right">${mtrblc.REC_OFFPEAK_TOD}</td>
									<td class="text-right">${mtrblc.BTPF_HT}</td>
									<td class="text-right">${mtrblc.ALLOC_ENG_GROSS}</td>
									<td class="text-right">${mtrblc.ADJ_ENG_GROSS}</td>
									<td class="text-right">${mtrblc.ADJ_ENG_NET}</td>
									<td class="text-right">${mtrblc.ADJ_TOD_NET}</td>
									<td class="text-right">${mtrblc.BTBKVAH}</td>
									<td class="text-right">${mtrblc.BTBLKVA_HT}</td>
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