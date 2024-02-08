<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="header.jsp"></jsp:include>
<script>

	requirejs([ 'jquery' ], function($) {
		$(document).ready(function() {

			$("#idForm").submit(function(e) {
				e.preventDefault(); // avoid to execute the actual submit of the form.
				var form = $(this);
				var url = form.attr('action');
				$.ajax({
					type : "POST",
					url : url,
					data : form.serialize(), // serializes the form's elements.
					success : function(data) {
						if(data=='Fail')
					    {
							$("#scno").val('');
							$("#result").empty();
							$("#result").append("<div id='exist' class='alert alert-danger' role='alert'>NO DATA FOUND</div>");
						}else{
							$("#scno").val('');
							$("#result").empty();
							$("#result").append(data);
						}
					}
				});
			});

		});
	});
</script>

<div class="row row-cards row-deck">
	      <form class="card" action="mtrnodetails" method="post" id="idForm">
                <div class="card-body">
                  <h3 class="card-title">Get Consumer Details </h3>
                  <div class="row">
                     <div class="col-md-3">
					      <label for="inputState">Meter Number</label>
					      <input type="text" class="form-control" name="scno" id="scno">
					   </div>
					   <div class="col-md-3" style="margin-top:39px;">
	                        <div class="form-check-inline">
							  <label class="form-check-label">
							    <input type="radio" class="form-check-input" value="M" name="con" checked="checked">Meter No
							  </label>
							</div>
							<div class="form-check-inline">
							  <label class="form-check-label">
							    <input type="radio" class="form-check-input" value="S" name="con">Service No
							  </label>
							</div>
					   </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label class="form-label">Get Consumer Details</label>
                        <button type="submit" class="btn btn-success" >Get Consumer Details</button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
</div>
<div id="result"></div>