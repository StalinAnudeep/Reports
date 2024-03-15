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

	<c:if test="${ not empty fn:trim(openAccessSubDivisionDetails)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<table
					class="table card-table table-vcenter text-nowrap datatable display"
					style="width: 100%;">
					<thead>
						<tr>
							<th class="bg-primary text-white text-center" colspan="14">${title}</th>
						</tr>
						<tr>
							<th>S.NO</th>
							<th>SUBDIVISION</th>
							<th class="text-right">APR-${FI}</th>
							<th class="text-right">MAY-${FI}</th>
							<th class="text-right">JUN-${FI}</th>
							<th class="text-right">JUL-${FI}</th>
							<th class="text-right">AUG-${FI}</th>
							<th class="text-right">SEP-${FI}</th>
							<th class="text-right">OCT-${FI}</th>
							<th class="text-right">NOV-${FI}</th>
							<th class="text-right">DEC-${FI}</th>
							<th class="text-right">JAN-${SI}</th>
							<th class="text-right">FEB-${SI}</th>
							<th class="text-right">MAR-${SI}</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${openAccessSubDivisionDetails}"
							varStatus="tagStatus">
							<tr style="font-weight: 500;">
								<td>${tagStatus.index + 1}</td>
								<%
								int s = 0;
								%>
								<c:forEach items="${mtrblc}" var="entry">
									<%
									if (s == 0) {
									%>
									<td class="text-right"><a
										href="openAccessReportForSection?subDivision=${mtrblc.SUBNAME}&fyear=${year}">${entry.value}</a></td>
									<%
									s++;
									} else {
									%>
									<td class="text-right">${entry.value}</td>
									<%
									}
									%>

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