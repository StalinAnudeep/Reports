<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="acdbalanceabstract" method="post">
		<div class="card-body">
			<h3 class="card-title">ACD Balance Abstract</h3>
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
					<div class="form-group">
						<label for="inputState">LEVI MONTH</label> <select id="mon"
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
				<div class="col-md-3">
					<div class="form-group">
					      <label for="inputZip">Select Year</label>
					      <select  class="form-control" name="year" id="year">
					      <option value="">--Select Year--</option>
					      </select>
			    </div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">GET ACD Balance Abstract</label>
						<button type="submit" class="btn btn-success">GETACD Balance Abstract</button>
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
							<th>CIRCLE</th>
							<th class="text-center">GOVT<BR>SCS</th>
							<th class="text-center">GOVT<BR>ACD<BR>DEM</th>
							<th class="text-center">GOVT<BR>PAY<BR>ACD</th>
							<th class="text-center">GOVT<BR>RJ<BR></th>
							<th class="text-center">GOVT<BR>CLOSING<BR>ACD</th>
							<th class="text-center">PVT<BR>SCS</th>
							<th class="text-center">PVT<BR>ACD<BR>DEM</th>
							<th class="text-center">PVT<BR>PAY<BR>ACD</th>
							<th class="text-center">PVT<BR>RJ<BR>ACD</th>
							<th class="text-center">PVT<BR>CLOSING<BR>ACD</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${acd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRCLE}</td>
								<td class="text-center">${mtrblc.GOVT_SCS}</td>
								<td class="text-center">${mtrblc.GOVT_ACDDEM}</td>
								<td class="text-center">${mtrblc.GOVT_PAYACD}</td>
								<td class="text-center">${mtrblc.GOVT_ACDRJ}</td>
								<td class="text-center">${mtrblc.GOVT_CLOSINGACD}</td>
								<td class="text-center">${mtrblc.PVT_SCS}</td>
								<td class="text-center">${mtrblc.PVT_ACDDEM}</td>
								<td class="text-center">${mtrblc.PVT_PAYACD}</td>
								<td class="text-center">${mtrblc.PVT_ACDRJ}</td>
								<td class="text-center">${mtrblc.PVT_CLOSINGACD}</td>
							</tr>
						</c:forEach>
					</tbody>
        			  <tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.GOVT_SCS).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.GOVT_ACDDEM).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.GOVT_PAYACD).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.GOVT_ACDRJ).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.GOVT_CLOSINGACD).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.PVT_SCS).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.PVT_ACDDEM).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.PVT_PAYACD).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.PVT_ACDRJ).sum()}</th>
							<th class="text-center">${acd.stream().map(mtrblc -> mtrblc.PVT_CLOSINGACD).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	</div>
	
<script>
	requirejs([ 'jquery' ], function($) {
		/*  $("#circle").append("<option value=ALL>ALL</option>"); */
		
		 $("#circle").append("<option value='CPDCL'>CPDCL</option>"); 
		 var currentYear = (new Date()).getFullYear();
		 var currnetMonth=(new Date()).getMonth()+1;
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'ACD_Balance_Abstract',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'ACD_Balance_Abstract',footer: true }
	            ]
	        }
	    });
	});
</script>