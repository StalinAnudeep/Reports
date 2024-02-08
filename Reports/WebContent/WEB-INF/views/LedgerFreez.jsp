<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>

<c:if test="${ not empty fn:trim(freez)}">
			<div class="card ">
		    <div class="card-body row-no-padding table-responsive-sm dataTables_wrapper">
			<table class="table card-table table-vcenter text-nowrap datatable display" style="width: 100%;">
					 <thead>
						<tr>
							<th>CIRCLE</th>
							<th>DATE</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mtrblc" items="${freez}"  varStatus="tagStatus">
							<tr>
								<td>${mtrblc.LEDGER_END_DT}</td>
								<td>${mtrblc.CIRCLE}</td>
							</tr>
						</c:forEach>
					</tbody>
	           </table>
				</div>
			</div>
			</c:if>