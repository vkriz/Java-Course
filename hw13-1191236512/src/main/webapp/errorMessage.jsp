<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<body bgcolor="<%= session.getAttribute("pickedBgCol") == null ? "white" : session.getAttribute("pickedBgCol") %>">
Invalid parameter value!<br>
a can be integer from [-100, 100] <br>
b can be integer from [-100, 100]<br>
and n can be integer from [1, 5]
</body>