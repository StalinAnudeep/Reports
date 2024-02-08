<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="subWiseDemand" method="post">
		<div class="card-body">
			<h3 class="card-title"><strong><span class="text-danger">HT27</span> -  SubDivision Wise Split Demand Report</strong></h3>
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
				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Year</label> <select
							class="form-control" name="year" required="required" id="year">
							<option value="">--Select Year--</option>
						</select>
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">GET  SubDivisionWiseSplitDemandReport</label>
						<button type="submit" class="btn btn-success">GET SubDivisionWiseSplitDemandReport</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(section)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>CIRCLE</th>
							<th>SUBNAME</th>
							<th>BMD</th>
							<th>BKVAH</th>
							<th>NORM DEM CHGS</th>
							<th>NORM ENG CHGS</th>
							<th>PENAL DEM CHGS</th>
							<th>PENAL ENG CHGS</th>
							<th>VOL CHG</th>
							<th>OTHCHG</th>
							<th>ED</th>
							<th>IED</th>
							<th>TOD CHGS</th>
							<th>COURT LPC</th>
							<th>NORMAL LPC</th>
							<th>CUST CHG</th>
							<th>ACD SURCHG</th>				
							<th>FSA</th>
							<th>TRNF CHG</th>
							<th>TRUE-UP CHG</th>
							<th>TOT DEMAND</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${section}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRNAME}</td>
								<td>${mtrblc.SUBNAME}</td>
								<td class="text-right">${mtrblc.BTBLKVA_HT}</td>
								<td class="text-right">${mtrblc.BTBKVAH}</td>
								<td class="text-right">${mtrblc.BTDEMCHG_NOR}</td>
								<td class="text-right">${mtrblc.BTENGCHG_NOR}</td>
								<td class="text-right">${mtrblc.BTDEMCHG_PEN}</td>
								<td class="text-right">${mtrblc.BTENGCHG_PEN}</td>
								<td class="text-right">${mtrblc.BTVOLTSURCHG}</td>
								<td class="text-right">${mtrblc.BTOTHERCHG}</td>
								<td class="text-right">${mtrblc.BTED}</td>
								<td class="text-right">${mtrblc.BTED_INT}</td>
								<td class="text-right">${mtrblc.BTTODCHG_HT}</td>
								<td class="text-right">${mtrblc.BTCOURT_LPC}</td>
								<td class="text-right">${mtrblc.BTADLCHG}</td>
								<td class="text-right">${mtrblc.BTACDSURCHG}</td>
								<td class="text-right">${mtrblc.BTCUSTCHG}</td>
								<td class="text-right">${mtrblc.BTFSACHG}</td>
								<td class="text-right">${mtrblc.BTDTRHIRE_CHG}</td>
								<td class="text-right">${mtrblc.BT_TU_CHG}</td>
								<td class="text-right">${mtrblc.BTCURDEM}</td>	
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="3" class="text-right">Grand Total</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTBLKVA_HT).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTBKVAH).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTDEMCHG_NOR).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTENGCHG_NOR).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTDEMCHG_PEN).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTENGCHG_PEN).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTVOLTSURCHG).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTOTHERCHG).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTED).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTED_INT).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTTODCHG_HT).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTCOURT_LPC).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTADLCHG).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTACDSURCHG).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTCUSTCHG).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTFSACHG).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTDTRHIRE_CHG).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BT_TU_CHG).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.BTCURDEM).sum()}</th>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: ' SubDivisionWiseSplitDemandReport',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'SubDivisionWiseSplitDemandReport',footer: true }
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