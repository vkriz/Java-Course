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
			
			.blogForm {
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
		
		<div class="blogForm">
			<c:choose>
				<c:when test="${ editing }">
					<h1>Blog editing</h1>
				</c:when>
				<c:otherwise>
					<h1>New blog</h1>
				</c:otherwise>
			</c:choose>

			<form action="${ action }" method="post">
				<div>
					<div>
				  		<span class="formLabel">Title</span>
				  		<input type="text" name="title" value='<c:out value="${ blog.title }"/>' size="100">
				 	</div>
				 	<c:if test="${errors.containsKey('title')}">
				 		<div class="greska"><c:out value="${errors.get('title')}"/></div>
				 	</c:if>
				</div>
	
				<div>
				 	<div>
				  		<span class="formLabel">Text</span>
				  		<textarea name="text" rows="10" cols="100">${ blog.text }</textarea>
				 	</div>
				 	<c:if test="${errors.containsKey('text')}">
				 		<div class="greska"><c:out value="${errors.get('text')}"/></div>
				 	</c:if>
				</div>
	
				<div class="formControls">
				  	<span class="formLabel">&nbsp;</span>
				  	<input type="submit" name="metoda" value="Save">
				</div>
				
			</form>			
		</div>
	</body>
</html>