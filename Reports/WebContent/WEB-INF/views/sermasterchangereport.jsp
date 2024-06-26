<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
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

.null {
	font-weight: bold;
}
</style>
<script>


</script>
<div class="row row-cards row-deck">
	<form class="card" action="sermasterchangereport" method="post">
		<div class="card-body">
			<h3 class="card-title"><strong>
				<span class="text-danger">HT09A</span> - Service Wise Change History HT
				</strong>
			</h3>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label for="inputCity">Enter Service
								Number</label> <input type="text" class="form-control"
							required="required" name="scno" id="scno"
							placeholder="Enter Service Number">
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label for="firstInput" class="form-label">From</label>
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
						<label for="firstInput" class="form-label">To</label>
						<div>
							<select class="form-control firstInput" name="tmonth"
								required="required" id="tmonth" ng-model="obj.month">
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
							</select> <select class="form-control secondInput" name="tyear"
								required="required" id="tyear" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div>		

				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">GET ChangeHistory</label>
						<button type="submit" class="btn btn-success">GET ChangeHistory</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(error)}">
		<div id="exist" class="alert alert-danger" role="alert">${error}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(list)}">
		  <div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					<thead>
						<tr>
							<th>S.NO</th>
							<th>USCNO</th>
							<th>TYPE</th>
							<th>OLD VALUE</th>
							<th>NEW VALUE</th>
							<th>CREATION DATE</th>
							<th>CHANGE DATE</th>
							<th>DEPT_ORDER_NO</th>
							<th>DEPT_ORDER_DT</th>
							<th>CHANGED_BY</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${list}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.CHG_TYPE_CD}</td>
								<td>${mtrblc.OLD_VAL}</td>
								<td>${mtrblc.NEW_VAL}</td>
								<td>${mtrblc.CRE_DTTM}</td>
								<td>${mtrblc.EFF_DT}</td>
								<td>${mtrblc.DEPT_ORDER_NO}</td>
								<td>${mtrblc.DEPT_ORDER_DT}</td>
								<td>${mtrblc.CHANGED_BY}</td>
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
		 for (var j = currentYear; j > 2015; j--) {
	     	$("#fyear").append("<option value="+j+">"+j+"</option>");
	     	$("#tyear").append("<option value="+j+">"+j+"</option>");
	     	
		 }
		 $('#fmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#fyear option[value="'+currentYear+'"]').prop('selected', true);
		 
		 $('#tmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#tyear option[value="'+currentYear+'"]').prop('selected', true);
		 
			 $.ajax({
			 		    type:"POST",
			 	        url:"getcodes",
			 	        success: function(data) 
			 	        {
			 	        	var saptype=jQuery.parseJSON(data);
			 	        	$("#mcode").append("<option value='ALL'>ALL</option>")
			 	        	$.each(saptype, function( k, v ) {
			 		    		$("#mcode").append("<option value="+k+">"+v+"</option>") 
			 	        	});
			 	        }
			 	 });
	});
</script>
<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
		window.JSZip = jszip;
		var currentYear = (new Date()).getFullYear();
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'ChangeHistory',footer: true },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'ChangeHistory',footer: true }
	            ]
	        }
	    });
		
		
		
		
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>