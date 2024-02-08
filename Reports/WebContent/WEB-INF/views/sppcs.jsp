<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
 <div class="card-body">
                  <h3 class="card-title">Startup Power Services Plant Capacity</h3>
</div>
<c:if test="${ not empty fn:trim(error)}">
		<div id="exist" class="alert alert-danger" role="alert">${error}</div>
</c:if>
<c:if test="${ not empty fn:trim(sppcs)}">
			<div class="card ">	
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>USNO</th>
						<th>GENERAL<br>TYPE</th>
						<th>PLANT<br>CAPACITY</th>
						<th>GENERAL<br>UNITS</th>
						<th>MAXIMUM<br>CAPACITY</th>
						<th>ALLOWABLE<br>MDPERC</th>
						<th>ALLOWABLE<br>MDKVA</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${sppcs}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.USCNO}</td>
							<td>${mtrblc.GEN_TYP}</td>
							<td>${mtrblc.PLANT_CAP}</td>
							<td>${mtrblc.GEN_UNITS}</td>
							<td>${mtrblc.MAX_CAP}</td>
							<td>${mtrblc.ALLW_MDPERC}</td>
							<td>${mtrblc.ALLW_MDKVA}</td>
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
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'Startup Power Services Plant Capacity' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Startup Power Services Plant Capacity'}
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>
