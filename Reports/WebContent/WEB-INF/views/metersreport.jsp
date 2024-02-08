	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>

<div class="row row-cards row-deck">
	      <form class="card" action="metersreport" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT115</span> -  Existing Meter Details Entry  Report</strong></h3>
                  <div class="row">
                    <div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
						</select>
					</div>
				</div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Get Details</label>
                        <button type="submit" class="btn btn-success">Get Details</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>

<c:if test="${ not empty fn:trim(bmd)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th rowspan="2">S.NO</th>
						<th rowspan="2">CIRCLE</th>
						<th rowspan="2">DIVISION</th>
						<th rowspan="2">SUB-DIVISION</th>
						<th colspan="2">TOT SCS</th>
						<th colspan="2">DETAILS SENT BY <BR>ADE/OP </th>
						<th colspan="2">DETAILS confirmed BY <BR>ADE/HT</th>	
						<th colspan="2">PENDING TO BE SENT BY <br> ADE/OP </th>
						<th colspan="2">PENDING TO BE CONFIRMED <br> ADE/HT</th>					
					</tr>
					<tr>
					<th>LT</th>
					<th>HT</th>
					<th>LT</th>
					<th>HT</th>
					<th>LT</th>
					<th>HT</th>
					<th>LT</th>
					<th>HT</th>
					<th>LT</th>
					<th>HT</th>

					
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${bmd}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.CIRNAME}</td>
							<td>${mtrblc.DIVNAME}</td>
							<td>${mtrblc.SUBNAME}</td>
							<td></td>
							<td>${mtrblc.TOT_SCS}</td>
							<td></td>
							<td>${mtrblc.ADE_CRM_SCS}</td>
							<td></td>
							<td>${mtrblc.MRT_CRM_SCS}</td>
							<td></td>
							<td>${mtrblc.TOT_SCS - mtrblc.ADE_CRM_SCS}</td>
							<td></td>
							<td>${mtrblc.ADE_CRM_SCS - mtrblc.MRT_CRM_SCS}</td>

						</tr>
					</c:forEach>
				</tbody>
		<%-- 		<tfoot>
						<tr>
							<th colspan="4" class="text-right">Grand Total</th>
							<th class="text-right">${bmd.stream().map(mtrblc -> mtrblc.TOT_SCS).sum()}</th>
							<th class="text-right">${bmd.stream().map(mtrblc -> mtrblc.ADE_CRM_SCS).sum()}</th>
							<th class="text-right">${bmd.stream().map(mtrblc -> mtrblc.MRT_CRM_SCS).sum()}</th>
						</tr>
					</tfoot> --%>
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
	
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'Existing Meter Details Entry  Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Existing Meter Details Entry  Report'}
	            ]
	        }
	    });
	});
</script>
<script> 
	requirejs([ 'jquery' ], function($) {
			$("td,th").each(function() { 
				if ($.isNumeric( $(this).text())) {
				    // It isn't a number	
				    $(this).html(parseFloat($(this).text()).toLocaleString('en-IN', {style: 'decimal', currency: 'INR'})); 
				}
			}
				
				
			)
			
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>