<%@ include file="include/taglibs.jsp" %>

<!DOCTYPE HTML>
<html>
	<head>
		<title>${episode.show.title} - S${episode.season}E${episode.episode} - ${episode.title}</title>
		<c:import url="include/meta.jsp" />
	</head>
	<body>
		<c:import url="include/header.jsp">
			<c:param value="shows" name="page"/>
			<c:param value="Torrents" name="title" />
		</c:import>
		<div class="main">
		<section class="full">
			<header><a href="<c:url value="/shows/${episode.show.id}"/>">${episode.show.title} - Season ${episode.season}, Episode ${episode.episode}: ${episode.title}</a></header>
			<c:choose>
				<c:when test="${!empty episode.torrents}">
					<c:forEach items="${episode.torrents}" var="torrent">
						<article>
							<summary>${torrent.filename}</summary>
							<aside>
								<c:if test="${torrent.hd}"><img src="<c:url value="/resources/img/television.png"/>" alt="hd" title="Content in HD"/></c:if>
								<a href="<c:url value="/shows/${episode.show.id}/download/${episode.season}/${episode.episode}/${torrent.id}"/>"><img src="<c:url value="/resources/img/drive--plus.png"/>" alt="download" title="Download to torrents folder"/></a>
								<a href="<c:url value="${torrent.url}"/>"><img src="<c:url value="/resources/img/globe--arrow.png"/>" alt="follow" title="Follow the link on this feed item"/></a>
							</aside>
							<!-- <div></div>-->
						</article>
					</c:forEach>
				</c:when>
				<c:otherwise><article>No torrents!</article></c:otherwise>
			</c:choose>
		</section>
		</div>
		<c:import url="include/footer.jsp" />
	</body>
</html>