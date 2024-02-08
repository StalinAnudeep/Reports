	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="cbmdexceed" method="post">
                <div class="card-body">
                  <h3 class="card-title">BMD Exceeded Report CPDCL</h3>
                  <div class="row">
                     <div class="col-md-3">
					      <label for="inputState">Month</label>
					      <select id="mon" class="form-control" name="month" required="required">
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
                        <label class="form-label">Year</label>
                       <select class="form-control" name="year" required="required" id="year">
					      	<option value="">--Select Year-- </option>
					    </select>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Get Details</label>
                        <button type="submit" class="btn btn-success">Get Details</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(bmd)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>CIRCLE<br>NAME</th>
						<th>SCS</th>
						<th>PENAL<br>DEMAND(KVA)</th>
						<th>PENAL <br>DEMAND<br> CHARGES(Rs)</th>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${bmd}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td><a href="cbmddivision?cir=${mtrblc.CIRCLE}&mon_year=${mon_year}">${mtrblc.CIRCLE}</a></td>
							<td class="text-right"><a href="cscscircle?cir=${mtrblc.CIRCLE}&mon_year=${mon_year}">${mtrblc.SCS}</a></td>
							<td class="text-right">${mtrblc.PENAL_MD}</td>
							<td class="text-right">${mtrblc.PENAL_DEMAND_CHARGES}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right"><a href="allcircle?mon_year=${mon_year}">${bmd.stream().map(mtrblc -> mtrblc.SCS).sum()}</a></th>
							<th class="text-right">${bmd.stream().map(mtrblc -> mtrblc.PENAL_MD).sum()}</th>
							<th class="text-right">${bmd.stream().map(mtrblc -> mtrblc.PENAL_DEMAND_CHARGES).sum()}</th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>
<c:if test="${ not empty fn:trim(division)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>Division<br>NAME</th>
						<th>SCS</th>
						<th>PENAL<br>DEMAND(KVA)</th>
						<th>PENAL <br>DEMAND<br> CHARGES(Rs)</th>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${division}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td><a href="cbmdsubdivision?div=${mtrblc.DIVNAME}&mon_year=${mon_year}&cir=${circle}">${mtrblc.DIVNAME}</a></td>
							<td class="text-right"><a href="cscsdivision?div=${mtrblc.DIVNAME}&mon_year=${mon_year}&cir=${circle}">${mtrblc.SCS}</a></td>
							<td class="text-right">${mtrblc.PENAL_MD}</td>
							<td class="text-right">${mtrblc.PENAL_DEMAND_CHARGES}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right"><a href="calldivision?mon_year=${mon_year}&cir=${circle}">${division.stream().map(mtrblc -> mtrblc.SCS).sum()}</a></th>
							<th class="text-right">${division.stream().map(mtrblc -> mtrblc.PENAL_MD).sum()}</th>
							<th class="text-right">${division.stream().map(mtrblc -> mtrblc.PENAL_DEMAND_CHARGES).sum()}</th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>


<c:if test="${ not empty fn:trim(subdivision)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>SubDivision<br>NAME</th>
						<th>SCS</th>
						<th>PENAL<br>DEMAND(KVA)</th>
						<th>PENAL <br>DEMAND<br> CHARGES(Rs)</th>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${subdivision}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td><a href="cbmdsection?div=${divisionName}&mon_year=${mon_year}&cir=${circle}&sub=${mtrblc.SUBNAME}">${mtrblc.SUBNAME}</a></td>
							<td class="text-right"><a href="cscssubdivision?div=${divisionName}&mon_year=${mon_year}&cir=${circle}&sub=${mtrblc.SUBNAME}">${mtrblc.SCS}</a></td>
							<td class="text-right">${mtrblc.PENAL_MD}</td>
							<td class="text-right">${mtrblc.PENAL_DEMAND_CHARGES}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right"><a href="callsubdivision?div=${divisionName}&mon_year=${mon_year}&cir=${circle}&sub=${sub}">${subdivision.stream().map(mtrblc -> mtrblc.SCS).sum()}</a></th>
							<th class="text-right">${subdivision.stream().map(mtrblc -> mtrblc.PENAL_MD).sum()}</th>
							<th class="text-right">${subdivision.stream().map(mtrblc -> mtrblc.PENAL_DEMAND_CHARGES).sum()}</th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>

<c:if test="${ not empty fn:trim(section)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>Section<br>NAME</th>
						<th>SCS</th>
						<th>PENAL<br>DEMAND(KVA)</th>
						<th>PENAL <br>DEMAND<br> CHARGES(Rs)</th>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${section}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.SECNAME}</td>
							<td class="text-right">${mtrblc.SCS}</td>
							<td class="text-right">${mtrblc.PENAL_MD}</td>
							<td class="text-right">${mtrblc.PENAL_DEMAND_CHARGES}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right"><a href="callsections?div=${divisionName}&mon_year=${mon_year}&cir=${circle}&sub=${sub}">${section.stream().map(mtrblc -> mtrblc.SCS).sum()}</a></th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.PENAL_MD).sum()}</th>
							<th class="text-right">${section.stream().map(mtrblc -> mtrblc.PENAL_DEMAND_CHARGES).sum()}</th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>




<c:if test="${ not empty fn:trim(divscs)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		    <center><h6>Service Wise Report ${mon_year}--${circle}</h6></center>
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>MONTH</th>
						<th>CIRCLE</th>
						<th>DIVISION<br>NAME</th>
						<th>SUB<br>NAME</th>
						<th>SECTION</th>
						<th>USCNO</th>
						<th>CAT</th>
						<th>VOLTAGE</th>
						<th>CONSUMER_NAME</th>
						<th>CMD</th>
						<th>BMD</th>
						<th>PENALMD</th>
						<th>PENAL<br>DEMAND<br>CHARGES</th>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${divscs}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td> <fmt:formatDate pattern = "dd-MM-yyyy" value = "${mtrblc.MONTH}" /></td>
							<td>${mtrblc.CIRCLE}</td>
							<td>${mtrblc.DIVNAME}</td>
							<td>${mtrblc.SUBNAME}</td>
							<td>${mtrblc.SECNAME}</td>
							<td>${mtrblc.USCNO}</td>
							<td>${mtrblc.CATEGORY}</td>
							<td>${mtrblc.VOLTAGE}</td>
							<td>${mtrblc.CONSUMER_NAME}</td>
							<td>${mtrblc.CMD}</td>
							<td>${mtrblc.BMD}</td>
							<td>${mtrblc.PENAL_MD}</td>
							<td>${mtrblc.PENAL_DEMAND_CHARGES}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="12" class="text-right">Grand Total</th>
							<th class="text-right">${divscs.stream().map(mtrblc -> mtrblc.PENAL_MD).sum()}</th>
							<th class="text-right">${divscs.stream().map(mtrblc -> mtrblc.PENAL_DEMAND_CHARGES).sum()}</th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>


<c:if test="${ not empty fn:trim(scsdivision)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		    <center><h6>Service Wise Report ${mon_year}--${circle}</h6></center>
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>MONTH</th>
						<th>CIRCLE</th>
						<th>DIVISION<br>NAME</th>
						<th>SUB<br>NAME</th>
						<th>SECTION</th>
						<th>USCNO</th>
						<th>CAT</th>
						<th>VOLTAGE</th>
						<th>CONSUMER_NAME</th>
						<th>CMD</th>
						<th>BMD</th>
						<th>PENALMD</th>
						<th>PENAL<br>DEMAND<br>CHARGES</th>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${scsdivision}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td> <fmt:formatDate pattern = "dd-MM-yyyy" value = "${mtrblc.MONTH}" /></td>
							<td>${mtrblc.CIRCLE}</td>
							<td>${mtrblc.DIVNAME}</td>
							<td>${mtrblc.SUBNAME}</td>
							<td>${mtrblc.SECNAME}</td>
							<td>${mtrblc.USCNO}</td>
							<td>${mtrblc.CATEGORY}</td>
							<td>${mtrblc.VOLTAGE}</td>
							<td>${mtrblc.CONSUMER_NAME}</td>
							<td>${mtrblc.CMD}</td>
							<td>${mtrblc.BMD}</td>
							<td>${mtrblc.PENAL_MD}</td>
							<td>${mtrblc.PENAL_DEMAND_CHARGES}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="12" class="text-right">Grand Total</th>
							<th class="text-right">${divscs.stream().map(mtrblc -> mtrblc.PENAL_MD).sum()}</th>
							<th class="text-right">${divscs.stream().map(mtrblc -> mtrblc.PENAL_DEMAND_CHARGES).sum()}</th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>



<c:if test="${ not empty fn:trim(scssub)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		    <center><h6>Service Wise Report ${mon_year}--${circle}</h6></center>
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>MONTH</th>
						<th>CIRCLE</th>
						<th>DIVISION<br>NAME</th>
						<th>SUB<br>NAME</th>
						<th>SECTION</th>
						<th>USCNO</th>
						<th>CAT</th>
						<th>VOLTAGE</th>
						<th>CONSUMER_NAME</th>
						<th>CMD</th>
						<th>BMD</th>
						<th>PENALMD</th>
						<th>PENAL<br>DEMAND<br>CHARGES</th>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${scssub}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td> <fmt:formatDate pattern = "dd-MM-yyyy" value = "${mtrblc.MONTH}" /></td>
							<td>${mtrblc.CIRCLE}</td>
							<td>${mtrblc.DIVNAME}</td>
							<td>${mtrblc.SUBNAME}</td>
							<td>${mtrblc.SECNAME}</td>
							<td>${mtrblc.USCNO}</td>
							<td>${mtrblc.CATEGORY}</td>
							<td>${mtrblc.VOLTAGE}</td>
							<td>${mtrblc.CONSUMER_NAME}</td>
							<td>${mtrblc.CMD}</td>
							<td>${mtrblc.BMD}</td>
							<td>${mtrblc.PENAL_MD}</td>
							<td>${mtrblc.PENAL_DEMAND_CHARGES}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="12" class="text-right">Grand Total</th>
							<th class="text-right">${scssub.stream().map(mtrblc -> mtrblc.PENAL_MD).sum()}</th>
							<th class="text-right">${scssub.stream().map(mtrblc -> mtrblc.PENAL_DEMAND_CHARGES).sum()}</th>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</c:if>





<c:if test="${ not empty fn:trim(allcircle)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		    <center><h6>Service Wise Report ${mon_year}--${circle}</h6></center>
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>MONTH</th>
						<th>CIRCLE</th>
						<th>DIVISION<br>NAME</th>
						<th>SUB<br>NAME</th>
						<th>SECTION</th>
						<th>USCNO</th>
						<th>CAT</th>
						<th>VOLTAGE</th>
						<th>CONSUMER_NAME</th>
						<th>CMD</th>
						<th>BMD</th>
						<th>PENALMD</th>
						<th>PENAL<br>DEMAND<br>CHARGES</th>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${allcircle}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td> <fmt:formatDate pattern = "dd-MM-yyyy" value = "${mtrblc.MONTH}" /></td>
							<td>${mtrblc.CIRCLE}</td>
							<td>${mtrblc.DIVNAME}</td>
							<td>${mtrblc.SUBNAME}</td>
							<td>${mtrblc.SECNAME}</td>
							<td>${mtrblc.USCNO}</td>
							<td>${mtrblc.CATEGORY}</td>
							<td>${mtrblc.VOLTAGE}</td>
							<td>${mtrblc.CONSUMER_NAME}</td>
							<td>${mtrblc.CMD}</td>
							<td>${mtrblc.BMD}</td>
							<td>${mtrblc.PENAL_MD}</td>
							<td>${mtrblc.PENAL_DEMAND_CHARGES}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th colspan="12" class="text-right">Grand Total</th>
							<th class="text-right">${allcircle.stream().map(mtrblc -> mtrblc.PENAL_MD).sum()}</th>
							<th class="text-right">${allcircle.stream().map(mtrblc -> mtrblc.PENAL_DEMAND_CHARGES).sum()}</th>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:'BMD Exceeded Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'BMD Exceeded Report'}
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
<jsp:include page="footer.jsp"></jsp:include>