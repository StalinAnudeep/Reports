<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>

<div class="row row-cards row-deck">
	      <form class="card" action="unpaid" method="post">
                <div class="card-body">
                  <h3 class="card-title">UnPaid Services</h3>
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
                        <label class="form-label">Get UnPaid Services</label>
                        <button type="submit" class="btn btn-success">Get UnPaid Services</button>
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
		                     <th rowspan="2" class="text-center">Month</th>
		                    <th rowspan="2" class="text-center">Service No</th>
		                    <th rowspan="2" class="text-center">Name</th>
		                    <th rowspan="2" class="text-center">Cat</th>
		                    <th rowspan="2" class="text-center">Vol</th>
		                    <th rowspan="2" class="text-center">Division</th>
		                    <th rowspan="2" class="text-center">Sub<br>Division</th>
		                    <th rowspan="2" class="text-center">Status<br>LIVE/UDC</th>
		                    <th rowspan="2" class="text-center">TYPE<br>Govt/Pvt</th>
		                    <th colspan="3" class="text-center">Opening Balance</th>
		                    <th rowspan="2" class="text-center">Demand<br>Other<br>Than <br>Court</th>
		                    <th rowspan="2" class="text-center">Demand<br>Other<br>Than <br>Court LPC</th>
		                    <th rowspan="2" class="text-center">Collection</th>
		                    <th colspan="3" class="text-center">Closing Balance</th>
		                </tr>
		                <tr>
		                    <th>As Per Court</th>
		                    <th>Other Than Court</th>
		                    <th>Total Arrears</th>
		                    <th>As Per Court</th>
		                    <th>Other Than Court</th>
		                    <th>Total Arrears</th>
		                </tr>
           			 </thead>
					<tbody>
						<c:forEach var="mtrblc" items="${live}"  varStatus="tagStatus">
						<c:if test = "${mtrblc.BIL_CIRCLE != 'Y_TOTAL '}">
							<tr>
						        <td>${tagStatus.index + 1}</td>
						        <td>${mtrblc.MON_YEAR}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.NAM}</td>
								<td>${mtrblc.SCAT}</td>
								<td>${mtrblc.ctactual_kv}</td>
								<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUBDIVISION}</td>
								<td>${mtrblc.CTSTA}</td>
								<td>${mtrblc.GOVT_PVT}</td>
								<td>${mtrblc.AS_PER_COURT_OB}</td>
								<td>${mtrblc.OTHER_TAHN_COURT_OB}</td>
								<td>${mtrblc.OB_TOTAL}</td>
								<td>${mtrblc.DEMAND_OTHER_THAN_COURT}</td>
								<td>${mtrblc.DEMAND_OTHER_THAN_COURT_LPC}</td>
								<td>${mtrblc.COLLECTION}</td>
								<td>${mtrblc.AS_PER_COURT_CB}</td>
								<td>${mtrblc.OTHER_TAHN_COURT_CB}</td>
								<td>${mtrblc.CB_TOTAL}</td>
							</tr>
						 </c:if>
						</c:forEach>
					</tbody>
				</table>
				</div>
			</div>
			</c:if>
			
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
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:  'D List Appeared' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'D List Appeared' }
	            ]
	        }
	    });
	});
</script>			
</div>