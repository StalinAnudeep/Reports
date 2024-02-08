<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
		<form class="card" action="amr" method="post">
			<div class="card-body">
				<h3 class="card-title">AMR READING CHECKLIST</h3>
				<div class="row">
				    <div class="col-md-2">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
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
							<label class="form-label">GET AMR CheckList</label>
							<button type="submit" class="btn btn-success">GET AMR CheckList</button>
						</div>
					</div>
				</div>
			</div>
		</form>

		<c:if test="${ not empty fn:trim(fail)}">
			<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
		</c:if>
		<c:if test="${ not empty fn:trim(amr)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
						<thead>
							<tr>
								<th>S.NO</th>
								<th>MSCNO</th>
								<th>CTNAME</th>
								<th>CAT</th>
								<th>MF</th>
								<th>OPN<BR>READING<BR>DATE</th>
								<th>CLS<BR>READING<BR>DATE</th>
								<th>OPN	<BR>KWH</th>
								<th>CLS	<BR>KWH</th>
								<th>REC<BR>KWH</th>
								<th>KWH<BR>UNITS</th>
								<th>OPN	<BR>KVAH</th>
								<th>CLS	<BR>KVAH</th>
								<th>REC<BR>KVAH</th>
								<th>KVAH<BR>UNITS</th>
								<th>REC<BR>KVA</th>
								<th>OPN	<BR>TOD1</th>
								<th>CLS	<BR>TOD1</th>
								<th>REC<BR>TOD1</th>
								<th>TOD1<BR>UNITS</th>
								<th>OPN	<BR>TOD2</th>
								<th>CLS	<BR>TOD2</th>
								<th>REC<BR>TOD2</th>
								<th>TOD2<BR>UNITS</th>
								<th>OPN	<BR>TOD5</th>
								<th>CLS	<BR>TOD5</th>
								<th>REC<BR>TOD5</th>
								<th>TOD5<BR>UNITS</th>
								<th>OPN	<BR>TOD6</th>
								<th>CLS	<BR>TOD6</th>
								<th>REC<BR>TOD6</th>
								<th>TOD6<BR>UNITS</th>
								<th >AMR</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${amr}" varStatus="tagStatus">
								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mtrblc.MSCNO}</td>
									<td>${mtrblc.CTNAME}</td>
									<td>${mtrblc.CTCAT}</td>
									<td>${mtrblc.MDMF_HT}</td>
									
									<td><fmt:formatDate pattern = "dd-MM-yyyy" value = "${mtrblc.MDOPNRDG_DT}"/></td>
									<td><fmt:formatDate pattern = "dd-MM-yyyy" value = "${mtrblc.MDCLRDG_DT}" /></td>
						
									<td>${mtrblc.MDOPNKWH_HT}</td>
									<td>${mtrblc.MDCLKWH_HT}</td>
									<td>${mtrblc.MDRKWH_HT}</td>
									<td>${mtrblc.MDRECKWH_HT}</td>
									
									<td>${mtrblc.MDOPNKVAH_HT}</td>
									<td>${mtrblc.MDCLKVAH_HT}</td>
									<td>${mtrblc.MDRKVAH_HT}</td>
									<td>${mtrblc.MDRECKVAH_HT}</td>
									
									
									
									<td>${mtrblc.MDRECKVA_HT}</td>
									
									<td>${mtrblc.MDTOD1_OPN}</td>
									<td>${mtrblc.MDTOD1_CLS}</td>
									<td>${mtrblc.MDTOD1_RKVAH}</td>
									<td>${mtrblc.MDTOD1_RECKVAH}</td>
									
									<td>${mtrblc.MDTOD2_OPN}</td>
									<td>${mtrblc.MDTOD2_CLS}</td>
									<td>${mtrblc.MDTOD2_RKVAH}</td>
									<td>${mtrblc.MDTOD2_RECKVAH}</td>
									
									<td>${mtrblc.MDTOD5_OPN}</td>
									<td>${mtrblc.MDTOD5_CLS}</td>
									<td>${mtrblc.MDTOD5_RKVAH}</td>
									<td>${mtrblc.MDTOD5_RECKVAH}</td>
									
									<td>${mtrblc.MDTOD6_OPN}</td>
									<td>${mtrblc.MDTOD6_CLS}</td>
									<td>${mtrblc.MDTOD6_RKVAH}</td>
									<td>${mtrblc.MDTOD6_RECKVAH}</td>
									
									<td>${mtrblc.MDAMR_FLAG}</td>
									
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'AMR' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'AMR' }
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>