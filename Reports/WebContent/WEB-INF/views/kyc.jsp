<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="kyc" method="post">
                <div class="card-body">
                  <h3 class="card-title"> <strong><span class="text-danger">HT115</span> -   Know Your Details </strong></h3>
                  
                  <c:if test="${ not empty fn:trim(kycabs)}">
					<table class="table table-striped table-bordered table-sm "
						style="width: 100%;">
						<thead>
							<tr>
								<th class="text-center">S.NO</th>
								<th class="text-center">CIRCLE</th>
								<th class="text-center">KYC ADDED SCS COUNT</th>
								<th class="text-center">PENDING SCS COUNT</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${kycabs}" varStatus="tagStatus">
								<tr>
									<td  class="text-center">${tagStatus.index + 1}</td>
									<td  class="text-center">${mtrblc.CIRNAME}</td>
									<td class="text-right">${mtrblc.KYC_COUNT}</td>
									<td class="text-right">${mtrblc.NKYC_COUNT}</td>
								</tr>
							</c:forEach>
						</tbody>
							 <tfoot>
						<tr>
							
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right">${kycabs.stream().map(mtrblc -> mtrblc.KYC_COUNT).sum()}</th>
							<th class="text-right">${kycabs.stream().map(mtrblc -> mtrblc.NKYC_COUNT).sum()}</th>

						</tr>
					</tfoot> 
					</table>
		</c:if>
		 <h3 class="card-title"> <strong>   List of Services Report </strong></h3>
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
                    <div class="col">
					<div class="form-group">
						<label class="form-label">Type</label> <select
							class="form-control" name="type" id="type"
							required="required">
							<option value="KYC" selected="selected">KYC Configured Services</option>
							<option value="NKYC">KYC Not Configured Services</option>
						</select>
					</div>
				</div>
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">GET  Details</label>
                        <button type="submit" class="btn btn-success">GET  Details</button>
                      </div>
                    </div>
                  </div>
                </div>
		

	</form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(error)}">
		<div id="exist" class="alert alert-danger" role="alert">${error}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(consumer)}">
			<div class="card ">	
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>SCNO</th>
						<th>NAME</th>
						<th>DIVISION</th>
						<th>SUB DIV</th>
						<th>SECTION</th>
					
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${consumer}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.CTUSCNO}</td>
							<td>${mtrblc.CTNAME}</td>
							<td>${mtrblc.DIVNAME}</td>
							<td>${mtrblc.SUBNAME}</td>
							<td>${mtrblc.SECNAME}</td>
							
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
		 var currnetMonth=(new Date()).getMonth()+1;
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#year").append("<option value="+j+">"+j+"</option>");
		 }
		 
		 $('#mon option:eq('+currnetMonth+')').prop('selected', true);
		 $('#year option[value="'+currentYear+'"]').prop('selected', true);

	});
</script>
<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
	    var circle='<c:out value="${circle}"/>'
	    console.log(circle+"====");
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:"Circle Wise Category Existing Service And Load of  "+""+circle},
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:"Circle Wise Category Existing Service And Load of "+" "+circle}
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>
