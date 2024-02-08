<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="SolarEnergyReport" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT70</span> - Solar Energy Report </strong></h3>
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
                     <div class="col-md-3">
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
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Year</label>
                       <select class="form-control" name="year" required="required" id="year">
					      	<option value="">--Select Year-- </option>
					    </select>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">GET Solar Energy Report</label>
                        <button type="submit" class="btn btn-success">GET Solar Energy Report </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(ser)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>USCNO</th>
						<th>NAME</th>
						<th>CAT</th>
						<th>SUBCAT</th>
						<th>MAIN METER ENERGY</th>
						<th>SOLAR PLANT CAPACITY</th>
						<th>SOLAR EXPORT ENERGY</th>
						<th>BILLED ENERGY</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${ser}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.CTUSCNO}</td>
							<td>${mtrblc.CTNAME}</td>
							<td>${mtrblc.CTCAT}</td>
							<td>${mtrblc.CTSUBCAT}</td>
							<td  class="text-right">${mtrblc.BTRKVAH_HT}</td>
							<td  class="text-right">${mtrblc.PLANT_CAP} - ${mtrblc.UNIT_OF_MEASURE} </td>
							<td  class="text-right"> ${mtrblc.BTBLSOLAR_HT}</td>
							<td  class="text-right">${mtrblc.BTBKVAH}</td>
						</tr>
					</c:forEach>
				</tbody>

					<tfoot>
						<tr>
							<th colspan="5" class="text-right">Grand Total</th>
							<th class="text-right">${ser.stream().map(mtrblc -> mtrblc.BTRKVAH_HT).sum()}</th>
							<th class="text-right">${ser.stream().map(mtrblc -> mtrblc.PLANT_CAP).sum()}</th>
							<th class="text-right">${ser.stream().map(mtrblc -> mtrblc.BTBLSOLAR_HT).sum()}</th>
							<th class="text-right">${ser.stream().map(mtrblc -> mtrblc.BTBKVAH).sum()}</th>

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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'Solar Energy Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Solar Energy Report'}
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>
		
