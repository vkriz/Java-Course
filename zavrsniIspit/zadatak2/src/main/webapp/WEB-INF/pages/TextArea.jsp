<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<body>
		<c:if test="${ error != null }">
			<c:out value="${error}"/>
		 </c:if>
		<div>
			<form action="drawServlet" method="post">
				<div>
					<div>
				  		<span class="formLabel">Text</span>
				  		<textarea name="text" rows="10" cols="100">${text }</textarea>
				 	</div>
				</div>
	
				<div class="formControls">
				  	<span class="formLabel">&nbsp;</span>
				  	<input type="submit" name="metoda" value="Draw">
				</div>
				
			</form>			
		</div>
	</body>
</html>