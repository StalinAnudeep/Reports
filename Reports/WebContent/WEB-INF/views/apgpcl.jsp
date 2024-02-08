<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
		<form class="card" action="apgpcl" method="post">
			<div class="card-body">
				<h3 class="card-title">APGPCL_FERRO_REPORT</h3>
				<div class="row">
				    <div class="col-md-2">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select TYPE</option>
						    <option value="A">APGPCL</option>
						    <option value="F">FERRO</option>
						    <option value="B">ALL</option>
						</select>
                      </div>
                    </div>
					<div class="col-md-2">
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
					<div class="col-md-5">
						<div class="form-group">
							<label class="form-label">GET APGPCL & FERRO</label>
							<button type="submit" class="btn btn-success">GET APGPCL & FERRO</button>
						</div>
					</div>
				</div>
			</div>
		</form>

		<c:if test="${ not empty fn:trim(fail)}">
			<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
		</c:if>
		<c:if test="${ not empty fn:trim(ap)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
						<thead>
							<tr>
							    <th>S.NO</th>
								<th>MON_YEAR</th>
								<th>USCNO</th>
								<th>NAME</th>
								<th>CAT</th>
								<th>SCAT</th>
								<th>CB<br>OTHERTHAN<br>COURT</th>
								<th>COURT_CC</th>
								<th>COURT_LPC</th>
								<th>STATUS</th>
								<th>TYPE</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${ap}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.MON_YEAR}</td>
									<td>${mtrblc.USCNO}</td>
									<td>${mtrblc.NAM}</td>
									<td>${mtrblc.CAT}</td>
									<td>${mtrblc.SCAT}</td>
									<td>${mtrblc.CB_OTHERTHAN_COURT}</td>
									<td>${mtrblc.COURT_CC}</td>
									<td>${mtrblc.COURT_LPC}</td>
									<td>${mtrblc.CTSTATUS}</td>
									<td>${mtrblc.TYPE}</td>
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
			"scrollX": true,
	        dom: 'Bfrltip',
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'APGPCL_FERRO_REPORT' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'APGPCL_FERRO_REPORT' }
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>