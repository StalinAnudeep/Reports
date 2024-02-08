<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="HODTypeCollection" method="post">
		<div class="card-body">
			<h3 class="card-title"><strong><span class="text-danger">HT106</span> - HOD Type Collection Abstract</strong></h3>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div>
				<div class="col-md-3">
					<label class="form-label">Month</label> <select id="mon"
						class="form-control" name="month" required="required">
						<option value="">Select</option>
						<option value="JAN">JANUARY</option>
						<option value="FEB">FEBRUARY</option>
						<option value="MAR">MARCH</option>
						<option value="APR">APRIL</option>
						<option value="MAY">MAY</option>
						<option value="JUN">JUNE</option>
						<option value="JUL">JULY</option>
						<option value="AUG">AUGUST</option>
						<option value="SEP">SEPTEMBER</option>
						<option value="OCT">OCTOBER</option>
						<option value="NOV">NOVEMBER</option>
						<option value="DEC">DECEMBER</option>
					</select>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Year</label> <select
							class="form-control" name="year" required="required" id="year">
							<option value="">--Select Year--</option>
						</select>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">GET Report</label>
						<button type="submit" class="btn btn-success">GET Report</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(cdmd)}">
		    <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		     <h2 class="text-center">${title}</h2>
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S NO</th>
							<th>HOD CODE</th>
							<th>HOD Type</th>
							<th  class="text-center">NOS</th>
							<th class="text-right">Collection</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${cdmd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.GMDEPTCODE}</td>
								<td>${mtrblc.TYPE}</td>
								<%-- <td  class="text-center">${mtrblc.NOS}</td> --%>
								<td id="fd" class="text-center"><a href="services?servtype=${mtrblc.GMDEPTCODE}&monyear=${monyear}&circle=${circle}&flag=S&page=HC&type=${mtrblc.TYPE}" target="_blank">${mtrblc.NOS}</a></td>
								<td class="text-right">${mtrblc.COLLECTION}</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="3" class="text-right">Grand Total</th>
							<th id="fd"  class="text-center"><a href="services?servtype=${mtrblc.GMDEPTCODE}&monyear=${monyear}&circle=${circle}&flag=TOT&page=HC&type=ALL" target="_blank">${cdmd.stream().map(mtrblc -> mtrblc.NOS).sum()}</a></th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th>
						</tr>
					</tfoot>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: '${title}',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: '${title}',footer: true }
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
				      if($(this).attr('id') != 'fd'){
				    $(this).html(parseFloat($(this).text()).toLocaleString('en-IN', {style: 'decimal', currency: 'INR'})); 
				      }
				}
			}
				
				
			)
			
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>