<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="SectionWiseArrears" method="post">
		<div class="card-body">
			<h3 class="card-title"><strong><span class="text-danger">HT26</span> - Section Wise Arrears Report</strong></h3>
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
						<label class="form-label">GET SectionWiseArrears</label>
						<button type="submit" class="btn btn-success">GET SectionWiseArrears</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(error)}">
		<div id="exist" class="alert alert-danger" role="alert">${error}</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(section)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>CIRCEL</th>
							<th>Division</th>
							<th>Sub Division</th>
							<th>Section</th>
							<th>Uscno</th>
							<th>Name</th>
							<th>Cat</th>
							<th>Sd</th>
							<th>Rkvah</th>
							<th>Bkvah</th>
							<th>Op Balance</th>
							<th>Demand</th>
							<th>Collection</th>
							<th>Journal</th>
							<th>CL Balance</th>
							<th>LED DATE</th>
							
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${section}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRCLE}</td>
								<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUBDIVISION}</td>
								<td>${mtrblc.SECTION}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.NAM}</td>
								<td>${mtrblc.CAT}</td>
								<td>${mtrblc.SD}</td>
								<td class="text-right">${mtrblc.REC_KVAH}</td>
								<td class="text-right">${mtrblc.MN_KVAH}</td>
								<td class="text-right">${mtrblc.OP_BALANCE}</td>
								<td class="text-right">${mtrblc.DEMAND}</td>
								<td class="text-right">${mtrblc.COLLECTION}</td>
								<td class="text-right">${mtrblc.JOURNAL}</td>
								<td class="text-right">${mtrblc.CL_BALANCE}</td>
								<td class="text-right">${mtrblc.MON_YEAR}</td>	
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							
							<th colspan="8" class="text-right">Grand Total</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.SD).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.REC_KVAH).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.MN_KVAH).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.OP_BALANCE).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.JOURNAL).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.CL_BALANCE).sum()}</th>
							<th></th>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'SectionWiseArrears',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'SectionWiseArrears',footer: true }
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