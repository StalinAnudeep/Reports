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
					var currnetMonth = (new Date()).getMonth() + 1;
					for (var j = currentYear; j > 2013; j--) {
						$("#fyear").append(
								"<option value="+j+">" + j + "</option>");
						$("#tyear").append(
								"<option value="+j+">" + j + "</option>");
					}
					$('#fmonth option:eq(' + currnetMonth + ')').prop(
							'selected', true);
					$('#fyear option[value="' + currentYear + '"]').prop(
							'selected', true);

					$('#tmonth option:eq(' + currnetMonth + ')').prop(
							'selected', true);
					$('#tyear option[value="' + currentYear + '"]').prop(
							'selected', true);
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
</style>


<div class="row row-cards row-deck">
	<c:if test="${ empty consumerdetails}">
		<form class="card" action="servicehistory" method="post" id="form">
			<div class="card-body">
				<h3 class="card-title">
					<strong><span class="text-danger">HT01</span> -
						Consumption, Billing, Collection And Arrears History(KVAH) </strong>
				</h3>
				<div class="row">
					<div class="col-md-3">
						<div class="form-group">
							<label for="inputCity">Enter Service Number</label> <input
								type="text" class="form-control" required="required" name="scno"
								id="scno" placeholder="Enter Service Number">
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
							<label class="form-label">Get Report</label>
							<button type="submit" class="btn btn-success">Get Report</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</c:if>

	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${not empty consumerdetails}">
		<div class="card">
			<!-- 	<div class="card-header">Service Details</div> -->
			<div class="card-body">

				<h4 class="text-center text-danger">
					Andhra Pradesh Central Power Distribution Corporation Limited <a
						href="servicehistory"
						style="float: right; color: #45aaf2 !important;"> <i
						class="fa fa-arrow-left" aria-hidden="true"></i>Back
					</a>
				</h4>
				<h5 class="text-center text-danger">
					Consumption, Billing, Collection And Arrears History Report For <span
						style="color: #467fcf;">${consumerdetails.CTUSCNO}</span> From :
					${fmonthYear} To : ${tmonthYear}
				</h5>


				<table class="table table-bordered table-sm table-condensed"
					style="border-right-width: 0px; border-bottom-width: 0px; border-left-width: 0px; border-top-width: 0px; font-size: 12px; font-weight: bold;">
					<tbody>

						<tr>
							<td class="bg-primary text-white text-right">Service Number</td>
							<td class="bg-light" style="font-size: 18px;">${consumerdetails.CTUSCNO}</td>

							<td class="bg-primary text-white text-right">Account ID</td>
							<td class="bg-light">${consumerdetails.CTACCT_ID}</td>





							<td class="bg-primary text-white text-right">Circle Name</td>
							<td class="bg-light">${consumerdetails.CIRNAME}</td>
							<td class="bg-info text-white text-center" colspan="2"><strong>Flags</strong></td>


						</tr>
						<tr>
							<td class="bg-primary text-white text-right">Name</td>
							<td class="">${consumerdetails.CTNAME}</td>

							<td class="bg-primary text-white text-right">CMD</td>
							<td class="bg-light">${consumerdetails.CTCMD_HT}</td>


							<td class="bg-primary text-white text-right">Division Name</td>
							<td class="">${consumerdetails.DIVNAME}</td>
							<td class="bg-primary text-white text-right">ED Flag</td>
							<td class="">${consumerdetails.CTEDFLAG}</td>

						</tr>
						<tr>
							<td class="bg-primary text-white text-right">Category</td>
							<td class="bg-light">${consumerdetails.CTCAT}</td>

							<td class="bg-primary text-white text-right">Voltage</td>
							<td class="">${consumerdetails.CTACTUAL_KV}</td>

							<td class="bg-primary text-white text-right">Sub-Division
								Name</td>
							<td class="bg-light">${consumerdetails.SUBNAME}</td>
							<td class="bg-primary text-white text-right">TDS Flag</td>
							<td class="bg-light">${consumerdetails.CTTDS_FLAG}</td>


						</tr>
						<tr>
							<td class="bg-primary text-white text-right">Sub-Category</td>
							<td class="">${consumerdetails.CTSUBCAT}</td>

							<td class="bg-primary text-white text-right">Sub-Station</td>
							<td class="bg-light">${consumerdetails.CTSS_NAME}</td>




							<td class="bg-primary text-white text-right">Section Code</td>
							<td class="">${consumerdetails.CTSECCD}</td>
							<td class="bg-primary text-white text-right">Solar Flag</td>
							<td class="">${consumerdetails.CTSOLAR_FLAG}</td>

						</tr>
						<tr>
							<td class="bg-primary text-white text-right">Add1</td>
							<td class="bg-light">${consumerdetails.CTADD1}</td>

							<td class="bg-primary text-white text-right">Feeder Code</td>
							<td class="bg-light">${consumerdetails.CTFEEDER_CODE}</td>


							<td class="bg-primary text-white text-right">Section Name</td>
							<td class="bg-light">${consumerdetails.SECNAME}</td>
							<td class="bg-primary text-white text-right">IND Feeder Flag</td>
							<td class="bg-light">${consumerdetails.CTINDFEEDFLAG_HT}</td>


						</tr>
						<tr>
							<td class="bg-primary text-white text-right">Add2</td>
							<td class="">${consumerdetails.CTADD2}</td>

							<td class="bg-primary text-white text-right">Feeder Name</td>
							<td class="">${consumerdetails.CTFEEDER_NAME}</td>

							<td class="bg-primary text-white text-right">ERO Name</td>
							<td class="">${consumerdetails.ERONAME}</td>
							<td class="bg-primary text-white text-right">SCST Flag</td>
							<td class="">${consumerdetails.CTSCSTFLAG}</td>

						</tr>
						<tr>
							<td class="bg-primary text-white text-right">Add3</td>
							<td class="bg-light">${consumerdetails.CTADD3}</td>

							<td class="bg-primary text-white text-right">DTR Code</td>
							<td class="">${consumerdetails.CTDTR_TYPE}</td>

							<td class="bg-primary text-white text-right">HOD Department</td>
							<td class="bg-light">${consumerdetails.CTHODDEP}</td>
							<td class="bg-primary text-white text-right">Aqua Flag</td>
							<td class="bg-light">${consumerdetails.CTAQUA_FLAG}</td>

						</tr>
						<tr>
							<td class="bg-primary text-white text-right">Add4</td>
							<td class="">${consumerdetails.CTADD4}</td>



							<td class="bg-primary text-white text-right">GOVT/PVT</td>
							<c:if test="${ consumerdetails.CTGOVT_PVT == 'Y' }">
								<td class="bg-light">GOVT</td>
							</c:if>
							<c:if test="${ consumerdetails.CTGOVT_PVT == 'N' }">
								<td class="bg-light">PVT</td>
							</c:if>

							<td class="bg-primary text-white text-right">HOD
								Sub-Department</td>
							<td class="">${consumerdetails.CTHODSUBDEP}</td>

							<td class="bg-primary text-white text-right">Colony Flag</td>
							<td class="">${consumerdetails.CTCOLNYFLAG_HT}</td>


						</tr>
						<tr>
							<td class="bg-primary text-white text-right">City</td>
							<td class="bg-light">${consumerdetails.CTCITY}</td>

							<td class="bg-primary text-white text-right">Status</td>
							<c:if test="${ consumerdetails.CTSTATUS == '1' }">
								<td class="">LIVE</td>
							</c:if>
							<c:if test="${ consumerdetails.CTSTATUS != '1' }">
								<td class="">BILLSTOP</td>
							</c:if>

							<td class="bg-primary text-white text-right">Main Meters</td>
							<td class="bg-light">${consumerdetails.CTMTRCOUNT_HT}</td>
							<td class="bg-primary text-white text-right">LF Flag</td>
							<td class="bg-light">${consumerdetails.CTLF_FLAG}</td>

						</tr>
						<tr>
							<td class="bg-primary text-white text-right">Pincode</td>
							<td>${consumerdetails.CTPINCODE}</td>

							<td class="bg-primary text-white text-right">Security
								Deposit</td>
							<td class="bg-light format">${sd}</td>

							<td class="bg-primary text-white text-right">Colony
								Meters</td>
							<td class="bg-light">${consumerdetails.CTMTRCOUNT_CL}</td>
							<td class="bg-primary text-white text-right">Meter Side Flag</td>
							<td>${consumerdetails.CT_METERSIDE_FLAG}</td>

						</tr>
						<tr>
							<td class="bg-primary text-white text-right">Mobile No</td>
							<td class="bg-light">${consumerdetails.CTMOBILE}</td>



							<td class="bg-primary text-white text-right">SD Required</td>
							<td>${consumerdetails.CTSDREQ}</td>


							<td class="bg-primary text-white text-right">LF Meters</td>
							<td class="">${consumerdetails.CTMTRCOUNT_LF}</td>
							<td class="bg-primary text-white text-right">Seasonal Flag</td>
							<td class="bg-light">${consumerdetails.CTSEASFLAG_HT}</td>

						</tr>
						<tr>
							<td class="bg-primary text-white text-right">Email</td>
							<td class="">${consumerdetails.CTEMAILID}</td>

							<td class="bg-primary text-white text-right">Last Paid Date</td>
							<td class="bg-light">${consumerdetails.LAST_PAID_DATE}</td>

							<td class="bg-primary text-white text-right">Social Group</td>
							<td>${consumerdetails.CTSOCIALGROUP}</td>

							<td class="bg-primary text-white text-right">Service Type</td>
							<td>${consumerdetails.STDESC}</td>
						</tr>
						<tr>
							<td class="bg-primary text-white text-right">PAN Number</td>
							<td class="bg-light">${consumerdetails.CTPANNO_TEMP}</td>

							<td class="bg-primary text-white text-right">Released On</td>
							<td class="">${consumerdetails.TCTENTDT}</td>

							<%-- <td class="bg-primary text-white text-right">Pole Number</td>
							<td class="bg-light">${consumerdetails.CTPOLENO}</td> --%>

							<td class="bg-primary text-white text-right"></td>
							<td></td>

							<td class="bg-primary text-white text-right"></td>
							<td class="bg-light"></td>
						</tr>


						<%-- 	<tr >
							<td class="bg-primary text-white text-right">Service Number</td>
							<td  class="bg-light">${consumerdetails.CTUSCNO}</td>
							<td class="bg-primary text-white text-right"
								>Account ID</td>
							<td  class="bg-light">${consumerdetails.CTACCT_ID}</td>
							<td class="bg-primary text-white text-right"
								>Section Code</td>
							<td  class="bg-light">${consumerdetails.CTSECCD}</td>
							<td class="bg-primary text-white text-right"
								>Circle Name</td>
							<td  class="bg-light">${consumerdetails.CIRNAME}</td>
						</tr>

						<tr >
							<td class="bg-primary text-white text-right">Division Name</td>
							<td  >${consumerdetails.DIVNAME}</td>
							<td class="bg-primary text-white text-right"
								>ERO Name</td>
							<td  >${consumerdetails.ERONAME}</td>
							<td class="bg-primary text-white text-right"
								>Sub-division Name</td>
							<td  >${consumerdetails.SUBNAME}</td>
							<td class="bg-primary text-white text-right"
								>Section Name</td>
							<td  >
								${consumerdetails.SECNAME}</td>
						</tr>

						<tr>
							<td class="bg-primary text-white text-right"
								>Name</td>
							<td  class="bg-light">${consumerdetails.CTNAME}</td>
							<td class="bg-primary text-white text-right"
								>Add1</td>
							<td  class="bg-light">${consumerdetails.CTADD1}</td>
							<td class="bg-primary text-white text-right"
								>Add2</td>
							<td  class="bg-light">${consumerdetails.CTADD2}</td>
							<td class="bg-primary text-white text-right"
								>Add3</td>
							<td  class="bg-light">${consumerdetails.CTADD3}</td>
						</tr>


						<tr >
							<td class="bg-primary text-white text-right"
								>Add4</td>
							<td  >${consumerdetails.CTADD4}</td>
							<td class="bg-primary text-white text-right"
								>City</td>
							<td  >${consumerdetails.CTCITY}</td>
							<td class="bg-primary text-white text-right"
								>Pincode</td>
							<td  >${consumerdetails.CTPINCODE}</td>
							<td class="bg-primary text-white text-right"
								>Category</td>
							<td  >${consumerdetails.CTCAT}</td>
						</tr>

						<tr >
							<td class="bg-primary text-white text-right"
								>Sub-Category</td>
							<td  class="bg-light">${consumerdetails.CTSUBCAT}</td>
							<td class="bg-primary text-white text-right"
								>Status</td>
							<c:if test="${ consumerdetails.CTSTATUS == '1' }">
								<td class="bg-light" >LIVE</td>
							</c:if>
							<c:if test="${ consumerdetails.CTSTATUS != '1' }">
								<td  class="bg-light">BILLSTOP</td>
							</c:if>
							<td class="bg-primary text-white text-right"
								>Group</td>
							<td  class="bg-light">${consumerdetails.CTGROUP}</td>
							<td class="bg-primary text-white text-right"
								>Released On</td>
							<td  class="bg-light">${consumerdetails.TCTENTDT}</td>

						</tr>
						<tr >
							<td class="bg-primary text-white text-right"
								>CMD</td>
							<td  >${consumerdetails.CTCMD_HT}</td>
							<td class="bg-primary text-white text-right"
								>Connected Load</td>
							<td  >${consumerdetails.CTCONNLD}</td>
							<td class="bg-primary text-white text-right"
								>Security Deposit</td>
							<td  >${consumerdetails.CTSDTOTAMT}</td>
							<td class="bg-primary text-white text-right"
								>Mobile No</td>
							<td  >${consumerdetails.CTMOBILE}</td>

						</tr>

						<tr >
							<td class="bg-primary text-white text-right"
								>Email</td>
							<td  class="bg-light">
								${consumerdetails.CTEMAILID}</td>
							<td class="bg-primary text-white text-right"
								>ED Flag</td>
							<td  class="bg-light">${consumerdetails.CTEDFLAG}</td>
							<td class="bg-primary text-white text-right"
								>DTR Type</td>
							<td  class="bg-light">${consumerdetails.CTDTR_TYPE}</td>
							<td class="bg-primary text-white text-right"
								>SCST Flag</td>
							<td  class="bg-light">${consumerdetails.CTSCSTFLAG}</td>

						</tr>

						<tr >
							<td class="bg-primary text-white text-right"
								>Meter Side Flag</td>
							<td  >${consumerdetails.CT_METERSIDE_FLAG}</td>
							<td class="bg-primary text-white text-right"
								>PAA Code</td>
							<td  >${consumerdetails.CTPAACODE}</td>
							<td class="bg-primary text-white text-right"
								>Social Group</td>
							<td  >${consumerdetails.CTSOCIALGROUP}</td>
							<td class="bg-primary text-white text-right">HOD Type</td>
							<td  >${consumerdetails.CTHODTYPE}</td>

						</tr>


						<tr >
							<td class="bg-primary text-white text-right"
								>HOD Department</td>
							<td  class="bg-light">${consumerdetails.CTHODDEP}</td>
							<td class="bg-primary text-white text-right"
								>HOD Sub-Department</td>
							<td  class="bg-light">${consumerdetails.CTHODSUBDEP}</td>
							<td class="bg-primary text-white text-right"
								>Seasonal Flag</td>
							<td class="bg-light" >${consumerdetails.CTSEASFLAG_HT}</td>
							<td class="bg-primary text-white text-right"
								>Pole Number</td>
							<td  class="bg-light">${consumerdetails.CTPOLENO}</td>

						</tr>
						<tr >
							<td class="bg-primary text-white text-right"
								>PAN Number</td>
							<td  >${consumerdetails.CTPANNO}</td>
							<td class="bg-primary text-white text-right"
								>Actual KV</td>
							<td  >${consumerdetails.CTACTUAL_KV}</td>
							<td class="bg-primary text-white text-right"
								>IND Feeder Flag</td>
							<td  >${consumerdetails.CTINDFEEDFLAG_HT}</td>
							<td class="bg-primary text-white text-right"
								>TDS Flag</td>
							<td  >${consumerdetails.CTTDS_FLAG}</td>

						</tr>


						<tr >
							<td class="bg-primary text-white text-right"
								>GOVT/PVT</td>
							<c:if test="${ consumerdetails.CTGOVT_PVT == 'Y' }">
								<td  class="bg-light">GOVT</td>
							</c:if>
							<c:if test="${ consumerdetails.CTGOVT_PVT == 'N' }">
								<td  class="bg-light">PVT</td>
							</c:if>
							<td class="bg-primary text-white text-right"
								>Aqua Flag</td>
							<td  class="bg-light">${consumerdetails.CTAQUA_FLAG}</td>
							<td class="bg-primary text-white text-right"
								>Feeder Code</td>
							<td  class="bg-light">${consumerdetails.CTFEEDER_CODE}</td>
							<td class="bg-primary text-white text-right"
								>Feeder Name</td>
							<td  class="bg-light">${consumerdetails.CTFEEDER_NAME}</td>

						</tr>

						<tr >
							<td class="bg-primary text-white text-right"
								>Sub-Station Name</td>
							<td  >${consumerdetails.CTSS_NAME}</td>
							<td class="bg-primary text-white text-right"
								>Main Meters</td>
							<td  >${consumerdetails.CTMTRCOUNT_HT}</td>
							<td class="bg-primary text-white text-right"
								>Solar Flag</td>
							<td  >${consumerdetails.CTSOLAR_FLAG}</td>
							<td class="bg-primary text-white text-right"
								>Colony Meters</td>
							<td  >${consumerdetails.CTCOLNYFLAG_HT}</td>

						</tr>

						<tr >
							<td class="bg-primary text-white text-right"
								>Colony Meters</td>
							<td  class="bg-light">${consumerdetails.CTMTRCOUNT_CL}</td>
							<td class="bg-primary text-white text-right"
								>LF Flag</td>
							<td  class="bg-light">${consumerdetails.CTLF_FLAG}</td>
							<td class="bg-primary text-white text-right"
								>LF Meters</td>
							<td  class="bg-light">${consumerdetails.CTMTRCOUNT_LF}</td>
							<td class="bg-primary text-white text-right"
								></td>
							<td  class="bg-light"></td>

						</tr> --%>

					</tbody>
				</table>
			</div>
			<div class="card-body"
				style="padding-left: 23px; padding-right: 23px; font-size: 12px; font-weight: bold;">
				<c:if test="${ not empty fn:trim(account)}">
					<table class="table table-striped table-bordered table-sm"
						id="hist">
						<thead>
							<tr>
								<th rowspan="2" class="align-middle text-center"text-right">S.NO</th>
								<th rowspan="2" class="align-middle text-center">MON_YEAR</th>
								<th rowspan="2" class="align-middle text-center">LOAD</th>
								<th rowspan="2" class="align-middle text-center">CAT</th>
								<th rowspan="2" class="align-middle text-center">CLS KWH</th>
								<th rowspan="2" class="align-middle text-center">KWH CON</th>
								<th rowspan="2" class="align-middle text-center">CLS KVAH</th>
								<th rowspan="2" class="align-middle text-center">KVAH CON</th>
								<th rowspan="2" class="align-middle text-center">MN KVAH</th>
								<th rowspan="2" class="align-middle text-center">MD</th>
								<th rowspan="2" class="align-middle text-center">STATUS</th>
								<th rowspan="2" class="align-middle text-center">OB</th>
								<th colspan="4" class="text-center">DEMAND</th>

								<th colspan="2" class="text-center">COLLECTION</th>

								<th rowspan="2" class="align-middle  text-center">CB</th>
								<th rowspan="2" class="align-middle  text-center">SD</th>


							</tr>

							<tr>

								<th>WITH OUT LPC</th>
								<th>LPC</th>
								<th>COURT LPC</th>
								<th>DEBIT_RJ</th>
								<th>COLLECTION</th>
								<th>CREDIT_RJ</th>



							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${account}" varStatus="tagStatus">
								<tr>
									<td class="text-right">${tagStatus.index + 1}</td>
									<td class=""text-left"">${mtrblc.MON_YEAR}</td>
									<td class="text-right">${mtrblc.LOAD}</td>
									<td class="text-center">${mtrblc.CAT}${mtrblc.SCAT}</td>
									<td class="text-right format">${mtrblc.MDCLKWH_HT}</td>
									<td class="CellWithComment text-right  format"><span
										class="text-info  format">${mtrblc.REC_KWH}</span> <span
										class="CellComment  format"
										style="width: 158px; height: 30px;">
											${mtrblc.MDRKWH_HT} * ${mtrblc.MDMF_HT} = ${mtrblc.REC_KWH} </span>
									</td>
									<td class="text-right CellWithComment  format"><span
										class="text-info  format">${mtrblc.MDCLKVAH_HT}</span> <span
										class="CellComment  format"
										style="width: 158px; height: 100px;"> - TOD1 =
											${mtrblc.MDTOD1_CLS} <br> + TOD2 = ${mtrblc.MDTOD2_CLS}
											<br> + TOD5 = ${mtrblc.MDTOD5_CLS} <br> - TOD6 =
											${mtrblc.MDTOD6_CLS}
									</span></td>
									<td class="text-right CellWithComment  format"><span
										class="text-info  format">${mtrblc.REC_KVAH}</span> <span
										class="CellComment  format"
										style="width: 158px; height: 100px;">
											${mtrblc.MDRKVAH_HT} * ${mtrblc.MDMF_HT} = ${mtrblc.REC_KVAH}
											<br> - TOD1 = ${mtrblc.MDTOD1_RECKVAH} <br> + TOD2
											= ${mtrblc.MDTOD2_RECKVAH} <br> + TOD5 =
											${mtrblc.MDTOD5_RECKVAH} <br> - TOD6 =
											${mtrblc.MDTOD6_RECKVAH}
									</span></td>
									<td class="text-right  format">${mtrblc.MN_KVAH}</td>
									<td class="text-right  format">${mtrblc.REC_MD}</td>
									<td class="text-center  format">${mtrblc.STAT}</td>
									<td class="text-right  format">${mtrblc.TOT_OB}</td>
									<td class="text-right  format">${mtrblc.DWOCC}</td>
									<td class="text-right  format">${mtrblc.LPC}</td>
									<td class="text-right  format">${mtrblc.DWCC}</td>
									<td class="text-right  format">${mtrblc.DEBIT_RJ}</td>
									<td class="text-right  format">${mtrblc.COLLECTION}</td>
									<td class="text-right  format">${mtrblc.CREDIT_RJ}</td>
									<td class="text-right  format">${mtrblc.TOTAL_CB}</td>
									<td class="text-right  format">${mtrblc.SD}</td>

								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
				<div class="col-md-12 text-center">
					<a href="printtextfile" class="btn btn-primary btn-lg btn-block"
						target="_blank"><i class="fa fa-print" aria-hidden="true"></i>
						Print</a>
				</div>
				<!-- target="_blank" -->
			</div>

			<div class="card-body row-no-padding table-responsive-sm dataTables_wrapper"
				style="padding-left: 23px; padding-right: 23px; font-size: 12px; font-weight: bold;">
				<c:if test="${ not empty fn:trim(ledgerhist)}">
					<table class="table card-table table-vcenter text-nowrap datatable display"
						id="supply">
						<thead>
						<tr>
						<th colspan="6" >Ledger History</th>
						<th colspan="3" class="bg-primary text-center text-white">Consumption</th>
						<th colspan="3" class="bg-success text-center text-white">Load</th>
						<th colspan="9" class="bg-danger text-center text-white">OB</th>
						<th colspan="9" class="bg-warning text-center text-white">Demand</th>
						<th colspan="9" class="bg-info text-center text-white">RJ-DEBIT</th>
						<th colspan="9" class="bg-primary text-center text-white">RJ-CREDIT</th>
						<th colspan="10" class="bg-success text-center text-white">PAYMENT</th>
						<th colspan="9" class="bg-danger text-center text-white">CLOSING BALANCE</th>
						<th colspan="5" class="bg-warning text-center text-white">Security Deposit</th>
						<th colspan="6" class="bg-info text-center text-white">TRUE_UP</th>
						<th colspan="6" class="bg-primary text-center text-white">FPPCA(2021-22)</th>
						<th colspan="6" class="bg-success text-center text-white">FPPCA_2</th>
						<th colspan="4" class="bg-danger text-center text-white">Court CC_Charges</th>
						</tr>
							<tr>
								<th  class="align-middle text-center">S.NO</th>
								<th  class="align-middle text-center">MON_YEAR</th>
								<th  class="align-middle text-center">USCNO</th>
								<th  class="align-middle text-center">CAT</th>
								<th  class="align-middle text-center">SCAT</th>
								<th  class="align-middle text-center">STATUS</th>
								<th  class="align-middle text-center">REC_KWH</th>
								<th  class="align-middle text-center">REC_KVAH</th>
								<th  class="align-middle text-center">MN_KVAH</th>
								<th  class="align-middle text-center">CMD</th>
								<th  class="align-middle text-center">REC_MD</th>
								<th  class="align-middle text-center">BMD</th>
								<th  class="align-middle text-center">OB_EC</th>
								<th  class="align-middle text-center">OB_ED</th>
								<th  class="align-middle text-center">OB_LPC</th>
								<th  class="align-middle text-center">OB_EDI</th>
								<th  class="align-middle text-center">OB_TUPC</th>
								<th  class="align-middle text-center">OB_OLD_FPP</th>
								<th  class="align-middle text-center">OB_NEW_FPP</th>
								<th  class="align-middle text-center">OB_FSA</th>
								<th  class="align-middle text-center">TOT_OB</th>
								<th  class="align-middle text-center">EC</th>
								<th  class="align-middle text-center">LPC</th>
								<th  class="align-middle text-center">ED</th>
								<th  class="align-middle text-center">EDI</th>
								<th  class="align-middle text-center">TUPC</th>
								<th  class="align-middle text-center">OLD_FPP</th>
								<th  class="align-middle text-center">NEW_FPP</th>
								<th  class="align-middle text-center">FSA</th>
								<th  class="align-middle text-center">CMD</th>
								<th  class="align-middle text-center">DRJ_EC</th>
								<th  class="align-middle text-center">DRJ_ED</th>
								<th  class="align-middle text-center">DRJ_LPC</th>
								<th  class="align-middle text-center">DRJ_IED</th>
								<th  class="align-middle text-center">DRJ_FSA</th>
								<th  class="align-middle text-center">DRJ TRUE_UP</th>
								<th  class="align-middle text-center">FPPCA-1</th>
								<th  class="align-middle text-center">FPPCA-2</th>
								<th  class="align-middle text-center">Total_DRJ</th>
								<th  class="align-middle text-center">CRJ_EC</th>
								<th  class="align-middle text-center">CRJ_ED</th>
								<th  class="align-middle text-center">CRJ_LPC</th>
								<th  class="align-middle text-center">CRJ_IED</th>
								<th  class="align-middle text-center">CRJ_FSA</th>
								<th  class="align-middle text-center">TRUE_UP</th>
								<th  class="align-middle text-center">FPPCA-1</th>
								<th  class="align-middle text-center">FPPCA-2</th>
								<th  class="align-middle text-center">Total_CRJ</th>
								<th  class="align-middle text-center">PAID_EC</th>
								<th  class="align-middle text-center">PAID_ED</th>
								<th  class="align-middle text-center">PAID_LPC</th>
								<th  class="align-middle text-center">PAID_EDI</th>
								<th  class="align-middle text-center">PAID_FSA</th>
								<th  class="align-middle text-center">PAID_TUPC</th>
								<th  class="align-middle text-center">FPPCA-1</th>
								<th  class="align-middle text-center">FPPCA-2</th>
								<th  class="align-middle text-center">PAID_FSA</th>
								<th  class="align-middle text-center">TOT_PAY</th>
								<th  class="align-middle text-center">CB_EC</th>
								<th  class="align-middle text-center">CB_LPC</th>
								<th  class="align-middle text-center">CB_ED</th>
								<th  class="align-middle text-center">CB_IED</th>
								<th  class="align-middle text-center">CB_FSA</th>
								<th  class="align-middle text-center">CB_TUPC</th>
								<th  class="align-middle text-center">CB_OLD_FPP</th>
								<th  class="align-middle text-center">CB_NEW_FPP</th>
								<th  class="align-middle text-center">CBTOT</th>
								<th  class="align-middle text-center">OB_SD</th>
								<th  class="align-middle text-center">PAID_SD</th>
								<th  class="align-middle text-center">DRJ_SD</th>
								<th  class="align-middle text-center">CRJ_SD</th>
								<th  class="align-middle text-center">CB_SD</th>
								<th  class="align-middle text-center">OB_TUPC</th>
								<th  class="align-middle text-center">TUPC</th>
								<th  class="align-middle text-center">DRJ_TUPC</th>
								<th  class="align-middle text-center">CRJ_TUPC</th>
								<th  class="align-middle text-center">PAID_TUPC</th>
								<th  class="align-middle text-center">CB_TUPC</th>
								<th  class="align-middle text-center">OB_OLD_FPP</th>
								<th  class="align-middle text-center">OLD_FPP</th>
								<th  class="align-middle text-center">DRJ_OLD_FPP</th>
								<th  class="align-middle text-center">CRJ_OLD_FPP</th>
								<th  class="align-middle text-center">PAID_OLD_FPP</th>
								<th  class="align-middle text-center">CB_OLD_FPP</th>
								<th  class="align-middle text-center">OB_NEW_FP</th>
								<th  class="align-middle text-center">NEW_FPP</th>
								<th  class="align-middle text-center">DRJ_NEW_FPP</th>
								<th  class="align-middle text-center">CRJ_NEW_FPP</th>
								<th  class="align-middle text-center">PAID_NEW_FPP</th>
								<th  class="align-middle text-center">CB_NEW_FPP</th>
								<th  class="align-middle text-center">OB_CCLPC</th>
								<th  class="align-middle text-center">CCLPC</th>
								<th  class="align-middle text-center">RJ_CCLPC</th>
								<th  class="align-middle text-center">CB_CCLPC</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="mtrblc" items="${ledgerhist}" varStatus="tagStatus">
								<tr>
									<td class="text-right">${tagStatus.index + 1}</td>
									<td class="text-left">${mtrblc.MON_YEAR}</td>
									<td class="text-left">${mtrblc.USCNO}</td>
									<td class="text-left">${mtrblc.CAT}</td>
									<td class="text-left">${mtrblc.SCAT}</td>
									<td class="text-left">${mtrblc.STATUS}</td>
									<td class="text-left">${mtrblc.REC_KWH}</td>
									<td class="text-left">${mtrblc.REC_KVAH}</td>
									<td class="text-left">${mtrblc.MN_KVAH}</td>
									<td class="text-left">${mtrblc.CMD}</td>
									<td class="text-left">${mtrblc.REC_MD}</td>
									<td class="text-left">${mtrblc.BMD}</td>
									<td class="text-left">${mtrblc.OB_EC}</td>
									<td class="text-left">${mtrblc.OB_ED}</td>
									<td class="text-left">${mtrblc.OB_LPC}</td>
									<td class="text-left">${mtrblc.OB_EDI}</td>
									<td class="text-left">${mtrblc.OB_TUPC}</td>
									<td class="text-left">${mtrblc.OB_OLD_FPP}</td>
									<td class="text-left">${mtrblc.OB_NEW_FPP}</td>
									<td class="text-left">${mtrblc.OB_FSA}</td>
									<td class="text-left">${mtrblc.TOT_OB}</td>
									<td class="text-left">${mtrblc.EC}</td>
									<td class="text-left">${mtrblc.LPC}</td>
									<td class="text-left">${mtrblc.ED}</td>
									<td class="text-left">${mtrblc.EDI}</td>
									<td class="text-left">${mtrblc.TUPC}</td>
									<td class="text-left">${mtrblc.OLD_FPP}</td>
									<td class="text-left">${mtrblc.NEW_FPP}</td>
									<td class="text-left">${mtrblc.FSA}</td>
									<td class="text-left">${mtrblc.CMD}</td>
									<td class="text-left">${mtrblc.DRJ_EC}</td>
									<td class="text-left">${mtrblc.DRJ_ED}</td>
									<td class="text-left">${mtrblc.DRJ_LPC}</td>
									<td class="text-left">${mtrblc.DRJ_IED}</td>
									<td class="text-left">${mtrblc.DRJ_FSA}</td>
									<td class="text-left">${mtrblc.DRJ_TUPC}</td>
									<td class="text-left">${mtrblc.FPPCA-1}</td>
									<td class="text-left">${mtrblc.FPPCA-2}</td>
									<td class="text-left">${mtrblc.PAID_FSA}</td>
									<td class="text-left">${mtrblc.Total_DRJ}</td>
									<td class="text-left">${mtrblc.CRJ_EC}</td>
									<td class="text-left">${mtrblc.CRJ_ED}</td>
									<td class="text-left">${mtrblc.CRJ_LPC}</td>
									<td class="text-left">${mtrblc.CRJ_IED}</td>
									<td class="text-left">${mtrblc.CRJ_FSA}</td>
									<td class="text-left">${mtrblc.TRUE_UP}</td>
									<td class="text-left">${mtrblc.FPPCA-1}</td>
									<td class="text-left">${mtrblc.FPPCA-2}</td>
									<td class="text-left">${mtrblc.Total_CRJ}</td>
									<td class="text-left">${mtrblc.PAID_EC}</td>
									<td class="text-left">${mtrblc.PAID_ED}</td>
									<td class="text-left">${mtrblc.PAID_LPC}</td>
									<td class="text-left">${mtrblc.PAID_EDI}</td>
									<td class="text-left">${mtrblc.PAID_FSA}</td>
									<td class="text-left">${mtrblc.PAID_TUPC}</td>
									<td class="text-left">${mtrblc.FPPCA-1}</td>
									<td class="text-left">${mtrblc.FPPCA-2}</td>
									<td class="text-left">${mtrblc.TOT_PAY}</td>
									<td class="text-left">${mtrblc.CB_EC}</td>
									<td class="text-left">${mtrblc.CB_LPC}</td>
									<td class="text-left">${mtrblc.CB_ED}</td>
									<td class="text-left">${mtrblc.CB_IED}</td>
									<td class="text-left">${mtrblc.CB_FSA}</td>
									<td class="text-left">${mtrblc.CB_TUPC}</td>
									<td class="text-left">${mtrblc.CB_OLD_FPP}</td>
									<td class="text-left">${mtrblc.CB_NEW_FPP}</td>
									<td class="text-left">${mtrblc.CBTOT}</td>
									<td class="text-left">${mtrblc.OB_SD}</td>
									<td class="text-left">${mtrblc.PAID_SD}</td>
									<td class="text-left">${mtrblc.DRJ_SD}</td>
									<td class="text-left">${mtrblc.CRJ_SD}</td>
									<td class="text-left">${mtrblc.CB_SD}</td>
									<td class="text-left">${mtrblc.OB_TUPC}</td>
									<td class="text-left">${mtrblc.TUPC}</td>
									<td class="text-left">${mtrblc.DRJ_TUPC}</td>
									<td class="text-left">${mtrblc.CRJ_TUPC}</td>
									<td class="text-left">${mtrblc.PAID_TUPC}</td>
									<td class="text-left">${mtrblc.CB_TUPC}</td>
									<td class="text-left">${mtrblc.OB_OLD_FPP}</td>
									<td class="text-left">${mtrblc.OLD_FPP}</td>
									<td class="text-left">${mtrblc.DRJ_OLD_FPP}</td>
									<td class="text-left">${mtrblc.CRJ_OLD_FPP}</td>
									<td class="text-left">${mtrblc.PAID_OLD_FPP}</td>
									<td class="text-left">${mtrblc.CB_OLD_FPP}</td>
									<td class="text-left">${mtrblc.OB_NEW_FPP}</td>
									<td class="text-left">${mtrblc.NEW_FPP}</td>
									<td class="text-left">${mtrblc.DRJ_NEW_FPP}</td>
									<td class="text-left">${mtrblc.CRJ_NEW_FPP}</td>
									<td class="text-left">${mtrblc.PAID_NEW_FPP}</td>
									<td class="text-left">${mtrblc.CB_NEW_FPP}</td>
									<td class="text-left">${mtrblc.OB_CCLPC}</td>
									<td class="text-left">${mtrblc.CCLPC}</td>
									<td class="text-left">${mtrblc.RJ_CCLPC}</td>
									<td class="text-left">${mtrblc.CB_CCLPC}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
				<!-- target="_blank" -->
			</div>
		</div>




	</c:if>



			
			<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
		
		var supply1=$("#supply").val();
		console.log(supply);
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        "scrollX": true,
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'AccountCopy'},
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'AccountCopy'}
	            ]
	        }
	    });
	});
</script>		
<script> 
	requirejs([ 'jquery' ], function($) {
			$("td,th").each(function() { 
				if ($.isNumeric( $(this).text())) {
				    // It isn't a number	
				    $(this).html(parseFloat($(this).text()).toLocaleString('en-IN', {style: 'decimal', currency: 'INR'})); 
				}
			}
				
				
			)
			
	});
</script> 	

</div>