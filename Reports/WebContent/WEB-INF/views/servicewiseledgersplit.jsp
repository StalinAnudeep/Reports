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
	      <form class="card" action="servicewiseledgersplit" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT121</span> - HT Service Wise CB Split</strong> </h3>
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
                        <label class="form-label">GET CB Split Report</label>
                        <button type="submit" class="btn btn-success">GETCB Split Report </button>
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
							<th class="text-right">CB EC</th>
							<th class="text-right">CB LPC</th>
							<th class="text-right">CB ED</th>
							<th class="text-right">CB EDI</th>
							<th class="text-right">CB FSA</th>
							<th class="text-right">CB TRUE UP</th>
							<th class="text-right">CB FPPCA1</th>
							<th class="text-right">CB FPPCA2</th>
							<th class="text-right">CB TOT</th>
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
								<td class="text-right">${mtrblc.CBEC}</td>
								<td class="text-right">${mtrblc.CBLPC}</td>
								<td class="text-right">${mtrblc.CBED}</td>
								<td class="text-right">${mtrblc.CBIED}</td>
								<td class="text-right">${mtrblc.CBFSA}</td>
								<td class="text-right">${mtrblc.CBTUPC}</td>
								<td class="text-right">${mtrblc.CBOLDFPP}</td>
								<td class="text-right">${mtrblc.CBNEWFPP}</td>
								<td class="text-right">${mtrblc.CBTOT}</td>
						
								
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