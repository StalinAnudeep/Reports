<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
		<form class="card" action="openaccesssales" method="post">
			<div class="card-body">
				<h3 class="card-title"><strong><span class="text-danger">HT30A</span> - Voltage Wise Open Access Sales </strong></h3>
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
							<label class="form-label">GET Voltage wise Open Access Sales </label>
							<button type="submit" class="btn btn-success">GET Voltage wise Open Access Sales </button>
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
							<th>CIRCLE</th>
							<th>CTACTUAL_KV</th>
							<th>NOS</th>
							<th>KVAH_ADJ_ENG</th>
							<th>TOD_ADJ_PEAK</th>
							<th>TOD_ADJ_OFF</th>
							<th>TOD_ADJ_ENG</th>
							<th>CS_CHARGES</th>
							<th>WHELL_CHARGES</th>
						</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${tp}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.CTACTUAL_KV}</td>
									<td>${mtrblc.CIRCLE}</td>									
									<td>${mtrblc.NOS}</td>
									<td class="text-right">${mtrblc.KVAH_ADJ_ENG}</td>
									<td class="text-right">${mtrblc.TOD_ADJ_PEAK}</td>
									<td class="text-right">${mtrblc.TOD_ADJ_OFF}</td>
									<td class="text-right">${mtrblc.TOD_ADJ_ENG}</td>
									<td class="text-right">${mtrblc.CS_CHARGES}</td>
									<td class="text-right">${mtrblc.WHELL_CHARGES}</td>
								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
						<tr>
							
							<th colspan="3" class="text-right">Grand Total</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.NOS).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.KVAH_ADJ_ENG).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.TOD_ADJ_PEAK).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.TOD_ADJ_OFF).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.TOD_ADJ_ENG).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.CS_CHARGES).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.WHELL_CHARGES).sum()}</th>
						</tr>
					</tfoot>
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
<script> 
	requirejs([ 'jquery' ], function($) {
			$("td,th").each(function() { 
				if ($.isNumeric( $(this).text())) {
				    // It isn't a number	
				    $(this).html(parseFloat($(this).text()).toLocaleString('en-IN', {style: 'decimal', currency: 'INR'})); 
				}
			}
				
				
			)
			
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>