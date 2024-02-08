<%@page import="javafx.scene.shape.Circle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.text.DecimalFormat" %>
 <script src="./assets/js/xlsx.full.min.js"></script>
<jsp:include page="header_lg.jsp"></jsp:include>

<style>



.null{
font-weight: bold;
}
.GOVT{
background-color: #cef4ff;
font-weight: bold;
}
.PVT{
background-color: #fff0dd;
font-weight: bold;
}
.TOTAL{
    background-color: #cef4ff;
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
.TOTAL_HD{
color: #fff !important;
    font-weight: bold  !important;
}
  thead>tr>th{
	color: #fff !important;
    font-weight: bold  !important;
   
} 

</style>
<div class="row row-cards row-deck">
		<form class="card" action="DCDivisionWiseReport" method="post">
			<div class="card-body">
				<h3 class="card-title"><strong><span class="text-danger">HT04</span> - Division Wise Daily Collection Report </strong></h3>
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
			<!-- 	<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Financial Year</label>  <select id="inputyear" class="form-control" name="year" required="required">
					      <option value="">Financial Year</option>
					      </select>
					</div>
				</div> -->
					
					
					<div class="col-md-4">
						<div class="form-group">
							<label class="form-label">Get Collection Report </label>
							<button type="submit" class="btn btn-success">Get Collection Report </button>
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
		    <button class="btn btn-primary"onclick="ExportToExcel('xlsx')">Export Table To Excel</button>
		     			<form name="frm"  style="overflow: auto;">
					<table id="tbl_exporttable_to_xls"
						class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer" style="width: 100%;">
						<thead>
							<tr>
								<th class="bg-primary text-white text-center" colspan="12">${title}</th>
								
								<th class="bg-primary text-white text-center" colspan="2">Amount in lakhs</th>
								
							</tr>
							<tr class="bg-primary text-white text-center">
							<th rowspan="2" style=" vertical-align: middle">TYPE</th>
								<th rowspan="2" style=" vertical-align: middle">CIRCLE</th>
								<th  rowspan="2" style=" vertical-align: middle">DIVISION</th>
								<th  colspan="4">Target for the Month of ${month_year}	</th>	
								<th  colspan="5">Progress as on ${date}	</th>
								<th>Total</th>			
								<th rowspan="2" style=" vertical-align: middle">Total <br>Dues</th> 	
								
							</tr>
							<tr class="bg-primary text-white text-center">
								<!-- <th>S.NO</th> -->


								
								<th style=" vertical-align: middle">No of <br> Services</th>
								<th style=" vertical-align: middle">Current <br>Month<br> Demand</th>
								<th style=" vertical-align: middle">Arrears <br>Amount</th>
								<th style=" vertical-align: middle">Total</th>
								<th style=" vertical-align: middle">No Of <br>Sevices <br>Collected</th>
								 <th style=" vertical-align: middle">Current <br>Month<br> Demand <br>Collected</th>
								<th style=" vertical-align: middle">% Collected <br>against CMD</th>
								<th style=" vertical-align: middle">Arrear <br>Amount <br>Collected
</th> 
								<th style=" vertical-align: middle">% Collected <br>against <br> Arrear <br>Amount</th> 
								<th style=" vertical-align: middle">% Collected <br>against  <br> Total <br>amount</th> 
								
							</tr>
							<tr class="bg-primary text-white text-center">							
							<th>1	</th>
							<th>2	</th>
							<th>3</th>	
							<th>4	</th>
							<th>5</th>	
							<th>6</th>	
							<th>7=(5+6)	</th>
							<th>8	</th>
							<th>9</th>	
							<th>10 = (9*100)/5	</th>
							<th>11	</th>
							<th>12 = (11*100)/6	</th>
							<th>13 = (9+11)*100/7	</th>
							<th>14 = (7-(9+11))</th>
							</tr>		
							
						</thead>
						<tbody>
							 <%
								int flag = 0;
									String cricle = "S";
									String type = "S";
							%> 
							<c:forEach var="mtrblc" items="${tp}" varStatus="tagStatus">
						 		<c:set var="cirl" value="${mtrblc.TYPE}" scope="request" />
								<c:set var="type" value="${mtrblc.CIR_TYPE}" scope="request" />
								<tr class="${mtrblc.TYPE}">
									
									
									
									<fmt:parseNumber value="${mtrblc.ARREAR_COLLECTED}" var="a" scope="request"/>
										<fmt:parseNumber value="${mtrblc.ARREAR_AMOUNT}" var="b" scope="request"/>
										<fmt:parseNumber value="${mtrblc.CURR_DEM_COLLECTED}}" var="a1" scope="request"/>
										<fmt:parseNumber value="${mtrblc.CURR_DEMAND}" var="b1" scope="request"/>
										<fmt:parseNumber value="${mtrblc.TOTAL}" var="tot" scope="request"/>
										<%
										double a = Double.parseDouble(String.valueOf(request.getAttribute("a")==null?"0":request.getAttribute("a")));
										double b = Double.parseDouble(String.valueOf(request.getAttribute("b")==null?"0":request.getAttribute("b")));
										DecimalFormat df = new DecimalFormat("0.00");
										double c = (a/b)*100;
										String s = df.format(c);
										
										
										double a1 = Double.parseDouble(String.valueOf(request.getAttribute("a1")==null?"0":request.getAttribute("a1")));
										double b1 = Double.parseDouble(String.valueOf(request.getAttribute("b1")==null?"0":request.getAttribute("b1")));
										
										double c1 = (a1/b1)*100;
										String s1 = df.format(c1);
										
										double tot = Double.parseDouble(String.valueOf(request.getAttribute("tot")==null?"0":request.getAttribute("tot")));
										//System.out.println(((a1+a)/tot)*100);
										double per = ((a1+a)/tot)*100;
										String s2 = df.format(per);
										
										
										
										%>
										
									 <!-- NOS, CURR_DEMAND, ARREAR_AMOUNT, TOTAL, NOS_COLLECTED, CURR_DEM_COLLECTED, COLLECTED_AGA_CMD, ARREAR_COLLECTED, ARR_C_AGA_CMD -->
									<c:if test="${mtrblc.DIVNAME eq 'TOTAL'}">
									<td class=" TOTAL_HD bg-primary">${mtrblc.TYPE}</td>
						
									<td class=" TOTAL_HD bg-primary" style="padding-left: 5px;">${mtrblc.CIRNAME}</td>
										<td class=" TOTAL_HD bg-primary"
											style="padding-left: 5px;">${mtrblc.DIVNAME}</td>
										 <td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;">${mtrblc.NOS}</td>
										<td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;">${mtrblc.CURR_DEMAND}</td>
										<td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;">${mtrblc.ARREAR_AMOUNT}</td>
										<td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;">${mtrblc.TOTAL}</td>
										<td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;">${mtrblc.NOS_COLLECTED}</td>
										<td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;">${mtrblc.CURR_DEM_COLLECTED}</td>
										<td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;"><%=s1 %> </td>
										<td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;">${mtrblc.ARREAR_COLLECTED}</td>
											<td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;"><%=s %></td>
											<td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;"><%=s2 %></td>
											<td class="text-right TOTAL_HD bg-primary"
											style="padding-left: 5px;">${mtrblc.TOTAL_DEUES}</td>
										
									</c:if>

									<c:if test="${mtrblc.DIVNAME ne 'TOTAL'}">
									<td style="padding-left: 5px;">${mtrblc.TYPE}</td>
									<td style="padding-left: 5px;">${mtrblc.CIRNAME}</td>
										<td style="padding-left: 5px;">${mtrblc.DIVNAME}</td>
										<td class="text-right" style="padding-left: 5px;">
											${mtrblc.NOS}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.CURR_DEMAND}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.ARREAR_AMOUNT}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.TOTAL}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.NOS_COLLECTED}</td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.CURR_DEM_COLLECTED}</td>
										<td class="text-right" style="padding-left: 5px;"><%=s1 %></td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.ARREAR_COLLECTED}</td>
										
										<td class="text-right" style="padding-left: 5px;"><%=s %></td>
										<td class="text-right" style="padding-left: 5px;"><%=s2 %></td>
										<td class="text-right" style="padding-left: 5px;">${mtrblc.TOTAL_DEUES}</td>
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
		 var currentYear = new Date().getFullYear();
			for(var i = 0; i < 2; i++){
			  var next = currentYear+1;
			  var year = currentYear + '-' + next.toString();
			  $('#inputyear').append(new Option(year, year));
			  currentYear--;
			}

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
	                
	            ]
	        }
	    });
	});
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
function ExportToExcel(type, fn, dl) {
    var elt = document.getElementById('tbl_exporttable_to_xls');
    var wb = XLSX.utils.table_to_book(elt, { sheet: "sheet1" });
    return dl ?
      XLSX.write(wb, { bookType: type, bookSST: true, type: 'base64' }):
      XLSX.writeFile(wb, fn || ('${title}.' + (type || 'xlsx')));
 }

</script>
<jsp:include page="footer.jsp"></jsp:include>