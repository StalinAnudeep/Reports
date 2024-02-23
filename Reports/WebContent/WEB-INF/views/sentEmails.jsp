<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">
	<form class="card" action="sentEmails" method="post">
		<div class="card-body">
			<h3 class="card-title">
				<span class="text-danger">HT23A </span><strong> -Number Of Services For Which SMS & EmailS Sent</strong>
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
				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Year</label> <select id="year"
							class="form-control" name="year" required="required">
							<option value="">Select Year</option>
						</select>
					</div>
				</div>

				<div class="col-md-2">
					<div class="form-group">
						<label class="form-label">Type</label> <select id="type"
							name="type" required="required" class="form-control">
							<option value="">Select Type</option>
							<option value="SMS">SMS</option>
							<option value="EMAIL">Email</option>
						</select>
					</div>
				</div>

				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Get Number Of Services For Which
							SMS & EmailS Sent</label>
						<button type="submit" class="btn btn-success">Get Number
							Of Services For Which SMS & EmailS Sent</button>
					</div>
				</div>
			</div>
		</div>
	</form>


	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>

	<c:if test="${ type eq 'EMAIL'}">

		<c:if test="${ not empty fn:trim(mailAndSmsReport)}">
			<div class="card ">
				<div
					class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
					<h2 class="text-center text-primary">${title}</h2>
					<table class="table card-table table-vcenter text-nowrap datatable"
						style="width: 100%;">
						<thead class="bg-primary">

							<tr>
								<th class="text-center text-light">SNO</th>
								<th class="text-center text-light">USCNO</th>
								<th class="text-center text-light">NAME</th>
								<th class="text-center text-light">CAT</th>
								<th class="text-center text-light">SUBCAT</th>
								<th class="text-center text-light">EMAIL</th>
							</tr>

						</thead>
						<tbody>
							<c:forEach var="mail" items="${mailAndSmsReport}"
								varStatus="tagStatus">

								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${mail.CTUSCNO}</td>
									<td>${mail.CTNAME}</td>
									<td>${mail.CTCAT}</td>
									<td>${mail.CTSUBCAT}</td>
									<td>${mail.EMAIL}</td>
								</tr>

							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:if>
	</c:if>

	<c:if test="${ type eq 'SMS'}">

		<c:if test="${ not empty fn:trim(mailAndSmsReport)}">
			<div class="card ">
				<div
					class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
					<h2 class="text-center text-primary">${title}</h2>
					<table class="table card-table table-vcenter text-nowrap datatable"
						style="width: 100%;">
						<thead class="bg-primary">

							<tr>
								<th class="text-center text-light">SNO</th>
								<th class="text-center text-light">USCNO</th>
								<th class="text-center text-light">NAME</th>
								<th class="text-center text-light">CAT</th>
								<th class="text-center text-light">SUBCAT</th>
								<th class="text-center text-light">MOBILENUMBER</th>
							</tr>

						</thead>
						<tbody>
							<c:forEach var="sms" items="${mailAndSmsReport}"
								varStatus="tagStatus">

								<tr>
									<td>${tagStatus.index + 1}</td>
									<td>${sms.CTUSCNO}</td>
									<td>${sms.CTNAME}</td>
									<td>${sms.CTCAT}</td>
									<td>${sms.CTSUBCAT}</td>
									<td>${sms.MOBILE}</td>
								</tr>

							</c:forEach>
						</tbody>

					</table>
				</div>
			</div>
		</c:if>
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
		$('#type option:eq(' + currentType + ')').prop('selected', true);

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