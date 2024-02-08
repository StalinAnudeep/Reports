<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>

<div class="row row-cards row-deck">
	<form class="card" action="acdReport2022" method="post">
		<div class="card-body">
			<h3 class="card-title">Get ACD Details  - Revised</h3>
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
					<div class="form-group">
						<label class="form-label">ACD</label>
						 <select class="form-control" name=acd required="required">
							<option value="">--Select ACD--</option>
							<option value="ALL">All</option>
							<option value="Y">ACD Services Issued Services</option>
							<option value="N">ACD Services Not Issued Services</option>
						</select>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">Year</label> <select id="inputyear"
							class="form-control" name="year" required="required">
							<option value="">--Select Financial Year--</option>
						</select>
					</div>
				</div>
				<div class=" col-md-3">
					<div class="form-group">
						<label class="form-label">Get ACD REPORT</label>
						<button type="submit" class="btn btn-success">Get ACD REPORT</button>
					</div>
				</div>
			</div>
			<div class="row" id="monthyearonly">
				<div class="col-md-3">
					<div class="form-group">
						<label for="inputState">LEVI MONTH</label> <select id="mon"
							class="form-control" name="monthyear">
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
				</div>
				<div class="col-md-3">
					<div class="form-group">
					      <label for="inputZip">Select Year</label>
					      <select id="yearonly" class="form-control" name="yearonly">
					      <option value="">--Select Year--</option>
					      </select>
			    </div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
				<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
			</c:if>
			
			<c:if test="${ not empty fn:trim(basic)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>CIRCLE</th>
							<th>SCNO</th>
							<th>STATUS</th>
							<th class="text-right">CMD in KVA</th>
							<th class="text-right">CAT</th>
							<th class="text-right">Sub Cat</th>
							<th class="text-right">VOLTAGE</th>
							<th class="text-right">AVG<br>BKVAH</th>
							<th class="text-right">AVG<br>TOD</th>
							<th class="text-right">2'Months<br> Charges</th>
							<th class="text-right">Deposit<br> Available</th>
							<th class="text-right">ACD To <br>Be Paid</th>
							<th class="text-right">Energy<br> Charges</th>
							<th class="text-right">Demand<br> Charges</th>
							<th class="text-right">ED<br> Charges</th>
							<th class="text-right">Customer<br> Charges</th>
							<th class="text-right">Tod<br> Charges</th>
							<th class="text-right">FIN_YEAR</th>
							<th class="text-right">LEVI_MTH</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${basic}"  varStatus="tagStatus">
							<tr>
						        <td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRCLE}</td>
								<td>${mtrblc.CTUSCNO}</td>
								<td>${mtrblc.type}</td>
								<td class="text-right">${mtrblc.CTCMD_HT}</td>
								<td class="text-right">${mtrblc.CTCAT}</td>
								<td class="text-right">${mtrblc.CTSUBCAT}</td>
								<td class="text-right">${mtrblc.CTACTUAL_KV}</td>
								<td class="text-right">${mtrblc.AVG_BKVAH}</td>
								<td class="text-right">${mtrblc.AVG_TOD}</td>
								<td class="text-right">${mtrblc.TWO_MONTHS}</td>
								<td class="text-right">${mtrblc.DEP}</td>
								<td class="text-right">${mtrblc.BALANCE}</td>
								<td class="text-right">${mtrblc.ENG}</td>
								<td class="text-right">${mtrblc.DMD}</td>
								<td class="text-right">${mtrblc.ED}</td>
								<td class="text-right">${mtrblc.CUST}</td>
								<td class="text-right">${mtrblc.TOD}</td>
								<td class="text-right">${mtrblc.FIN_YEAR}</td>
								<td>${monthyear}</td>
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
								$("#circle").append("<option value="+k+">" + v+ "</option>");

							});
						}
					});
					$("#circle").append("<option value='ALL'>ALL</option>");
					
					$("#monthyearonly").hide();
					
					 $("#inputyear").change(function(){
						  var datevalue=$("#inputyear").val().split("-");
						  var value=parseInt(datevalue[1]);
						  if(value>2019){
							  $("#monthyearonly").show();
							  $("#mon").attr('required', true);
							  $("#yearonly").attr('required', true);
						  }else{
							  $("#monthyearonly").hide();
							  $("#mon").attr('required', false);
							  $("#yearonly").attr('required', false);
						  }
						});
					 var currentYear = (new Date()).getFullYear();
					 for (var j = currentYear; j > 2015; j--) {
				     	$("#yearonly").append("<option value="+j+">"+j+"</option>");
					 }
					
				});

		var currentYear = (new Date()).getFullYear()+1;
		for (var j = currentYear; j > 2015; j--) {
			var jj = j - 1 + "-" + j;
			$("#inputyear").append("<option value="+jj+">" + jj + "</option>");
		}

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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'ACD_REPORT' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'ACD_REPORT' }
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>