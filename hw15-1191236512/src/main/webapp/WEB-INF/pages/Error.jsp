<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Blog</title>
		<style>
			body {
				margin: 0;
			}
			
			header {
				height: 60px;
				padding: 10px 15px;
				background-color: #ededed;
				box-shadow: 0 .2rem .4rem rgba(0,0,0,.4);
			}
			
			.homeLink {
				float: left;
				font-size: 25px;
				padding-top: 15px;
			}
			
			.userInfo {
				float: right;
				padding-top: 10px;
				text-align: right;
			}
			.error {
				padding: 15px;
				margin: 15px;
				box-shadow: 0 .2rem .4rem rgba(0,0,0,.4);
			}
		</style>
	</head>
  	<body>
  
  		<header>
			<div class="homeLink">
				<a href="<%=request.getContextPath() + "/servleti/main"%>">
					Home
				</a>
			</div>
			<c:choose>
				<c:when test='<%= session.getAttribute("current.user.id") != null %>'>
	    			<div class="userInfo">
	    				<a href="<%=request.getContextPath() + "/servleti/author/" + session.getAttribute("current.user.nick")%>">
	    					<c:out value='<%= session.getAttribute("current.user.fn") + " " + session.getAttribute("current.user.ln") %>'></c:out>
						</a>
						<div class="logoutLink">
		    				<a href="<%=request.getContextPath() + "/servleti/logout"%>">Logout</a>
		    			</div>
	    			</div>
	    		</c:when>
	    		<c:otherwise>
	    			<div class="userInfo">
	    				Not logged in <br>
		    			<a href="<%=request.getContextPath() + "/servleti/main"%>">Login</a>
	    			</div>
	    		</c:otherwise>
	    	</c:choose>
		</header>

		<div class="error">
			${error} <br>
			<a href="<%=request.getContextPath() + "/servleti/main"%>">Go to main page</a>
		</div>
  	</body>
</html>