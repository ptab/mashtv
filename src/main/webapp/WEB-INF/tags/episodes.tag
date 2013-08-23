<%@ tag body-content="empty" %>
<%@ attribute name="show" required="true" %>
<%@ attribute name="list" required="true" type="java.lang.Object" %>
<%@ attribute name="delete" type="java.lang.Boolean" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
	<c:when test="${!empty list}">
	<c:forEach items="${list}" var="episode">
		<article>
			<details>Season <span>${episode.season}</span> Episode <span>${episode.episode}</span></details>
			<aside>
				<a href="<c:url value="/shows/${show}/toggle/${episode.season}/${episode.episode}"/>">
				<c:choose>
					<c:when test="${episode.downloaded}"><img src="<c:url value="/img/light-bulb-off.png"/>" alt="old" title="Already downloaded (click to toggle)"/></c:when>
					<c:otherwise><img src="<c:url value="/img/light-bulb.png"/>" alt="new" title="New episode (click to toggle)"/></c:otherwise>
				</c:choose>
				</a>
				<a href="<c:url value="/shows/${show}/torrents/${episode.season}/${episode.episode}"/>"><img src="<c:url value="/img/drive-download.png"/>" alt="torrents" title="List torrents"/></a>
				<a href="<c:url value="/shows/${show}/delete/${episode.season}/${episode.episode}"/>"><img src="<c:url value="/img/minus.png"/>" alt="delete" title="Delete this episode"/></a>
			</aside>
			<div>${episode.title}</div>
		</article>
	</c:forEach>
	</c:when>
	<c:otherwise><article>No recent episodes.</article></c:otherwise>
</c:choose>