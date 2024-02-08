<%@page import="javafx.scene.shape.Circle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<style>



.null{
font-weight: bold;
}
.CRD{
background-color: #cef4ff;
font-weight: bold;
}
.ONG{
background-color: #cef4ff;
font-weight: bold;
}
.VJA{
    background-color: #fff0dd;
    font-weight: bold;
}
.GNT{
    background-color: #fff0dd;
    font-weight: bold;
}
.ZZZ{
    background-color: #cef4ff;
    font-weight: bold;
}
.NOSTAT
{
color: #fff !important;
    font-weight: bold  !important;
}
.NULLCIR
{

    font-weight: bold  !important;
     background-color: #4ff1f1;
}
.TOTAL{
color: #fff !important;
    font-weight: bold  !important;
}
  thead>tr>th{
	color: #fff !important;
    font-weight: bold  !important;
} 
</style>
<div class="row row-cards row-deck">
		<form class="card" action="CBAndDemandAbstract" method="post">
			<div class="card-body">
				<h3 class="card-title"><strong><span class="text-danger">HT92C</span> - CB Split (Arrear, Demand Part)  Abstract</strong>   </h3>
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
						<label for="inputState">Month</label> <select id="mon"
							class="form-control" name="month" required="required">
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
							<label class="form-label">Year</label> <select
								class="form-control" name="year" required="required" id="year">
								<option value="">--Select Year--</option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label class="form-label">Get Report</label>
							<button type="submit" class="btn btn-success">Get Report</button>
						</div>
					</div>
				</div>
			</div>
		</form>

		<c:if test="${ not empty fn:trim(fail)}">
			<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
		</c:if>
		<c:if test="${ not empty fn:trim(tp)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		    <h2 class="text-center">${title}</h2>
		    <div class="bg-info text-white text-center" onclick="exportThisWithParameter('multiLevelTable', '${title}')" style="cursor: pointer; border: 1px solid #ccc; text-align: center;width:19%;padding-bottom: 10px;padding-top: 10px;">Excel</div>
			
			<table id="multiLevelTable" class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer" style="width: 100%;">
						<thead>
						<tr >
							<th class="bg-primary text-white text-center" colspan="9">${title}</th>
							</tr>
						<tr class="bg-primary text-white text-center">
							<!-- <th>S.NO</th> -->

							<th>CIRNAME</th>
							<th>TYPE</th>
							<th>CAT</th>
							<th>CAT COUNT</th>
							<th>NEG_CB</th>
							<th>POS_CB</th>
							<th>TOTAL_CB</th>
							<th>DEMAND_PART</th>
							<th>ARREAR_PART</th>
						</tr>
					</thead>
						<tbody>
						<%int flag = 0; 
						String cricle ="S";
						String circletype="S";%>
							<c:forEach var="mtrblc" items="${tp}" varStatus="tagStatus">
							<c:set var="cirl" value="${mtrblc.CIRNAME}" scope="request"/>
							<c:set var="cirltype" value="${mtrblc.CIR_TYPE}" scope="request"/>
							
								<tr class="${mtrblc.CIRNAME}">
								<%
									if (!cricle.equals((String) request.getAttribute("cirl"))) {
								%>
								<%-- <td rowspan="${CIRCOUNT[cirl]}" ><strong> <c:if
											test="${mtrblc.CIRNAME eq 'ZZZ'}">APCPDCL
								</c:if> <c:if test="${mtrblc.CIRNAME ne 'ZZZ'}">${mtrblc.CIRNAME}
								</c:if>
								</strong></td> --%>
								<td rowspan="${CIRCOUNT[cirl]}"><a href="DivisionWiseCBAndDemandAbstract?cir=${mtrblc.CIRNAME}&mon_year=${mon}"><strong> <c:if
											test="${mtrblc.CIRNAME eq 'ZZZ'}">APCPDCL
								</c:if> <c:if test="${mtrblc.CIRNAME ne 'ZZZ'}">${mtrblc.CIRNAME}
								</c:if>
								</strong></a></td>
								<%
									} else if (cricle.equals("S")) {
								%>
								<td rowspan="${CIRCOUNT[cirl]}" ><strong><c:if
											test="${mtrblc.CIRNAME eq 'ZZZ'}">APCPDCL
								</c:if> <c:if test="${mtrblc.CIRNAME ne 'ZZZ'}">${mtrblc.CIRNAME}
								</c:if></strong></td>
								<%
									}
									cricle = (String) request.getAttribute("cirl");
								%>
								
								
								
								
								
								<c:set var = "string3" value = "${mtrblc.CIRNAME}ZZZT" />
								
								<%
									if (!circletype.equals((String) request.getAttribute("cirltype"))) {
								%>
								<td rowspan="${CIRCOUNT[cirltype]}" style="text-align:center"><strong> <c:if
											test="${mtrblc.CIR_TYPE eq string3}">CATEGORY TOTAL
								</c:if> <c:if test="${mtrblc.CIR_TYPE ne string3}">${mtrblc.TYPE}
								</c:if>
								</strong></td>
								<%
									} else if (cricle.equals("S")) {
								%>
								<td rowspan="${CIRCOUNT[cirltype]}"  style="text-align:center"><strong><c:if
											test="${mtrblc.CIR_TYPE eq string3}">CATEGORY TOTAL
								</c:if> <c:if test="${mtrblc.CIR_TYPE ne string3}">${mtrblc.TYPE}
								</c:if></strong></td>
								<%
									}
								circletype = (String) request.getAttribute("cirltype");
								%>

								<%-- <td class="text-left">${mtrblc.TYPE}</td>	 --%>
								<c:if  test="${mtrblc.CTCAT eq 'TOTAL'}">
								<td class="text-left TOTAL bg-primary" style="padding-left: 5px;">${mtrblc.CTCAT}</td>	
								    <td class="text-right TOTAL bg-primary" style="padding-left: 5px;">${mtrblc.CAT_COUNT}</td>							
									<td class="text-right TOTAL bg-primary" style="padding-left: 5px;">${mtrblc.NEG_CB}</td>
									<td class="text-right TOTAL bg-primary" style="padding-left: 5px;">${mtrblc.POS_CB}</td>
									<td class="text-right TOTAL bg-primary" style="padding-left: 5px;">${mtrblc.TOTAL_CB}</td>
									<td class="text-right TOTAL bg-primary" style="padding-left: 5px;">${mtrblc.DEMAND_PART}</td>
									<td class="text-right TOTAL bg-primary" style="padding-left: 5px;">${mtrblc.ARREAR_PART}</td>
									</c:if>
									<c:if  test="${mtrblc.CTCAT ne 'TOTAL'}">
									<td class="text-left" style="padding-left: 5px;">${mtrblc.CTCAT}</td>	
								    <td class="text-right" style="padding-left: 5px;">${mtrblc.CAT_COUNT}</td>							
									<td class="text-right " style="padding-left: 5px;;">${mtrblc.NEG_CB}</td>
									<td class="text-right" style="padding-left: 5px;">${mtrblc.POS_CB}</td>
									<td class="text-right" style="padding-left: 5px;">${mtrblc.TOTAL_CB}</td>
									<td class="text-right" style="padding-left: 5px;">${mtrblc.DEMAND_PART}</td>
									<td class="text-right" style="padding-left: 5px;">${mtrblc.ARREAR_PART}</td>
									</c:if>
									
								</tr>
							</c:forEach>
						</tbody>
					<%-- 	<tfoot>
						<tr>
							<th colspan="3" class="text-right">Grand Total</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.NEG_CB).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.POS_CB).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.TOTAL_CB).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.DEMAND_PART).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.ARREAR_PART).sum()}</th>
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

<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        "paging": false,
	        "ordering": false,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'CB Split (Arrear, Demand Part)  Abstract',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'CB Split (Arrear, Demand Part)  Abstract',footer: true }
	            ]
	        }
	    });
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