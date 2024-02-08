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
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#year").append("<option value="+j+">"+j+"</option>");
	     }
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


<div class="row row-cards row-deck">
	      <form class="card" action="dlistDivision" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT46(2.1)</span> - D List Division Wise Abstract</strong></h3>
                  <div class="row">
                 <div class="col-md-3">
                  <div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
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
                        <label class="form-label">Get D List Division Wise Abstract</label>
                        <button type="submit" class="btn btn-success">Get Details</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
			
			
			
			
			
			<c:if test="${ not empty fn:trim(fail)}">
				<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
			</c:if>
			<c:if test="${ not empty fn:trim(live)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					 <thead>
		                <tr>
		                    <th rowspan="2" class="text-center">S.NO</th>
		                    <th rowspan="2" class="text-center">Circle</th>
		                    <th rowspan="2" class="text-center">Division</th>
		                    <th colspan="2" class="text-center">Govt</th>
		                    <th colspan="2" class="text-center">PVT</th>
		                    <th colspan="2" class="text-center">TOTAL</th>
		                </tr>
		                <tr>
		                    <th>USCNO</th>
		                    <th>Sum Of CB</th>
		                    <th>USCNO</th>
		                    <th>Sum Of CB</th>
		                    <th>USCNO</th>
		                    <th>Sum Of CB</th>
		                </tr>
           			 </thead>
					<tbody>
						<c:forEach var="mtrblc" items="${live}"  varStatus="tagStatus">
						<c:if test = "${mtrblc.BIL_CIRCLE != 'Y_TOTAL '}">
							<tr>
						        <td>${tagStatus.index + 1}</td>
								<td>${mtrblc.BIL_CIRCLE}</td>
								<td>${mtrblc.BILDIV}</td>
								<td>${mtrblc.DLIST_GOVTSCS}</td>
								<td>${mtrblc.DLIST_GOVTCB}</td>
								<td>${mtrblc.DLIST_PVTSCS}</td>
								<td>${mtrblc.DLIST_PVTCB}</td>
								<td>${mtrblc.DLIST_GOVTSCS + mtrblc.DLIST_PVTSCS}</td>
								<td>${mtrblc.SUMCB}</td>
							</tr>
						 </c:if>
						</c:forEach>
					</tbody>
					<tfoot>
					<c:forEach var="mtrblc" items="${live}"  varStatus="tagStatus">
					    <c:if test = "${mtrblc.BIL_CIRCLE == 'Y_TOTAL '}">
						<tr>
						        <th colspan="3" class="text-right">Grand Total</th>
								<th>${mtrblc.DLIST_GOVTSCS}</th>
								<th>${mtrblc.DLIST_GOVTCB}</th>
								<th>${mtrblc.DLIST_PVTSCS}</th>
								<th>${mtrblc.DLIST_PVTCB}</th>
								<th>${mtrblc.DLIST_GOVTSCS + mtrblc.DLIST_PVTSCS}</th>
								<th>${mtrblc.SUMCB}</th>
							</tr>
						</c:if>
						</c:forEach>
					</tfoot>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:  'D List Division Wise Abstract' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'D List Division Wise Abstract' }
	            ]
	        }
	    });
	});
</script>			
</div>