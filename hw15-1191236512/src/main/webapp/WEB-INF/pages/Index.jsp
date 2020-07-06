<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Blog</title>
		
		<style type="text/css">
			.error {
			   	font-family: fantasy;
			   	font-weight: bold;
			   	font-size: 0.9em;
			   	color: #FF0000;
			   	padding-left: 110px;
			}
			
			.formLabel {
			   	display: inline-block;
			   	width: 100px;
               	font-weight: bold;
			   	text-align: right;
	            padding-right: 10px;
			}
			
			.formControls {
			  	margin-top: 10px;
			}
			
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
			
			.loginForm, .registeredAuthors, .registerLink {
				padding: 15px;
				margin: 15px;
				box-shadow: 0 .2rem .4rem rgba(0,0,0,.4);
			}
			
			.registerLink {
				font-size: 21px;
			}
			
			ul.unstyled {
				list-style: none;
			}
			
			li {
				padding-bottom: 10px;
				font-size: 20px;
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
		
		<c:if test='<%= session.getAttribute("current.user.id") == null %>'>
			<div class="loginForm">
				<form action="main" method="post">
					<h2>Login</h2>
					<div>
					 	<div>
					  		<span class="formLabel">Nick</span>
					  		<input type="text" name="nick" value='<c:out value="${nick}"/>' size="20">
					 	</div>
					 	<c:if test="${errors.containsKey('nick')}">
					 		<div class="error"><c:out value="${errors.get('nick')}"/></div>
					 	</c:if>
					</div>
					<div>
			 			<div>
			  				<span class="formLabel">Password</span>
			  				<input type="password" name="password" value='<c:out value="${password}"/>' size="20">
			 			</div>
			 			<c:if test="${errors.containsKey('password')}">
			 				<div class="error"><c:out value="${errors.get('password')}"/></div>
			 			</c:if>
					</div>
			
					<div>
						<c:if test="${errors.containsKey('invalidLogin')}">
					 		<div class="error"><c:out value="${errors.get('invalidLogin')}"/></div>
					 	</c:if>
					</div>
	
					<div class="formControls">
						<span class="formLabel">&nbsp;</span>
						<input type="submit" name="method" value="Login">
					</div>
				</form>
			</div>
		</c:if>
		
		<div class="registerLink">
			New users are always welcome!
			<a href="<%=request.getContextPath() + "/servleti/register"%>">Register</a> for free.
		</div> 
			
		<div class="registeredAuthors">
			<h2>Registered authors</h2>
			<div class="userList">
				<ul class="unstyled">
					<c:forEach var="user" items="${ blogUsers }">
						<li>
							<a href="author/${ user.getNick() }">${ user.getFirstName() } ${ user.getLastName() } (${ user.getNick() })</a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</body>
</html>