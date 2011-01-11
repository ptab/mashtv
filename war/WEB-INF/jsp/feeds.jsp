<%@ include file="include/taglibs.jsp" %>

<!DOCTYPE HTML>
<html>
	<head>
		<title>Feed Manager</title>
		<c:import url="include/meta.jsp" />
	</head>
	<body>
		<c:import url="include/header.jsp">
			<c:param value="feeds" name="page"/>
			<c:param value="Feeds" name="title" />
		</c:import>
		<div class="main">
		<section class="form">
			<header>New feed</header>
			<article>
				<form:form method="post" action="feeds/add" commandName="feed">
					<form:input path="url" placeholder="Enter feed URL" />
					<input type="submit" value="Add" />
				</form:form>
			</article>
		</section>
		<section class="list">
			<header>Feeds</header>
			<c:choose>
				<c:when test="${!empty feedList}">
					<c:forEach items="${feedList}" var="feed">
						<article>
							<summary><a href="<c:url value="${feed.url}"/>">${feed.url}</a></summary>
							<aside>
								<a href="<c:url value="/feeds/load/${feed.id}"/>">load</a>
								<a href="<c:url value="/feeds/delete/${feed.id}"/>"><img src="<c:url value="/resources/img/minus.png" />" alt="delete" /></a>
							</aside>
						</article> 
					</c:forEach>
				</c:when>
				<c:otherwise><article>No feeds added yet!</article></c:otherwise>
			</c:choose>
		</section>
		</div>
		<c:import url="include/footer.jsp" />
	</body>
</html>