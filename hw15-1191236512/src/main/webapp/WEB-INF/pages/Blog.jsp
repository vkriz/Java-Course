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
			
			.blog, .comments {
				padding: 15px;
				margin: 15px;
				box-shadow: 0 .2rem .4rem rgba(0,0,0,.4);
			}
			
			.comments form {
				padding-top: 15px;
			}
			
			.comments ul {
				list-style: none;
			}
			
			.comments li {
				padding: 20px;
				margin-bottom: 10px;
				border: 1px solid #cdcdcd;
			}
			
			.comment-text {
				padding-top: 10px;
				padding-left: 10px;
			}
			
			.author {
				font-size: 16px;
				color: #585858;
			}
			
			.date, .lastModified {
				font-size: 14px;
				color: #585858;
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
  
  		<c:choose>
		    <c:when test="${blogEntry==null}">
		      No entry!
		    </c:when>
		    <c:otherwise>
		    	<div class="blog">
		    		<h1><c:out value="${blogEntry.title}"/></h1>
		    		<span class="author">
				      	<c:out value="${blogEntry.getCreator().getFirstName()}"/>
				      	<c:out value="${blogEntry.getCreator().getLastName()}"/>,
			      	</span>
				    <span class="date">
				    	<c:out value="${blogEntry.getCreatedAt()}"/>
				    </span>
				    <p><c:out value="${blogEntry.text}"/></p>
				    <span class="lastModified">
				    	Last modified: 
				    	<c:out value="${blogEntry.getLastModifiedAt()}"/>
				    </span>
				    <br>
				     <c:if test="${ allowEditPost == true }">
					 	<a href="${blogEntry.id}/edit">Edit post</a>
					 </c:if>
		    	</div>
		    </c:otherwise>
	    </c:choose>
  
  		<div class="comments">
  			<h2>Comments</h2>
			<c:if test="${!blogEntry.comments.isEmpty()}">
			 	<ul>
			    	<c:forEach var="e" items="${blogEntry.comments}">
			       		<li>
			       			<div style="font-weight: bold">
				       			<span class="author">
							    	<c:out value="${e.usersEMail}"/>,
						      	</span>
							    <span class="date">
							    	<c:out value="${e.postedOn}"/>
							    </span>
			       			</div>
			       			<div class="comment-text">
			       				<c:out value="${e.message}"/>
			       			</div>
			       		</li>
			     	</c:forEach>
		     	</ul>
		  	</c:if>
  
			<form action="${ blogEntry.id }" method="post">
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
				  		<span class="formLabel">Text</span>
				  		<textarea name="text" rows="4" cols="100">${ text }</textarea>
				 	</div>
				 	<c:if test="${errors.containsKey('text')}">
				 		<div class="greska"><c:out value="${errors.get('text')}"/></div>
					</c:if>
				</div>
		
				<div class="formControls">
					<span class="formLabel">&nbsp;</span>
				  	<input type="submit" name="metoda" value="Comment">
				</div>
			</form>
		</div>
	</body>
</html>