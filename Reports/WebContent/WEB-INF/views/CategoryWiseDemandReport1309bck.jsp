<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.util.*"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="CategoryWiseDemandReport" method="post">
		<div class="card-body">
			<h3 class="card-title">Category Wise Demand Report</h3>
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
						<label class="form-label">GET CategoryWiseDemandReport</label>
						<button type="submit" class="btn btn-success">GET CategoryWiseDemandReport</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(consolidate)}">
	<%
		Set<String> circles=new LinkedHashSet<String>();
		Set<String> cats=new LinkedHashSet<String>();
		Map<String,Map<String,String>> updated=new HashMap<String,Map<String,String>>();
		if(request.getAttribute("consolidate")!=null){
			List<Map<String,Object>> data=(List<Map<String,Object>>) request.getAttribute("consolidate"); 
			
			if(data!=null){
				for(Map<String,Object> rows:data){
					if(!rows.get("CIRCLE").equals("TOTAL")){
						circles.add((String)rows.get("CIRCLE"));
					}
					if(!rows.get("CAT").equals("TOTAL")){
						cats.add((String)rows.get("CAT"));
					}
					
					
					if(updated.containsKey((String)rows.get("CAT"))){
						Map<String,String> cirdata=updated.get((String)rows.get("CAT"));
						cirdata.put((String)rows.get("CIRCLE"),String.valueOf(rows.get("TOTAL_DEMAND")));
					} else{
						
						Map<String,String> cirdata=new HashMap<String,String>();
						cirdata.put((String)rows.get("CIRCLE"),String.valueOf(rows.get("TOTAL_DEMAND")));
						updated.put((String)rows.get("CAT"),cirdata);
					}
				}
			}
		}
	
	%>
	 <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							
							<th class="text-right">CAT</th>
							<%for(String cirname:circles){ %>
								<th class="text-right"><%=cirname %></th>
							<%} %>
							<th class="text-right">Total<br>Demand</th>

						</tr>
					</thead>
					<tbody>
						<%for(String cat:cats){ %>
							<tr>
								<td><%=cat %></td>
								<%for(String cir:circles){ %>
									<%if(updated.get(cat).containsKey(cir)){%>
										<td><%=updated.get(cat).get(cir) %></td>
									<%} else{%>
										<td>0</td>
									<%} %>
								<%} %>
								<%if(updated.get(cat).containsKey("TOTAL")){%>
										<td><%=updated.get(cat).get("TOTAL") %></td>
									<%} else{%>
										<td>0</td>
								<%} %>
							</tr>
						<%} %>
						
					</tbody>
					<tfoot>
						<tr>
							<th >Grand Total</th>
							<%for(String cir:circles){ %>
									<%if(updated.get("TOTAL").containsKey(cir)){%>
										<th><%=updated.get("TOTAL").get(cir) %></th>
									<%} else{%>
										<th>0</th>
									<%} %>
								<%} %>
								<%if(updated.get("TOTAL").containsKey("TOTAL")){%>
										<th><%=updated.get("TOTAL").get("TOTAL") %></th>
									<%} else{%>
										<th>0</th>
								<%} %>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	<c:if test="${ not empty fn:trim(cwdr)}">
		    <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>CIRCLE</th>
							<th class="text-right">CAT</th>
							<th class="text-right">NOS</th>
							<th class="text-right">RKWH</th>
							<th class="text-right">RKVAH</th>
							<th class="text-right">BKVAH</th>
							<th class="text-right">Adjusted<br>Units</th>
							<th class="text-right">Demand<br>Without<br> CCLPC</th>
							<th class="text-right">Demand<br>With<br>CCLPC</th>
							<th class="text-right">Total<br>Demand</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${cwdr}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRCLE}</td>
								<td class="text-right">${mtrblc.CAT}</td>
								<td class="text-right">${mtrblc.NOS}</td>
								<td class="text-right">${mtrblc.BTRKWH_HT}</td>
								<td class="text-right">${mtrblc.BTRKVAH_HT}</td>
								<td class="text-right">${mtrblc.BTBKVAH}</td>
								<td class="text-right">${mtrblc.Adjusted_Units}</td>
								<td class="text-right">${mtrblc.BTCURDEM}</td>
								<td class="text-right">${mtrblc.BTCOURT_LPC}</td>
								<td class="text-right">${mtrblc.TOTAL_DEMAND}</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="3">Grand Total</th>
							<th class="text-right">${cwdr.stream().map(mtrblc -> mtrblc.NOS).sum()}</th>
							<th class="text-right">${cwdr.stream().map(mtrblc -> mtrblc.BTRKWH_HT).sum()}</th>
							<th class="text-right">${cwdr.stream().map(mtrblc -> mtrblc.BTRKVAH_HT).sum()}</th>
							<th class="text-right">${cwdr.stream().map(mtrblc -> mtrblc.BTBKVAH).sum()}</th>
							<th class="text-right">${cwdr.stream().map(mtrblc -> mtrblc.Adjusted_Units).sum()}</th>
							<th class="text-right">${cwdr.stream().map(mtrblc -> mtrblc.BTCURDEM).sum()}</th>
							<th class="text-right">${cwdr.stream().map(mtrblc -> mtrblc.BTCOURT_LPC).sum()}</th>
							<th class="text-right">${cwdr.stream().map(mtrblc -> mtrblc.TOTAL_DEMAND).sum()}</th>
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
										"<option value='"+k+"'>" + v
												+ "</option>");

							});
						}
					  
					});
				});
		$("#circle").append("<option value='ALL'>ALL</option>");
		
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
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'CategoryWiseDemandReport',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'CategoryWiseDemandReport',footer: true }
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>