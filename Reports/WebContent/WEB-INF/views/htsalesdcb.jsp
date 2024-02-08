<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<jsp:include page="header.jsp"></jsp:include>
		
	    <div class="row row-cards row-deck">
	      <form class="card" action="HtSalesDCB" method="post">
                <div class="card-body">
                  <h3 class="card-title"> <strong><span class="text-danger">HT02A</span> - HT Year Wise Cat,Sub-Cat Wise Sales Dcb Abstract</strong></h3>
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
                        <label class="form-label">Year</label>
                       <select id="inputyear" class="form-control" name="year" required="required">
					      <option value="">Select Financial Year</option>
					      </select>
                      </div>
                    </div>
                      <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Cat/Sub Cat</label>
                       <select id="cat" class="form-control" name="cat" required="required">
					      <option value="">Select Cat/Sub Cat</option>
					      <option value="CAT">Category</option>
					      <option value="SUBCAT">Sub Category</option>
					      </select>
                      </div>
                    </div>
                    <div class="col-sm-6 col-md-3">
                      <div class="form-group">
                        <label class="form-label">GET HT SALES DCB </label>
                        <button type="submit" class="btn btn-success">GET HT SALES DCB </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(single)}">
		<div class="card ">
		<div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		<h2 class="text-center">${title}</h2>
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr class="bg-primary" style="color:#fff;" >
						<td><strong>S.NO</strong></td>
						<td><strong>Category</strong></td>
						<c:if test="${CAT eq 'SUBCAT'}">
						<td><strong>Sub Category</strong></td>
						</c:if>
						<td><strong>No.Services</strong></td>
						<td><strong>Sales(RKWH)(MU)</strong></td>
						<td><strong>Sales(BKVAH)(MU)</strong></td>
						<td><strong>OB(Cr)</strong></td>
						<td><strong>DEM(Cr)</strong></td>				
						<td><strong>Collection(Cr)</strong></td>
						<td><strong>CB(Cr)</strong></td>
						

						
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${single}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.CATEGORY}</td>
							<c:if test="${CAT eq 'SUBCAT'}">
						<td>${mtrblc.CTSUBCAT}</td>
						</c:if>
							<td  class="text-right">${mtrblc.NOS}</td>
							<td  class="text-right">${mtrblc.REC_KWH}</td>	
							<td  class="text-right">${mtrblc.MN_KVAH}</td>	
							<td  class="text-right">${mtrblc.OB}</td>
							<td  class="text-right">${mtrblc.DEM}</td>
							<td  class="text-right">${mtrblc.COLL}</td>
													
							<td  class="text-right">${mtrblc.CB}</td>


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
		$("#circle").append("<option value=ALL>ALL</option>");
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

		/* var currentYear = (new Date()).getFullYear();
		for (var j = currentYear; j > 2015; j--) {
			var jj = j - 1 + "-" + j;
			$("#inputyear").append("<option value="+jj+">" + jj + "</option>");
		} */
		
		 var currentYear = new Date().getFullYear();
			for(var i = 0; i < 7; i++){
			  var next = currentYear+1;
			  var year = currentYear + '-' + next.toString();
			  $('#inputyear').append(new Option(year, year));
			  currentYear--;
			}
	});
</script>

<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
	
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: '${title}' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: '${title}' }
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>