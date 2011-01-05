<%@ include file="include/include.jsp" %>

<!DOCTYPE HTML>
<html>
	<head>
		<title>TV Show Manager</title>
		<c:import url="include/meta.jsp" />
	</head>
	<body>
		<c:import url="include/header.jsp">
			<c:param value="shows" name="page"/>
			<c:param value="Shows" name="title" />
		</c:import>
		<div class="main">
			<section class="form">
				<header>New TV show</header>
				<article>
					<form:form method="post" action="shows/add" commandName="show">
						<form:input path="title" placeholder="Enter show" />
						<input type="submit" value="Add" />
					</form:form>
				</article>
			</section>
			<section class="list">
				<header>Shows</header>
				<c:choose>
					<c:when test="${!empty showList}">
						<c:forEach items="${showList}" var="show">
							<article>
								<summary><a href="<c:url value="/shows/${show.id}"/>">${show.title}</a></summary>
								<aside><a href="<c:url value="/shows/delete/${show.id}"/>">delete</a></aside>
								<div> </div>
							</article> 
						</c:forEach>
					</c:when>
					<c:otherwise><article>No TV shows added yet!</article></c:otherwise>
				</c:choose>
			</section>
		</div>
		<c:import url="include/footer.jsp" />
	</body>
</html>