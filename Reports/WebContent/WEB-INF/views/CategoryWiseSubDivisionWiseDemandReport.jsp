<%@page import="javafx.scene.shape.Circle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<style>



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
		<c:if test="${ not empty fn:trim(fail)}">
			<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
		</c:if>
		<c:if test="${ not empty fn:trim(tp)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		    <h2 class="text-center">${title}</h2>
				<div class="row">
				<div class="col-md-6">
					<div class="bg-info text-white text-center"
						onclick="exportThisWithParameter('multiLevelTable', '${title}')"
						style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>
				</div>	
				<div class="col-md-6 text-right">
					<a href="CategoryWiseDemandReport" class="btn btn-primary">Back</a>
					</div>
				</div>
				<form name="frm"  style="overflow: auto;">
					<table id="multiLevelTable"
						class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer" style="width: 100%;">
						<thead>
							<tr>
								<th class="bg-primary text-white text-center" colspan="22">${title}</th>
							</tr>
							<tr class="bg-primary text-white text-center">
								<!-- <th>S.NO</th> -->
								<th>DIVISION</th>
								<th>TYPE</th>
								<th>CAT</th>
								<th>NOS</th>
								<th>KWH</th>
								<th>KVAH</th>
								<th>ADJ UNITS</th>
								<th>ENERGY CHARGES</th>
								<th>FIXED CHARGES</th>
								<th>TOD CHARGES</th>
								<th>CUSTOMER CHARGES</th>
								<th>LPC</th>
								<th>ED</th>
								<th>EDI</th>
								<th>FSA</th>
								<th>OTHER CHARGES</th>
								<th>TRUE-UP CHARGES</th>
								<th>FPPCA CHARGES</th>
								<th>FPPCA CHARGES(APR-23)</th>
								<th>FPPCA Charges 21-22 (LT-HT)</th>
								<!-- <th>ISD</th> -->
								<th>WITHOUT CLPC</th>
								<th>COURT LPC</th>
								<th>TOTAL</th>

								<!-- <th>NET ISD</th> -->
							</tr>
						</thead>
						<tbody>
							<%
								int flag = 0;
									String cricle = "S";
									String type = "S";
							%>
							<c:forEach var="mtrblc" items="${tp}" varStatus="tagStatus">
								<c:set var="cirl" value="${mtrblc.CIRCLE}" scope="request" />
								<c:set var="type" value="${mtrblc.CIR_TYPE}" scope="request" />
								<tr class="${mtrblc.CIRCLE}">
									<%
										if (!cricle.equals((String) request.getAttribute("cirl"))) {
									%>
									<td rowspan="${CIRCOUNT[cirl]}">${mtrblc.CIRCLE}</td>
									<%
										}
										cricle = (String) request.getAttribute("cirl");
									%>


									<%
										if (!type.equals((String) request.getAttribute("type"))) {
									%>
									<td rowspan="${TYPECOUNT[type]}" style="padding-left: 5px;">${mtrblc.TYPE}</td>
									<%
										}
										type = (String) request.getAttribute("type");
									%>
									
									<c:if test="${mtrblc.CTCAT eq 'TOTAL'}">
										<td class="text-left TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.CTCAT}</td>
										<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.NOS}</td>									
										<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.BTRKWH_HT}</td>
										<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.BTBKVAH}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.ADJ_UNITS}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.ENERGY_CHARGES}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.FIXED_CHARGES}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.TODCHG}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.CUSTOMER_CHARGES}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.LPC}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.ED}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.EDI}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.FSA}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.OTHER_CHARGES}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.TRUE_UP}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.BT_FPP_CHG}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.BT_PFPP_CHG}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.BT_LT_FPP_CHG}</td>
											<%-- <td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.ISD}</td> --%>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.WITHOUT_CLPC}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.COURT_LPC}</td>
											<td class="text-right TOTAL bg-primary"
											style="padding-left: 5px;">${mtrblc.TOTAL}</td>
											

									</c:if>

									<c:if test="${mtrblc.CTCAT ne 'TOTAL'}">
										<td class="text-right" style="padding-left: 5px;">
											${mtrblc.CTCAT}</td>
										<td class="text-right" style="padding-left: 5px;"> ${mtrblc.NOS}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.BTRKWH_HT}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.BTBKVAH}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.ADJ_UNITS}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.ENERGY_CHARGES}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.FIXED_CHARGES}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.TODCHG}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.CUSTOMER_CHARGES}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.LPC}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.ED}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.EDI}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.FSA}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.OTHER_CHARGES}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.TRUE_UP}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.BT_FPP_CHG}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.BT_PFPP_CHG}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.BT_LT_FPP_CHG}</td>
									<%-- 	<td class="text-right" style="padding-left: 5px;">${mtrblc.ISD}</td> --%>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.WITHOUT_CLPC}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.COURT_LPC}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.TOTAL}</td>

									</c:if>
								</tr>


							</c:forEach>
						</tbody>
					</table>
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
		 var currentYear = (new Date()).getFullYear();
		 var currnetMonth=(new Date()).getMonth()+1;
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#year").append("<option value='"+j+"'>"+j+"</option>");
		 }
		 
		 $('#mon option:eq('+currnetMonth+')').prop('selected', true);
		 $('#year option[value="'+currentYear+'"]').prop('selected', true);

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