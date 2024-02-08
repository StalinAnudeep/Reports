<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	      <form class="card" action="ConsumerMaster" method="post">
                <div class="card-body">
                  <h3 class="card-title"> <strong><span class="text-danger">HT18</span> -  Consumer Master Report </strong></h3>
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">GET  Consumer Master Report</label>
                        <button type="submit" class="btn btn-success">GET  Consumer Master Report</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(error)}">
		<div id="exist" class="alert alert-danger" role="alert">${error}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(consumer)}">
			<div class="card ">	
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable" style="width: 100%;">
				<thead>
					<tr>
						<th>S.NO</th>
						<th>SCNO</th>
						<th>SUB<br>DIVISION</th>
						<th>FEEDER <br>CODE</th>
						<th>NAME</th>
						<th>ADD1</th>
						<th>ADD2</th>
						<th>ADD3</th>
						<th>ADD4</th>
						<th>CAT</th>
						<th>SUB CAT</th>
						<th>CMD</th>
						<th>MOBILE</th>
						<th>EMAIL</th>
						<th>NATURE</th>
						<th>STATUS</th>
						<th>MTRNO</th>
						<th>METER MAKE</th>
						<th>SUPPLY<br>RELEASE<br>DATE</th>
						<th>ACTUAL<br>VOLTAGE<br>SUPPLY</th>
						<th>MF</th>
						<th>EXTENT<br>OF<br>LAND</th>
						<th>SURVEY<br>NUMBER</th>
						<th>LINKED<br>SCNO1</th>
						<th>TYPE<br>OF<br>LAND</th>
						<th>ED Flag	</th>
						<th>TDS Flag</th>
						<th>Solar Flag	</th>
						<th>SCST Flag</th>
						<th>Aqua Flag</th>
						<th>Colony Flag	</th>
						<th>LF Flag</th>
						<th>Meter Side Flag</th>
						<th>Seasonal Flag</th>
						<th>GOVT/PVT</th>
						<th>DEPT CODE</th>
						<th>DEPT NAME</th>
						<th>HOD NAME</th>
						<th>SD</th>
						<th>CB</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mtrblc" items="${consumer}" varStatus="tagStatus">
						<tr>
							<td>${tagStatus.index + 1}</td>
							<td>${mtrblc.CTUSCNO}</td>
							<td>${mtrblc.SUBNAME}</td>
							<td>${mtrblc.CTFEEDER_CODE}</td>
							<td>${mtrblc.CTNAME}</td>
							<td>${mtrblc.CTADD1}</td>
							<td>${mtrblc.CTADD2}</td>
							<td>${mtrblc.CTADD3}</td>
							<td>${mtrblc.CTADD4}</td>
							<td>${mtrblc.CTCAT}</td>
							<td>${mtrblc.CTSUBCAT}</td>
							<td>${mtrblc.CTCMD_HT}</td>
							<td>${mtrblc.CTMOBILE}</td>
							<td>${mtrblc.CTEMAILID}</td>
							<td>${mtrblc.STDESC}</td>
							<td>${mtrblc.CTSTATUS}</td>
							<td>${mtrblc.MDMTRNO}</td>
							<td>${mtrblc.METER_MAKE}</td>
							<td>${mtrblc.CTSUPCONDT}</td>
							<td>${mtrblc.CTACTUAL_KV}</td>
							<td>${mtrblc.MDMF_HT}</td>
							<td>${mtrblc.EXTENT_OF_LAND}</td>
							<td>${mtrblc.SURVEY_NUMBER}</td>
							<td>${mtrblc.LINKED_SCNO1}</td>
							<td>${mtrblc.TYPE_OF_LAND}</td>
							<td>${mtrblc.CTEDFLAG}</td>
							<td>${mtrblc. CTTDS_FLAG}</td> 
							<td>${mtrblc. CTSOLAR_FLAG}</td> 
							 <td>${mtrblc.CTSCSTFLAG}</td>
							 <td>${mtrblc.CTAQUA_FLAG}</td> 
							 <td>${mtrblc.CTCOLNYFLAG_HT}</td> 
							 <td>${mtrblc.CTLF_FLAG}</td>
							<td>${mtrblc. CT_METERSIDE_FLAG}</td> 
							<td>${mtrblc. CTSEASFLAG_HT}</td>
							<td>${mtrblc. TYPE}</td>
							<td>${mtrblc. DEPTCODE}</td>
							<td>${mtrblc. DEPT_NAME}</td>
							<td>${mtrblc. HOD_NAME}</td>
							<td>${mtrblc. SD}</td>
							<td>${mtrblc. CB}</td>
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
	     	$("#year").append("<option value="+j+">"+j+"</option>");
		 }
		 
		 $('#mon option:eq('+currnetMonth+')').prop('selected', true);
		 $('#year option[value="'+currentYear+'"]').prop('selected', true);

	});
</script>
<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
	    var circle='<c:out value="${circle}"/>'
	    console.log(circle+"====");
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title:"Circle Wise Category Existing Service And Load of  "+""+circle},
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:"Circle Wise Category Existing Service And Load of "+" "+circle}
	            ]
	        }
	    });
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>
