<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
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


<div class="row row-cards row-deck">
	      <form class="card" action="acdnoticemonthly" method="post">
                <div class="card-body">
                  <h3 class="card-title">ACD Notice Monthly Report </h3>
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
                     <div class="col-md-3">
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
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Year</label>
                       <select class="form-control" name="year" required="required" id="year">
					      	<option value="">--Select Year-- </option>
					    </select>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">GET ACD Report</label>
                        <button type="submit" class="btn btn-success">GET ACD Report </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
			
			<c:if test="${ not empty fn:trim(fail)}">
				<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
			</c:if>
			
			<c:if test="${ not empty fn:trim(account)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">

			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					
	                 <thead>	                   
						<tr>
							<th class="text-center">S.NO</th>
							<th class="text-center">CIRCLE</th>
							<th class="text-center">DIVISION</th>
							<th class="text-center">SUB DIVISION</th>
							<th class="text-center">SECTION</th>
							<th class="text-center">USCNO</th>
							<th class="text-center">ACD AMOUNT</th>
							<th class="text-center">TYPE</th>
							<th class="text-center">LEVI MONTH</th>
							
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${account}"  varStatus="tagStatus">
							<tr>
						        <td class="text-center">${tagStatus.index + 1}</td>
								<td class="text-center">${mtrblc.CIRNAME}</td>
								<td class="text-center">${mtrblc.DIVNAME}</td>
								<td class="text-center">${mtrblc.SUBNAME}</td>
								<td class="text-center">${mtrblc.SECNAME}</td>
								<td class="text-center">${mtrblc.USCNO}</td>
								<td class="text-right">${mtrblc.ACD}</td>
								<td class="text-center">${mtrblc.TYPE}</td>
								<td class="text-center">${mtrblc.LEVI_MTH}</td>
								
								
							</tr>
						</c:forEach>
					</tbody> 
	          
	          
	          
	          
	          
	          
	 
	           </table>
				</div>
			</div>
			</c:if>
			
			<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'ACD Notice Monthly Report',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'ACD Notice Monthly Report',footer: true }
	            ]
	        }
	    });
	});
	

</script>		

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
	
</div>