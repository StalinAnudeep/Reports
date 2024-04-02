<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="htServicesReport" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<span class="text-danger">HT 143 </span><strong> -HT
					Services Information </strong>
			</h3>
			<div class="row">
				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div>

				<div class="col-md-2">
					<div class="form-group">
						<label for="inputState">Month</label> <select id="mon"
							class="form-control" name="month">
							<option value="">Select</option>
							<option value="01">JANUARY</option>
							<option value="02">FEBRUARY</option>
							<option value="03">MARCH</option>
							<option value="04">APRIL</option>
							<option value="05">MAY</option>
							<option value="06">JUNE</option>
							<option value="07">JULY</option>
							<option value="08">AUGUST</option>
							<option value="09">SEPTEMBER</option>
							<option value="10">OCTOBER</option>
							<option value="11">NOVEMBER</option>
							<option value="12">DECEMBER</option>
						</select>
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Year</label> <select id="year"
							class="form-control" name="year" required="required">
							<option value="">Select Year</option>
						</select>
					</div>
				</div>

				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Get Ht Services</label>
						<button type="submit" class="btn btn-success">Get Ht
							Services</button>
					</div>
				</div>
			</div>
		</div>
	</form>


	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>



	<c:if test="${ not empty fn:trim(list)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center text-primary">${title}</h2>
				<table class="table card-table table-vcenter text-nowrap datatable"
					style="width: 100%;">
					<thead class="bg-primary">

						<tr style="font-weight: bold;">
							<th class="text-center text-light" rowspan="2">SNO</th>
							<th class="text-center text-light" rowspan="2">CIRCLE</th>
							<th class="text-center text-light" colspan="6">SERVICES
								RELEASED DURING THE MONTH OF ${mon_year}</th>
							<th class="text-center text-light" colspan="6">CAT WISE HT
								SERVICES EXISTING AS ON ${mon_year} (LIVE+UDC)</th>
							<th class="text-center text-light" colspan="4">HT SERVICES
								EXISTING AS ON ${mon_year}</th>
						</tr>

						<tr>
							<th class="text-center text-light">HT1</th>
							<th class="text-center text-light">HT2</th>
							<th class="text-center text-light">HT3</th>
							<th class="text-center text-light">HT4</th>
							<th class="text-center text-light">HT5</th>
							<th class="text-center text-light">TOTAL</th>
							<th class="text-center text-light">HT1</th>
							<th class="text-center text-light">HT2</th>
							<th class="text-center text-light">HT3</th>
							<th class="text-center text-light">HT4</th>
							<th class="text-center text-light">HT5</th>
							<th class="text-center text-light">TOTAL</th>
							<th class="text-center text-light">LIVE</th>
							<th class="text-center text-light">UDC</th>
							<th class="text-center text-light">BILLSTOP</th>
							<th class="text-center text-light">TOTAL</th>

						</tr>

					</thead>
					<tbody>
						<c:forEach var="SD" items="${list}" varStatus="tagStatus">

							<tr style="font-weight: 500;">
								<td>${tagStatus.index + 1}</td>
								<td>${SD.releasedCircle}</td>
								<td>${SD.releasedHt1}</td>
								<td>${SD.releasedHt2}</td>
								<td>${SD.releasedHt3}</td>
								<td>${SD.releasedHt4}</td>
								<td>${SD.releasedHt5}</td>
								<td class="text-primary">${SD.releasedHt1 + SD.releasedHt2+ SD.releasedHt3 +SD.releasedHt4 + SD.releasedHt5}</td>
								<td>${SD.existingHt1}</td>
								<td>${SD.existingHt2}</td>
								<td>${SD.existingHt3}</td>
								<td>${SD.existingHt4}</td>
								<td>${SD.existingHt5}</td>
								<td class="text-primary">${SD.existingHt1 + SD.existingHt2+ SD.existingHt3 +SD.existingHt4 + SD.existingHt5}</td>
								<td>${SD.live}</td>
								<td>${SD.UDC}</td>
								<td>${SD.billStop}</td>
								<td class="text-primary">${SD.live + SD.UDC + SD.billStop}</td>
							</tr>

						</c:forEach>
					</tbody>
					<tfoot>
						<c:if test="${circle eq 'ALL'}">
							<tr style="font-weight: bold;">
								<th class="text-right" colspan="2">APCPDCL Total</th>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.releasedHt1).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.releasedHt2).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.releasedHt3).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.releasedHt4).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.releasedHt5).sum()}</td>
								<td class="text-left text-primary format">${ list.stream().map(sd -> sd.releasedHt1).sum()
								+ list.stream().map(sd -> sd.releasedHt2).sum() 
								+list.stream().map(sd -> sd.releasedHt3).sum()
								+list.stream().map(sd -> sd.releasedHt4).sum() +
								list.stream().map(sd -> sd.releasedHt5).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.existingHt1).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.existingHt2).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.existingHt3).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.existingHt4).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.existingHt5).sum()}</td>
								<td class="text-left text-primary format">${ list.stream().map(sd -> sd.existingHt1).sum()
								+ list.stream().map(sd -> sd.existingHt2).sum() 
								+list.stream().map(sd -> sd.existingHt3).sum()
								+list.stream().map(sd -> sd.existingHt4).sum() +
								list.stream().map(sd -> sd.existingHt5).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.live).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.UDC).sum()}</td>
								<td class="text-left text-primary format">${list.stream().map(sd -> sd.billStop).sum()}</td>
								<td class="text-left text-primary format">${ list.stream().map(sd -> sd.UDC).sum()
								+ list.stream().map(sd -> sd.live).sum() 
								+list.stream().map(sd -> sd.billStop).sum()}</td>
							</tr>
						</c:if>
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
		var currnetMonth = (new Date()).getMonth() + 1;

		for (var j = currentYear; j > 2015; j--) {
			$("#year").append("<option value="+j+">" + j + "</option>");

		}
		$('#mon option:eq(' + currnetMonth + ')').prop('selected', true);
		$('#year option[value="' + currentYear + '"]').prop('selected', true);

	});
</script>

<script>
	require([ 'jquery', 'datatables.net', 'datatables.net-jszip',
			'datatables.net-buttons', 'datatables.net-buttons-flash',
			'datatables.net-buttons-html5' ], function($, datatable, jszip) {
		window.JSZip = jszip;
		$('.datatable').DataTable({
			dom : 'Bfrltip',
			"scrollX" : true,
			buttons : {
				buttons : [ {
					extend : 'csv',
					className : 'btn btn-xs btn-primary',

					footer : true
				}, {
					extend : 'excel',
					className : 'btn btn-xs btn-primary',
					footer : true
				} ]
			}
		});
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>