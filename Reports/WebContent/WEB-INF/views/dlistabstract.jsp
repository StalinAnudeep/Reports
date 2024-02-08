<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="dlistabstract" method="post">
		<div class="card-body">
			<h3 class="card-title">D-List_Abstract</h3>
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
						<label class="form-label">GET D-List_Abstract</label>
						<button type="submit" class="btn btn-success">GET D-List_Abstract</button>
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
	<h4 style="color:blue;font-family: cursive;">${stype} HT D-list Abstract for ${month}</h4>
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
							<th>Cat</th>
							<th>Load</th>
							<th>GOVT<br>PVT</th>
							<th>CB</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${dlist}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.CIRCLE}</td>
								<td>${mtrblc.DIVISION}</td>
								<td>${mtrblc.SUB_DIV}</td>
								<td>${mtrblc.SECNAME}</td>
								<td>${mtrblc.SCNO}</td>
								<td>${mtrblc.CAT}</td>
								<td>${mtrblc.LOAD}</td>
								<td>${mtrblc.GOV_PVT}</td>
								<td>${mtrblc.CB}</td>
							</tr>
						</c:forEach>
					</tbody>
					  <tfoot>
						<tr>
							<th colspan="9" class="text-right">Grand Total</th>
							<th colspan="1" class="text-left">${dlist.stream().map(mtrblc -> mtrblc.CB).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(dcl)}">
	      <h4 style="color:blue;font-family: cursive;">${stype} HT D-list Abstract for ${month}</h4>
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Circle</th>
							<th class="text-center">Govt_Scs</th>
							<th class="text-center">Govt_Cb(in crore.)</th>
							<th class="text-center">Pvt_Scs</th>
							<th class="text-center">PVT_Cb(in crore.)</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${dcl}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td><a href="allcirclesdlist?cir=${mtrblc.CIRCLE}&div=0&sub=0&sec=0&type=div">${mtrblc.CIRCLE}</a></td>
								<td class="text-center"><a href="serviceswise?cir=${mtrblc.CIRCLE}&div=0&sub=0&sec=0&type=cir&status=GOVT">${mtrblc.GOVT_SCS}</a></td>
								<td class="text-center">${mtrblc.GOVT_CB}</td>
								<td class="text-center"><a href="serviceswise?cir=${mtrblc.CIRCLE}&div=0&sub=0&sec=0&type=cir&status=PVT">${mtrblc.PVT_SCS}</a></td>
								<td class="text-center">${mtrblc.PVT_CB}</td>
							</tr>
						</c:forEach>
					</tbody>
        			  <tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-center"><a href="dlistscs?cir=0&div=0&sub=0&sec=0&type=all&status=GOVT&stype=${stype}">${dcl.stream().map(mtrblc -> mtrblc.GOVT_SCS).sum()}</a></th>
							<th class="text-center">${dcl.stream().map(mtrblc -> mtrblc.GOVT_CB).sum()}</th>
							<th class="text-center"><a href="dlistscs?cir=0&div=0&sub=0&sec=0&type=all&status=PVT&stype=${stype}">${dcl.stream().map(mtrblc -> mtrblc.PVT_SCS).sum()}</a></th>
							<th class="text-center">${dcl.stream().map(mtrblc -> mtrblc.PVT_CB).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	
	
	
	<c:if test="${ not empty fn:trim(di)}">
	<h4 style="color:blue;font-family: cursive;">${stype} HT D-list Abstract for ${month}</h4>
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Division</th>
							<th class="text-center">Govt_Scs</th>
							<th class="text-center">Govt_Cb(in crore.)</th>
							<th class="text-center">Pvt_Scs</th>
							<th class="text-center">PVT_Cb(in crore.)</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${di}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td><a href="allcirclesdlist?cir=${circle}&mon_year=${mon_year}&div=${mtrblc.DIVISION}&sub=0&sec=0&type=sub">${mtrblc.DIVISION}</a></td>
								<td class="text-center"><a href="serviceswise?cir=${circle}&div=${mtrblc.DIVISION}&sub=0&sec=0&type=div&status=GOVT">${mtrblc.GOVT_SCS}</a></td>
								<td class="text-center">${mtrblc.GOVT_CB}</td>
								<td class="text-center"><a href="serviceswise?cir=${circle}&div=${mtrblc.DIVISION}&sub=0&sec=0&type=div&status=PVT">${mtrblc.PVT_SCS}</a></td>
								<td class="text-center">${mtrblc.PVT_CB}</td>
							</tr>
						</c:forEach>
					</tbody>
        			  <tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-center"><a href="dlistscs?cir=${circle}&div=${division}&sub=0&sec=0&type=div&status=GOVT&stype=${stype}">${di.stream().map(mtrblc -> mtrblc.GOVT_SCS).sum()}</a></th>
							<th class="text-center">${di.stream().map(mtrblc -> mtrblc.GOVT_CB).sum()}</th>
							<th class="text-center"><a href="dlistscs?cir=${circle}&div=${division}&sub=0&sec=0&type=div&status=PVT&stype=${stype}">${di.stream().map(mtrblc -> mtrblc.PVT_SCS).sum()}</a></th>
							<th class="text-center">${di.stream().map(mtrblc -> mtrblc.PVT_CB).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	
	
	<c:if test="${ not empty fn:trim(sub)}">
	<h4 style="color:blue;font-family: cursive;">${stype} HT D-list Abstract for ${month}</h4>
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Sub Division</th>
							 <th class="text-center">Govt_Scs</th>
							<th class="text-center">Govt_Cb(in crore.)</th>
							<th class="text-center">Pvt_Scs</th>
							<th class="text-center">PVT_Cb(in crore.)</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${sub}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td><a href="allcirclesdlist?cir=${circle}&mon_year=${mon_year}&div=${division}&sub=${mtrblc.SUB_DIV}&sec=0&type=sec">${mtrblc.SUB_DIV}</a></td>
								<td class="text-center"><a href="serviceswise?cir=${circle}&div=${division}&sub=${mtrblc.SUB_DIV}&sec=0&type=sub&status=GOVT">${mtrblc.GOVT_SCS}</a></td>
								<td class="text-center">${mtrblc.GOVT_CB}</td>
								<td class="text-center"><a href="serviceswise?cir=${circle}&div=${division}&sub=${mtrblc.SUB_DIV}&sec=0&type=sub&status=PVT">${mtrblc.PVT_SCS}</a></td>
								<td class="text-center">${mtrblc.PVT_CB}</td>
							</tr>
						</c:forEach>
					</tbody>
        			  <tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-center"><a href="dlistscs?cir=${circle}&div=${division}&sub=0&sec=0&type=sub&status=GOVT&stype=${stype}">${sub.stream().map(mtrblc -> mtrblc.GOVT_SCS).sum()}</a></th>
							<th class="text-center">${sub.stream().map(mtrblc -> mtrblc.GOVT_CB).sum()}</th>
							<th class="text-center"><a href="dlistscs?cir=${circle}&div=${division}&sub=0&sec=0&type=sub&status=PVT&stype=${stype}">${sub.stream().map(mtrblc -> mtrblc.PVT_SCS).sum()}</a></th>
							<th class="text-center">${sub.stream().map(mtrblc -> mtrblc.PVT_CB).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
	
	<c:if test="${ not empty fn:trim(sec)}">
	     <h4 style="color:blue;font-family: cursive;">${stype} HT D-list Abstract for ${month}</h4>
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>Section</th>
						    <th class="text-center">Govt_Scs</th>
							<th class="text-center">Govt_Cb(in crore.)</th>
							<th class="text-center">Pvt_Scs</th>
							<th class="text-center">PVT_Cb(in crore.)</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach var="mtrblc" items="${sec}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.SECNAME}</td>
								<td class="text-center"><a href="serviceswise?cir=${circle}&div=${division}&sub=${subdivision}&sec=${mtrblc.SECNAME}&type=sec&status=GOVT">${mtrblc.GOVT_SCS}</a></td>
								<td class="text-center">${mtrblc.GOVT_CB}</td>
								<td class="text-center"><a href="serviceswise?cir=${circle}&div=${division}&sub=${subdivision}&sec=${mtrblc.SECNAME}&type=sec&status=PVT">${mtrblc.PVT_SCS}</a></td>
								<td class="text-center">${mtrblc.PVT_CB}</td>
							</tr>
						</c:forEach>
					</tbody>
        			  <tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-center"><a href="dlistscs?cir=${circle}&div=${division}&sub=${subdivision}&sec=${mtrblc.SECNAME}&type=sec&status=GOVT&stype=${stype}">${sec.stream().map(mtrblc -> mtrblc.GOVT_SCS).sum()}</a></th>
							<th class="text-center">${sec.stream().map(mtrblc -> mtrblc.GOVT_CB).sum()}</th>
							<th class="text-center"><a href="dlistscs?cir=${circle}&div=${division}&sub=${subdivision}&sec=${mtrblc.SECNAME}&type=sec&status=PVT&stype=${stype}">${sec.stream().map(mtrblc -> mtrblc.PVT_SCS).sum()}</a></th>
							<th class="text-center">${sec.stream().map(mtrblc -> mtrblc.PVT_CB).sum()}</th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</c:if>
</div>


<script>
	requirejs([ 'jquery' ], function($) {
		/* $(document).ready(
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
				}); */
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
		
	$(document).ready(function() {
			function numDifferentiation(value) {
				var val = Math.abs(value)
				if (val >= 10000000) {
					val = (val / 10000000).toFixed(2) + ' Cr';
				} else if (val >= 100000) {
					val = (val / 100000).toFixed(2) + ' Lac';
				}
				return val;
			}
		});

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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',  title:'D-List_Abstract',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'D-List_Abstract',footer: true }
	            ]
	        }
	    });
	});
</script>