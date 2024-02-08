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
	     	$("#tyear").append("<option value="+j+">"+j+"</option>");
	     }
		 $('#fmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#fyear option[value="'+currentYear+'"]').prop('selected', true);
		 
		 $('#tmonth option:eq('+currnetMonth+')').prop('selected', true);
		 $('#tyear option[value="'+currentYear+'"]').prop('selected', true);
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
.bg-light {
    background-color: #d8e4f154 !important;
}

.CellWithComment{
  position:relative;
}

.CellComment{
  display:none;
  position:absolute; 
  z-index:100;
  border:1px;
  background-color:white;
  border-style:solid;
  border-width:1px;
  border-color:#e81a40;
  padding:3px;
  color:#e81a40; 
  top:20px; 
  left:20px;
}

.CellWithComment:hover span.CellComment{
  display:block;
}
</style>
<div class="row row-cards row-deck">
		<form class="card" action="servicewisedemandsplit" method="post">
			<div class="card-body">
				<h3 class="card-title">Service Wise Demand Split Report</h3>
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
						<label for="firstInput">From</label>
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
						<label for="firstInput">To</label>
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

					<div class="col-md-3">
						<div class="form-group">
							<label class="form-label">GET Demand Split Report</label>
							<button type="submit" class="btn btn-success">Demand Split Report</button>
						</div>
					</div>
				</div>
			</div>
		</form>

		<c:if test="${ not empty fn:trim(fail)}">
			<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
		</c:if>
		<c:if test="${ not empty fn:trim(demand)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
						<thead>
							<tr>
								<th>S.NO</th>
								<th>DATE</th>
								<th>USCNO</th>
								<th>CAT</th>
								<th>VOL in <br>kva</th>
								<th>BMD</th>
								<th>BKVAH</th>
								<th>NORM DEM CHGS</th>
								<th>NORM ENG CHGS</th>
								<th>PENAL DEM CHGS</th>
								<th>PENAL ENG CHGS</th>
								<th>VOL CHG</th>
								<th>OTHCHG</th>
								<th>ED</th>
								<th>EDI</th>
								<th>TOD CHG</th> 
								<th>COURT LPC</th>
								<th>NORMAL LPC</th>
								<th>ACD SURCHG</th>
								<th>CUSTCHG</th>
								<th>FSA</th>
								<th>TRNF</th>
								<th>TOTAL_DEMAND</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${demand}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.BTBLDT}</td>
									<td>${mtrblc.BTSCNO}</td>
									<td>${mtrblc.BTBLCAT}</td>
									<td>${mtrblc.BTACTUAL_KV}</td>
									<td>${mtrblc.BTBLKVA_HT}</td>
									<td>${mtrblc.BTBKVAH}</td>
									<td>${mtrblc.BTDEMCHG_NOR}</td>
									<td>${mtrblc.BTENGCHG_NOR}</td>
									<td>${mtrblc.BTDEMCHG_PEN}</td>
									<td>${mtrblc.BTENGCHG_PEN}</td>
									<td>${mtrblc.BTVOLTSURCHG}</td>
									<td>${mtrblc.BTOTHERCHG}</td>
									<td>${mtrblc.BTED}</td>
									<td>${mtrblc.BTED_INT}</td>
									<td>${mtrblc.BTTODCHG_HT}</td>
									<td>${mtrblc.BTCOURT_LPC}</td>
									<td>${mtrblc.BTADLCHG}</td>
									<td>${mtrblc.BTACDSURCHG}</td>
									<td>${mtrblc.BTCUSTCHG}</td>
									<td>${mtrblc.BTFSACHG}</td>
									<td>${mtrblc.BTDTRHIRE_CHG}</td>
									<td>${mtrblc.BTCURDEM}</td>
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
			$("#circle").append("<option value=ALL>ALL</option>");
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'Demand Split Report' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'Demand Split Report' }
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>