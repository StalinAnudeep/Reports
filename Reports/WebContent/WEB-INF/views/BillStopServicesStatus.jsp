<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<script>
requirejs([ 'jquery' ], function($) {
$(document).ready(function() {
	 var currentYear = (new Date()).getFullYear();
	 var currnetMonth=(new Date()).getMonth()+1;
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#fyear").append("<option value="+j+">"+j+"</option>");
	     	$("#tyear").append("<option value="+j+">"+j+"</option>");
	     }
		 $('#fmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#fyear option[value="'+currentYear+'"]').prop('selected', true);
		 
		/*  $('#tmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#tyear option[value="'+currentYear+'"]').prop('selected', true); */
});
});
</script>
<style>
.firstInput {
	width: 50%;
	display: inline-block;
	float: left;
	border-bottom-right-radius: 0;
	border-top-right-radius: 0;
}

.secondInput {
	width: 50%;
	display: inline-block;
	float: left;
	border-bottom-left-radius: 0;
	border-top-left-radius: 0;
	border-left: 0px;
}
.bg-light {
    background-color: #d8e4f154 !important;
}

.CellWithComment{
  position:relative;
}

.CellComment{
  display:none;
  position:absolute; 
  z-index:100;
  border:1px;
  background-color:white;
  border-style:solid;
  border-width:1px;
  border-color:#e81a40;
  padding:3px;
  color:#e81a40; 
  top:20px; 
  left:20px;
}

.CellWithComment:hover span.CellComment{
  display:block;
}
  thead>tr>th{
	color: #fff !important;
    font-weight: bold  !important;
}  
</style>

<div class="row row-cards row-deck">
	<form class="card" action="BillStopServicesStatus" method="post">
		<div class="card-body">
			<h3 class="card-title"><strong><span class="text-danger">HT02C</span> - Bill Stop Services Report</strong></h3>
			<div class="row">
				<!-- <div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div> -->
	
				<div class="col-md-4">
					<div class="form-group">
						<label for="firstInput">Month & Year</label>
						<div>
							<select class="form-control firstInput" name="fmonth"
								required="required" id="fmonth" ng-model="obj.month">
								<option value="">Month</option>
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
							</select> <select class="form-control secondInput" name="fyear"
								required="required" id="fyear" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">GET Report</label>
						<button type="submit" class="btn btn-success">GET
							Report</button>
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
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<table id="multiLevelTable" class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer" style="width: 100%;">
					<thead>
					<tr >
							<th class="bg-secondary text-center" colspan="12"><strong>${title}</strong></th>
							</tr>
						<tr class="text-white" >
							<th rowspan="2" class="bg-primary" style="vertical-align: middle;">Sr.No</th>
							<th rowspan="2" class="text-center bg-primary" style="vertical-align: middle;">Circle</th>
							<th colspan="2" class="text-center bg-info"  style="vertical-align: middle;">Opening Balance</th>
							<th colspan="2" class="text-center bg-primary">Added during the month</th>
							<th colspan="2" class="text-center bg-info">BS To LIVE</th>
							<th colspan="2" class="text-center bg-primary">Collected During The Month</th>
							<th colspan="2" class="text-center bg-info">Pending At The End Of The Month</th>
							
							
						</tr>
						<tr >
							<th class="text-center bg-info">NOS</th>
							<th class="text-center bg-info">Amount</th>
							
							<th class="text-center bg-primary">NOS</th>
							<th class="text-center bg-primary">Amount</th>
							
							<th class="text-center bg-info">NOS</th>
							<th class="text-center bg-info" >Amount</th>
							
							<th class="text-center bg-primary">NOS</th>
							<th class="text-center bg-primary">Amount</th>
							
							<th class="text-center bg-info">NOS</th>
							<th class="text-center bg-info">Amount</th>
							
						
						</tr>

					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${cdmd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								
								<td>${mtrblc.CIRCLE}</td>
								<td class="text-right">${mtrblc.BS_OB_NOS}</td>
								<td class="text-right">${mtrblc.BS_OB_AMT}</td>
								<td class="text-right">${mtrblc.BS_DURING_NOS}</td>
								
								<td class="text-right">${mtrblc.BS_DURING_AMT}</td>
								<td class="text-right">${mtrblc.LIVE_DURING_NOS}</td>
								<td class="text-right">${mtrblc.LIVE_DURING_AMT}</td>
								
								<td class="text-right">${mtrblc.BS_COLLECTED_NOS}</td>
								<td class="text-right">${mtrblc.BS_COLLECTED_AMT}</td>
								<td class="text-right">${mtrblc.BS_PENDING_NOS}</td>
								
								<td class="text-right">${mtrblc.BS_PENDING_AMT}</td>
								

							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BS_OB_NOS).sum()}</th>
<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BS_OB_AMT).sum()}</th>
<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BS_DURING_NOS).sum()}</th>

<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BS_DURING_AMT).sum()}</th>
<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.LIVE_DURING_NOS).sum()}</th>
<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.LIVE_DURING_AMT).sum()}</th>

<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BS_COLLECTED_NOS).sum()}</th>
<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BS_COLLECTED_AMT).sum()}</th>
<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BS_PENDING_NOS).sum()}</th>

<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BS_PENDING_AMT).sum()}</th>

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
		var currentYear = (new Date()).getFullYear();
		var currnetMonth = (new Date()).getMonth() + 1;
		for (var j = currentYear; j > 2015; j--) {
			$("#year").append("<option value="+j+">" + j + "</option>");
		}

		$('#mon option:eq(' + currnetMonth + ')').prop('selected', true);
		$('#year option[value="' + currentYear + '"]').prop('selected', true);

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

<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        "paging": false,
	        "ordering": false,
	        buttons: {
	            buttons: [
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'CB Split (Arrear, Demand Part)  Abstract',footer: true }
	            ]
	        }
	    });
	});
</script>


<jsp:include page="footer.jsp"></jsp:include>