<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
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


</style>
<div class="row row-cards row-deck">
	      <form class="card" action="totbillstop" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT66</span> - Total Bill Stop Services Report</strong></h3>
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
                    <div class="col-md-5">
				<label for="inputState">CB Amount</label> 
				<div>
					<select id='dropdown' class="form-control firstInput" name="dropdown">
						<option value="ALL">All Amounts</option>
						<option value="POS">+ Positive</option>
						<option value="NEG">- Negative</option>
					</select> 
					<input type="number" class="form-control  secondInput"id="cbamount" name="cbamount" disabled="true" />
				</div>
				</div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Total Bill Stop Services Report</label>
                        <button type="submit" class="btn btn-success">Total Bill Stop Services Report</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(bstop)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>USCNO</th>
						<th>NAME</th>
						<th>TYPE</th>
						
						<th>BILL STOP ARREAR </th>
						<th>DEPOSIT </th>
						<th>Closing Reading</th>
						<th>BILL STOP DATE</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${bstop}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.USCNO}</td>
							<td>${mtrblc.NAM}</td>
							<td>${mtrblc.TYPE}</td>
							<td class="text-right">${mtrblc.CBTOT}</td>
							<td class="text-right">${mtrblc.CB_SD}</td>
							<td class="text-right">${mtrblc.MDCLKVAH_HT}</td>
							<td class="text-right"> ${mtrblc.MDCLRDG_DT}</td>
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
		 var currentYear = (new Date()).getFullYear();
		 var currnetMonth=(new Date()).getMonth()+1;
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#year").append("<option value="+j+">"+j+"</option>");
		 }
		 $('#mon option:eq('+currnetMonth+')').prop('selected', true);
		 $('#year option[value="'+currentYear+'"]').prop('selected', true);
		 $("#circle").append("<option value=ALL>ALL</option>");
		 $('#dropdown').change(function() {
			  	if( $(this).val() == 'POS' || $(this).val() == 'NEG') {
			       		$('#cbamount').prop( "disabled", false );
			       		$('#cbamount').prop( "required", true );
			    } else {       
			      $('#cbamount').prop( "disabled", true );
			      $('#cbamount').prop( "required", false );
			    }
			  });

	});
</script>
<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
	
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'Total_Bill_Stop_Services' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Total_Bill_Stop_Services'}
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
		
