<%@page import="javafx.scene.shape.Circle"%>
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
	     	$("#fyear").append("<option value="+j+">"+j+"</option>");

	     }
		 $('#fmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#fyear option[value="'+currentYear+'"]').prop('selected', true);
		 
		
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

.null{
font-weight: bold;
}
.CRD{
background-color: #cef4ff;
font-weight: bold;
}
.ONG{
background-color: #fff0dd;
font-weight: bold;
}
.VJA{
    background-color: #fff0dd;
    font-weight: bold;
}
.GNT{
    background-color: #cef4ff;
    font-weight: bold;
}
.APCPDCL{
    background-color: #fff0dd;
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
		<form class="card" action="EDAbstract" method="post">
			<div class="card-body">
				<h3 class="card-title"><strong><span class="text-danger">HT111</span> -ED Abstract Monthly </strong></h3>
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
						<label class="form-label">ED Flag</label>
						<div class="form-control">
						 <label
							class="radio-inline"> <input type="radio" name="edflag" value="Y"
							 required="required">Yes
						</label> <label class="radio-inline"> <input type="radio" value="N"
							name="edflag" required="required">No
						</label> 
						</div>
					</div>
				</div>
				
					
					<div class="col-md-3">
						<div class="form-group">
							<label class="form-label">Get ED Abstract Monthly </label>
							<button type="submit" class="btn btn-success">Get ED Abstract Monthly </button>
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
		     <form name="frm"  style="overflow: auto;">
			
			<c:if  test="${CIR eq 'ALL'}">
			<div class="bg-info text-white text-center" onclick="exportThisWithParameter('multiLevelTable', '${title}')" style="cursor: pointer; border: 1px solid #ccc; text-align: center;width:19%;padding-bottom: 10px;padding-top: 10px;">Excel</div>
			
			<table id="multiLevelTable" class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer" style="width: 100%;">
							<thead>
								<tr>
									<th class="bg-primary text-white text-center" colspan="4">${title}</th>
								</tr>
								<tr class="bg-primary text-white text-center">

									<th>MONTH</th>
									<th>CIRNAME</th>
									<th>KVAH</th>
									<th>ED AMOUNT</th>
								</tr>
							</thead>
							<tbody>
						<%int flag = 0; 
						String cricle ="S";
						String circletype="S";%>
							<c:forEach var="mtrblc" items="${tp}" varStatus="tagStatus">
							<c:set var="cirl" value="${mtrblc.BDT}" scope="request"/>
							<tr class="${mtrblc.CIRCLE}">
							<%
									if (!cricle.equals((String) request.getAttribute("cirl"))) {
								%>
							<td rowspan="${CIRCOUNT[cirl]}" >${mtrblc.BDT}</td>
							<%}
							cricle = (String) request.getAttribute("cirl");
							%><c:if  test="${mtrblc.CIRCLE eq 'TOTAL'}">
								<%--  <td class="text-left TOTAL bg-primary"style="padding-left: 5px;"> ${mtrblc.BDT}</td> --%>
								<td class="text-right TOTAL bg-primary"style="padding-left: 5px;">${mtrblc.CIRCLE}</td>
								<td class="text-right TOTAL bg-primary"style="padding-left: 5px;">${mtrblc.KVAH}</td>
								<td class="text-right TOTAL bg-primary"style="padding-left: 5px;">${mtrblc.ED_AMT}</td>
								</c:if>
								
								<c:if  test="${mtrblc.CIRCLE ne 'TOTAL'}">
							<%-- 	<td class="text-right" style="padding-left: 5px;"> ${mtrblc.BDT}</td> --%>
							<td class="text-right"style="padding-left: 5px;">${mtrblc.CIRCLE}</td>
								<td class="text-right"style="padding-left: 5px;">${mtrblc.KVAH}</td>
								<td class="text-right"style="padding-left: 5px;">${mtrblc.ED_AMT}</td>
								</c:if>
							</tr>
		
							
							</c:forEach>
						</tbody>
					
					</table>
					</c:if>
					
					<c:if  test="${CIR ne 'ALL'}">
						<table id="multiLevelTable"
							class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
							style="width: 100%;">
							<thead>
								<tr>
									<th class="bg-primary text-white text-center" colspan="9">${title}</th>
								</tr>
								<tr class="bg-primary text-white text-center">

									<th>MONTH</th>
									<th>CIRNAME</th>
									<th>KVAH</th>
									<th>ED AMOUNT</th>
								</tr>
							</thead>
							
							<tbody>
							<c:forEach var="mtrblc" items="${tp}" varStatus="tagStatus">
							<tr>
								<td class="text-right"style="padding-left: 5px;">${mtrblc.BDT}</td>
								<td class="text-right"style="padding-left: 5px;">${mtrblc.CIRCLE}</td>
								<td class="text-right"style="padding-left: 5px;">${mtrblc.KVAH}</td>
								<td class="text-right"style="padding-left: 5px;">${mtrblc.ED_AMT}</td>						
							</tr>
							</c:forEach>
							</tbody>
						<%-- 		<tfoot>
						<tr>
							<th  class="text-right">Grand Total</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.TOB).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.DEMAND).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.COLL_ARREAR).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.COLL_DEMAND).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.COLLECTION).sum()}</th>
							<th class="text-right">${tp.stream().map(mtrblc -> mtrblc.CB).sum()}</th>
						</tr>
					</tfoot>  --%>
						</table>


					</c:if>
					</form>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'Services Wise ledger Closing Balance',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'Services Wise ledger Closing Balance',footer: true }
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
<script>
function getNext(circle,agewise){
	alert(circle)
	document.frm.action="AgeWiseServiceBalance?circle="+circle+"&agewise="+agewise;
	document.frm.method="post";
	document.frm.submit();
	window.open(url, '_blank').focus();
}

</script>
<jsp:include page="footer.jsp"></jsp:include>