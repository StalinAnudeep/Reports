<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<div class="row row-cards row-deck">

	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>

	<c:if test="${ not empty fn:trim(wheelingChargesSectionDetails)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">${title}</h2>
				<table
					class="table card-table table-vcenter text-nowrap datatable display"
					style="width: 100%;">
					<thead>
						<tr class="bg-primary text-white text-center">
							<th class="text-white" rowspan="2"
								style="vertical-align: middle;">S.NO</th>
							<th class="text-white" rowspan="2"
								style="vertical-align: middle;">DIVISION</th>
							<th class="text-white" colspan="2">APR-${FI}</th>
							<th class="text-white" colspan="2">MAY-${FI}</th>
							<th class="text-white" colspan="2">JUN-${FI}</th>
							<th class="text-white" colspan="2">JUL-${FI}</th>
							<th class="text-white" colspan="2">AUG-${FI}</th>
							<th class="text-white" colspan="2">SEP-${FI}</th>
							<th class="text-white" colspan="2">OCT-${FI}</th>
							<th class="text-white" colspan="2">NOV-${FI}</th>
							<th class="text-white" colspan="2">DEC-${FI}</th>
							<th class="text-white" colspan="2">JAN-${SI}</th>
							<th class="text-white" colspan="2">FEB-${SI}</th>
							<th class="text-white" colspan="2">MAR-${SI}</th>
						</tr>
						<tr class="bg-primary text-white text-center">
							<c:forEach var="i" begin="1" end="12">
								<th class="text-white">NOS</th>
								<th class="text-white">AMT</th>
							</c:forEach>

						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${wheelingChargesSectionDetails}"
							varStatus="tagStatus">
							<tr style="font-weight: 500;">
								<td>${tagStatus.index + 1}</td>
								<c:forEach items="${mtrblc}" var="entry">
									<td class="text-right">${entry.value}</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</c:if>
</div>
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
					title : 'ACD_REPORT'
				}, {
					extend : 'excel',
					className : 'btn btn-xs btn-primary',
					title : 'ACD_REPORT'
				} ]
			}
		});
	});
</script>
<jsp:include page="footer.jsp"></jsp:include>
