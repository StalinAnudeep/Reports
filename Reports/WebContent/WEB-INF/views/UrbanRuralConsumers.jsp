<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<script>
requirejs([ 'jquery' ], function($) {
$(document).ready(function() {
	 var currentYear = (new Date()).getFullYear();
	 var currnetMonth=(new Date()).getMonth()+1;
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#fyear").append("<option value="+j+">"+j+"</option>");
	     	$("#tyear").append("<option value="+j+">"+j+"</option>");
	     }
		 $('#fmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#fyear option[value="'+currentYear+'"]').prop('selected', true);
		 
		/*  $('#tmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#tyear option[value="'+currentYear+'"]').prop('selected', true); */
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

.CellWithComment{
  position:relative;
}

.CellComment{
  display:none;
  position:absolute; 
  z-index:100;
  border:1px;
  background-color:white;
  border-style:solid;
  border-width:1px;
  border-color:#e81a40;
  padding:3px;
  color:#e81a40; 
  top:20px; 
  left:20px;
}

.CellWithComment:hover span.CellComment{
  display:block;
}
  thead>tr>th{
	color: #fff !important;
    font-weight: bold  !important;
} 
thead th {
    
}
</style>

<div class="row row-cards row-deck">
	<form class="card" action="UrbanRuralConsumers" method="post">
		<div class="card-body">
			<h3 class="card-title">
<strong><span class="text-danger">HT02E</span> - CEA (Rural,Urban Consumers) Report </strong></h3>
			<div class="row">
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div>
	
				 <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">Year</label>
                       <select id="inputyear" class="form-control" name="year" required="required">
					      <option value="">Select Financial Year</option>
					      </select>
                      </div>
                    </div>				
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">GET HT CEA Report</label>
						<button type="submit" class="btn btn-success">GET HT CEA Report</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(cdmd)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<div class="bg-info text-white text-center" onclick="exportThisWithParameter('multiLevelTable', '${title}')" style="cursor: pointer; border: 1px solid #ccc; text-align: center;width:19%;padding-bottom: 10px;padding-top: 10px;">Excel</div>		
				<table id="multiLevelTable"
						class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer" style="width: 100%;">
					<thead>
					<tr>	
						 <th class="text-center bg-info text-white" colspan="15" style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">Details of HT Electricity Consumers, Connected Load and Consumption , FY - ${fyear}  </th>
						 <th class="text-center bg-info text-white" style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">Format - 15</th>
					</tr>
						<tr style="border-left: 1px solid #f8f9fa;">
							<th class="text-center bg-primary  text-white"  rowspan="3" style="vertical-align: middle; border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">SR.NO</th>
							<th class="text-center bg-primary  text-white" rowspan="3"  style="vertical-align: middle; border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">CATEGORY</th>
							<th class="text-center  bg-primary  text-white" colspan="6" style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">No.of Consumers</th>
							<th class="text-center  bg-primary  text-white" colspan="6" style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">Connected Load(KW)</th>
							<th class="text-center  bg-primary  text-white" rowspan="2" colspan="2"  style="vertical-align: middle;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">Energy Consumption (KWH)</th>
						</tr>
						<tr style="border-left: 1px solid #f8f9fa;">
						<th class="text-center  bg-primary text-white" colspan="2" style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">At the beginning of the Year<br>${frommonth}</th>
						<th class="text-center  bg-primary text-white" colspan="2" style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">Added during the Month<br> ${tomonth}</th>
						<th class="text-center  bg-primary text-white" colspan="2" style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">At the end of the year<br>${frommonth} To ${tomonth}</th>
						<th class="text-center  bg-primary text-white" colspan="2" style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">At the beginning of the Year<br>${frommonth}</th>
						<th class="text-center  bg-primary text-white" colspan="2" style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">Added during the Month<br>${tomonth}</th>
						<th class="text-center  bg-primary text-white" colspan="2" style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">At the end of the year<br>${frommonth} To ${tomonth}</th>
						</tr>
						<tr class="bg-primary">
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">RURAL</th>
							<th class="text-center "style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">URBAN</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">RURAL</th> 
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">URBAN</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">RURAL</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">URBAN</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">RURAL</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">URBAN</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">RURAL</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">URBAN</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">RURAL</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">URBAN</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">RURAL</th>
							<th class="text-center"style="vertical-align: bottom;border-bottom: 2px solid #f8f9fa;border-left: 2px solid #f8f9fa;">URBAN</th>							
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${cdmd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CATEGORY}</td>
								<td class="text-right">${mtrblc.BEGIN_RURAL_NOS}</td>
								 <td class="text-right">${mtrblc.BEGIN_URBAN_NOS}</td>
									<td class="text-right">${mtrblc.DURING_RURAL_NOS}</td> 
								<td class="text-right">${mtrblc.DURING_URBAN_NOS}</td>
							<td class="text-right">${mtrblc.END_RURAL_NOS}</td>
								<td class="text-right">${mtrblc.END_URBAN_NOS}</td>
								<td class="text-right">${mtrblc.BEGIN_RURAL_LOAD}</td>
								<td class="text-right">${mtrblc.BEGIN_URBAN_LOAD}</td>
								<td class="text-right">${mtrblc.DURING_RURAL_LOAD}</td>
								<td class="text-right">${mtrblc.DURING_URBAN_LOAD}</td>
								<td class="text-right">${mtrblc.END_RURAL_LOAD}</td>
								<td class="text-right">${mtrblc.END_URBAN_LOAD}</td>
								<td class="text-right">${mtrblc.KWH_RURAL}</td>
								<td class="text-right">${mtrblc.KWH_URBAN}</td>
								
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>						
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BEGIN_URBAN_NOS).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BEGIN_RURAL_NOS).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.DURING_URBAN_NOS).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.DURING_RURAL_NOS).sum()}</th> 
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.END_URBAN_NOS).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.END_RURAL_NOS).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BEGIN_URBAN_LOAD).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.BEGIN_RURAL_LOAD).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.DURING_URBAN_LOAD).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.DURING_RURAL_LOAD).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.END_URBAN_LOAD).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.END_RURAL_LOAD).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.KWH_URBAN).sum()}</th>
								<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.KWH_RURAL).sum()}</th>
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
			for (var j = currentYear+1; j > 2019; j--) {
				var jj = j - 1 + "-" + j;
				$("#inputyear").append("<option value="+jj+">" + jj + "</option>");
			}

		$('#mon option:eq(' + currnetMonth + ')').prop('selected', true);
		$('#year option[value="' + currentYear + '"]').prop('selected', true);

	});
</script>
 <script>
	require([ 'jquery', 'datatables.net', 'datatables.net-jszip',
			'datatables.net-buttons', 'datatables.net-buttons-flash',
			'datatables.net-buttons-html5' ], function($, datatable, jszip) {

		window.JSZip = jszip;
		$('.datatable').DataTable({
			dom : 'Bfrltip',
			"scrollX" : true,
			buttons : {
				buttons : [ ]
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
<jsp:include page="footer.jsp"></jsp:include>