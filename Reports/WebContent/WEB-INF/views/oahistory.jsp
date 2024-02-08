<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<script>
requirejs([ 'jquery' ], function($) {
$(document).ready(function() {
	 var currentYear = (new Date()).getFullYear();
	 var currnetMonth=(new Date()).getMonth()+1;
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#fyear").append("<option value="+j+">"+j+"</option>");
	     	$("#tyear").append("<option value="+j+">"+j+"</option>");
	     }
		 $('#fmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#fyear option[value="'+currentYear+'"]').prop('selected', true);
		 
		 $('#tmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#tyear option[value="'+currentYear+'"]').prop('selected', true);
});
});
</script>
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
.bg-light {
    background-color: #d8e4f154 !important;
}

.CellWithComment{
  position:relative;
}

.CellComment{
  display:none;
  position:absolute; 
  z-index:100;
  border:1px;
  background-color:white;
  border-style:solid;
  border-width:1px;
  border-color:#e81a40;
  padding:3px;
  color:#e81a40; 
  top:20px; 
  left:20px;
}

.CellWithComment:hover span.CellComment{
  display:block;
}
</style>


<div class="row row-cards row-deck">

	<form class="card" action="oahistory" method="post" id="form">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT118</span> - Old Open Access History </strong>
			</h3>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label for="inputCity">Enter Service
								Number</label> <input type="text" class="form-control"
							required="required" name="scno" id="scno"
							placeholder="Enter Service Number">
					</div>
				</div>		
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Get Open Access History</label>
						<button type="submit" class="btn btn-success">Get Open Access History</button>
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
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th class="text-center">S.NO</th>
						<th class="text-center">NAME</th>
						<th class="text-center">USCNO</th>
						<th class="text-center">MON YEAR</th>
						<th class="text-center">ENG_SRC_TYPE</th>
						<th class="text-center">KVAH_ALLOCATED_ENG</th>
						<th class="text-center">KVAH_ADJ_ENG</th>
						<th class="text-center">TOD_ALLOCATED_ENG</th>
						<th class="text-center">TOD_ADJ_ENG</th>
						<th class="text-center">TOD_ALLOCATED_PEAK</th>
					<th class="text-center">TOD_ADJ_PEAK</th>
					<th class="text-center">TOD_ALLOCATED_OFF</th>
					<th class="text-center">TOD_ADJ_OFF</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${account}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							        <td class="text-center">${mtrblc.CTNAME}</td>
									<td class="text-center">${mtrblc.USCNO}</td>
									<td class="text-center">${mtrblc.MON_YEAR}</td>
									<td class="text-right">${mtrblc.ENG_SRC_TYPE}</td>
									<td class="text-right">${mtrblc.KVAH_ALLOCATED_ENG}</td>
									<td class="text-right">${mtrblc.KVAH_ADJ_ENG}</td>
									<td class="text-right">${mtrblc.TOD_ALLOCATED_ENG}</td>
									<td class="text-right">${mtrblc.TOD_ADJ_ENG}</td>
									<td class="text-right">${mtrblc.TOD_ALLOCATED_PEAK}</td>
									<td class="text-right">${mtrblc.TOD_ADJ_PEAK}</td>
									<td class="text-right">${mtrblc.TOD_ALLOCATED_OFF}</td>
									<td class="text-right">${mtrblc.TOD_ADJ_OFF}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</c:if>
</div>



<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
	
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'ISD_Details' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'ISD_Details' }
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>

