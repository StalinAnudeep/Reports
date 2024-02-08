<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<body data-scrolling-animations="true">
	<div class="sp-body">
		
		<%-- <%@include file="../staticheader.jsp" %> --%>
		<div id="content" style="text-align:center;">
	    	<div class="custom-error" style="margin-top:40px;margin-bottom:20px;min-height:250px;">
	    		<div>
	    			<h1 class="error-title title" style="font-size: 8.0em; text-shadow: 3px 3px 0 rgba(0, 0, 0, 0.1);height:1em;">500</h1>
	    			<h2 class="error-message" style="font-size:2.0em;">Internal Server Error</h2>
	    			<div class="error-content">
	    				<c:choose>
	    					<c:when test="${empty error}">
	    						<p>The Web server encountered an unexpected problem.</p>
	    					</c:when>
	    					<c:otherwise>
	    						<p>${error}</p>
	    					</c:otherwise>
	    				</c:choose>				    				
	    				<p><a href="home"><span>Return to the Homepage</span></a></p>
	    			</div>
	    		</div>
	    	</div>
	    </div>
		<div class="container-fluid"></div>
		<hr/>
		
	 	<%-- <%@include file="../footer.jsp" %> --%>
		
	</div>
</body>
</html>