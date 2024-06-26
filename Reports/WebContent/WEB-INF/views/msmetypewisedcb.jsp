<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="msmetypewisedcb" method="post">
		<div class="card-body">
			<h3 class="card-title"><strong><span class="text-danger">HT101A</span> -  MSME TYPE WISE DCB </strong></h3>
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
                      <div class="form-group">
                        <label class="form-label">MNSE Type</label>
                        <select class="form-control"  id="mnsetype" name="mnsetype" required="required">
						    <option value="">Select Type</option>
						    <option value="ALL">ALL</option>
						    <option value="SMALL">SMALL</option>
						    <option value="MEDIUM">MEDIUM</option>
							<option value="MICRO">MICRO</option>
						</select>
                      </div>
                    </div>
				<div class="col-md-2">
					<div class="form-group">
						<label for="inputState">Ledger Month</label> <select id="mon"
							class="form-control" name="month">
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
				<div class="col-md-2">
					<div class="form-group">
						<label for="inputZip">Select Year</label> <select
							class="form-control" name="year" id="year">
							<option value="">--Select Year--</option>
						</select>
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">GET MSME Report</label>
						<button type="submit" class="btn btn-success">GET MSME Report</button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="hmon" name="hmon"> <input
			type="hidden" id="hyear" name="hyear">
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>


	<c:if test="${ not empty fn:trim(acd)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">${title}</h2>
				<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
					<thead>
					<tr>
								<th>S.NO</th>
								<th>CATEGORY</th>
								<th>POLICY_NAME</th>
								<th>NOS</th>
								<th>OB</th>
								<th>SALES</th>
								<th>DEMAND </th>
								<th>COLL_ARREAR </th>
								<th>COLL_DEMAND </th>
								<th>COLLECTION </th>
								<th>DRJ </th>
								<th>CRJ </th>
								<th>CB </th>
							</tr>
					
				
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${acd}" varStatus="tagStatus">
							<tr>
								<td >${tagStatus.index + 1}</td>								
								<td>${mtrblc.CATEGORY}</td>
								<td>${mtrblc.POLICY_NAME}</td>
								<td class="text-right">${mtrblc.NOS}</td>
								<td class="text-right">${mtrblc.OB}</td>
								<td class="text-right">${mtrblc.SALES}</td>
								<td class="text-right">${mtrblc.DEMAND}</td>
								<td class="text-right"> ${mtrblc.COLL_ARREAR}</td>
								<td class="text-right"> ${mtrblc.COLL_DEMAND }</td>
								<td class="text-right"> ${mtrblc.COLLECTION }</td>
								<td class="text-right"> ${mtrblc.DRJ }</td>
								<td class="text-right"> ${mtrblc.CRJ }</td>
								<td class="text-right"> ${mtrblc.CB }</td>
								
							</tr>
							

						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="3" class="text-right">Grand Total</th>
							<th class="text-left">${acd.stream().map(mtrblc -> mtrblc.NOS).sum()}</th>
							<th class="text-left">${acd.stream().map(mtrblc -> mtrblc.OB).sum()}</th>
							<th class="text-left">${acd.stream().map(mtrblc -> mtrblc.SALES).sum()}</th>
							<th class="text-left">${acd.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
							<th class="text-left">${acd.stream().map(mtrblc -> mtrblc.COLL_ARREAR).sum()}</th>
							<th class="text-left">${acd.stream().map(mtrblc -> mtrblc.COLL_DEMAND).sum()}</th>
							<th class="text-left">${acd.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th>
							<th class="text-left">${acd.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-left">${acd.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
							<th class="text-left">${acd.stream().map(mtrblc -> mtrblc.CB).sum()}</th>
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
		var currnetMonth = (new Date()).getMonth() + 1;
		$("#hyear").val(currentYear);
		$("#hmon").val($("#mon").val());

		for (var j = currentYear; j > 2015; j--) {
			$("#year").append("<option value="+j+">" + j + "</option>");

		}
		$('#mon option:eq(' + currnetMonth + ')').prop('selected', true);
		$('#year option[value="' + currentYear + '"]').prop('selected', true);

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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'cb_closing' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'cb_closing' }
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