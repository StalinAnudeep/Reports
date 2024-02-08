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
		<h2 class="text-center">${title} </h2>
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<table class="table card-table table-vcenter text-nowrap datatable"
					style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>USCNO</th>
							<th>NAME</th>
							<th>LOAD</th>
							<th>REC MD</th>
							<th>OB</th>
							<th>SALES</th>
							<th>DEMAND</th>
							<th>DRJ</th>
							<th>TOTAL DEMAND</th>
							<th>COLL ARREAR</th>
							<th>COLL DEMAND</th>
							<th>CRJ</th>
							<th>TOTAL COLL </th>
														
							<th>CB</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${agewise}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td class="text-left">${mtrblc.CTUSCNO}</td>
								<td class="text-left">${mtrblc.CTNAME}</td>
								<td class="text-right">${mtrblc.LOAD}</td>
								<td class="text-right">${mtrblc.REC_MD}</td>
								<td class="text-right">${mtrblc.OB}</td>
								<td class="text-right">${mtrblc.SALES}</td>
								<td class="text-right">${mtrblc.DEMAND}</td>
								<td  class="text-right">${mtrblc.DRJ}</td>
								<td  class="text-right">${mtrblc.DEMAND+mtrblc.DRJ}</td>
								<td class="text-right">${mtrblc.COLL_ARREAR}</td>
								<td class="text-right">${mtrblc.COLL_DEMAND}</td>
								<td  class="text-right">${mtrblc.CRJ}</td>
								<td class="text-right">${mtrblc.COLLECTION + mtrblc.CRJ}</td>
								<td  class="text-right">${mtrblc.CB}</td>

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
