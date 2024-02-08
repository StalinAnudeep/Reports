<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="feederwiseabstract" method="post">
		<div class="card-body">
			<h3 class="card-title"><strong><span class="text-danger">HT103</span> - Feeder Wise DCB Abstract</strong></h3>
			<div class="row">
  <div class="col-md-2">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
				<div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Feeder</label>
                        <select class="form-control"  id="feeder" name="feeder" required="required">
					
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
						<label class="form-label">GET Feeder Wise Abstract</label>
						<button type="submit" class="btn btn-success">GET Feeder Wise Abstract</button>
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
				 <div class="bg-info text-white text-center" onclick="exportThisWithParameter('multiLevelTable', '${title}')" style="cursor: pointer; border: 1px solid #ccc; text-align: center;width:19%;padding-bottom: 10px;padding-top: 10px;">Excel</div>
				<table id="multiLevelTable"
						class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer" style="width: 100%;">
					<thead>
					<tr>
								<th class="bg-primary text-white text-center" colspan="14">${title}</th>
							</tr>
						<tr>
							<th class="text-center" rowspan="2" style="vertical-align: middle;">S.NO</th>														
							<th rowspan="2" style="vertical-align: middle;">FEEDER CODE</th>
							<th rowspan="2" style="vertical-align: middle;">FEEDER NAME</th>	
							<th rowspan="2" style="vertical-align: middle;">NOS</th>						
							<th rowspan="2" style="vertical-align: middle;">OB</th>
							<th rowspan="2" style="vertical-align: middle;">SALES</th>
							<th colspan="3" class="text-center">DEMAND</th>
							
							<th colspan="4" class="text-center">COLLECTION</th>
							<!-- <th  rowspan="2">DRJ</th> -->
						
							<th  rowspan="2" class="text-center" style="vertical-align: middle;">CB</th>
						</tr>
						<tr>
						<th>DEMAND</th>
						<th>DRJ</th>
						<th>TOTAL</th>
						<th>COLL ARREAR</th>
							<th>COLL DEMAND</th>
							<th>CRJ</th>
							<th>TOTAL</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${acd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>								
								<td id="fd">${mtrblc.FEEDER_CD}</td>
								<td>${mtrblc.FEEDER_NAME}</td>
								<td  class="text-right">${mtrblc.NOS}</td>
								<td class="text-right">${mtrblc.OB}</td>
								<td class="text-right">${mtrblc.SALES}</td>
								<td class="text-right">${mtrblc.DEMAND}</td>
								<td  class="text-right">${mtrblc.DRJ}</td>
								<td  class="text-right">${mtrblc.DEMAND+mtrblc.DRJ}</td>
								<td class="text-right">${mtrblc.COLL_ARREAR}</td>
								<td class="text-right">${mtrblc.COLL_DEMAND}</td>
								<td  class="text-right">${mtrblc.CRJ}</td>
								<td class="text-right">${mtrblc.COLLECTION + mtrblc.CRJ}</td>
								<td  class="text-right">${mtrblc.CB}</td>

							</tr>
							

						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							
							<th colspan="3" class="text-right">Grand Total</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.NOS).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.OB).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.SALES).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.DEMAND).sum()  + acd.stream().map(mtrblc -> mtrblc.DRJ).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLL_ARREAR).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLL_DEMAND).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
					<%-- 		<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th> --%>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.COLLECTION).sum() + acd.stream().map(mtrblc -> mtrblc.CRJ).sum()}</th>
							<th class="text-right">${acd.stream().map(mtrblc -> mtrblc.CB).sum()}</th>
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
		

	/* 	$(document).ready(
				function() {
					$.ajax({
						type : "POST",
						url : "getFeeders/ALL",
						success : function(data) {
							var saptype = jQuery.parseJSON(data);
							$.each(saptype, function(k, v) {
								$("#feeder").append(
										"<option value="+k+">" + v
												+ "</option>");

							});
						}
					  
					});
				});
			$("#feeder").append("<option value=ALL>ALL</option>"); */

		var currentYear = (new Date()).getFullYear();
		var currnetMonth = (new Date()).getMonth() + 1;
		$("#hyear").val(currentYear);
		$("#hmon").val($("#mon").val());

		for (var j = currentYear; j > 2015; j--) {
			$("#year").append("<option value="+j+">" + j + "</option>");

		}
		$('#mon option:eq(' + currnetMonth + ')').prop('selected', true);
		$('#year option[value="' + currentYear + '"]').prop('selected', true);
		
		
		$("#circle")
		.change(
				function() {
					var circle = $(this).val();
					$.ajax({
						type : "POST",
						url : "getFeeders/"+circle,
						success : function(data) {
							var slctSubcat = $('#feeder');
							slctSubcat.empty();
							$("#feeder").append("<option value='0' selected> Select Feeder </option>");
							$("#feeder").append("<option value='ALL' > ALL </option>");
							var saptype = jQuery.parseJSON(data);
							$.each(saptype, function(k, v) {
								$("#feeder").append(
										"<option value="+k+">" + v
												+ "</option>");

							});
						}
					  
					});
				});

	});
</script>
<script>
	
</script>
<!-- <script>
	require([ 'jquery', 'datatables.net', 'datatables.net-jszip',
			'datatables.net-buttons', 'datatables.net-buttons-flash',
			'datatables.net-buttons-html5' ], function($, datatable, jszip) {
		window.JSZip = jszip;
		$('.datatable').DataTable({
			dom : 'Bfrltip',
			"scrollX" : true,
			"bPaginate": false,
			buttons : {
				buttons : [ ]
			}
		});
	});
</script>  -->

<script type="text/javascript">
	var exportThisWithParameter = (function() {
		var uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"  xmlns="http://www.w3.org/TR/REC-html40"><head> <!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets> <x:ExcelWorksheet><x:Name>{worksheet}</x:Name> <x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions> </x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook> </xml><![endif]--></head><body> <table>{table}</table></body></html>', base64 = function(
				s) {
			return window.btoa(unescape(encodeURIComponent(s)))
		}, format = function(s, c) {
			return s.replace(/{(\w+)}/g, function(m, p) {
				return c[p];
			})
		}
		return function(tableID, excelName) {

			tableID = document.getElementById(tableID)
			var ctx = {
				worksheet : excelName || 'Worksheet',
				table : tableID.innerHTML
			}
			 var link = document.createElement("a");
            link.download = "${title}.xls";
            link.href = uri + base64(format(template, ctx));
            link.click();
			
		}
	})()
</script>
<script> 
	requirejs([ 'jquery' ], function($) {
			$("td,th").each(function() { 
				if ($.isNumeric( $(this).text())) {
				    // It isn't a number	
				    if($(this).attr('id') != 'fd'){
				    $(this).html(parseFloat($(this).text()).toLocaleString('en-IN', {style: 'decimal', currency: 'INR'})); 
				    }
				}
			}
				
				
			)
			
	});
	
	
</script>