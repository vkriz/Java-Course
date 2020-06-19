<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%!
private String elapsedTime() {
	long elapsed = System.currentTimeMillis() 
					- (long) getServletContext().getAttribute("startTime");
	
	long seconds = elapsed / 1000;
	long minutes = seconds / 60;
	long hours = minutes / 60;
	long days = hours / 24;
	
	return days + " days " 
			+ hours % 24 + " hours " 
			+ minutes % 60 + " minutes " 
			+ seconds % 60 + " second and "
			+ elapsed % 1000 + " miliseconds";
}
%>

<html>
   <body bgcolor="<%= session.getAttribute("pickedBgCol") == null ? "white" : session.getAttribute("pickedBgCol") %>">
     This app has been running for: <%= elapsedTime() %>
   </body>
</html>