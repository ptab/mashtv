<%@ include file="taglibs.jsp" %>

<header>
	<details>${param.title}</details>
	<nav>
		<a class="<c:if test="${param.page == 'latest'}">selected</c:if>" href="<c:url value="/latest"/>">latest</a>
		<a class="<c:if test="${param.page == 'shows'}">selected</c:if>" href="<c:url value="/shows"/>">shows</a>
		<a class="<c:if test="${param.page == 'feeds'}">selected</c:if>" href="<c:url value="/feeds"/>">feeds</a>
	</nav>
</header>
