<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<jsp:include page="header.jsp"></jsp:include>
		
	    <div class="row row-cards row-deck">
	      <form class="card" action="SingleLineIsdReport" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT73</span> - Single Line ISD Report</strong></h3>
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
                        <label class="form-label">Year</label>
                       <select id="inputyear" class="form-control" name="year" required="required">
					      <option value="">--Select Financial Year--</option>
					      </select>
                      </div>
                    </div>
                    <div class="col-sm-6 col-md-4">
                      <div class="form-group">
                        <label class="form-label">Get ISD</label>
                        <button type="submit" class="btn btn-success">Get ISD SINGLE LINE</button>
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
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>SCNO</th>
						<th>Category</th>
						<th>FIN YEAR</th>
						<th>OPENING SD</th>
						<th>PAID SD</th>
						<th>ADJ SD</th>
						<th>CLOSING SD</th>
						<th>TOT ISD</th>
						<th>TDS AMT</th>
						<th>NET ISD</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${single}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.ISCNO}</td>
							<td>${mtrblc.CTCAT}</td>
							<td>${mtrblc.FIN_YEAR}</td>
							<td>${mtrblc.OB_SD}</td>
							<td>${mtrblc.PAID_ACD}</td>
							<td>${mtrblc.RJ_SD}</td>
							<td>${mtrblc.CB_SD}</td>
							<td>${mtrblc.TOT_ISD}</td>
							<td>${mtrblc.TDS_AMT}</td>
							<td>${mtrblc.NET_ISD}</td>
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
		for (var j = currentYear; j > 2020; j--) {
			var jj = j - 1 + "-" + j;
			$("#inputyear").append("<option value="+jj+">" + jj + "</option>");
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'ISD_Details' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'ISD_Details' }
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>