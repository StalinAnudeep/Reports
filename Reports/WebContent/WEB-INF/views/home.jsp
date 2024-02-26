<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<!-- <style>
 thead>tr>th{
	color: #fff !important;
    font-weight: bold  !important;
} 
</style> -->
<div class="row row-cards row-deck">
	<div class="col-sm-6 col-lg-4">
		<div class="card">
			<!--      <div class="card-header">
                    <h4 class="card-title">HT Reports</h4>
                  </div> -->
			<table class="table card-table table-sm">
				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT-DEMAND</th>

				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT08</span></td>
					<td><a href="CategoryWiseDemandReport">Category Wise
							Demand Abstract</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT10</span></td>
					<td><a href="DemandReport">Demand Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT10A</span></td>
					<td><a href="circelwisedemandreport">Circle Wise Demand
							Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT11</span></td>
					<td><a href="ComparisonDemandReport">Demand Comparison
							Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT25</span></td>
					<td><a href="secWiseSplitDemand">Section Wise Split Demand
							Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT27</span></td>
					<td><a href="subWiseSplitDemand"> SubDivision Wise Split
							Demand Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT30</span></td>
					<td><a href="voltagedemand">Voltage wise Demand Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT30B</span></td>
					<td><a href="fyvoltagedemand">Voltage wise Financial Year
							Demand Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT32</span></td>
					<td><a href="demandsplit">Demand Split Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT32A</span></td>
					<td><a href="servicewisedemandsplit">Service Wise Demand
							Split Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT32B</span></td>
					<td><a href="seasonaldemandsplit">Seasonal Demand Split
							Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT84</span></td>
					<td><a href="catsplit">Category Wise Split Demand</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT98</span></td>
					<td><a href="hoddemandsplit">Department Wise, Service Wise
							Demand Split </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT98A</span></td>
					<td><a href="hoddemandabstract">Department Wise Demand
							Split Abstract </a></td>
					<td></td>
				</tr>
				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT-COLLECTION</th>

				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT03</span></td>
					<td><a href="paymenthistory">Payment History</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT04</span></td>
					<td><a href="DCDivisionWiseReport">Division Wise Daily
							Collection Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT04A</span></td>
					<td><a href="DCSectionWiseReport">Section Wise Daily
							Collection Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT28</span></td>
					<td><a href="dailyCollection"> Total Daily Collections
							Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT104</span></td>
					<td><a href="CollectionAbstract">Collection Abstract</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT105</span></td>
					<td><a href="ServTypeCollection">Service Type Collection
							Abstract</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT106</span></td>
					<td><a href="HODTypeCollection">HOD Type Collection
							Abstract</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT122</span></td>
					<td><a href="pvtcoll">Private Department Collection</a></td>
					<td></td>
				</tr>

				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT-RJ</th>

				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT113</span></td>
					<td><a href="journalhistory">RJ History</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT12</span></td>
					<td><a href="RJServiceWiseReport">RJ Service Wise Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT12A</span></td>
					<td><a href="RJTypeWiseReport">RJ Type Wise Abstract</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT12B</span></td>
					<td><a href="RJNoWiseReport">RJ No Wise Abstract</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT12C</span></td>
					<td><a href="RJHODWiseReport">RJ HOD Wise Abstract</a></td>
					<td></td>
				</tr>

				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT
						- D-LIST</th>
				</tr>
				<tr>
					<td><span>HT26</span></td>
					<td><a href="SectionWiseArrears">Section Wise Arrears
							Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT46</span></td>
					<td><a href="dlist" data-toggle="collapse" data-target="#demo"><img
							src="./demo/brand/new.gif" style="width: 40px;"> D- List
							Reports</a></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="3" id="demo" class="collapse"><ul
							class="list-group">
							<li class="list-group-item"><a href="dlist"><img
									src="./demo/brand/new.gif" style="width: 40px;"> 1)D-
									List Report</a></li>
							<li class="list-group-item"><a href="dlistappeared">2)
									Division Wise D- List Appeared</a></li>
							<li class="list-group-item"><a href="dlistDivision">2.1)D-
									List Division Wise Abstract</a></li>
							<li class="list-group-item"><a href="acddlist">3)ACD D-
									List Report</a></li>
						</ul></td>
				</tr>


				<tr>
					<td><span>HT77A</span></td>
					<td><a href="arrears"> Arrears above 50000 Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT91</span></td>
					<td><a href="unpaid"> Unpaid HT Services</a></td>
					<td></td>
				</tr>



				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT-ACD</th>

				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT37</span></td>
					<td><a href="acdReport">ACD Review Report(ALL)</a></td>
					<td></td>
				</tr>
				<!-- 	<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT37</span></td>
					<td><a href="acdReport2022">ACD Review Report(ALL) -
							Revised</a></td>
					<td></td>
				</tr> -->
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT37A</span></td>
					<td><a href="ACDAbstract">ACD Notices Issued Abstract</a></td>
					<td></td>
				</tr>
				<!-- 	<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT37A</span></td>
					<td><a href="ACDAbstract2022">ACD Notices Issued Abstract
							- Revised </a></td>
					<td></td>
				</tr> -->

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT37B</span></td>
					<td><a href="acdnoticeabstract">ACD Notices Service List</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT37C</span></td>
					<td><a href="acdsurcharg">Consumer Wise ACD Balance & ACD
							Sur-Charge </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT37D</span></td>
					<td><a href="acdbalanceabstract">ACD Balance Abstract </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT37E</span></td>
					<td><a href="acdbalancereport">ACD Balances </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT37F</span></td>
					<td><a href="ACDCollection">ACD Collection </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT37G</span></td>
					<td><a href="acdnoticemonthly">ACD Notice Monthly Report</a></td>
					<td></td>
				</tr>


				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT-
						MAILS & PHONE NUMBERS</th>
				</tr>
				<tr>
					<td><span>HT20</span></td>
					<td><a href="EmailChecklist">List of Services Having Email
							ID's </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT21</span></td>
					<td><a href="noemails">List of services which don't have
							Email ID's</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT23</span></td>
					<td><a href="emailsent">Number of services for which
							EMAILS & SMS sent</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT23A</span></td>
					<td><a href="sentEmails">Services For Which
							Emails And SMS Sent</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT35</span></td>
					<td><a href="MobileCheckList">List of Services Having
							Mobile Numbers </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT36</span></td>
					<td><a href="nomobiles">List of services which don't have
							Mobile Numbers</a></td>
					<td></td>
				</tr>


				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT-
						RAC</th>

				</tr>
				<tr>

					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT123</span></td>
					<td><a href="monthWiseTariffReport">Month Wise Tariff
							Report</a></td>
					<td></td>
				</tr>

				<tr>

					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT124</span></td>
					<td><a href="financialYearTariffReport">FinancialYear Tariff
							Report</a></td>
					<td></td>
				</tr>

			</table>
		</div>
	</div>


	<div class="col-sm-6 col-lg-4">
		<div class="card">
			<!-- <div class="card-header">
                    <h4 class="card-title">HT Reports</h4>
                  </div> -->
			<table class="table card-table table-sm">

				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT-
						DCB</th>

				</tr>
				<tr>

					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT01</span></td>
					<td><a href="servicehistory">Service History</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT02A</span></td>
					<td><a href="HtSalesYearly">HT Sales Yearly Wise(Transco)</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT95</span></td>
					<td><a href="cmdca">Section Wise , Category Wise DCB
							Report </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT95A</span></td>
					<td><a href="dcbabstract">Section Wise , Category Wise DCB
							Split Report </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT97</span></td>
					<td><a href="hodreport">Department Wise, Service Wise DCB
							Report </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT97A</span></td>
					<td><a href="hodabstract">Department Wise, Section Wise
							DCB Report </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT97B</span></td>
					<td><a href="hodwiseabstract">Department Wise DCB Abstract
					</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT97C</span></td>
					<td><a href="HtDCBCollectionSplitMonthly">HT DCB
							Collection Split Monthly</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT97D</span></td>
					<td><a href="HtDCBCollectionSplitMonthlyWise">HT DCB
							Collection Split Monthly</a></td>
					<td></td>
				</tr>


				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT99</span></td>
					<td><a href="TrueUpCollection">True-Up Charges DCB </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT100</span></td>
					<td><a href="servicetypewiseabstract">Service Type Wise
							DCB Abstract </a></td>
					<td></td>
				</tr>
				<tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT100A</span></td>
					<td><a href="servicetyperevenue">Service Type Sales &
							Revenue Report </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT100B</span></td>
					<td><a href="servicetypecatwiseabstract">Service Type
							,Category Wise DCB Abstract </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT102</span></td>
					<td><a href="voltagewiseabstract">Voltage Wise DCB
							Abstract </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT103</span></td>
					<td><a href="feederwiseabstract">Feeder Wise DCB Abstract
					</a></td>
					<td></td>
				</tr>







				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT-
						LEDGER & CB</th>

				</tr>
				<tr>
					<td><span>HT06</span></td>
					<td><a href="accountCopy">Account Copy Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT06A</span></td>
					<td><a href="LedgerAccountCopy">Ledger Account Copy</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT22</span></td>
					<td><a href="NegativeCb">Negative CB Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT53</span></td>
					<td><a href="monthcb">Monthly Wise Closing Balance</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT69</span></td>
					<td><a href="ssd">Service Wise Ledger Sd Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT92</span></td>
					<td><a href="cbledger"> Services Wise Ledger Closing
							Balances</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT92A</span></td>
					<td><a href="cbledgerabs">Status Wise Ledger Closing
							Balances Abstract</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT92B</span></td>
					<td><a href="catcbledgerabs">Category Wise Ledger Closing
							Balances Abstract</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT92C</span></td>
					<td><a href="CBAndDemandAbstract">CB Split (Arrear, Demand
							Part) Abstract </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT92D</span></td>
					<td><a href="CBAndDemand">CB Split (Arrear, Demand Part)
							Service wise Report </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT92E</span></td>
					<td><a href="AgeWiseConsumerLedgerAbstract">Age Wise
							Arrears Abstract </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT92F</span></td>
					<td><a href="AgeWiseGovtPvtStatusLedgerAbstract">Age Wise
							Govt-Pvt-Status Wise Abstract </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT96</span></td>
					<td><a href="lgdcodes">LGD Report </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT96A</span></td>
					<td><a href="lgdcodesservicewise">LGD Service Wise Report
					</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT101</span></td>
					<td><a href="msmewiseabstract">List of MSME Services
							Arrears Report </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT101A</span></td>
					<td><a href="msmetypewisedcb">MSME Type Wise DCB Abstract</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT114</span></td>
					<td><a href="courtcases">Service Wise Court Case Report</a></td>
					<td></td>
				</tr>




				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT-
						ENERGY AUDIT</th>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT02</span></td>
					<td><a href="HtSales">HT Sales (Transco)</a></td>
					<td></td>
				</tr>
				<tr>

					<td><span>HT05</span></td>
					<td><a href="Tp_Sales">Third Party Sales Confirmation</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT15</span></td>
					<td><a href="feedercon">Feeder wise Consumption Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT17</span></td>
					<td><a href="consumptionVariation">HT Variation In
							Consumption Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT29</span></td>
					<td><a href="Tp_Sales2"> Voltage Wise Third Party Sales</a></td>
					<td></td>
				</tr>


				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT30A</span></td>
					<td><a href="openaccesssales"> Voltage Wise Open Access
							Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT31</span></td>
					<td><a href="Volopen">Voltage wise other Energy Sales
							report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT93</span></td>
					<td><a href="energysales">Energy Audit Sales Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT93A</span></td>
					<td><a href="energysalesoa">Energy Audit Sales Report(Open
							Access 2021-23)</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT93B</span></td>
					<td><a href="energysalesoamw">Energy Audit Sales Report
							Open Access 30 % Month Wise</a></td>
					<td></td>
				</tr>
				<tr>
					<td><img src="./demo/brand/new.gif" style="width: 40px;"><span>HT121</span></td>
					<td><a href="servicewiseledgersplit">Service Wise CB Split</a></td>
					<td></td>
				</tr>
				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT-ISD
						& TDS</th>

				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT73</span></td>
					<td><a href="hello">Single Line ISD Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT73A</span></td>
					<td><a href="ISDAbstract">ISD Abstract</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT73B</span></td>
					<td><a href="isdcalculation">Service Wise ISD Calculation
					</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT73C</span></td>
					<td><a href="tdsreport">Service Wise TDS Report </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT73D</span></td>
					<td><a href="TDSAbstract"> TDS Abstract </a></td>
					<td></td>
				</tr>

			</table>
		</div>
	</div>
	<div class="col-sm-6 col-lg-4">
		<div class="card">
			<!-- <div class="card-header">
                    <h4 class="card-title">HT Reports</h4>
                  </div> -->
			<table class="table card-table table-sm">






				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT
						- MASTER</th>
				</tr>


				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT02B</span></td>
					<td><a href="HtSalesDCB">HT Year Wise Cat,Sub-Cat Wise
							Sales Dcb Abstract</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT02C</span></td>
					<td><a href="HtCatStatusWiseServicesWithLoad">HT CAT Wise
							STATUS Wise Services With Load</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT02D</span></td>
					<td><a href="BillStopServicesStatus">HT Bill Stop Services
							Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT02E</span></td>
					<td><a href="UrbanRuralConsumers">CEA (Rural,Urban
							Consumers) Report </a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT02F</span></td>
					<td><a href="meterstatus">HT-Metering Status Month Wise
							Report </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT02G</span></td>
					<td><a href="solarmeterstatus">HT-Solar Metering Status
							Month Wise Report </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT09</span></td>
					<td><a href="masterchangereport">Change History HT</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT13</span></td>
					<td><a href="kwhmonthlyreport">Category-wise Monthly
							Electricity Consumption</a></td>
					<td></td>
				</tr>





				<tr>
					<td><span>HT18</span></td>
					<td><a href="ConsumerMaster">Consumer Master Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT19</span></td>
					<td><a href="Liveandbill">HT Live & Billed Services</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT66</span></td>
					<td><a href="totbillstop">Total Bill Stop Services Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT71</span></td>
					<td><a href="ServiceRelease">HT Service Release Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT72</span></td>
					<td><a href="ServiceWiseEd">HT Service Wise ED Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT81</span></td>
					<td><a href="liveandbilstopcategory">Circle Wise Category
							Existing Service And Load</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT85</span></td>
					<td><a href="udc">UDC Services Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT86</span></td>
					<td><a href="Govt">Govt_Services</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT87</span></td>
					<td><a href="cbmdexceed">BMD Exceeded Report CPDCL</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT88</span></td>
					<td><a href="threebmd">Three Months BMD Exceeded CPDCL
							Services</a></td>
					<td></td>
				</tr>



				<tr class="bg-primary">
					<th class="text-center" colspan="3"
						style="color: #fff !important; font-weight: bold !important;">HT
						- OTHERS</th>
				</tr>

				<tr>
					<td><span>HT16</span></td>
					<td><a href="billanalysis"> HT Bill Analysis Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT48</span></td>
					<td><a href="mf">MF Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT63</span></td>
					<td><a href="LowVoltage">Low Voltage Sur Charge Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT68</span></td>
					<td><a href="virtual">Virtual Bank List Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT70</span></td>
					<td><a href="SolarEnergyReport">Solar Energy Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT75</span></td>
					<td><a href="aqua">Aqua Culture Subsidy Charges Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT78</span></td>
					<td><a href="Lfi">Load Factor Incentive Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT82</span></td>
					<td><a href="sppcs">Startup Power Services Plant Capacity</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT83</span></td>
					<td><a href="amr">AMR Check List</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT89</span></td>
					<td><a href="metermf"> Meter & MF Change Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span>HT90</span></td>
					<td><a href="mtrnodetails"> MIS Data Verify</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span>HT94</span></td>
					<td><a href="apgpcl">Apgpcl & Ferro Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT107</span></td>
					<td><a href="FeederData">Edgegrid - Feeder Data Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT107A</span></td>
					<td><a href="ICEData">Edgegrid - ICE Data Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT108</span></td>
					<td><a href="SolarExport">Circle wise SRT units exported
							to the Grid</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT109</span></td>
					<td><a href="TCSExemption">TCS Exemption Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT110</span></td>
					<td><a href="gridsupporting">Grid Supporting Monthly
							Charges Details</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT110A</span></td>
					<td><a href="catwisegridsupporting">Category Wise Grid
							Supporting Monthly Charges Details</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT111</span></td>
					<td><a href="EDAbstract">ED Abstract</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT112</span></td>
					<td><a href="trueupchg">Service Wise True Up Charges (2014
							-2019)</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT112A</span></td>
					<td><a href="TrueUpAbstract">True Up Abstract (2014 -2019)
					</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT112B</span></td>
					<td><a href="trueupkvahhistory">Service Wise True Up
							Billed Units History (2014 -2019) </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT120</span></td>
					<td><a href="lthttrueupchg">LT - HT Service Wise True Up
							Charges(2014 -2019) </a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;"></span></td>
					<td><a href="isucufiledownload">ISU CU File Download</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;"></span></td>
					<td><a href="kyc">Know Your Details Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT115</span></td>
					<td><a href="metersreport">Existing Meter Details Entry
							Report</a></td>
					<td></td>
				</tr>
				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT116</span></td>
					<td><a href="masterreport">Master Details Entry Report</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT117</span></td>
					<td><a href="lthtfppcachg">LT - HT Service Wise FPPCA
							Charges (2021-2022)</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT117A</span></td>
					<td><a href="htbsfppcachg"> HT Bill StopServices FPPCA
							Charges (2021-2022)</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT118</span></td>
					<td><a href="oahistory">HT Old Open Access History</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT119</span></td>
					<td><a href="oldcolonyrdng">HT Old Colony History</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT119 A</span></td>
					<td><a href="oldcolonyrdngtu">HT Old Colony True Up
							Charges (2014-2019)</a></td>
					<td></td>
				</tr>

				<tr>
					<td><span><img src="./demo/brand/new.gif"
							style="width: 40px;">HT125</span></td>
					<td><a href="fyConsumption"> FY Consumption Report</a></td>
					<td></td>
				</tr>




			</table>
		</div>
	</div>

</div>
<script>
	requirejs(
			[ 'jquery' ],
			function($) {
				$(document)
						.ready(
								function() {
									$(".on-page-search")
											.on(
													"keyup",
													function() {
														var v = $(this).val();
														$(".results")
																.removeClass(
																		"results");
														$("a.demo-links")
																.each(
																		function() {
																			if (v != ""
																					&& $(
																							this)
																							.text()
																							.search(
																									new RegExp(
																											v,
																											'gi')) != -1) {
																				$(
																						this)
																						.addClass(
																								"results");
																			}
																		});
													});
								});
			});
</script>

<script>
	require([ 'jquery', 'datatables.net', 'datatables.net-jszip',
			'datatables.net-buttons', 'datatables.net-buttons-flash',
			'datatables.net-buttons-html5' ], function($, datatable, jszip) {

		window.JSZip = jszip;
		$('.datatable').DataTable({
			dom : 'Bfrltip',
			buttons : {
				buttons : [ {
					extend : 'csv',
					className : 'btn btn-xs btn-primary',
					title : 'ISD_Details'
				}, {
					extend : 'excel',
					className : 'btn btn-xs btn-primary',
					title : 'ISD_Details'
				} ]
			}
		});
	});
</script>

<style>
a {
	text-decoration: none;
	font-family: cursive;
	font-size: small;
}

a:hover {
	color: blue;
	text-decoration: none;
	cursor: pointer;
}

span {
	color: green
}

.card-title {
	color: blue;
}
</style>
<jsp:include page="footer.jsp"></jsp:include>