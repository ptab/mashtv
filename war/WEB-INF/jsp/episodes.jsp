<%@ include file="include/include.jsp" %>

<!DOCTYPE HTML>
<html>
	<head>
		<title>${show.title}</title>
		<jsp:include page="include/meta.jsp" />
	</head>
	<body>
		<c:import url="include/header.jsp">
			<c:param value="latest" name="page"/>
			<c:param value="Shows" name="title" />
		</c:import>
		<div class="main">
		<section>
			<header><a href="<c:url value="/shows/${show.id}" />">${show.title}</a></header>
			<c:choose>
				<c:when test="${!empty episodes}">
					<c:forEach items="${episodes}" var="episode">
						<article>
							<details>Season <span>${episode.season}</span> Episode <span>${episode.episode}</span></details>
							<aside>
								<div>
									<a href="<c:url value="/shows/${show.id}/toggle/${episode.season}/${episode.episode}"/>">
										<c:choose>
											<c:when test="${episode.downloaded}"></c:when>
											<c:otherwise><img src="<c:url value="/resources/alert.png"/>" alt="New episode!" /></c:otherwise>
										</c:choose>
									</a>
								</div>
								<div><a href="<c:url value="/shows/${show.id}/torrents/${episode.season}/${episode.episode}"/>"><img src="<c:url value="/resources/bittorrent.gif"/>" alt="List torrents" /></a></div>
								<div><a href="<c:url value=""/>"></a><img src="<c:url value="/resources/txt.png"/>" alt="Subtitles" /></a></div>
							</aside>
							<div>${episode.title}</div>
						</article>
					</c:forEach>
				</c:when>
				<c:otherwise><article>No recent episodes.</article></c:otherwise>
			</c:choose>
		</section>
		</div>
		<c:import url="include/footer.jsp" />
	</body>
</html>