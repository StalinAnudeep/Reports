<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="oldcolonyrdngtu" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT112A</span> - Old Colony True Up Charges (2014-2019)</strong></h3>
                  <div class="row">
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">GET Old Colony True Up Charges</label>
                        <button type="submit" class="btn btn-success">GET Old Colony True Up Charges</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(error)}">
		<div id="exist" class="alert alert-danger" role="alert">${error}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(trueup)}">
			<div class="card ">	
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
						<tr>
							<th>S.NO</th>
							<th>Division</th>
							<th>Sub<br>Division
							</th>
							<th>Section</th>
							<th>SC.NO</th>
							<th>NAME</th>
							<th>TOT SUM REC_KVAH</th>
							<th>RATE</th>
							<th>TOTAL AMOUNT</th>
						</tr>
					</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${trueup}"  varStatus="tagStatus">
							<tr>
						       <td>${tagStatus.index + 1}</td>
								<td>${mtrblc.DIVNAME}</td>
								<td>${mtrblc.SUBNAME}</td>
								<td>${mtrblc.SECNAME}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.CTNAME}</td>
                                <td class="text-right">${mtrblc.REC_KVAH}</td>
                                <td class="text-right">${mtrblc.RATE}</td>
                                <td class="text-right"> ${mtrblc.TOT_AMT}</td>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'Email_Check_List' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Email_Check_List'}
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>
