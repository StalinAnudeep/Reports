<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="PanCheckList" method="post">
                <div class="card-body">
                  <h3 class="card-title">HT PAN Check List</h3>
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">GET PAN Check List</label>
                        <button type="submit" class="btn btn-success">GET PAN Check List</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
	<c:if test="${ not empty fn:trim(error)}">
		<div id="exist" class="alert alert-danger" role="alert">${error}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(pan)}">
			<div class="card ">	
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
					<thead>
						<tr>
						    <th>#</th>
							<th>SCNO</th>
							<th>NAME_OF_CONSUMER</th>
							<th>Category</th>
							<th>SubCategory</th>
							<th>PAN NUMBER</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${pan}"  varStatus="tagStatus">
							<tr>
						       <td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CTUSCNO}</td>
								<td>${mtrblc.CTNAME}</td>
								<td>${mtrblc.CTCAT}</td>
								<td>${mtrblc.CTSUBCAT}</td>
								<td>${mtrblc.PER_ID_NBR}</td>
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
					$("#circle").append("<option value=ALL>ALL</option>");
				});
		 var currentYear = (new Date()).getFullYear();
		 var currnetMonth=(new Date()).getMonth()+1;
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#year").append("<option value="+j+">"+j+"</option>");
		 }
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'Pan_Check_List' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Pan_Check_List'}
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>
