<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
      <form class="card" action="Govt" method="post">
                <div class="card-body">
                  <h3 class="card-title">Govt_Services</h3>
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
                      <div class="form-group">
                        <label class="form-label">GET Govt_Services</label>
                        <button type="submit" class="btn btn-success">GET Govt_Services</button>
                      </div>
                    </div>
                  </div>
                  </div>
                 </form>
            
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(govt)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
					    <th>s.no</th>
					    <th>Div Name</th>
					    <th>Sub Name</th>
					    <th>Section Name</th>
					     <th>USCNO</th>
					     <th>Name</th>
					     <th>Address</th>
					     <th>Cat</th>
					     <th>CMD</th>
					     <th>Voltage <br>in<br>KV</th>
					     <th>Closing Balance</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${govt}" varStatus="tagStatus">
						<tr>
						    
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.DIVNAME}</td>
							<td>${mtrblc.SUBNAME}</td>
							<td>${mtrblc.SECNAME}</td>
							<td>${mtrblc.CTUSCNO}</td>
							<td>${mtrblc.CTNAME}</td>
							<td>${mtrblc.CTADD1}</td>
							<td>${mtrblc.CTCAT}</td>
							<td>${mtrblc.CTCMD_HT}</td>
							<td>${mtrblc.CTACTUAL_KV}</td>
							<td>${mtrblc.CBTOT}</td>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'Govt_Services' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Govt_Services'}
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>
		
