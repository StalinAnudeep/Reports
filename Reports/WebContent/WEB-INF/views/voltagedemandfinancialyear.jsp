<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
		<form class="card" action="fyvoltagedemand" method="post">
			<div class="card-body">
				<h3 class="card-title">Voltage Wise Financial Year Demand Report</h3>
				<div class="row">
				    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
					<div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">Year</label>
                       <select id="inputyear" class="form-control" name="year" required="required">
					      <option value="">Select Financial Year</option>
					      </select>
                      </div>
                    </div>
					<div class="col-md-4">
						<div class="form-group">
							<label class="form-label">GET Voltage wise demand Report</label>
							<button type="submit" class="btn btn-success">GET Voltage wise demand Report</button>
						</div>
					</div>
				</div>
			</div>
		</form>

		<c:if test="${ not empty fn:trim(fail)}">
			<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
		</c:if>
		<c:if test="${ not empty fn:trim(vol)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		    <h4>HT SALES VOLTAGE WISE PARTICULARS</h4>
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
						<thead>
							<tr>
								<th>S.NO</th>
								<th  class="text-center">ACTUAL KV</th>
								<th  class="text-center">KWH</th>
								<th  class="text-center">KVAH</th>

							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${vol}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.CTACTUAL_KV}</td>
									<td class="text-right">${mtrblc.RKWH}</td>
									<td class="text-right">${mtrblc.RKVAH}</td>

								</tr>
							</c:forEach>
						</tbody>
						<%-- <tfoot>
						<tr>
							<th colspan="3">Grand Total</th>
							<th class="text-right">${vol.stream().map(mtrblc -> mtrblc.NOS).sum()}</th>
							<th class="text-right">${vol.stream().map(mtrblc -> mtrblc.BTRKWH_HT).sum()}</th>
							<th class="text-right">${vol.stream().map(mtrblc -> mtrblc.BTBKVAH).sum()}</th>
							<th class="text-right">${vol.stream().map(mtrblc -> mtrblc.CB_CCLPC).sum()}</th>
							<th class="text-right">${vol.stream().map(mtrblc -> mtrblc.BTCURDEM).sum()}</th>
						</tr>
						</tfoot> --%>
					</table>
				</div>
			</div>
		</c:if>
		
		<c:if test="${ not empty fn:trim(tp)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		    <h4>THIRD ACCESS & OPEN ACCESS UNITS</h4>
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
									<thead>
							<tr>
								<th>S.NO</th>
								<th class="text-center">ACTUAL KV</th>
								<th  class="text-center">THIRD PARTY</th>
								<th  class="text-center">OPEN ACCESS</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${tp}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.CTACTUAL_KV}</td>
									<td class="text-right">${mtrblc.THIRD_PARTY}</td>
									<td class="text-right">${mtrblc.OPEN_ACCESS}</td>
								</tr>
							</c:forEach>
						</tbody>

						<%-- <tfoot>
						<tr>
							<th colspan="2">Grand Total</th>
							<th>${tp.stream().map(mtrblc -> mtrblc.NOS).sum()}</th>
							<th>${tp.stream().map(mtrblc -> mtrblc.TP_ADJ_ENG).sum()}</th>
						</tr>
						</tfoot> --%>
					</table>
				</div>
			</div>
		</c:if>
</div>
<script>
	requirejs([ 'jquery' ], function($) {
		$("#circle").append("<option value=ALL>ALL</option>");
		$(document).ready(function() {
			
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
			var currentYear = (new Date()).getFullYear();
			for (var j = currentYear; j > 2015; j--) {
				var jj = j - 1 + "-" + j;
				$("#inputyear").append("<option value="+jj+">" + jj + "</option>");
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'Voltage wise demand Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'Voltage wise demand Report' }
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>