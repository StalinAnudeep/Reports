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
		 for (var j = currentYear; j > 2013; j--) {
	     	$("#fyear").append("<option value="+j+">"+j+"</option>");
	     	$("#tyear").append("<option value="+j+">"+j+"</option>");
	     }
		 $('#fmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#fyear option[value="'+currentYear+'"]').prop('selected', true);
		 
		 $('#tmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#tyear option[value="'+currentYear+'"]').prop('selected', true);
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

.CellWithComment {
	position: relative;
}

.CellComment {
	display: none;
	position: absolute;
	z-index: 100;
	border: 1px;
	background-color: white;
	border-style: solid;
	border-width: 1px;
	border-color: #e81a40;
	padding: 3px;
	color: #e81a40;
	top: 20px;
	left: 20px;
}

.CellWithComment:hover span.CellComment {
	display: block;
}
</style>
<div class="row row-cards row-deck">
	<form class="card" action="trueupkvahhistory" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT112B</span> - Service
					Wise True Up Billed Units History (2014 -2019)</strong>
			</h3>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label for="inputCity">Enter Service Number</label> <input
							type="text" class="form-control" required="required" name="scno"
							id="scno" placeholder="Enter Service Number">
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label for="firstInput">From</label>
						<div>
							<select class="form-control firstInput" name="fmonth"
								required="required" id="fmonth" ng-model="obj.month">
								<option value="">Month</option>
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
							</select> <select class="form-control secondInput" name="fyear"
								required="required" id="fyear" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label for="firstInput">To</label>
						<div>
							<select class="form-control firstInput" name="tmonth"
								required="required" id="tmonth" ng-model="obj.month">
								<option value="">Month</option>
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
							</select> <select class="form-control secondInput" name="tyear"
								required="required" id="tyear" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div>

				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">GET Report</label>
						<button type="submit" class="btn btn-success">True Up
							KVAHS Report</button>
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
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<table class="table table-bordered table-sm table-condensed"
					style="border-right-width: 0px; border-bottom-width: 0px; border-left-width: 0px; border-top-width: 0px;font-size: 12px; font-weight: bold;">
					<tbody>
					
						<tr >
							<td class="bg-primary text-white text-right">Service Number</td>	
							<td  class="bg-light" style="font-size: 18px;">${consumerdetails.CTUSCNO}</td>		
							
							<td class="bg-primary text-white text-right">Account ID</td>
							<td  class="bg-light">${consumerdetails.CTACCT_ID}</td>
							
							
							
							
							
							<td class="bg-primary text-white text-right">Circle Name</td>	
							
							<td  class="bg-light">${consumerdetails.CIRNAME}</td>
						<!-- 	<td class="bg-info text-white text-center" colspan="2"><strong>Flags</strong></td> -->	
							
											
						</tr>
					<tr >
							<td class="bg-primary text-white text-right">Name</td>	
							<td  class="">${consumerdetails.CTNAME}</td>		
							
							<td class="bg-primary text-white text-right">CMD</td>	
							<td  class="bg-light">${consumerdetails.CTCMD_HT}</td>
							
							
							<td class="bg-primary text-white text-right">Division Name</td>	
							<td  class="">${consumerdetails.DIVNAME}</td>
							
							
						<%-- 	<td class="bg-primary text-white text-right">ED Flag</td>	
							<td  class="">${consumerdetails.CTEDFLAG}</td> --%>
											
						</tr>
						<tr >
							<td class="bg-primary text-white text-right">Category</td>	
							<td  class="bg-light">${consumerdetails.CTCAT}</td>			
							
							<td class="bg-primary text-white text-right">Voltage</td>	
							<td  class="">${consumerdetails.CTACTUAL_KV}</td>
							
							<td class="bg-primary text-white text-right">Sub-Division Name</td>	
							<td  class="bg-light">${consumerdetails.SUBNAME}</td>	
							
							<%-- <td class="bg-primary text-white text-right">TDS Flag</td>	
							<td  class="bg-light">${consumerdetails.CTTDS_FLAG}</td> --%>
							
										
						</tr>
						<tr >
							<td class="bg-primary text-white text-right">Sub-Category</td>		
							<td  class="">${consumerdetails.CTSUBCAT}</td>	
							
							<td class="bg-primary text-white text-right">Sub-Station</td>
							<td  class="bg-light">${consumerdetails.CTSS_NAME}</td>
							
							
							
							
							<td class="bg-primary text-white text-right">Section Code</td>		
							<td  class="">${consumerdetails.CTSECCD}</td>	
							
						<%-- 	<td class="bg-primary text-white text-right">Solar Flag</td>	
							<td  class="">${consumerdetails.CTSOLAR_FLAG}</td> --%>
											
						</tr>
				
					</tbody>
				</table>
				<table class="table card-table table-vcenter text-nowrap datatable"
					style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th class="text-center">MON_YEAR</th>
<!-- 							<th>USCNO</th>
							<th>CIRCLE</th>
							<th>DIVISION</th>
							<th>SUBDIVISION</th>
							<th>SECTION</th> -->
							<th class="text-right">KVAH</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${demand}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td class="text-center">${mtrblc.MON_YEAR}</td>
<%-- 								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.CIRCLE}</td>
								<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUBDIVISION}</td>
								<td>${mtrblc.SECTION}</td> --%>
								<td class="text-right">${mtrblc.MN_KVAH}</td>
							</tr>
						</c:forEach>
					<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right">${demand.stream().map(mtrblc -> mtrblc.MN_KVAH).sum()}</th>
						</tr>
					</tfoot>
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
<jsp:include page="footer.jsp"></jsp:include>