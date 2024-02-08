<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header_lg.jsp"></jsp:include>
<script>
requirejs([ 'jquery' ], function($) {
$(document).ready(function() {
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
<style>

 thead>tr>th{
	color: #fff !important;
    font-weight: bold  !important;
} 
/* tr:nth-child(even) {
            background-color: rgb(70 127 207 / 25%);
        } */
       
.EVEN
{background-color: #fdf59a;}
.ODD
{background-color: #b3dfff;}

</style>


<div class="row row-cards row-deck">
	      <form class="card" action="servicechangehistory" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT01A</span> - HT Service Change History</strong> </h3>
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                          <label for="inputCity">Enter Service Number</label>
					      <input type="text" class="form-control"   required="required" name="scno" id="scno" placeholder="Enter Service Number">
                      </div>
                    </div>

                    <div class="col-md-6">
                      <div class="form-group">
                        <label class="form-label">GET Service Change History</label>
                        <button type="submit" class="btn btn-success">GET Service Change History </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
			
			<c:if test="${ not empty fn:trim(fail)}">
				<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
			</c:if>
			
			<c:if test="${ not empty fn:trim(account)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		        <h3 class="text-center">Master Change Details</h3>
		    <input type="hidden" value="${supply}" id="supply">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					
	                 <thead>
						<tr class="bg-primary">
							<th class="text-right">S.NO</th>
							<th>USCNO</th>
							<th>TYPE</th>
							<th>OLD_VAL</th>
							<th>NEW_VAL</th>
							<th>DEPT ORDER NO</th>
							<th>DEPT ORDER DATE</th>
							<th>EFFECTED DATE</th>
							<th>CREATION DATE</th>
							<th>CHANGED BY</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${account}"  varStatus="tagStatus">
							<tr>
								<td>${tagStatus.index + 1}</td>
								<td>${mtrblc.USCNO}</td>
								<td>${mtrblc.TYPE}</td>
								<td>${mtrblc.OLD_VAL}</td>
								<td>${mtrblc.NEW_VAL}</td>
								<td>${mtrblc.DEPT_ORDER_NO}</td>
								<td>${mtrblc.DEPT_ORDER_DT}</td>
								<td>${mtrblc.EFF_DT}</td>
								<td>${mtrblc.CRE_DTTM}</td>
								<td>${mtrblc.CHANGED_BY}</td>
							</tr>
						</c:forEach>
					</tbody> 	 
	           </table>
				</div>
			</div>
			</c:if>
			
			<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(meter)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
		     <h3 class="text-center">Meter & Cubical Change Details</h3>
			<table class="table card-table table-sm table-vcenter text-nowrap datatable" style="width: 100%;font-weight: 600;">
				<thead>
					<tr class="bg-primary">
						<th class="text-center">S.NO</th>
						<th class="text-center">USCNO</th>
						<th class="text-center">CHANGE DETAILS</th>
						<th class="text-center" colspan="4">OLD VALUES</th>
						<th class="text-center" colspan="4">NEW VALUES</th>
					</tr>
				</thead>
				 <tbody>
					<c:forEach var="mtrblc" items="${meter}" varStatus="tagStatus">

					
					<c:set var="cssclass" value="${(tagStatus.index+1) % 2 == 0  ? 'EVEN' : 'ODD'}"/>

			<c:if test="${mtrblc.TYPE_CHANGE eq 'B'}">
						<tr class="${cssclass}" >
							<td class="text-center" rowspan="8"  style="color:#04c;">${tagStatus.index + 1}</td>
							<td  class="text-center" rowspan="8"  style="color:#04c;">${mtrblc.USCNO}</td>
							<td  rowspan="8" class="text-center"  style="color:#04c;">TYPE : ${mtrblc.TYPE_CHANGE_C}<br>
							Changed On : ${mtrblc.CHANGED_ON_C}<br>
							Changed By : ${mtrblc.CREATED_BY_C} </td>
							<%-- <c:if test="${mtrblc.TYPE_CHANGE eq 'C' || mtrblc.TYPE_CHANGE eq 'B'}"> --%>
							<td style="padding-left: 4.8px;color:#04c;" class="text-right"><strong>Voltage</strong> </td>
							<td>${mtrblc.C_VOLTAGE_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>CT Ration</strong>  </td>
							<td>${mtrblc.CT_RATIO_OLD} </td>
							
							<td style="color:#04c;"  class="text-right"><strong> Voltage</strong>  </td>
							<td> ${mtrblc.C_VOLTAGE_NEW} </td>							
							<td style="color:#04c;"  class="text-right"><strong> CT Ration </strong> </td>
							<td>${mtrblc.CT_RATIO_NEW} </td>
							<%-- </c:if> --%>
					</tr>
					<%-- <c:if test="${mtrblc.TYPE_CHANGE eq 'C' || mtrblc.TYPE_CHANGE eq 'B'}"> --%>
					 <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>CT Class</strong>  </td>
							<td >${mtrblc.CT_CLASS_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>PT Ration </strong> </td>
							<td >${mtrblc.PT_RATIO_OLD} </td>
							
							<td  style="color:#04c;"  class="text-right"><strong>CT Class </strong> </td>
							<td >${mtrblc.CT_CLASS_NEW} </td>							
							<td  style="color:#04c;"  class="text-right"><strong>PT Ration </strong> </td>
							<td >${mtrblc.PT_RATIO_NEW} </td>
					 </tr>
					
					   <tr class="${cssclass}">									 
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>PT Class </strong> </td>
							<td >${mtrblc.PT_CLASS_OLD} </td>
							<td  style="color:#04c;"  class="text-right"> <strong>MF</strong> </td>
							<td > ${mtrblc.MF_OLD}</td>
							
							<td  style="color:#04c;"  class="text-right"><strong>PT Class</strong>  </td>
							<td >${mtrblc.PT_CLASS_NEW} </td>
							<td  style="color:#04c;"  class="text-right"><strong>MF</strong>  </td>
							<td >${mtrblc.MF_NEW} </td>

						</tr>
						<%-- </c:if>
						<c:if test="${mtrblc.TYPE_CHANGE eq 'M' || mtrblc.TYPE_CHANGE eq 'B'}"> --%>
						
						 <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter No </strong> </td>
							<td >${mtrblc.METER_NO_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Make </strong> </td>
							<td >${mtrblc.METER_MAKE_OLD} </td>
							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter No </strong> </td>
							<td >${mtrblc.METER_NO_NEW} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Make </strong> </td>
							<td >${mtrblc.METER_MAKE_NEW} </td>
					 </tr>
						 <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Type</strong>  </td>
							<td >${mtrblc.METER_TYPE_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Manufacture Date </strong> </td>
							<td >${mtrblc.MANUFACTURE_DATE_OLD} </td>
							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Type </strong> </td>
							<td >${mtrblc.METER_TYPE_NEW} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Manufacture Date</strong>   </td>
							<td >${mtrblc.MANUFACTURE_DATE_NEW} </td>
					 </tr>
					  <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Display Type </strong> </td>
							<td >${mtrblc.METER_DISPLAY_TYPE_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Use </strong> </td>
							<td >${mtrblc.METER_USE_OLD} </td>
							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Display Type</strong>  </td>
							<td >${mtrblc.METER_DISPLAY_TYPE_NEW} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Use </strong>  </td>
							<td >${mtrblc.METER_USE_NEW} </td>
					 </tr>
					  <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter CT Ratio</strong>  </td>
							<td >${mtrblc.M_CT_RATIO_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter CT Class </strong> </td>
							<td >${mtrblc.M_CT_CLASS_OLD } </td>
							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter CT Ratio</strong> </td>
							<td >${mtrblc.M_CT_RATIO_NEW} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter CT Class</strong> </td>
							<td >${mtrblc.M_CT_CLASS_NEW } </td>
					 </tr>
					  <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter PT Ratio </strong> </td>
							<td >${mtrblc.M_PT_RATIO_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter PT Class</strong>  </td>
							<td >${mtrblc.M_PT_CLASS_OLD } </td>
							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter PT Ratio</strong> </td>
							<td >${mtrblc.M_PT_RATIO_NEW} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter PT Class</strong> </td>
							<td >${mtrblc.M_PT_CLASS_NEW } </td>
					 </tr>
					 </c:if>
					 <c:if test="${mtrblc.TYPE_CHANGE eq 'C'}">
						<tr class="${cssclass}" >
							<td class="text-center" rowspan="3"  style="color:#04c;">${tagStatus.index + 1}</td>
							<td  class="text-center" rowspan="3"  style="color:#04c;">${mtrblc.USCNO}</td>
							<td  rowspan="5" class="text-center"  style="color:#04c;">TYPE : ${mtrblc.TYPE_CHANGE_C}<br>
							Changed On : ${mtrblc.CHANGED_ON_C}<br>
							Changed By : ${mtrblc.CREATED_BY_C} </td>
							<%-- <c:if test="${mtrblc.TYPE_CHANGE eq 'C' || mtrblc.TYPE_CHANGE eq 'B'}"> --%>
							<td style="padding-left: 4.8px;color:#04c;" class="text-right"><strong>Voltage</strong> </td>
							<td>${mtrblc.C_VOLTAGE_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>CT Ration</strong>  </td>
							<td>${mtrblc.CT_RATIO_OLD} </td>
							
							<td style="color:#04c;"  class="text-right"><strong> Voltage</strong>  </td>
							<td> ${mtrblc.C_VOLTAGE_NEW} </td>							
							<td style="color:#04c;"  class="text-right"><strong> CT Ration </strong> </td>
							<td>${mtrblc.CT_RATIO_NEW} </td>
							<%-- </c:if> --%>
					</tr>
					<%-- <c:if test="${mtrblc.TYPE_CHANGE eq 'C' || mtrblc.TYPE_CHANGE eq 'B'}"> --%>
					 <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>CT Class</strong>  </td>
							<td >${mtrblc.CT_CLASS_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>PT Ration </strong> </td>
							<td >${mtrblc.PT_RATIO_OLD} </td>
							
							<td  style="color:#04c;"  class="text-right"><strong>CT Class </strong> </td>
							<td >${mtrblc.CT_CLASS_NEW} </td>							
							<td  style="color:#04c;"  class="text-right"><strong>PT Ration </strong> </td>
							<td >${mtrblc.PT_RATIO_NEW} </td>
					 </tr>
					
					   <tr class="${cssclass}">									 
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>PT Class </strong> </td>
							<td >${mtrblc.PT_CLASS_OLD} </td>
							<td  style="color:#04c;"  class="text-right"> <strong>MF</strong> </td>
							<td > ${mtrblc.MF_OLD}</td>
							
							<td  style="color:#04c;"  class="text-right"><strong>PT Class</strong>  </td>
							<td >${mtrblc.PT_CLASS_NEW} </td>
							<td  style="color:#04c;"  class="text-right"><strong>MF</strong>  </td>
							<td >${mtrblc.MF_NEW} </td>

						</tr>
					 </c:if>
					 <c:if test="${mtrblc.TYPE_CHANGE eq 'M'}">
						<tr class="${cssclass}" >
							<td class="text-center" rowspan="5"  style="color:#04c;">${tagStatus.index + 1}</td>
							<td  class="text-center" rowspan="5"  style="color:#04c;">${mtrblc.USCNO}</td>
							<td  rowspan="5" class="text-center"  style="color:#04c;">TYPE : ${mtrblc.TYPE_CHANGE_C}<br>
							Changed On : ${mtrblc.CHANGED_ON_C}<br>
							Changed By : ${mtrblc.CREATED_BY_C} </td>
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter No </strong> </td>
							<td >${mtrblc.METER_NO_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Make </strong> </td>
							<td >${mtrblc.METER_MAKE_OLD} </td>
							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter No </strong> </td>
							<td >${mtrblc.METER_NO_NEW} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Make </strong> </td>
							<td >${mtrblc.METER_MAKE_NEW} </td>
					</tr>
					
					
						 <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Type</strong>  </td>
							<td >${mtrblc.METER_TYPE_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Manufacture Date </strong> </td>
							<td >${mtrblc.MANUFACTURE_DATE_OLD} </td>
							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Type </strong> </td>
							<td >${mtrblc.METER_TYPE_NEW} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Manufacture Date</strong>   </td>
							<td >${mtrblc.MANUFACTURE_DATE_NEW} </td>
					 </tr>
					  <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Display Type </strong> </td>
							<td >${mtrblc.METER_DISPLAY_TYPE_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Use </strong> </td>
							<td >${mtrblc.METER_USE_OLD} </td>
							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Display Type</strong>  </td>
							<td >${mtrblc.METER_DISPLAY_TYPE_NEW} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter Use </strong>  </td>
							<td >${mtrblc.METER_USE_NEW} </td>
					 </tr>
					  <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter CT Ratio</strong>  </td>
							<td >${mtrblc.M_CT_RATIO_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter CT Class </strong> </td>
							<td >${mtrblc.M_CT_CLASS_OLD } </td>
							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter CT Ratio</strong> </td>
							<td >${mtrblc.M_CT_RATIO_NEW} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter CT Class</strong> </td>
							<td >${mtrblc.M_CT_CLASS_NEW } </td>
					 </tr>
					  <tr class="${cssclass}">																
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter PT Ratio </strong> </td>
							<td >${mtrblc.M_PT_RATIO_OLD} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter PT Class</strong>  </td>
							<td >${mtrblc.M_PT_CLASS_OLD } </td>
							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter PT Ratio</strong> </td>
							<td >${mtrblc.M_PT_RATIO_NEW} </td>							
							<td style="padding-left: 4.8px;color:#04c;"  class="text-right"><strong>Meter PT Class</strong> </td>
							<td >${mtrblc.M_PT_CLASS_NEW } </td>
					 </tr>
					 </c:if>
					</c:forEach>
				</tbody> 
			</table>
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
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'AccountCopy'+" "+"AND SUPPLY RELEASE DATE"+" "+supply1},
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title:'AccountCopy'+" "+"AND SUPPLY RELEASE DATE"+" "+supply1}
	            ]
	        }
	    });
	});
</script>		
<!-- <script> 
	requirejs([ 'jquery' ], function($) {
			$("td,th").each(function() { 
				if ($.isNumeric( $(this).text())) {
				    // It isn't a number	
				    $(this).html(parseFloat($(this).text()).toLocaleString('en-IN', {style: 'decimal', currency: 'INR'})); 
				}
			}
				
				
			)
			
	});
</script>  -->	
</div>