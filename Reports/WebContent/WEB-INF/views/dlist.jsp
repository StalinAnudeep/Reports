<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
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


</style>
<div class="row row-cards row-deck">
	<form class="card" action="dlist" method="post">
		<div class="card-body">
			<h3 class="card-title"><strong><span class="text-danger">HT46(1)</span> - D List Report</strong></h3>
			<div class="row">
				<div class="col">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div>
				<div class="col">
					<label for="inputState">CMD in KVA</label> 
					<select id="cmd"
						class="form-control" name="cmd" required="required">
						<option value="All">ALL</option>
						<option value="gte1000">Above 1 MVA</option>
					    <option value="lt1000">Below 1 MVA</option>
					</select>
				</div>
				<div class="col">
				<label for="inputState">CB Amount</label> 
				<div>
					<select id='dropdown' class="form-control firstInput" name="dropdown">
						<option value="ALL">All Amounts</option>
						<option value="CA">Greater Than CB Amount</option>
					</select> 
					<input type="number" class="form-control  secondInput"id="cbamount" name="cbamount" disabled="true" />
				</div>
				</div>
				<div class="col">
					<div class="form-group">
						<label class="form-label">Govt/Pvt</label> <select
							class="form-control" name="type" id="type"
							required="required">
							<option value="">Select Status</option>
							<option value="All">ALL</option>
							<option value="GOVT">GOVT</option>
							<option value="PVT">PVT</option>
						</select>
					</div>
				</div>
				
				<div class="col">
					<div class="form-group">
						<label class="form-label">Status</label> <select
							class="form-control" name="status" id="circle"
							required="required">
							<option value="">Select Status</option>
							<option value="All">ALL</option>
							<option value="LIVE">LIVE</option>
							<option value="UDC">UDC</option>
						</select>
					</div>
				</div>
				<div class="col">
					<div class="form-group">
						<label class="form-label">GET D-LIST</label>
						<button type="submit" class="btn btn-success">GET D-LIST</button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="hmon" name="hmon">
		<input type="hidden" id="hyear" name="hyear">
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(dlist)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Division</th>
							<th>Sub<br>Division</th>
							<th>Section</th>
							<th>SC.NO</th>
							<th>NAME</th>
							<th>CAT</th>
							<th>STATUS</th>
							<th>GOVT<br>PVT</th>
							<th>RKVAH</th>
							<th>OB <br>${demand}</th>
							<th>DEMAND<br> ${demand}</th>
							<th>NET_RJ</th>
							<th>COLLECTION</th>
							<th>COURT CB</th>
							<th>COURT CUMULATIVE LPC</th>
							<th>CB (EXCLUDING ED)</th>
							<th>ED </th>
							<th>TOT CB </th>
							
							<th>LAST<br>PAY<br>DATE</th>
							<th>LAST<br>PAID<br>AMOUNT</th>
							<th>ACD DEMAND</th>
							<th>ACD BALANCE TO BE PAY</th>
							<!-- <th>LAST<br>PAID<br>AMOUNT</th> -->
							<!-- <th>ACD Notice</th>
							<th>ACD Balance</th> -->
							<th>Reason For Pending</th>
							<th>Remarks</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${dlist}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUB_DIV}</td>
								<td>${mtrblc.SECNAME}</td>
								<td>${mtrblc.SCNO}</td>
								<td>${mtrblc.NAME}</td>
								<td>${mtrblc.CAT}</td>
								<td>${mtrblc.STATUS}</td>
								<td>${mtrblc.GOV_PVT}</td>
								<td class="text-right">${mtrblc.RKVAH}</td>
								<td class="text-right">${mtrblc.OB}</td>
								<td class="text-right">${mtrblc.DEMAND}</td>
								<td class="text-right">${mtrblc.NET_RJ}</td>
								<td class="text-right">${mtrblc.CASH_COLL}</td>
								<td class="text-right">${mtrblc.CB_OTH}</td>
								<td class="text-right">${mtrblc.CB_CCLPC}</td>
								<td class="text-right">${mtrblc.CB  - mtrblc.ED}</td>
								<td class="text-right">${mtrblc.ED }</td>
								<td class="text-right">${mtrblc.CB }</td>
								
								<td class="text-right"><fmt:formatDate pattern = "dd-MM-yyyy" value = "${mtrblc.LAST_PAID_DT}" /></td>
								<td class="text-right">${mtrblc.LAST_PAID_AMT}</td>
								<td class="text-right">${mtrblc.ACD_DEMAND}</td>
								<td class="text-right">${mtrblc.ACD_BAL}</td>
								<%-- <td class="text-right">${mtrblc.LAST_PAID_AMT}</td> --%>
								<%-- <td class="text-right">${mtrblc.ACD_DEMAND}</td>
								<td class="text-right">${mtrblc.ACD_BAL}</td> --%>
								<td>${mtrblc.REASON}</td>
								<td>${mtrblc.REMARK}</td>
							</tr>
						</c:forEach>
					</tbody>
					<%-- <tfoot>
						<tr>
							
							<th colspan="8" class="text-right">Grand Total</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.SD).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.REC_KVAH).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.MN_KVAH).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.OP_BALANCE).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.JOURNAL).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.CL_BALANCE).sum()}</th>
							<th></th>
						</tr>
					</tfoot> --%>
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
		 $("#hyear").val(currentYear);
		 $("#hmon").val($("#mon").val());
		 
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#year").append("<option value="+j+">"+j+"</option>");
	     	
		 }
		 $('#mon option:eq('+currnetMonth+')').prop('selected', true);
		 $('#year option[value="'+currentYear+'"]').prop('selected', true);
		 $('#dropdown').change(function() {
			  	if( $(this).val() == 'CA') {
			       		$('#cbamount').prop( "disabled", false );
			    } else {       
			      $('#cbamount').prop( "disabled", true );
			    }
			  });

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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'D-List_Report',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'D-List_Report',footer: true }
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
