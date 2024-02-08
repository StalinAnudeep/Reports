<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
		<form class="card" action="solarmeterstatus" method="post">
			<div class="card-body">
				<h3 class="card-title"><strong><span class="text-danger">HT02G</span>- Solar Metering Status Month Wise Report</strong></h3>
				<div class="row">
				    <div class="col-md-2">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
					<div class="col-md-2">
						<label class="form-label">Month</label> <select id="mon"
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
					<div class="col-md-3">
						<div class="form-group">
							<label class="form-label">Year</label> <select
								class="form-control" name="year" required="required" id="year">
								<option value="">--Select Year--</option>
							</select>
						</div>
					</div>
					<div class="col-md-5">
						<div class="form-group">
							<label class="form-label">GET Solar Meter Status Report</label>
							<button type="submit" class="btn btn-success">Get Solar Meter Status Report</button>
						</div>
					</div>
				</div>
			</div>
		</form>

		<c:if test="${ not empty fn:trim(fail)}">
			<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
		</c:if>
		<c:if test="${ not empty fn:trim(demand)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
						<thead>
							<tr>
								<th>S.NO</th>
								<th>CATEGORY</th>
<th>AREA</th>
<th>NOS</th>
<th>BILLED_SCS</th>
<th>REC_KWH</th>
<th>REC_KVAH</th>
<th>BILLED_KVAH</th>
<th>DEMAND</th>
<th>COLLECTION</th>
<th>TOTAL_CB</th>
								
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${demand}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.CATEGORY}</td>
									<td>${mtrblc.AREA}</td>
									<td class="text-right">${mtrblc.NOS}</td>
									<td class="text-right">${mtrblc.BILLED_SCS}</td>
									<td class="text-right">${mtrblc.REC_KWH}</td>
									<td class="text-right">${mtrblc.REC_KVAH}</td>
									<td class="text-right">${mtrblc.BILLED_KVAH}</td>
									<td class="text-right">${mtrblc.DEMAND}</td>
									<td class="text-right">${mtrblc.COLLECTION}</td>
									<td class="text-right">${mtrblc.TOTAL_CB}</td>
								</tr>
							</c:forEach>
						</tbody>
							<tfoot>
						<tr>
							<th colspan="3" class="text-right">Grand Total</th>	
							<th class="text-right">${demand.stream().map(mtrblc -> mtrblc.NOS).sum()}</th>					
								 <th class="text-right">${demand.stream().map(mtrblc -> mtrblc.BILLED_SCS).sum()}</th>
								<th class="text-right">${demand.stream().map(mtrblc -> mtrblc.REC_KWH).sum()}</th>
								<th class="text-right">${demand.stream().map(mtrblc -> mtrblc.REC_KVAH).sum()}</th>
								<th class="text-right">${demand.stream().map(mtrblc -> mtrblc.BILLED_KVAH).sum()}</th> 
								<th class="text-right">${demand.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
								<th class="text-right">${demand.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th>
								<th class="text-right">${demand.stream().map(mtrblc -> mtrblc.TOTAL_CB).sum()}</th>
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
			$("#circle").append("<option value=ALL>ALL</option>");
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
	        "paging":   false,
	        "ordering": false,
	        "info":     false,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'Demand Split Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'Demand Split Report' }
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