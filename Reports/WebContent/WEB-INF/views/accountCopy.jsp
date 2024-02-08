<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<script>
requirejs([ 'jquery' ], function($) {
$(document).ready(function() {
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


<div class="row row-cards row-deck">
	      <form class="card" action="accountCopy" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT06</span> - HT Account Copy Report</strong> </h3>
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                          <label for="inputCity">Enter Service Number</label>
					      <input type="text" class="form-control"   required="required" name="scno" id="scno" placeholder="Enter Service Number">
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
                        <label class="form-label">GET Account Copy Report</label>
                        <button type="submit" class="btn btn-success">GET Account Copy Report </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
			
			<c:if test="${ not empty fn:trim(fail)}">
				<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
			</c:if>
			
			<c:if test="${ not empty fn:trim(account)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		    <input type="hidden" value="${supply}" id="supply">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<%-- <thead>
						<tr>
							<th>S.NO</th>
							<th>SCNO</th>
							<th>NAME</th>
							<th class="text-right">LD<br>DATE</th>
							<th class="text-right">OB<br>WITHOUT<br>COURT</th>
							<th class="text-right">OB<br>COURT</th>
							<th class="text-right">TOT_OB</th>
							<th class="text-right">DEMAND<br>WITHOUT<br>COURTLPC</th>
							<th class="text-right">COLLECTION</th>
							<th class="text-right">COURT<br>RJ_CC</th>
							<th class="text-right">COURT<br>RJ_LPC</th>
							<th class="text-right">DRJ</th>
							<th class="text-right">CRJ</th>
							<th class="text-right">CB<br>WITHOUT<br>COURT</th>
							<th class="text-right">CB<br>COURT</th>
							<th class="text-right">TOT_CB</th>
							<th class="text-right">SD</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${account}"  varStatus="tagStatus">
							<tr>
						        <td>${tagStatus.index + 1}</td>
								<td>${mtrblc.SCNO}</td>
								<td>${mtrblc.NAME_OF_CONSUMER}</td>
								<td class="text-right">${mtrblc.LD_DATE}</td>
								<td class="text-right">${mtrblc.OB_WITHOUT_COURT}</td>
								<td class="text-right">${mtrblc.OB_COURT}</td>
								<td class="text-right">${mtrblc.TOT_OB}</td>
								<td class="text-right">${mtrblc.DEMAND_WITHOUT_COURT_LPC}</td>
								<td class="text-right">${mtrblc.COLLECTION}</td>
								<td class="text-right">${mtrblc.COURT_RJ_CC}</td>
								<td class="text-right">${mtrblc.COURT_RJ_LPC}</td>
								<td class="text-right">${mtrblc.DRJ}</td>
								<td class="text-right">${mtrblc.CRJ}</td>
								<td class="text-right">${mtrblc.CB_WITHOUT_COURT}</td>
								<td class="text-right">${mtrblc.CB_COURT}</td>
								<td class="text-right">${mtrblc.TOT_CB}</td>
								<td class="text-right">${mtrblc.SD}</td>
							</tr>
						</c:forEach>
					</tbody>
					
	 --%>			
	                 <thead>
	                    <tr><th colspan="28" class='text-danger'>SUPPLY RELEASE DATE :${supply}</th></tr>
						<tr>
							<th class="text-right">S.NO</th>
							<th class="text-right">USCNO</th>
							<th class="text-right">MONYEAR</th>
							<th class="text-right">NAME</th>
							<th class="text-right">ADDRESS1</th>
							<th class="text-right">ADDRESS2</th>
							<th class="text-right">CAT</th>
							<th class="text-right">STATUS</th>
							<th class="text-right">OPENING_RDG</th>
							<th class="text-right">CLOSING_RDG</th>
							<th class="text-right">MF</th>
							<th class="text-right">LD<br>DATE</th>
							<th class="text-right">LOAD</th>
							<th class="text-right">TOD CHARGES</th>
							<th class="text-right">TOD CONSUMPTION</th>
							<th class="text-right">SD</th>
							<th class="text-right">RMD</th>
							<th class="text-right">BMD</th>
							<th class="text-right">RKWH</th>
							<th class="text-right">BKWH</th>
							<th class="text-right">RKVAH</th>
							<th class="text-right">SOLAR</th>
							<th class="text-right">BKVAH</th>
							<th class="text-right">OP BALANCE</th>
							<th class="text-right">DEMAND</th>
							<th class="text-right">COLLECTION</th>
							<th class="text-right">DEBIT JOURNAL</th>
							<th class="text-right">CREDIT JOURNAL</th>
							<th class="text-right">CCLPC</th>
							<th class="text-right">CL_BALANCE</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${account}"  varStatus="tagStatus">
							<tr>
						        <td>${tagStatus.index + 1}</td>
								<td>${mtrblc.SCNO}</td>
								<td class="text-right">${mtrblc.LD_DATE}</td>
								<td>${mtrblc.NAME_OF_CONSUMER}</td>
								<td class="text-right">${mtrblc.CTADD1}</td>
								<td class="text-right">${mtrblc.CTADD2}</td>
								<td class="text-right">${mtrblc.CAT}</td>
								<td class="text-right">${mtrblc.STATUS}</td>
								<td class="text-right">${mtrblc.MDOPNKVAH_HT}</td>
								<td class="text-right">${mtrblc.MDCLKVAH_HT}</td>
								<td class="text-right">${mtrblc.MDMF_HT}</td>
								<td class="text-right">${mtrblc.LD_DATE}</td>
								<td class="text-right">${mtrblc.LOAD}</td>
								<td class="text-right">${mtrblc.BTTODCHG_HT}</td>
								<td class="text-right">${mtrblc.BTTODCHG_HT}</td>
								<td class="text-right">${mtrblc.SD}</td>
								<td class="text-right">${mtrblc.BTRKVA_HT}</td>
								<td class="text-right">${mtrblc.BTBLKVA_HT}</td>
								<td class="text-right">${mtrblc.BTRKWH_HT}</td>
								<td class="text-right">0</td>
								<td class="text-right">${mtrblc.BTRKVAH_HT}</td>
								<td class="text-right">${mtrblc.BTBLSOLAR_HT}</td>
								<td class="text-right">${mtrblc.BTBKVAH}</td>
								<td class="text-right">${mtrblc.OB_WITHOUT_COURT}</td>
								<td class="text-right">${mtrblc.DEMAND_WITHOUT_COURT_LPC}</td>
								<td class="text-right">${mtrblc.COLLECTION}</td>
								<td class="text-right">${mtrblc.DRJ}</td>
								<td class="text-right">${mtrblc.CRJ}</td>
								<%-- <td class="text-right">${mtrblc.COURT_LPC}</td> --%>
								<td class="text-right">${mtrblc.CB_COURT}</td>
								<td class="text-right">${mtrblc.TOT_CB}</td>
								
							</tr>
						</c:forEach>
					</tbody> 
	          
	          
	          
	          
	          
	          
	 
	           </table>
				</div>
			</div>
			</c:if>
			
			<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
		
		var supply1=$("#supply").val();
		console.log(supply);
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'AccountCopy'+" "+"AND SUPPLY RELEASE DATE"+" "+supply1},
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'AccountCopy'+" "+"AND SUPPLY RELEASE DATE"+" "+supply1}
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
</div>