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
	<form class="card" action="HtSalesYearly" method="post">
		<div class="card-body">
			<h3 class="card-title">
<strong><span class="text-danger">HT02A</span> - HT Sales Yearly Wise(Transco)</strong></h3>
			<div class="row">
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div>
	
				 <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">Year</label>
                       <select id="inputyear" class="form-control" name="year" required="required">
					      <option value="">Select Financial Year</option>
					      </select>
                      </div>
                    </div>
				<!-- <div class="col-md-3">
					<div class="form-group">
						<label for="firstInput">To</label>
						<div>
							<select class="form-control firstInput" name="tmonth"
								required="required" id="tmonth" ng-model="obj.month">
								<option value="">Month</option>
								<option value="01">JANUARY</option>
								<option value="02">FEBRUARY</option>
								<option value="03">MARCH</option>
								<option value="04">APRIL</option>
								<option value="05">MAY</option>
								<option value="06">JUNE</option>
								<option value="07">JULY</option>
								<option value="08">AUGUST</option>
								<option value="09">SEPTEMBER</option>
								<option value="10">OCTOBER</option>
								<option value="11">NOVEMBER</option>
								<option value="12">DECEMBER</option>
							</select> <select class="form-control secondInput" name="tyear"
								required="required" id="tyear" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div> -->
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">GET HT Sales Yearly Wise</label>
						<button type="submit" class="btn btn-success">GET
							HT Sales Yearly Wise</button>
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
				<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>SR.NO</th>
							<th class="text-center">BMONTH</th>
						<!-- 	 <th class="text-center">CIRCLE</th> -->
							<!--<th class="text-center">DIVISION</th>
							<th class="text-center">SUB_DIVISION</th>
							<th class="text-center">SECTION</th>-->
							<th class="text-center">CAT</th>
							<th class="text-center">SUBCAT</th>
							<th class="text-center">DESCRIPT</th> 
							<th class="text-right">VOLTAGE</th>
							<th class="text-right">NOS</th>
							<th class="text-right">CMD</th>
							<th class="text-right">RMD</th>
							<th class="text-right">REC_KWH_UNITS</th>
							<th class="text-right">REC_KVAH_UNITS</th>
							<th class="text-right">BILLED_KVAH_UNITS</th>
							<th class="text-right">OFF_PEAK_UNITS</th>
							<th class="text-right">PEAK_UNITS</th>
							<th class="text-right">COLONY_UNITS</th>
							<th class="text-right">ECHG</th>
							<th class="text-right">FCHG</th>
							<th class="text-right">CCHG</th>
							<th class="text-right">EDCHG</th>
							<th class="text-right">DEMAND_CHGS</th>
							<th class="text-right">COLL</th>
							<th class="text-right">INCENTIVE_AMT</th>
							<th class="text-right">VOLTAGE_SURCHARGE_AMT</th>


						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${cdmd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.BILL_DT}</td>
							<%-- 	 <td>${mtrblc.CIRCLE}</td> --%>
								<%--<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUB_DIVISION}</td>
								<td>${mtrblc.SECTION}</td>--%>
								<td>${mtrblc.CAT}</td>
								<td>${mtrblc.SUBCAT}</td>
								<td>${mtrblc.DESCRIPT}</td> 
								<td>${mtrblc.VOLTAGE}</td>
								<td class="text-right">${mtrblc.NOS}</td>
								<td class="text-right">${mtrblc.CMD}</td>
								<td class="text-right">${mtrblc.RMD}</td>
								<td class="text-right">${mtrblc.REC_KWH}</td>
								<td class="text-right">${mtrblc.REC_UNITS}</td>
								<td class="text-right">${mtrblc.BILLED_UNITS}</td>
								<td class="text-right">${mtrblc.OFF_PEAK_UNITS}</td>
								<td class="text-right">${mtrblc.PEAK_UNITS}</td>
								<td class="text-right">${mtrblc.COLONY_UNITS}</td>
								<td class="text-right">${mtrblc.ECHG}</td>
								<td class="text-right">${mtrblc.FCHG}</td>
								<td class="text-right">${mtrblc.CCHG}</td>
								<td class="text-right">${mtrblc.EDCHG}</td>
								<td class="text-right">${mtrblc.DEMAND_CHGS}</td>
								<td class="text-right">${mtrblc.COLL}</td>
								<td class="text-right">${mtrblc.INCENTIVE_AMT}</td>
								<td class="text-right">${mtrblc.VOLTAGE_SURCHARGE_AMT}</td>
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
			for (var j = currentYear+1; j > 2019; j--) {
				var jj = j - 1 + "-" + j;
				$("#inputyear").append("<option value="+jj+">" + jj + "</option>");
			}

		$('#mon option:eq(' + currnetMonth + ')').prop('selected', true);
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
				    $(this).html(parseFloat($(this).text()).toLocaleString('en-IN', {style: 'decimal', currency: 'INR'})); 
				}
			}
				
				
			)
			
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>