<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Blog</title>
		
		<style type="text/css">
			.greska {
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
			
			.registerForm {
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
	
		<div class="registerForm">
			<h1>Registration</h1>
			<form action="register" method="post">
				<div>
			 		<div>
			  			<span class="formLabel">First Name</span>
				  		<input type="text" name="firstName" value='<c:out value="${firstName}"/>' size="20">
				 	</div>
				 	<c:if test="${errors.containsKey('firstName')}">
				 		<div class="greska"><c:out value="${errors.get('firstName')}"/></div>
				 	</c:if>
				</div>
		
				<div>
				 	<div>
				  		<span class="formLabel">Last Name</span>
				  		<input type="text" name="lastName" value='<c:out value="${lastName}"/>' size="20">
				 	</div>
				 	<c:if test="${errors.containsKey('lastName')}">
				 		<div class="greska"><c:out value="${errors.get('lastName')}"/></div>
					</c:if>
				</div>
		
				<div>
				 	<div>
				  		<span class="formLabel">Nick</span>
				  		<input type="text" name="nick" value='<c:out value="${nick}"/>' size="20">
				 	</div>
				 	<c:if test="${errors.containsKey('nick')}">
				 		<div class="greska"><c:out value="${errors.get('nick')}"/></div>
					</c:if>
				</div>
				
				<div>
				 	<div>
				  		<span class="formLabel">E-mail</span>
				  		<input type="text" name="email" value='<c:out value="${email}"/>' size="30">
				 	</div>
				 	<c:if test="${errors.containsKey('email')}">
				 		<div class="greska"><c:out value="${errors.get('email')}"/></div>
				 	</c:if>
				</div>
				
				<div>
				 	<div>
				  		<span class="formLabel">Password</span>
				  		<input type="password" name="password" value='<c:out value="${password}"/>' size="20">
				 	</div>
				 	<c:if test="${errors.containsKey('password')}">
				 		<div class="greska"><c:out value="${errors.get('password')}"/></div>
				 	</c:if>
				</div>
		
				<div class="formControls">
				  	<span class="formLabel">&nbsp;</span>
					<input type="submit" name="metoda" value="Register">
				</div>
			</form>
		</div>
	</body>
</html>