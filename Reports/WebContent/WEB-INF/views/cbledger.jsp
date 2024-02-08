<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="cbledger" method="post">
		<div class="card-body">
			<h3 class="card-title"><strong><span class="text-danger">HT92</span> - Services Wise Ledger Closing Balance</strong></h3>
			<div class="row">
				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label for="inputState">LEDGER MONTH</label> <select id="mon"
							class="form-control" name="month">
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
				</div>
				<div class="col-md-2">
					<div class="form-group">
					      <label for="inputZip">Select Year</label>
					      <select  class="form-control" name="year" id="year">
					      <option value="">--Select Year--</option>
					      </select>
			    </div>
				</div>
				
				  <div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Status</label> <select
							class="form-control" name="status" id="status"
							required="required">
							<option value="">Select Status</option>
							<option value="A">ALL</option>
							<option value="L">LIVE</option>
							<option value="B">BILL STOP</option>
							<option value="U">UDC</option>
						</select>
					</div>
				</div>
				
				
				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">GET CB Balance</label>
						<button type="submit" class="btn btn-success">GET CB Balance</button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="hmon" name="hmon">
		<input type="hidden" id="hyear" name="hyear">
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	
	
	<c:if test="${ not empty fn:trim(acd)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>SCNO</th>
							<th>LED_MONTH</th>
							<th>CIRNAME</th>
							<th>DIVNAME</th>
							<th>SUBNAME</th>
							<th>SECNAME</th>
							<th>NAME</th>
							<th>CAT</th>
							<th>SUBCAT</th>
							<th>LOAD</th>
							<th>STATUS</th>
							<th>LOCATION TYPE</th>
							<th>BILL_STOP_DATE</th>
							<th> DISMANTLE_DATE</th>
							<th>LAST_PAID_DATE</th>
							<th>VOLTAGE</th>
							<th>SUPPLY CONNECTION DATE </th>
							<th>REC KWH</th>
							<th>REC KVAH</th>							
							<th>BKVAH</th>
							<th>OB_OTHERTHAN_COURT</th>
							<th>OB_COURT</th>
							<th>TOT_OB</th>
							<th>DEMAND_WITHOUT_COURT</th>
							<th>COURT_LPC</th>
							<th>DRJ</th>
							<th>CRJ</th>
							<th>COURT_RJ</th>
							<th>COLLECTION</th>
							<th>CB_OTHERTHAN_COURT</th>
							<th>CB_COURT</th>
							<th>TOTAL_CB</th>
							<th>CB_SD</th>
							<th>TYPE_OF_SERVICE</th>

						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${acd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.SCNO}</td>
								<td>${mtrblc.LED_MONTH}</td>
								<td>${mtrblc.CIRNAME}</td>
								<td>${mtrblc.DIVNAME}</td>
								<td>${mtrblc.SUBNAME}</td>
								<td>${mtrblc.SECNAME}</td>
								<td>${mtrblc.NAME}</td>
								<td>${mtrblc.CAT}</td>
								<td>${mtrblc.SUBCAT}</td>
								<td class="text-right">${mtrblc.LOAD}</td>
								<td>${mtrblc.STATUS}</td>
								<td>${mtrblc.CTLOCA_TYPE}</td>
								<td>${mtrblc.BILL_STOP_DATE}</td>
								<td>${mtrblc.DISMANTLE_DATE}</td>
								<td>${mtrblc.LAST_PAID_DATE}</td>
								<td class="text-right">${mtrblc.VOLTAGE}</td>
								<td>${mtrblc.CTSUPCONDT}</td>							
								<td class="text-right">${mtrblc.REC_KWH}</td>
								<td class="text-right">${mtrblc.REC_KVAH}</td>
								<td class="text-right">${mtrblc.BKVAH}</td>
								<td class="text-right">${mtrblc.OB_OTHERTHAN_COURT}</td>
								<td class="text-right">${mtrblc.OB_COURT}</td>
								<td class="text-right">${mtrblc.TOT_OB}</td>
								<td class="text-right">${mtrblc.DEMAND_WITHOUT_COURT}</td>
								<td class="text-right">${mtrblc.COURT_LPC}</td>
								<td class="text-right">${mtrblc.DRJ}</td>
								<td class="text-right">${mtrblc.CRJ}</td>
								<td class="text-right">${mtrblc.COURT_RJ}</td>
								<td class="text-right">${mtrblc.COLLECTION}</td>
								<td class="text-right">${mtrblc.CB_OTHERTHAN_COURT}</td>
								<td class="text-right">${mtrblc.CB_COURT}</td>
								<td class="text-right">${mtrblc.TOTAL_CB}</td>
								<td class="text-right">${mtrblc.CB_SD}</td>
								<td>${mtrblc.TYPE_OF_SERVICE}</td>



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
		 var currnetMonth=(new Date()).getMonth();
		 $("#hyear").val(currentYear);
		 $("#hmon").val($("#mon").val());
		 
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'Services Wise ledger Closing Balance',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Services Wise ledger Closing Balance',footer: true }
	            ]
	        }
	    });
	});
</script>