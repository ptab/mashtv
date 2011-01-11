<%@ include file="include/taglibs.jsp" %>

<!DOCTYPE HTML>
<html>
	<head>
		<title>Latest episodes</title>
		<c:import url="include/meta.jsp" />
	</head>
	<body>
		<c:import url="include/header.jsp">
			<c:param name="page" value="latest" />
			<c:param name="title" value="Latest episodes" />
		</c:import>
		<div class="main">
		<c:choose>
			<c:when test="${!empty shows}">
				<c:forEach items="${shows}" var="show">
					<section>
						<header>
							<a href="<c:url value="/shows/${show.id}" />">${show.title}</a>
						</header>
						<mash:episodes show="${show.id}" list="${latest[show.id]}" />
					</section>
				</c:forEach>
			</c:when>
			<c:otherwise>No tv shows configured!</c:otherwise>
		</c:choose>
		</div>
		<c:import url="include/footer.jsp" />
	</body>
</html>