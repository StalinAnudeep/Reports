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
</style>

<div class="row row-cards row-deck">
	<form class="card" action="FeederData" method="post">
		<div class="card-body">
			<h3 class="card-title">
<strong><span class="text-danger">HT107</span> - Edgegrid - Feeder Data Report</strong></h3>
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
						<label class="form-label">Month & Year</label>
						<div>
							<select class="form-control firstInput" name="month"
								required="required" id="month" ng-model="obj.month">
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
							</select> <select class="form-control secondInput" name="year"
								required="required" id="year" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div>

				<div class="col-md-4">
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
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<table
					class="table  card-table table-vcenter text-nowrap datatable display"
					style="width: 100%;">
					<thead>
						<tr>
							<th>SR.NO</th>
							<th class="text-center">CIRCLE</th>
							<th class="text-center">DIVISION</th>
							<th class="text-center">SUB_DIVISION</th>
							<th class="text-center">SECTION</th>
							<th class="text-center">USCNO</th>
							<th class="text-center">NAME</th>
							<th class="text-center">PHONE NO</th>
							<th class="text-center">EMAIL</th>
							<th class="text-right">CAT</th>
							<th class="text-right">SUB-CAT</th>
							<th class="text-right">VOLTAGE</th>
							<th class="text-right">SOCIAL CAT</th>
							<th class="text-right">LOCATION</th>
							<th class="text-right">DTR NAME</th>
							<th class="text-right">FEEDER NAME</th>
							<th class="text-right">FEEDER CODE</th>
							<th class="text-right">CMD</th>
							<th class="text-right">RMD</th>
							<th class="text-right">PF</th>
							<th class="text-right">MONTH</th>
							<th class="text-right">OPEN RDG</th>
							<th class="text-right">CLOSE RDG</th>
							<th class="text-right">KWH UNITS</th>
							<th class="text-right">KVAH UNITS</th>
							<th class="text-right">OFF_PEAK</th>
							<th class="text-right">PEAK</th>
							<th class="text-right">EC</th>
							<th class="text-right">TOD CHG</th>
							<th class="text-right">CC</th>
							<th class="text-right">ED</th>
							<th class="text-right">SUR CHG</th>
							<th class="text-right">IED</th>
							<th class="text-right">DEMAND CHG</th>
							<th class="text-right">BILL AMT</th>
							<th class="text-right">DUE AMT</th>
							<th class="text-right">LAST PAY DATE</th>
							<th class="text-right">LAST PAY AMT</th>
							<th class="text-center">DUE DATE</th>
							<th class="text-center">DISCONNECTION DATE</th>


						</tr>
					</thead>
					<tbody>
	
						<c:forEach var="mtrblc" items="${cdmd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRCLE}</td>
								<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUB_DIVISION}</td>
								<td>${mtrblc.SECTION}</td>
								<td>${mtrblc.CTUSCNO}</td>
								<td>${mtrblc.NAME}</td>
								<td id="fd">${mtrblc.PHONE_NO}</td>
								<td>${mtrblc.EMAIL}</td>
								<td>${mtrblc.CAT}</td>
								<td class="text-right">${mtrblc.SUBCAT}</td>
								<td class="text-right">${mtrblc.VOLTAGE}</td>								
								<td class="text-right">${mtrblc.SOCCAT}</td>
								<td class="">${mtrblc.LOCATION}</td>
								<td class="text-right">${mtrblc.DTRNAME}</td>
								<td class="text-right">${mtrblc.FEEDER_NAME}</td>
								<td id="fd" class="text-right">${mtrblc.FEEDER_CD}</td>
								<td class="text-right">${mtrblc.CMD}</td>
								<td class="text-right">${mtrblc.RMD}</td>
								<td class="text-right">${mtrblc.PF}</td>
								<td class="text-right">${mtrblc.MONTH}</td>
								<td class="text-right">${mtrblc.OP_RDG}</td>
								<td class="text-right">${mtrblc.CL_RDG}</td>
								<td class="text-right">${mtrblc.KWH_UNITS}</td>
								<td class="text-right">${mtrblc.KVAH_UNITS}</td>
								<td class="text-right">${mtrblc.OFF_PEAK}</td>
								<td class="text-right">${mtrblc.PEAK}</td>
								<td class="text-right">${mtrblc.EC}</td>
								<td class="text-right">${mtrblc.TOD_CHG}</td>
								<td class="text-right">${mtrblc.CC}</td>
								<td class="text-right">${mtrblc.ED}</td>
								<td class="text-right">${mtrblc.SURCHG}</td>
								<td class="text-right">${mtrblc.IED}</td>
								<td class="text-right">${mtrblc.DEMAND_CHGRS}</td>
								<td class="text-right">${mtrblc.TOTAL_BILL_AMOUNT}</td>
								<td class="text-right">${mtrblc.AMT_DUE}</td>
								<td class="text-right">${mtrblc.LAST_PAY_DT}</td>
								<td class="text-right">${mtrblc.LAST_PAY_AMT}</td>
								<td class="text-center">${mtrblc.BTDUEDT}</td>
								<td class="text-center">${mtrblc.BTDISCDT}</td>
							</tr>
						</c:forEach>
					</tbody>
				<%-- 	<tfoot>
						<tr>
							<th colspan="3" class="text-right">Grand Total</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BTCURDEM).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.WC_BTCURDEM).sum()}</th>
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
		var currnetMonth = (new Date()).getMonth() + 1;
		for (var j = currentYear; j > 2015; j--) {
			$("#year").append("<option value="+j+">" + j + "</option>");
		}

		$('#month option:eq(' + currnetMonth + ')').prop('selected', true);
		$('#year option[value="' + currentYear + '"]').prop('selected', true);

	});
</script>
<script>
	require([ 'jquery', 'datatables.net', 'datatables.net-jszip',
			'datatables.net-buttons', 'datatables.net-buttons-flash',
			'datatables.net-buttons-html5' ], function($, datatable, jszip) {

		window.JSZip = jszip;
		$('.datatable').DataTable({
			dom : 'Bfrltip',
			"scrollX" : true,
			buttons : {
				buttons : [ {
					extend : 'csv',
					className : 'btn btn-xs btn-primary',
					title : 'DemandReport',
					footer : true
				}, {
					extend : 'excel',
					className : 'btn btn-xs btn-primary',
					title : 'DemandReport',
					footer : true
				} ]
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