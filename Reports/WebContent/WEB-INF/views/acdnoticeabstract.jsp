<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="acdnoticeabstract" method="post">
		<div class="card-body">
			<h3 class="card-title">ACD_Notice_Abstract</h3>
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
						<label for="inputState">LEVI MONTH</label> <select id="mon"
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
				<div class="col-md-3">
					<div class="form-group">
					      <label for="inputZip">Select Year</label>
					      <select  class="form-control" name="year" id="year">
					      <option value="">--Select Year--</option>
					      </select>
			    </div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="form-label">GET ACD Abstract</label>
						<button type="submit" class="btn btn-success">GET ACD Abstract</button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="hmon" name="hmon">
		<input type="hidden" id="hyear" name="hyear">
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(dlist)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Circle</th>
							<th>Division</th>
							<th>Sub<br>Division</th>
							<th>Section</th>
							<th>Uscno</th>
							<th>ACD</th>
							<th>GOVT<br>PVT</th>
							<th>LEVI<BR>MONTH</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${dlist}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRNAME}</td>
								<td>${mtrblc.DIVNAME}</td>
								<td>${mtrblc.SUBNAME}</td>
								<td>${mtrblc.SECNAME}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.ACD}</td>
								<td>${mtrblc.TYPE}</td>
								<td>${mtrblc.LEVI_MTH}</td>
								
							</tr>
						</c:forEach>
					</tbody>
					  <tfoot>
						<tr>
							<th colspan="6" class="text-right">Grand Total</th>
							<th colspan="3" class="text-left">${dlist.stream().map(mtrblc -> mtrblc.ACD).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(dcl)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Circle</th>
							<th class="text-center">Govt_Scs</th>
							<th class="text-center">Govt_Acd</th>
							<th class="text-center">Pvt_Scs</th>
							<th class="text-center">PVT_ACD</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${dcl}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td><a href="allcircles?cir=${mtrblc.CIRNAME}&mon_year=${mon_year}&div=0&sub=0&sec=0&type=div">${mtrblc.CIRNAME}</a></td>
								<td class="text-center"><a href="allservices?cir=${mtrblc.CIRNAME}&mon_year=${mon_year}&div=0&sub=0&sec=0&type=cir&status=Y">${mtrblc.GOVT_SCS}</a></td>
								<td class="text-center">${mtrblc.GOVT_ACD}</td>
								<td class="text-center"><a href="allservices?cir=${mtrblc.CIRNAME}&mon_year=${mon_year}&div=0&sub=0&sec=0&type=cir&status=N">${mtrblc.PVT_SCS}</a></td>
								<td class="text-center">${mtrblc.PVT_ACD}</td>
							</tr>
						</c:forEach>
					</tbody>
        			  <tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-center"><a href="alltotal?cir=0&mon_year=${mon_year}&div=0&sub=0&sec=0&type=all&status=Y&stype=${stype}">${dcl.stream().map(mtrblc -> mtrblc.GOVT_SCS).sum()}</a></th>
							<th class="text-center">${dcl.stream().map(mtrblc -> mtrblc.GOVT_ACD).sum()}</th>
							<th class="text-center"><a href="alltotal?cir=0&mon_year=${mon_year}&div=0&sub=0&sec=0&type=all&status=Y&stype=${stype}">${dcl.stream().map(mtrblc -> mtrblc.PVT_SCS).sum()}</a></th>
							<th class="text-center">${dcl.stream().map(mtrblc -> mtrblc.PVT_ACD).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	
	
	
	<c:if test="${ not empty fn:trim(di)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Division</th>
							<th class="text-center">Govt_Scs</th>
							<th class="text-center">Govt_Acd</th>
							<th class="text-center">Pvt_Scs</th>
							<th class="text-center">PVT_ACD</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${di}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td><a href="allcircles?cir=${circle}&mon_year=${mon_year}&div=${mtrblc.DIVNAME}&sub=0&sec=0&type=sub">${mtrblc.DIVNAME}</a></td>
								<td class="text-center"><a href="allservices?cir=${circle}&mon_year=${mon_year}&div=${mtrblc.DIVNAME}&sub=0&sec=0&type=div&status=Y">${mtrblc.GOVT_SCS}</a></td>
								<td class="text-center">${mtrblc.GOVT_ACD}</td>
								<td class="text-center"><a href="allservices?cir=${circle}&mon_year=${mon_year}&div=${mtrblc.DIVNAME}&sub=0&sec=0&type=div&status=N">${mtrblc.PVT_SCS}</a></td>
								<td class="text-center">${mtrblc.PVT_ACD}</td>
							</tr>
						</c:forEach>
					</tbody>
        			  <tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-center"><a href="alltotal?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=0&sec=0&type=div&status=Y&stype=${stype}">${di.stream().map(mtrblc -> mtrblc.GOVT_SCS).sum()}</a></th>
							<th class="text-center">${di.stream().map(mtrblc -> mtrblc.GOVT_ACD).sum()}</th>
							<th class="text-center"><a href="alltotal?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=0&sec=0&type=div&status=N&stype=${stype}">${di.stream().map(mtrblc -> mtrblc.PVT_SCS).sum()}</a></th>
							<th class="text-center">${di.stream().map(mtrblc -> mtrblc.PVT_ACD).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	
	
	<c:if test="${ not empty fn:trim(sub)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Sub Division</th>
							<th class="text-center">Govt_Scs</th>
							<th class="text-center">Govt_Acd</th>
							<th class="text-center">Pvt_Scs</th>
							<th class="text-center">PVT_ACD</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${sub}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td><a href="allcircles?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=${mtrblc.SUBNAME}&sec=0&type=sec">${mtrblc.SUBNAME}</a></td>
								<td class="text-center"><a href="allservices?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=${mtrblc.SUBNAME}&sec=0&type=sub&status=Y">${mtrblc.GOVT_SCS}</a></td>
								<td class="text-center">${mtrblc.GOVT_ACD}</td>
								<td class="text-center"><a href="allservices?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=${mtrblc.SUBNAME}&sec=0&type=sub&status=N">${mtrblc.PVT_SCS}</a></td>
								<td class="text-center">${mtrblc.PVT_ACD}</td>
							</tr>
						</c:forEach>
					</tbody>
        			  <tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-center"><a href="alltotal?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=0&sec=0&type=sub&status=Y&stype=${stype}">${sub.stream().map(mtrblc -> mtrblc.GOVT_SCS).sum()}</a></th>
							<th class="text-center">${sub.stream().map(mtrblc -> mtrblc.GOVT_ACD).sum()}</th>
							<th class="text-center"><a href="alltotal?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=0&sec=0&type=sub&status=N&stype=${stype}">${sub.stream().map(mtrblc -> mtrblc.PVT_SCS).sum()}</a></th>
							<th class="text-center">${sub.stream().map(mtrblc -> mtrblc.PVT_ACD).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(sec)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Section</th>
							<th class="text-center">Govt_Scs</th>
							<th class="text-center">Govt_Acd</th>
							<th class="text-center">Pvt_Scs</th>
							<th class="text-center">PVT_ACD</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${sec}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.SECNAME}</td>
								<td class="text-center"><a href="allservices?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=${subdivision}&sec=${mtrblc.SECNAME}&type=sec&status=Y">${mtrblc.GOVT_SCS}</a></td>
								<td class="text-center">${mtrblc.GOVT_ACD}</td>
								<td class="text-center"><a href="allservices?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=${subdivision}&sec=${mtrblc.SECNAME}&type=sec&status=N">${mtrblc.PVT_SCS}</a></td>
								<td class="text-center">${mtrblc.PVT_ACD}</td>
							</tr>
						</c:forEach>
					</tbody>
        			  <tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-center"><a href="alltotal?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=${subdivision}&sec=${mtrblc.SECNAME}&type=sec&status=Y&stype=${stype}">${sec.stream().map(mtrblc -> mtrblc.GOVT_SCS).sum()}</a></th>
							<th class="text-center">${sec.stream().map(mtrblc -> mtrblc.GOVT_ACD).sum()}</th>
							<th class="text-center"><a href="alltotal?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=${subdivision}&sec=${mtrblc.SECNAME}&type=sec&status=N&stype=${stype}">${sec.stream().map(mtrblc -> mtrblc.PVT_SCS).sum()}</a></th>
							<th class="text-center">${sec.stream().map(mtrblc -> mtrblc.PVT_ACD).sum()}</th>
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
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'ACD_Notice_Abstract',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'ACD_Notice_Abstract',footer: true }
	            ]
	        }
	    });
	});
</script>