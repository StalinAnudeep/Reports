<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
		<form class="card" action="demandsplit" method="post">
			<div class="card-body">
				<h3 class="card-title"><strong><span class="text-danger">HT32</span> - Demand Split Report</strong></h3>
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
						<label for="inputState">Month</label> <select id="mon"
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
							<label class="form-label">GET Demand Split Report</label>
							<button type="submit" class="btn btn-success">Demand Split Report</button>
						</div>
					</div>
				</div>
			</div>
		</form>

		<c:if test="${ not empty fn:trim(fail)}">
			<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
		</c:if>
		<c:if test="${ not empty fn:trim(demand)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
						<thead>
							<tr>
								<th>S.NO</th>
								<th>DATE</th>
								<th>USCNO</th>
								<th>CAT</th>
								<th>VOL in <br>kva</th>
								<th>BMD</th>
								<th>BKVAH</th>
								<th>NORM DEM CHGS</th>
								<th>NORM ENG CHGS</th>
								<th>PENAL DEM CHGS</th>
								<th>PENAL ENG CHGS</th>
								<th>VOL CHG</th>
								<th>OTHCHG</th>
								<th>ED</th>
								<th>EDI</th>
								<th>TOD CHG</th> 
								<th>COURT LPC</th>
								<th>NORMAL LPC</th>
								<th>ACD SURCHG</th>
								<th>CUSTCHG</th>
								<th>FSA</th>
								<th>TRUE UP CHG</th>
								<th>TRNF</th>
								<th>TOTAL_DEMAND</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${demand}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.BTBLDT}</td>
									<td>${mtrblc.BTSCNO}</td>
									<td>${mtrblc.BTBLCAT}</td>
									<td class="text-right">${mtrblc.BTACTUAL_KV}</td>
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
									<td class="text-right">${mtrblc.BT_TU_CHG}</td>
									<td class="text-right">${mtrblc.BTDTRHIRE_CHG}</td>
									<td class="text-right">${mtrblc.BTCURDEM}</td>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'Demand Split Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'Demand Split Report' }
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