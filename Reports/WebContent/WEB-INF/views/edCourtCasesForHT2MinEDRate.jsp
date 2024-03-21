<%@page import="javafx.scene.shape.Circle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>

<div class="row row-cards row-deck">
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(courtDetais)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<h2 class="text-center">${title}</h2>
				<div class="row">
					<div class="col-md-10">
						<div class="bg-info text-white text-center"
							onclick="exportThisWithParameter('multiLevelTable', '${title}')"
							style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>
					</div>
					<div class="col-md-2">
						<div class="text-right">
							<form class="card" action="edCourtCasesReport" method="post"
								id="form">
								<input type="hidden" value='${circle}' name="circle" id="circle">
								<button type="submit" class="btn btn-link">
									<i class="fa fa-arrow-left" aria-hidden="true"></i> Back
								</button>

							</form>
							<%-- <a
										href="edCourtCasesReport?circle=${circle}">BACK</a> --%>
						</div>
					</div>
				</div>
				<table id="multiLevelTable"
					class="table table-sm card-table table-vcenter text-nowrap datatable display dataTable no-footer"
					style="width: 100%;">
					<thead class="bg-primary">
						<tr class="bg-primary text-light">
						<tr class="bg-primary text-light">
							<th class="text-center text-light">S.NO</th>
							<th class="text-center text-light">CIRCLE</th>
							<th class="text-center text-light">DIVNAME</th>
							<th class="text-center text-light">SUBNAME</th>
							<th class="text-center text-light">SECNAME</th>
							<th class="text-center text-light">USCNO</th>
							<th class="text-center text-light">NAME</th>
							<th class="text-center text-light">CAT</th>
							<th class="text-center text-light">TYPE</th>
							<th class="text-center text-light">RECORDED KVH</th>
							<th class="text-center text-light">BILLED KVH</th>
							<th class="text-center text-light">RECORDED KVH * 0.06</th>
						</tr>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="cd" items="${courtDetais}" varStatus="tagStatus">
							<tr style="font-weight: 500;">
								<td>${tagStatus.index + 1}</td>
								<td class="text-center">${cd.CIRNAME}</td>
								<td class="text-center">${cd.DIVNAME}</td>
								<td class="text-right format">${cd.SUBNAME}</td>
								<td class="text-right format">${cd.SECNAME}</td>
								<td class="text-right format">${cd.CTUSCNO}</td>
								<td class="text-left format">${cd.CTNAME}</td>
								<td class="text-right format">${cd.CTCAT}</td>
								<td class="text-right format">${cd.TYPE}</td>
								<td class="text-right format">${cd.BTRKVAH_HT}</td>
								<td class="text-right format">${cd.BTBKVAH}</td>
								<td class="text-right format">${cd.BTED}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</c:if>
</div>

<script type="text/javascript">
	var exportThisWithParameter = (function() {
		var uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"  xmlns="http://www.w3.org/TR/REC-html40"><head> <!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets> <x:ExcelWorksheet><x:Name>{worksheet}</x:Name> <x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions> </x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook> </xml><![endif]--></head><body> <table>{table}</table></body></html>', base64 = function(
				s) {
			return window.btoa(unescape(encodeURIComponent(s)))
		}, format = function(s, c) {
			return s.replace(/{(\w+)}/g, function(m, p) {
				return c[p];
			})
		}
		return function(tableID, excelName) {

			tableID = document.getElementById(tableID)
			var ctx = {
				worksheet : excelName || 'Worksheet',
				table : tableID.innerHTML
			}
			var link = document.createElement("a");
			link.download = "${title}.xls";
			link.href = uri + base64(format(template, ctx));
			link.click();

		}
	})()
</script>
<script>
	requirejs([ 'jquery' ], function($) {
		$(".format").each(
				function() {
					if ($.isNumeric($(this).text())) {
						// It isn't a number	
						$(this).html(
								parseFloat($(this).text()).toLocaleString(
										'en-IN', {
											style : 'decimal',
											currency : 'INR'
										}));
					}
				}

		)

	});
</script>
<jsp:include page="footer.jsp"></jsp:include>