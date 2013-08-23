<%@ include file="include/taglibs.jsp" %>

<!DOCTYPE HTML>
<html>
	<head>
		<title>${show.title}</title>
		<jsp:include page="include/meta.jsp" />
	</head>
	<body>
		<c:import url="include/header.jsp">
			<c:param name="page" value="shows" />
			<c:param name="title" value="Shows" />
		</c:import>
		<div class="main">
		<section>
			<header><a href="<c:url value="/shows/${show.id}" />">${show.title}</a></header>
			<mash:episodes show="${show.id}" list="${episodes}" />
		</section>
		</div>
		<c:import url="include/footer.jsp" />
	</body>
</html>