<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="Volopen" method="post">
                <div class="card-body">
                  <h3 class="card-title">Voltage Wise Open Access Energy Sales Report </h3>
                  <div class="row">
                     <div class="col-md-4">
					      <label for="inputState">Month</label>
					      <select id="mon" class="form-control" name="month" required="required">
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
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">Year</label>
                       <select class="form-control" name="year" required="required" id="year">
					      	<option value="">--Select Year-- </option>
					    </select>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">GET Voltage wise other Energy Sales report</label>
                        <button type="submit" class="btn btn-success">GET Voltage wise other Energy Sales report </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
			<c:if test="${ not empty fn:trim(open)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
					<thead>
						<tr>
						    <th>#</th>
							<th>OASCNO</th>
							<th>NAME</th>
							<th>CAT</th>
							<th>Volt</th>
							<th>Energy<BR>Source</th>
							<th>CMD</th>
							<th>REC_KVAH</th>
							<th>REC_KVA</th>
							<th>REC_PEAK_TOD</th>
							<th>REC_OFFPEAK_TOD</th>
							<th>OA <BR>KVAH <BR>Adjusted</th>
							<th>OA <BR>MD <BR>Adjusted if any</th>
							<th>OA <BR>PEAK<BR> TOD</th>
							<th>OA <BR>OFF <BR>PEAK TOD</th>
							<th>NET<BR> TOD</th>
							<th>CS<BR>CHARGES</th>
							<th>Monthly <BR>Minimum<BR> Energy</th>
							<th>Difference <BR>Between <BR>Rkvah & minimum energy</th>
							<th>Billed<BR>KAVH</th>
							<th>Billed <BR>MD</th>
							<th>Billed<BR> TOD</th>
							<th>WHEELING<BR>CASH</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${open}"  varStatus="tagStatus">
							<tr>
						       <td>${tagStatus.index + 1}</td>
								<td>${mtrblc.OAUSCNO}</td>
								<td>${mtrblc.CTNAME}</td>
								<td>${mtrblc.CTCAT}</td>
								<td class="text-right">${mtrblc.CTACTUAL_KV}</td>
								<td class="text-left">${mtrblc.ENG_SRC_TYPE}</td>
								<td class="text-right">${mtrblc.CTCMD_HT}</td>
								<td class="text-right">${mtrblc.RECKVAH}</td>
								<td class="text-right">${mtrblc.RMD}</td>
								<td class="text-right">${mtrblc.REC_PEAK_TOD}</td>
								<td class="text-right">${mtrblc.REC_OFFPEAK_TOD}</td>
								<td class="text-right">${mtrblc.KVAH_ADJ_ENG}</td>
								<td class="text-right">0</td>
								<td class="text-right">${mtrblc.TOD_ADJ_PEAK}</td>
								<td class="text-right">${mtrblc.TOD_ADJ_OFF}</td>
								<td class="text-right">${mtrblc.TOD_ADJ_ENG}</td>
								<td class="text-right">${mtrblc.CS_CHARGES}</td>
								<td class="text-right">${mtrblc.MINCHG}</td>
								<td class="text-right">${mtrblc.DIFFMIN}</td>
								<td class="text-right">${mtrblc.BTBKVAH}</td>
								<td class="text-right">${mtrblc.BTBLKVA_HT}</td>
								<td class="text-right">${mtrblc.BTTODCHG_HT}</td>
								<td class="text-right">${mtrblc.WHELL_CHARGES}</td>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'Voltage wise other Energy Sales report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Voltage wise other Energy Sales report'}
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>		