<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">

        <!-- </div> -->
	<c:if test="${ not empty fn:trim(error)}">
		<div id="exist" class="alert alert-danger" role="alert">${error}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(agewise)}">
		<div class="card ">
		<div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		<h2 class="text-center">Age Wise Arrears  Service Wise Report  </h2>
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<table class="table card-table table-vcenter text-nowrap datatable"
					style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>CIRCLE</th>
							<th>DIVISION</th>
							<th>SUB DIVISION</th>
							<th>SECTION</th>
							<th>SCNO</th>
							<th>NAME</th>
							<th>TYPE</th>
							<th>CTCAT</th>
							
							<th>AGE</th>
							<th>CC</th>
							<th>ED</th>
							<th>EDI</th>
							<th>LPC</th>
							<th>OTH</th>
							<th>CB</th>
							<th>LAST PAID MONTH</th>


						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${agewise}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.S_CIRNAME}</td>
								<td>${mtrblc.DIVNAME}</td>
								<td>${mtrblc.SUBNAME}</td>
								<td>${mtrblc.SECNAME}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.CTNAME}</td>
								<td>${mtrblc.TYPE}</td>
								<td>${mtrblc.CTCAT}</td>
								
								<td>${mtrblc.AGE}</td>
								<td>${mtrblc.CC}</td>
								<td>${mtrblc.ED}</td>
								<td>${mtrblc.EDI}</td>
								<td>${mtrblc.LPC}</td>
								<td>${mtrblc.OTH}</td>
								<td>${mtrblc.CB}</td>
								<td>${mtrblc.LAST_PAID_MONTH}</td>

							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
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
