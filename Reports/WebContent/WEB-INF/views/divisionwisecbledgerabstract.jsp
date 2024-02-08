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
background-color: #bee5eb;
font-weight: bold;
}
.ONG{
background-color: #ffeeba;
font-weight: bold;
}
.VJA{
    background-color: #f5c6cb;
    font-weight: bold;
}
.GNT{
    background-color: #b8daff;
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
 thead>tr>th{
	color: #fff !important;
    font-weight: bold  !important;
} 
</style>
<div class="row row-cards row-deck">
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	
	
	<c:if test="${ not empty fn:trim(acd)}">
		  <div class="card ">
		 
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		    <div class="row">
		    <div class="col-md-12">
		     <a href="cbledgerabs" class="btn btn-primary">Back</a>
		     </div>
		     </div>
		    <h3 class="card-title text-center"><strong>${title}</strong></h3>
		    <hr>
		    	
					

				</div>
				<table
					class="table table-sm card-table table-vcenter text-nowrap datatable display"
					style="width: 100%;">
					<thead>
						<tr class="bg-primary text-white">
						<!-- 	<th>S.NO</th> -->
							<th rowspan="2" style="vertical-align: middle;" class="text-center">DIVISION</th>
							<th  rowspan="2" style="vertical-align: middle;" class="text-center">STATUS</th>
							<th  rowspan="2" style="vertical-align: middle;" class="text-center">USCNO COUNT</th>
							<th  colspan="3" class="text-center">OB</th>
							<th  colspan="3" class="text-center">DEMAND</th>
							<th  colspan="3" class="text-center">COLLECTION</th>
							<th  colspan="3" class="text-center">CB</th>
							<th  rowspan="2" style="vertical-align: middle;" class="text-center">SD</th>
						</tr>
						<tr class="bg-primary text-white">
						<th >OB_OTHERTHAN_COURT</th>
							<th>OB_COURT</th>
							<th>TOT_OB</th>
							<th>DEMAND_WITHOUT_COURT</th>
							<th>COURT_LPC</th>
							<th>DR_RJ</th>
							<th>CR_RJ</th>
							<th>COURT_RJ</th>
							<th>COLLECTION</th>
							<th>CB_OTHERTHAN_COURT</th>
							<th>CB_COURT</th>
							<th>TOTAL_CB</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${acd}" varStatus="tagStatus">
						<c:set var="cirl" value="${mtrblc.CIR}" scope="request"/>
						<c:set var="statusl" value="${mtrblc.STATUS}" scope="request"/>

							<%-- <c:if test="${fn:length(str1) eq 0}">
								<tr class="bg-info NOSTAT text-white text-right">
							</c:if>
							<c:if test="${fn:length(str1) ne 0}">
								<tr class="${mtrblc.CIR} text-right">
							</c:if>
 --%>







							<%
								String sts =(String) request.getAttribute("statusl");
										String cirl = (String) request.getAttribute("cirl");
										if (sts == null) {
							%>
							<tr class="${mtrblc.CIR} null bg-white text-right">
								<td><%=request.getAttribute("cirl") == null ? sts==null?"Grand":"" : cirl%></td>
								<td>Total</td>
								<%
									} else {
												if (cirl == null) {
								%>
							
							
							<tr class="NULLCIR text-right">

								<td>Total </td>
								<td><%=request.getAttribute("statusl")%></td>

								<%
									} else {
								%>
							
							
								<tr class="${mtrblc.CIR}  text-right">
								<td><a href="subdivisionwisecbledgerabs?cir=<%=request.getAttribute("cirl")%>&mon_year=${mon}"><%=request.getAttribute("cirl")%></a></td>
							<%-- 	<td><%=request.getAttribute("cirl")%></td> --%>
								<td><%=request.getAttribute("statusl")%></td>
								<%
									}
											}
								%>
								<td>${mtrblc.USCNO_COUNT}</td>
								<td>${mtrblc.OB_OTHERTHAN_COURT}</td>
								<td>${mtrblc.OB_COURT}</td>
								<td>${mtrblc.TOT_OB}</td>
								<td>${mtrblc.DEMAND_WITHOUT_COURT}</td>
								<td>${mtrblc.COURT_LPC}</td>
								<td>${mtrblc.DR_RJ}</td>
								<td>${mtrblc.CR_RJ}</td>
								<td>${mtrblc.COURT_RJ}</td>
								<td>${mtrblc.COLLECTION}</td>
								<td>${mtrblc.CB_OTHERTHAN_COURT}</td>
								<td>${mtrblc.CB_COURT}</td>
								<td>${mtrblc.TOTAL_CB}</td>
								<td>${mtrblc.CB_SD}</td>


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