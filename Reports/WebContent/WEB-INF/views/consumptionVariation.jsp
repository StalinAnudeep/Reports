<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="consumptionVariation" method="post">
                <div class="card-body">
                  <h3 class="card-title">
<strong><span class="text-danger">HT17</span> - HT Variation In Consumption </strong></h3>
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
                        <label class="form-label">Service Type</label>
                        <select class="form-control"  id="servicetype" name="servicetype" required="required">
						    <option value="">Select Service Type</option>
						</select>
                      </div>
                    </div>
                     <div class="col-md-2">
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
                    <div class="col-md-2">
                      <div class="form-group">
                        <label class="form-label">Year</label>
                       <select class="form-control" name="year" required="required" id="year">
					      	<option value="">--Select Year-- </option>
					    </select>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">GET Variation In Consumption </label>
                        <button type="submit" class="btn btn-success">GET Variation In Consumption </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(consumption)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>CIRCLE</th>
						<th>SCNO</th>
						<th>NAME</th>
						<th>SERVICE TYPE</th>
						<th>LOAD</th>
						<th>REC_KVAH(${premonth})</th>
						<th>REC_KVAH(${monthYear})</th>
						<th>REC_TOD(${premonth})</th>
						<th>REC_TOD(${monthYear})</th>
						<th>BMD(${premonth})</th>
						<th>BMD(${monthYear})</th>
						<th>BKVAH(${premonth})</th>
						<th>BKVAH(${monthYear})</th>
						<th>THIRD_UNITS</th>
						<th>DIFF</th>
						<th>PRE_DIFF</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${consumption}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.CIRCLE}</td>
							<td>${mtrblc.CTUSCNO}</td>
							<td>${mtrblc.CTNAME}</td>
							<td>${mtrblc.STDESC}</td>
							<td>${mtrblc.CTCMD_HT}</td>
							<td class="text-right">${mtrblc.PRE_RECKVAH}</td>
							<td class="text-right">${mtrblc.CUR_RECKVAH}</td>
							<td class="text-right">${mtrblc.PRE_REC_TOD}</td>
							<td class="text-right">${mtrblc.CUR_REC_TOD}</td>
							<td class="text-right">${mtrblc.PRE_BMD}</td>
							<td class="text-right">${mtrblc.CUR_BMD}</td>
							<td class="text-right">${mtrblc.PRE_BTBKVAH}</td>
							<td class="text-right">${mtrblc.CUR_BTBKVAH}</td>
							<td class="text-right">${mtrblc.BTTP_KVAH}</td>
							<td class="text-right">${mtrblc.DIFF}</td>
							<td class="text-right">${mtrblc.PRE_DIFF}</td>
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
		$(document).ready(
				function() {
					$.ajax({
						type : "POST",
						url : "getServiceTypes",
						success : function(data) {
							var saptype = jQuery.parseJSON(data);
							$.each(saptype, function(k, v) {
								$("#servicetype").append(
										"<option value="+k+">" + v
												+ "</option>");

							});
						}
					  
					});
				});
		$("#circle").append("<option value=ALL>ALL</option>");
		$("#servicetype").append("<option value=ALL>ALL</option>");
		
		 var currentYear = (new Date()).getFullYear();
		 var currnetMonth=(new Date()).getMonth()+1;
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#year").append("<option value="+j+">"+j+"</option>");
		 }
		 $('#mon option:eq('+currnetMonth+')').prop('selected', true);
		 $('#year option[value="'+currentYear+'"]').prop('selected', true);
		 sortDropDownListByText();
	});
</script>
<script>
function sortDropDownListByText() {
	// Loop for each select element on the page.
	$("#servicetype").each(function() {
	// Keep track of the selected option.
	var selectedValue = $(this).val();
	// Sort all the options by text. I could easily sort these by val.
	$(this).html($("option", $(this)).sort(function(a, b) {
	return a.text.toUpperCase() == b.text.toUpperCase() ? 0 : a.text.toUpperCase() < b.text.toUpperCase() ? -1 : 1
	}));
	});
}

</script>
<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
	
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'Variation In Consumption' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'Variation In Consumption' }
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
		