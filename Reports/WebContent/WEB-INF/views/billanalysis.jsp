<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
		<form class="card" action="billanalysis" method="post">
			<div class="card-body">
				<h3 class="card-title"><strong><span class="text-danger">HT16</span> - HT Bill Analysis Report</strong></h3>
				<div class="row">
				    <div class="col-md-2">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
					<div class="col-md-2">
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
					<div class="col-md-5">
						<div class="form-group">
							<label class="form-label">GET HT Bill Analysis Report</label>
							<button type="submit" class="btn btn-success">GET HT Bill Analysis Report</button>
						</div>
					</div>
				</div>
			</div>
		</form>

		<c:if test="${ not empty fn:trim(fail)}">
			<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
		</c:if>
		<c:if test="${ not empty fn:trim(bill)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
						<thead>
							<tr>
								<th>S.NO</th>
								<th>DATE</th>
								<th>CAT</th>
								<th>CMD</th>
								<th>VOL in <br>kva</th>
								<th>USCNO</th>
								<th>RKWH</th>
								<th>RKVAH</th>
								<th>BKVAH</th>
								<th>TOD_UNITS</th>
								<th>DEMAND</th>
								<th>ENG_CHG</th>
								<th>TOD_CHG</th>
								<th>ED</th>
								<th>COL_CHG</th>
								<th>PEN__DEMAND_CHG</th>
								<th>ENG_PEN_CHG</th>
								<th>VOL_CHG</th>
								<th>AQUASUB_CHG</th>
								<th>LF_INCENTIVE_CHG</th>
								<th>CROSS_SUB_CHG</th>
								<th>ADD_CHAG</th>
								<th>ED_INT</th>
								<th>OTHERCHG</th>
								<th>TRHIRE_SGST</th>
								<th>TRHIRE_CGST</th>
								<th>ACDSURCHG</th>
								<th>CUSTCHG</th>
								<th>WHEELCHGCASH</th>
								<th>TRUE-UP CHG</th>
								<th>CCLPC</th>
								<th>TOTAL_DEMAND</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${bill}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.BTBLDT}</td>
									<td>${mtrblc.BTBLCAT}</td>
									<td>${mtrblc.BTCMD_HT}</td>
									<td>${mtrblc.BTACTUAL_KV}</td>
									<td>${mtrblc.BTSCNO}</td>
									<td class="text-right">${mtrblc.BTRKWH_HT}</td>
									<td class="text-right">${mtrblc.BTRKVAH_HT}</td>
									<td class="text-right">${mtrblc.BTBKVAH}</td>
									<td class="text-right">${mtrblc.TOD_UNITS}</td>
									<td class="text-right">${mtrblc.BTDEMCHG_NOR}</td>
									<td class="text-right">${mtrblc.BTENGCHG_NOR}</td>
									<td class="text-right">${mtrblc.BTTODCHG_HT}</td>
									<td class="text-right">${mtrblc.BTED}</td>
									<td class="text-right">${mtrblc.BTCOLNYCHG_HT}</td>
									<td class="text-right">${mtrblc.BTDEMCHG_PEN}</td>
									<td class="text-right">${mtrblc.BTENGCHG_PEN}</td>
									<td class="text-right">${mtrblc.BTVOLTSURCHG}</td>
									<td class="text-right">${mtrblc.BTAQUASUB_CHG}</td>
									<td class="text-right">${mtrblc.BTLFINCENTIVE_HT}</td>
									<td class="text-right">${mtrblc.BTCROSSSUBCHG}</td>
									<td class="text-right">${mtrblc.BTADLCHG}</td>
									<td class="text-right">${mtrblc.BTED_INT}</td>
									<td class="text-right">${mtrblc.BTOTHERCHG}</td>
									<td class="text-right">${mtrblc.BTDTRHIRE_SGST}</td>
									<td class="text-right">${mtrblc.BTDTRHIRE_CGST}</td>
									<td class="text-right">${mtrblc.BTACDSURCHG}</td>
									<td class="text-right">${mtrblc.BTCUSTCHG}</td>
									<td class="text-right">${mtrblc.BTWHEELCHGCASH_HT}</td>
									<td class="text-right">${mtrblc.BT_TU_CHG}</td>
									<td class="text-right">${mtrblc.BTCOURT_LPC}</td>
									<td class="text-right">${mtrblc.BTCURDEM}</td>
								</tr>
							</c:forEach>
						</tbody>
						<%-- <tfoot>
						<tr>
							<th colspan="2">Grand Total</th>
							<th class="text-center">${email.stream().map(mtrblc -> mtrblc.CTEMAILID).sum()}</th>
							<th class="text-center">${email.stream().map(mtrblc -> mtrblc.EMAIL).sum()}</th>
						</tr>
						</tfoot> --%>
					</table>
				</div>
			</div>
		</c:if>
</div>
<script>
	requirejs([ 'jquery' ], function($) {
		$(document).ready(function() {
			
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
			
			
			
			 var currentYear = (new Date()).getFullYear();
			 var currnetMonth=(new Date()).getMonth()+1;
				 for (var j = currentYear; j > 2015; j--) {
			     	$("#year").append("<option value="+j+">"+j+"</option>");
			     }
				 $('#mon option:eq('+currnetMonth+')').prop('selected', true);
				 $('#year option[value="'+currentYear+'"]').prop('selected', true);
		});

	});
</script>

<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
	
		window.JSZip = jszip;
		$('.datatable').DataTable({
			"scrollX": true,
	        dom: 'Bfrltip',
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'HT Bill Analysis Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'HT Bill Analysis Report' }
	            ]
	        }
	    });
	});
</script>


<jsp:include page="footer.jsp"></jsp:include>