<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<style type="text/css">
			table.rez td {text-align: center;}
		</style>
	</head>
	
	<body>
		<h1>Rezultati glasanja</h1>
		<p>Ovo su rezultati glasanja.</p>
		<table border="1" class="rez">
			<thead>
				<tr>
					<th>Opcija</th>
					<th>Broj glasova</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="option" items="${ results }">
					<tr>
						<td>${ option.getOptionTitle() }</td>
						<td>${ option.getVotesCount() }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<h2>Grafički prikaz rezultata</h2>
		<img alt="Pie-chart" src="glasanje-grafika?pollID=${ poll.getId() }" width="400" height="400"/>
		
		<h2>Rezultati u XLS formatu</h2> 
		<p>
			Rezultati u XLS formatu dostupni su <a href="glasanje-xls?pollID=${ poll.getId() }">ovdje</a>
		</p>
		
		<h2>Razno</h2>
		<p>
			Linkovi na pobjednike:
		</p>
		<ul>
			<c:forEach var="winner" items="${ winners }">
				<li>
					<a href="${ winner.getOptionLink() }" target="_blank">${ winner.getOptionTitle() }</a>
				</li>
			</c:forEach>
		</ul>
	</body>
</html>