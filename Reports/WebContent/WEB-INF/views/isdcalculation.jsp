<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<style>
	
       table {
        border-spacing: 0px;
        table-layout: fixed;
        margin-left: auto;
        margin-right: auto;
        width: 310px;
      }  
       td {
        
        width:9%!important;
        word-wrap: break-word!important;
      } 
    </style>
    <!--     <style>
    table {border-collapse:collapse; table-layout:fixed; width:310px;}
       table td {border:solid 1px #fab; width:100px;
      word-break: break-all;}
      </style> -->


		<jsp:include page="header_lg.jsp"></jsp:include>
		
	    <div class="row row-cards row-deck">
	      <form class="card" action="isdcalculation" method="post">
                <div class="card-body">
                  <h3 class="card-title"><strong><span class="text-danger">HT73B</span> - Service Wise ISD Calculation </strong> </h3>
                  <div class="row">
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">Circle</label>
                        <select class="form-control" name="circle" id="circle" required="required">
						    <option value="">Select Circle</option>
						</select>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-label">Year</label>
                       <select id="inputyear" class="form-control" name="year" required="required">
					      <option value="">--Select Financial Year--</option>
					      </select>
                      </div>
                    </div>
                    <div class="col-sm-6 col-md-4">
                      <div class="form-group">
                        <label class="form-label">Get ISD Calculation</label>
                        <button type="submit" class="btn btn-success">Get ISD Calculation</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
        <!-- </div> -->
	<c:if test="${ not empty fn:trim(fail)}">
		<div id="exist" class="alert alert-danger" role="alert">${fail}</div>
	</c:if>
	<c:if test="${ not empty fn:trim(single)}">
		<div class="card" style="overflow: auto;">
		<div class="card-body row-no-padding table-responsive-sm">
		<div class="row">
		<div class="bg-info text-white text-center col-md-3" onclick="exportThisWithParameter('multiLevelTable', '${title}')" style="cursor: pointer; border: 1px solid #ccc; text-align: center;width:19%;padding-bottom: 10px;padding-top: 10px;">Excel</div>
		<div class="col-md-6">
	<!-- 	<input id=txt_searchall type="text" placeholder="Search.."> -->
		</div>
		</div>
			<table id="multiLevelTable" class="table table-bordered " style="width: 100%;">
				<thead>
					<tr>
							<th>S.NO</th>
							<th>ISCNO</th>
							<th>CTNAME</th>
							<th>FROM DATE</th>
							<th>TO DATE</th>
							<th>SD SEGMENT AMOUNT</th>
							<th>SD ADD AMOUNT</th>
							<th>ISD SEGMENT AMOUNT</th>
							<th>ADD SEGMENT AMOUNT</th>
							<th>ISD RATE</th>
							<th>NO.OF.DAYS</th>

						</tr>
				</thead>
				<tbody>
				<%
									String scno = "S";
							%>
							<c:set var="count" value="1" scope="page" />
					<c:forEach var="mtrblc" items="${single}" varStatus="tagStatus">
					<c:set var="scno" value="${mtrblc.ISCNO}" scope="request" />
					
						<tr >
							
							<%
										if (!scno.equals((String) request.getAttribute("scno"))) {
									%>
									<td class="text-left  "
											style="padding-left: 5px; vertical-align: middle;" rowspan="${CIRCOUNT[scno]}">${count}</td>
									<td class="text-left  "
											style="padding-left: 5px;  vertical-align: middle;" rowspan="${CIRCOUNT[scno]}">${mtrblc.ISCNO}</td>
									<td class="text-left  "
											style="padding-left: 5px; vertical-align: middle;" rowspan="${CIRCOUNT[scno]}">${mtrblc.CTNAME}</td>
										<c:set var="count" value="${count + 1}" scope="page"/>
									<%
										}
							scno = (String) request.getAttribute("scno");
									%>

							
							<td class="text-right  "
											style="padding-left: 5px;">${mtrblc.FROM_DT}</td>
							<td class="text-right  "
											style="padding-left: 5px;">${mtrblc.TO_DT}</td>
							<td class="text-right  "
											style="padding-left: 5px;">${mtrblc.SD_SEG_AMT}</td>
							<td class="text-right  "
											style="padding-left: 5px;">${mtrblc.SD_ADD}</td>
							<td class="text-right  "
											style="padding-left: 5px;">${mtrblc.ISD_SEG_AMT}</td>
							<td class="text-right  "
											style="padding-left: 5px;">${mtrblc.ADD_SEG_AMT}</td>
							<td class="text-right  "
											style="padding-left: 5px;">${mtrblc.ISD_RATE}</td>
							<td class="text-right  "
											style="padding-left: 5px;">${mtrblc.NUM_OF_DAYS}</td>
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
		for (var j = currentYear; j > 2020; j--) {
			var jj = j - 1 + "-" + j;
			$("#inputyear").append("<option value="+jj+">" + jj + "</option>");
		}
	});
</script>

<script>
	require([  'jquery','datatables.net','datatables.net-jszip','datatables.net-buttons','datatables.net-buttons-flash','datatables.net-buttons-html5'], function($,datatable,jszip ) {
	
		window.JSZip = jszip;
		$('.datatable').DataTable({
	        dom: 'Bfrltip',
	        buttons: {
	            buttons: [
	                { extend: 'csv', className: 'btn btn-xs btn-primary',title: 'ISD_Details' },
	                { extend: 'excel', className: 'btn btn-xs btn-primary',title: 'ISD_Details' }
	            ]
	        }
	    });
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
<script type='text/javascript'>
    $(document).ready(function(){

        // Search all columns
        $('#txt_searchall').keyup(function(){
            var search = $(this).val();

            if(search.length>4){
            $('table tbody tr').hide();
            var len = $('table tbody tr:not(.notfound) td:contains("'+search+'")').length;
          

            if(len > 0){
              $('table tbody tr:not(.notfound) td:contains("'+search+'")').each(function(){
                  $(this).closest('tr').show();
              });
            }else{
              $('.notfound').show();
            }
            }
        });
    });
    // Case-insensitive searching (Note - remove the below script for Case sensitive search )
    $.expr[":"].contains = $.expr.createPseudo(function(arg) {
        return function( elem ) {
            return $(elem).text().toUpperCase().indexOf(arg.toUpperCase()) >= 0;
        };
    });
</script>




<jsp:include page="footer.jsp"></jsp:include>