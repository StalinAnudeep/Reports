<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="arrears" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT77A</span> - MONTH WISE ARREARS ABOVE 50000</strong></h3>
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
					      <label for="inputState">Month</label>
					      <select id="mon" class="form-control" name="month" required="required">
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
                    <div class="col-md-2">
                      <div class="form-group">
                        <label class="form-label">Year</label>
                       <select class="form-control" name="year" required="required" id="year">
					      	<option value="">--Select Year-- </option>
					    </select>
                      </div>
                    </div>
                    	<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Status</label> <select
							class="form-control" name="status" id="circle"
							required="required">
							<option value="">Select Status</option>
							<option value="All">ALL</option>
							<option value="LIVE">LIVE</option>
							<option value="BILLSTOP">BILLSTOP</option>
						</select>
					</div>
				</div>
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">GET MONTH WISE ARREARS ABOVE 50000</label>
                        <button type="submit" class="btn btn-success">GET MONTH WISE ARREARS ABOVE 50000 </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(arrears)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
						<tr>
							<th>S.NO</th>
							<th>USCNO</th>
							<th>NAME</th>
							<th>DIVISION</th>
							<th>SUBDIVISION</th>
							<th>SECTION</th>
							<th>ADD1</th>
							<th>ADD2</th>
							<th>ADD3</th>
							<th>CAT</th>
							<th>SUBCAT</th>
							<th>STATUS</th>
							<th>GOVT_PVT</th>
							<th>CB</th>
							<th>COURT CASE</th>
							<th>TOTAL</th>

						</tr>
					</thead>
				<tbody>
						<c:forEach var="mtrblc" items="${arrears}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.NAM}</td>
								<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUBDIVISION}</td>
								<td>${mtrblc.SECTION}</td>
								<td>${mtrblc.CTADD1}</td>
								<td>${mtrblc.CTADD2}</td>
								<td>${mtrblc.CTADD3}</td>
								<td>${mtrblc.CAT}</td>
								<td>${mtrblc.SCAT}</td>
								<td>${mtrblc.STS}</td>
								<td>${mtrblc.GOVT_PVT}</td>
								<td class="text-right">${mtrblc.CBTOT}</td>
								<td class="text-right">${mtrblc.CB_OTH}</td>
								<td class="text-right">${mtrblc.TOTTAL}</td>
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
	
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:' MONTH WISE ARREARS ABOVE 50000 Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'MONTH WISE ARREARS ABOVE 50000 Report'}
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
		
