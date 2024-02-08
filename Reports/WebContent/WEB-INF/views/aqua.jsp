<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="aqua" method="post">
                <div class="card-body">
                  <h3 class="card-title">Aqua Culture Subsidy Charges Report</h3>
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
                        <label class="form-label">GET Aqua  subsidy Charges Report</label>
                        <button type="submit" class="btn btn-success">GET Aqua  Subsidy Charges Report </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(error)}">
		<div id="exist" class="alert alert-danger" role="alert">${error}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(aqua)}">
			<div class="card ">	
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>CIRCLE</th>
						<th>USCNO</th>
						<th>CONSUMER<br>NAME</th>
						<th>CMD</th>
						<th>CAT</th>
						<th>STATUS</th>
						<th>VOS</th>
						<th>BILLED<br>KVAH</th>
						<th>ACTUAL <br>ENERGY CHARGES</th>
						<th>NET<br>CHARGES</th>					
						<th>SUBSIDY <br> CHARGES</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${aqua}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.CIRCLE}</td>
							<td>${mtrblc.CTUSCNO}</td>
							<td>${mtrblc.CTNAME}</td>
							<td>${mtrblc.CTCMD_HT}</td>
							<td>${mtrblc.CTCAT}</td>
							<td>${mtrblc.CTSTATUS}</td>
							<td>${mtrblc.CTACTUAL_KV}</td>
							<td>${mtrblc.BTBKVAH}</td>
							<td>${mtrblc.BTENGCHG_NOR}</td>
							<td>${mtrblc.BTAQUAENG_CHG}</td>
							<td>${mtrblc.BTAQUASUB_CHG}</td>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'AQUA CULTURE SUBIDY CHARGES REPORT' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'AQUA CULTURE SUBIDY CHARGES REPORT'}
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>
