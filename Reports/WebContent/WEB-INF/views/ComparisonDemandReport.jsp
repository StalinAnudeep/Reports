<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="ComparisonDemandReport" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT11</span> - Comparison Demand Report</strong> </h3>
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
                        <label class="form-label">GET Comparison Demand Report</label>
                        <button type="submit" class="btn btn-success">GET Comparison Demand Report</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
			<c:if test="${ not empty fn:trim(cdr)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
					<thead>
						<tr>
						    <th>#</th>
							<th  class="text-center">DIV</th>
							<th class="text-center">USCNO</th>
							<th class="text-center">LOAD</th>
							<th class="text-center">CAT</th>
							<th class="text-center">SUB-CAT</th>
							<th class="text-center">CMD(${monthYear})</th>
							<th class="text-center">BMD(${premonth})</th>
							<th class="text-center">ENGCHG(${monthYear})</th>
							<th class="text-center">ENGCHG(${premonth})</th>
							<th class="text-center">CMD(${monthYear})</th>
							<th class="text-center">CMD(${premonth})</th>
							<th class="text-center">CMD DIFFERENCE</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${cdr}"  varStatus="tagStatus">
							<tr>
						       <td class="text-right">${tagStatus.index + 1}</td>
								<td class="text-center">${mtrblc.DIVNAME}</td>
								<td class="text-center">${mtrblc.BTSCNO}</td>
								<td class="text-right">${mtrblc.CUR_BTCMD_HT}</td>
								<td class="text-center">${mtrblc.CTCAT}</td>
								<td class="text-center">${mtrblc.CTSUBCAT}</td>
								<td class="text-right">${mtrblc.CUR_BTBLKVA_HT}</td>
								<td class="text-right">${mtrblc.PRE_BTBLKVA_HT}</td>
								<td class="text-right">${mtrblc.CUR_ENGCHG}</td>
								<td class="text-right">${mtrblc.PRE_ENGCHG}</td>
								<td class="text-right">${mtrblc.CUR_CMD}</td>
								<td class="text-right">${mtrblc.PRE_CMD}</td>
								<td class="text-right">${mtrblc.DIFF_CMD}</td>
								
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="5">Grand Total</th>
							<th class="text-right">${cdr.stream().map(mtrblc -> mtrblc.CUR_BTCMD_HT).sum()}</th>
							<th class="text-right">${cdr.stream().map(mtrblc -> mtrblc.CUR_BTBLKVA_HT).sum()}</th>
							<th class="text-right">${cdr.stream().map(mtrblc -> mtrblc.PRE_BTBLKVA_HT).sum()}</th>
							<th class="text-right">${cdr.stream().map(mtrblc -> mtrblc.CUR_ENGCHG).sum()}</th>
							<th class="text-right">${cdr.stream().map(mtrblc -> mtrblc.PRE_ENGCHG).sum()}</th>
							<th class="text-right">${cdr.stream().map(mtrblc -> mtrblc.CUR_CMD).sum()}</th>
							<th class="text-right">${cdr.stream().map(mtrblc -> mtrblc.PRE_CMD).sum()}</th>
							<th class="text-right">${cdr.stream().map(mtrblc -> mtrblc.DIFF_CMD).sum()}</th>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'Comparison Demand Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Comparison Demand Report'}
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