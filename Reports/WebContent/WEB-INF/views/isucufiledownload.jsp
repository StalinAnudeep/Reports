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
.CRDA{
background-color: #cef4ff;
font-weight: bold;
}
.ONGOLE{
background-color: #fff0dd;
font-weight: bold;
}
.GUNTUR{
    background-color: #fff0dd;
    font-weight: bold;
}
.VIJAYAWADA{
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
		
	<%-- 	<div class="card">
			<div class="card-body">
				<h3 class="card-title"><strong>ISU CU File Download  </strong></h3>
				<div class="row">
				<form  action="ISU_CS_PARTNER_V1" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ISU_CS_PARTNER_V1</button>
						</div>
					</div>
					</form>
					<form  action="ISU_CS_PART_REL_V1" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ISU_CS_PART_REL_V1</button>
						</div>
					</div>
					</form>
					
					<form  action="ISU_CS_FICA_ACCOUNT_V1" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ISU_CS-FICA_ACCOUNT_V1</button>
						</div>
					</div>
					</form>
					
					<form  action="ISU_DM_CONNOBJ_V1" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ISU_DM_CONNOBJ_V1</button>
						</div>
					</div>
					</form>
					
						<form  action="ISU_DM_PREMISE_V1" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ISU_DM_PREMISE_V1</button>
						</div>
					</div>
					</form>
					
					<form  action="INSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">INSTALLATION</button>
						</div>
					</div>
					</form>
					
						<form  action="DEVICE" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DEVICE</button>
						</div>
					</div>
					</form>
					
					
					<form  action="MOVE_IN" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">MOVE_IN</button>
						</div>
					</div>
					</form>
					
						<form  action="METER_READ" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">METER_READ</button>
						</div>
					</div>
					</form>
					<form  action="METER_READ_01" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">METER_READ_01</button>
						</div>
					</div>
					</form>
					
					<form  action="SECURITY" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">SECURITY</button>
						</div>
					</div>
					</form>
					
					<form  action="PAYMENT" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">PAYMENT</button>
						</div>
					</div>
					</form>
					
					
					
					<form  action="DM_DEVICE_MTR" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVICE_MTR_CTPT</button>
						</div>
					</div>
					</form>
					
					<form  action="DM_DEVICE_CT_PT_11" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVICE_CT_PT_11</button>
						</div>
					</div>
					</form>
					
					<form  action="DM_DEVICE_CT_PT_33_132" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVICE_CT_PT_33_132</button>
						</div>
					</div>
					</form>
					
					
					<form  action="DM_DEVLOC" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVLOC</button>
						</div>
					</div>
					</form>
					
					<form  action="BI_FACTS" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">BI_FACTS</button>
						</div>
					</div>
					</form>
					
					<form  action="DMFULLINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DMFULLINSTALLATION</button>
						</div>
					</div>
					</form>
					
					<form  action="TECHINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">TECHINSTALLATION</button>
						</div>
					</div>
					</form>
					
					<form  action="OPENITEMS" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">OPENITEMS</button>
						</div>
					</div>
					</form>
					
					
					<form  action="DEVICEGRP" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DEVICEGRP</button>
						</div>
					</div>
					</form>
					
					
				</div>
			</div>
			</div> --%>
			
					<div class="card">
			<div class="card-body">
				<h3 class="card-title"><strong>ISU CU File Download  </strong></h3>
				<div class="row">
				<form  action="M3_ISU_CS_PARTNER_V1" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ISU_CS_PARTNER_V1</button>
						</div>
					</div>
					</form>
					<%-- <form  action="ISU_CS_PART_REL_V1" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ISU_CS_PART_REL_V1</button>
						</div>
					</div>
					</form> --%>
					
					<form  action="M3_ISU_CS_FICA_ACCOUNT_V1" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ISU_CS-FICA_ACCOUNT_V1</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_ISU_DM_CONNOBJ_V1" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ISU_DM_CONNOBJ_V1</button>
						</div>
					</div>
					</form>
					
						<form  action="M3_ISU_DM_PREMISE_V1" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ISU_DM_PREMISE_V1</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_INSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">INSTALLATION</button>
						</div>
					</div>
					</form>
					
					<%-- 	<form  action="DEVICE" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DEVICE</button>
						</div>
					</div>
					</form> --%>
					
					
					<form  action="MOVE_IN" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">MOVE_IN</button>
						</div>
					</div>
					</form>
					
						<form  action="METER_READ" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">METER_READ</button>
						</div>
					</div>
					</form>
					<form  action="METER_READ_01" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">METER_READ_01</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_SECURITY" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">SECURITY</button>
						</div>
					</div>
					</form>
					<%-- 	<form  action="M3_ACD" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">ACD</button>
						</div>
					</div>
					</form> --%>
					
					<form  action="M3_PAYMENT" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">PAYMENT</button>
						</div>
					</div>
					</form>
					
					
					
					<form  action="M3_DM_DEVICE_MTR" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVICE_MTR_CTPT</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_DM_DEVICE_CT_PT_11" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVICE_CT_PT_11</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_DM_DEVICE_CT_PT_33_132" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVICE_CT_PT_33_132</button>
						</div>
					</div>
					</form>
					
					
					<form  action="M3_DM_DEVLOC" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVLOC</button>
						</div>
					</div>
					</form>
					
					<form  action="BI_M3_FACTS" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">BI_FACTS</button>
						</div>
					</div>
					</form>
					
					<form  action="BI_M3_NEW_FACTS" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">BI_NEW_FACTS (Note: True Up Months)</button>
						</div>
					</div>
					</form>
					
					<form  action="DMFULLINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">METER DMFULLINSTALLATION</button>
						</div>
					</div>
					</form>
					
					<%-- <form  action="M3_CTPT_DMFULLINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">CTPT DMFULLINSTALLATION</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_CT_PT_DMFULLINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">CT & PT DMFULLINSTALLATION</button>
						</div>
					</div>
					</form> --%>
					
					<form  action="M3_CTPT_HT_TECHINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">CT PT HT TECHINSTALLATION</button>
						</div>
					</div>
					</form>
					<form  action="M3_CT_PT_HT_TECHINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">CT&PT HT TECHINSTALLATION</button>
						</div>
					</div>
					</form>
					
					<form  action="OPENITEMS" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">OPENITEMS</button>
						</div>
					</div>
					</form>
					
					
					<form  action="M3_DEVICEGRP" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DEVICEGRP</button>
						</div>
					</div>
					</form>
					
					
					<form  action="M3_LT_DM_DEVICE_MTR" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_LT_DEVICE_MTR_CTPT</button>
						</div>
					</div>
					</form>
					<form  action="M3_LT_DM_DEVICE_CT_PT_11" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_LT_DEVICE_CT_PT_11</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_LT_TECHINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">LT TECHINSTALLATION</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_LT_DMFULLINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">LT DMFULLINSTALLATION</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_LT_DM_DEVLOC" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_LT_DEVLOC</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_SMCMTECHINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">SM CMTECHINSTALLATION</button>
						</div>
					</div>
					</form>
					
					
					
					<form  action="M3_DISCENTER" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DISCENTER</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_DISCORDER" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DISCORDER</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_DISCDOC" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DISCDOC</button>
						</div>
					</div>
					</form>
					
					
					<form  action="M3_DM_DEVICE_MTR_CT" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVICE_MTR_CT(CT Meter)</button>
						</div>
					</div>
					</form>
					
					<form  action="CTDMFULLINSTALLATION" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">METER DMFULLINSTALLATION(CT Meter)</button>
						</div>
					</div>
					</form>
					
					<form  action="METER_READ_CT" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">METER_READ(CT Meter)</button>
						</div>
					</div>
					</form>
					<hr>
					
					<form  action="M3_DM_DEVICE_MTR_MC" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVICE_MTR_CTPT(Meter Change)</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_DM_DEVICE_CT_PT_11_CC" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVICE_CT_PT_11(Cubical Change)</button>
						</div>
					</div>
					</form>
					
					<form  action="M3_DM_DEVICE_CT_PT_33_132_CC" method="post">
					<div class="col-md-4">
						<div class="form-group">
							<button type="submit" class="btn btn-success">DM_DEVICE_CT_PT_33_132(Cubical Change)</button>
						</div>
					</div>
					</form>
					
				</div>
			</div>
			</div>
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