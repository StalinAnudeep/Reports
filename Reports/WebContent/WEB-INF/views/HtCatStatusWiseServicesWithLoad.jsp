<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<script>
	requirejs([ 'jquery' ], function($) {
		$(document).ready(
				function() {
					var currentYear = (new Date()).getFullYear();
					var currnetMonth = (new Date()).getMonth() ;
					for (var j = currentYear; j > 2015; j--) {
						$("#fyear").append(
								"<option value="+j+">" + j + "</option>");
						$("#tyear").append(
								"<option value="+j+">" + j + "</option>");
					}
					$('#fmonth option:eq(' + currnetMonth + ')').prop(
							'selected', true);
					$('#fyear option[value="' + currentYear + '"]').prop(
							'selected', true);

					/*  $('#tmonth option:eq('+currnetMonth+')').prop('selected', true);
					 $('#tyear option[value="'+currentYear+'"]').prop('selected', true); */
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

.CellWithComment {
	position: relative;
}

.CellComment {
	display: none;
	position: absolute;
	z-index: 100;
	border: 1px;
	background-color: white;
	border-style: solid;
	border-width: 1px;
	border-color: #e81a40;
	padding: 3px;
	color: #e81a40;
	top: 20px;
	left: 20px;
}

.CellWithComment:hover span.CellComment {
	display: block;
}

thead>tr>th {
	color: #fff !important;
	font-weight: bold !important;
}
</style>

<div class="row row-cards row-deck">
	<form class="card" action="HtCatStatusWiseServicesWithLoad"
		method="post">
		<div class="card-body">
			<h3 class="card-title">
				<strong><span class="text-danger">HT02C</span> - HT CAT
					Wise STATUS Wise Services With Load</strong>
			</h3>
			<div class="row">
				<!-- <div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Circle</label> <select
							class="form-control" name="circle" id="circle"
							required="required">
							<option value="">Select Circle</option>
						</select>
					</div>
				</div> -->

				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">Month & Year</label>
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
				<!-- <div class="col-md-3">
					<div class="form-group">
						<label for="firstInput">To</label>
						<div>
							<select class="form-control firstInput" name="tmonth"
								required="required" id="tmonth" ng-model="obj.month">
								<option value="">Month</option>
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
							</select> <select class="form-control secondInput" name="tyear"
								required="required" id="tyear" ng-model="obj.year">
								<option value="">Year</option>
							</select>
						</div>
					</div>
				</div> -->
				<div class="col-md-4">
					<div class="form-group">
						<label class="form-label">GET Report</label>
						<button type="submit" class="btn btn-success">GET Report</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(cdmd)}">
		<div class="card ">
			<div
				class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
				<div class="bg-info text-white text-center"
					onclick="exportThisWithParameter('multiLevelTable', '${title}')"
					style="cursor: pointer; border: 1px solid #ccc; text-align: center; width: 19%; padding-bottom: 10px; padding-top: 10px;">Excel</div>
				<table id="multiLevelTable"
					class="table  card-table table-sm table-vcenter text-nowrap datatable display"
					style="width: 100%;">
					<thead>
						<tr>
							<th class="bg-primary text-white text-center" colspan="74">${title}</th>
						</tr>
						<tr class="bg-primary">
							<th rowspan="3" style="vertical-align: middle;">SR.NO</th>
							<th rowspan="3" class="text-center"
								style="vertical-align: middle;">CIRCLE</th>
							<th class="text-center" colspan="12">HT1</th>
							<th class="text-center" colspan="12">HT2</th>
							<th class="text-center" colspan="12">HT3</th>
							<th class="text-center" colspan="12">HT4</th>
							<th class="text-center" colspan="12">HT5</th>
							<th class="text-center bg-danger" colspan="12">TOTAL</th>


						</tr>
						<tr class="bg-primary">
							<th class="text-center" colspan="3">BILL STOP</th>
							<th class="text-center" colspan="3">LIVE</th>
							<th class="text-center" colspan="3">UDC</th>
							<th class="text-center" colspan="3">TOTAL</th>

							<th class="text-center" colspan="3">BILL STOP</th>
							<th class="text-center" colspan="3">LIVE</th>
							<th class="text-center" colspan="3">UDC</th>
							<th class="text-center" colspan="3">TOTAL</th>

							<th class="text-center" colspan="3">BILL STOP</th>
							<th class="text-center" colspan="3">LIVE</th>
							<th class="text-center" colspan="3">UDC</th>
							<th class="text-center" colspan="3">TOTAL</th>

							<th class="text-center" colspan="3">BILL STOP</th>
							<th class="text-center" colspan="3">LIVE</th>
							<th class="text-center" colspan="3">UDC</th>
							<th class="text-center" colspan="3">TOTAL</th>

							<th class="text-center" colspan="3">BILL STOP</th>
							<th class="text-center" colspan="3">LIVE</th>
							<th class="text-center" colspan="3">UDC</th>
							<th class="text-center" colspan="3">TOTAL</th>

							<th class="text-center bg-danger" colspan="3">BILL STOP</th>
							<th class="text-center bg-danger" colspan="3">LIVE</th>
							<th class="text-center bg-danger" colspan="3">UDC</th>
							<th class="text-center bg-danger" colspan="3">TOTAL</th>
						</tr>
						<tr class="bg-primary">

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center">NOS</th>
							<th data-orderable="false" class="text-center">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center ">NOS</th>
							<th data-orderable="false" class="text-center ">LOAD</th>
							<th data-orderable="false" class="text-center">CB</th>

							<th data-orderable="false" class="text-center bg-danger">NOS</th>
							<th data-orderable="false" class="text-center bg-danger">LOAD</th>
							<th data-orderable="false" class="text-center bg-danger">CB</th>

							<th data-orderable="false" class="text-center bg-danger ">NOS</th>
							<th data-orderable="false" class="text-center bg-danger">LOAD</th>
							<th data-orderable="false" class="text-center bg-danger">CB</th>

							<th data-orderable="false" class="text-center bg-danger">NOS</th>
							<th data-orderable="false" class="text-center bg-danger">LOAD</th>
							<th data-orderable="false" class="text-center bg-danger">CB</th>

							<th class="text-center  bg-danger " data-orderable="false">NOS</th>
							<th class="text-center  bg-danger " data-orderable="false">LOAD</th>
							<th data-orderable="false" class="text-center bg-danger">CB</th>
							<!-- <th class="text-center">NOS</th>
							<th class="text-center">LOAD</th> -->


						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${cdmd}" varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>

								<td>${mtrblc.CIRCLE}</td>
								<td class="text-right">${mtrblc.HT1_BS_NOS}</td>
								<td class="text-right">${mtrblc.HT1_BS_LOAD}</td>
								<td class="text-right">${mtrblc.HT1_BS_CB}</td>

								<td class="text-right">${mtrblc.HT1_LIVE_NOS}</td>
								<td class="text-right">${mtrblc.HT1_LIVE_LOAD}</td>
								<td class="text-right">${mtrblc.HT1_LIVE_CB}</td>

								<td class="text-right">${mtrblc.HT1_UDC_NOS}</td>
								<td class="text-right">${mtrblc.HT1_UDC_LOAD}</td>
								<td class="text-right">${mtrblc.HT1_UDC_CB}</td>

								<td class="text-right">${mtrblc.HT1_NOS_TOT}</td>
								<td class="text-right">${mtrblc.HT1_LOAD_TOT}</td>
								<td class="text-right">${mtrblc.HT1_CB_TOT}</td>

								<td class="text-right">${mtrblc.HT2_BS_NOS}</td>
								<td class="text-right">${mtrblc.HT2_BS_LOAD}</td>
								<td class="text-right">${mtrblc.HT2_BS_CB}</td>

								<td class="text-right">${mtrblc.HT2_LIVE_NOS}</td>
								<td class="text-right">${mtrblc.HT2_LIVE_LOAD}</td>
								<td class="text-right">${mtrblc.HT2_LIVE_CB}</td>

								<td class="text-right">${mtrblc.HT2_UDC_NOS}</td>
								<td class="text-right">${mtrblc.HT2_UDC_LOAD}</td>
								<td class="text-right">${mtrblc.HT2_UDC_CB}</td>

								<td class="text-right">${mtrblc.HT2_NOS_TOT}</td>
								<td class="text-right">${mtrblc.HT2_LOAD_TOT}</td>
								<td class="text-right">${mtrblc.HT2_CB_TOT}</td>


								<td class="text-right">${mtrblc.HT3_BS_NOS}</td>
								<td class="text-right">${mtrblc.HT3_BS_LOAD}</td>
								<td class="text-right">${mtrblc.HT3_BS_CB}</td>

								<td class="text-right">${mtrblc.HT3_LIVE_NOS}</td>
								<td class="text-right">${mtrblc.HT3_LIVE_LOAD}</td>
								<td class="text-right">${mtrblc.HT3_LIVE_CB}</td>

								<td class="text-right">${mtrblc.HT3_UDC_NOS}</td>
								<td class="text-right">${mtrblc.HT3_UDC_LOAD}</td>
								<td class="text-right">${mtrblc.HT3_UDC_CB}</td>

								<td class="text-right">${mtrblc.HT3_NOS_TOT}</td>
								<td class="text-right">${mtrblc.HT3_LOAD_TOT}</td>
								<td class="text-right">${mtrblc.HT3_CB_TOT}</td>

								<td class="text-right">${mtrblc.HT4_BS_NOS}</td>
								<td class="text-right">${mtrblc.HT4_BS_LOAD}</td>
								<td class="text-right">${mtrblc.HT4_BS_CB}</td>

								<td class="text-right">${mtrblc.HT4_LIVE_NOS}</td>
								<td class="text-right">${mtrblc.HT4_LIVE_LOAD}</td>
								<td class="text-right">${mtrblc.HT4_LIVE_CB}</td>

								<td class="text-right">${mtrblc.HT4_UDC_NOS}</td>
								<td class="text-right">${mtrblc.HT4_UDC_LOAD}</td>
								<td class="text-right">${mtrblc.HT4_UDC_CB}</td>


								<td class="text-right">${mtrblc.HT4_NOS_TOT}</td>
								<td class="text-right">${mtrblc.HT4_LOAD_TOT}</td>
								<td class="text-right">${mtrblc.HT4_CB_TOT}</td>

								<td class="text-right">${mtrblc.HT5_BS_NOS}</td>
								<td class="text-right">${mtrblc.HT5_BS_LOAD}</td>
								<td class="text-right">${mtrblc.HT5_BS_CB}</td>

								<td class="text-right">${mtrblc.HT5_LIVE_NOS}</td>
								<td class="text-right">${mtrblc.HT5_LIVE_LOAD}</td>
								<td class="text-right">${mtrblc.HT5_LIVE_CB}</td>

								<td class="text-right">${mtrblc.HT5_UDC_NOS}</td>
								<td class="text-right">${mtrblc.HT5_UDC_LOAD}</td>
								<td class="text-right">${mtrblc.HT5_UDC_CB}</td>

								<td class="text-right">${mtrblc.HT5_NOS_TOT}</td>
								<td class="text-right">${mtrblc.HT5_LOAD_TOT}</td>
								<td class="text-right">${mtrblc.HT5_CB_TOT}</td>

								<td class="text-right bg-warning"><strong>${mtrblc.BS_NOS_TOT}</strong></td>
								<td class="text-right bg-warning"><strong>${mtrblc.BS_LOAD_TOT}</strong></td>
								<td class="text-right bg-warning"><strong>${mtrblc.BS_CB_TOT}</strong></td>

								<td class="text-right bg-warning"><strong>${mtrblc.LIVE_NOS_TOT}</strong></td>
								<td class="text-right bg-warning"><strong>${mtrblc.LIVE_LOAD_TOT}</strong></td>
								<td class="text-right bg-warning"><strong>${mtrblc.LIVE_CB_TOT}</strong></td>

								<td class="text-right bg-warning"><strong>${mtrblc.UDC_NOS_TOT}</strong></td>
								<td class="text-right bg-warning"><strong>${mtrblc.UDC_LOAD_TOT}</strong></td>
								<td class="text-right bg-warning"><strong>${mtrblc.UDC_CB_TOT}</strong></td>

								<td class="text-right bg-success"><strong>${mtrblc.NOS_TOT}</strong></td>
								<td class="text-right bg-success"><strong>${mtrblc.LOAD_TOT}</strong></td>
								<td class="text-right bg-success"><strong>${mtrblc.CB_TOT}</strong></td>

							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="2" class="text-right">Grand Total</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_BS_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_BS_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_BS_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_LIVE_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_LIVE_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_LIVE_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_UDC_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_UDC_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_UDC_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_NOS_TOT).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_LOAD_TOT).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT1_CB_TOT).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_BS_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_BS_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_BS_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_LIVE_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_LIVE_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_LIVE_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_UDC_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_UDC_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_UDC_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_NOS_TOT).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_LOAD_TOT).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT2_CB_TOT).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_BS_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_BS_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_BS_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_LIVE_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_LIVE_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_LIVE_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_UDC_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_UDC_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_UDC_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_NOS_TOT).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_LOAD_TOT).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT3_CB_TOT).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_BS_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_BS_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_BS_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_LIVE_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_LIVE_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_LIVE_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_UDC_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_UDC_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_UDC_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_NOS_TOT).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_LOAD_TOT).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT4_CB_TOT).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_BS_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_BS_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_BS_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_LIVE_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_LIVE_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_LIVE_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_UDC_NOS).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_UDC_LOAD).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_UDC_CB).sum()}</th>

							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_NOS_TOT).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_LOAD_TOT).sum()}</th>
							<th class="text-right">${cdmd.stream().map(mtrblc -> mtrblc.HT5_CB_TOT).sum()}</th>

							<th class="text-right  bg-warning">${cdmd.stream().map(mtrblc -> mtrblc.BS_NOS_TOT).sum()}</th>
							<th class="text-right  bg-warning">${cdmd.stream().map(mtrblc -> mtrblc.BS_LOAD_TOT).sum()}</th>
							<th class="text-right  bg-warning">${cdmd.stream().map(mtrblc -> mtrblc.BS_CB_TOT).sum()}</th>

							<th class="text-right  bg-warning">${cdmd.stream().map(mtrblc -> mtrblc.LIVE_NOS_TOT).sum()}</th>
							<th class="text-right  bg-warning">${cdmd.stream().map(mtrblc -> mtrblc.LIVE_LOAD_TOT).sum()}</th>
							<th class="text-right  bg-warning">${cdmd.stream().map(mtrblc -> mtrblc.LIVE_CB_TOT).sum()}</th>

							<th class="text-right  bg-warning">${cdmd.stream().map(mtrblc -> mtrblc.UDC_NOS_TOT).sum()}</th>
							<th class="text-right  bg-warning">${cdmd.stream().map(mtrblc -> mtrblc.UDC_LOAD_TOT).sum()}</th>
							<th class="text-right  bg-warning">${cdmd.stream().map(mtrblc -> mtrblc.UDC_CB_TOT).sum()}</th>

							<th class="text-right bg-success">${cdmd.stream().map(mtrblc -> mtrblc.NOS_TOT).sum()}</th>
							<th class="text-right bg-success">${cdmd.stream().map(mtrblc -> mtrblc.LOAD_TOT).sum()}</th>
							<th class="text-right bg-success">${cdmd.stream().map(mtrblc -> mtrblc.CB_TOT).sum()}</th>
						</tr>
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
		var currentYear = (new Date()).getFullYear();
		var currnetMonth = (new Date()).getMonth() + 1;
		for (var j = currentYear; j > 2015; j--) {
			$("#year").append("<option value="+j+">" + j + "</option>");
		}

		$('#mon option:eq(' + currnetMonth + ')').prop('selected', true);
		$('#year option[value="' + currentYear + '"]').prop('selected', true);

	});
</script>
<!--  <script>
	require([ 'jquery', 'datatables.net', 'datatables.net-jszip',
			'datatables.net-buttons', 'datatables.net-buttons-flash',
			'datatables.net-buttons-html5' ], function($, datatable, jszip) {

		window.JSZip = jszip;
		$('.datatable').DataTable({
			dom : 'Bfrltip',
			"scrollX" : true,
			buttons : {
				buttons : [ ]
			}
		});
		
	});
</script>  -->
<script>
	requirejs([ 'jquery' ], function($) {
		$("td,th").each(
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

<jsp:include page="footer.jsp"></jsp:include>