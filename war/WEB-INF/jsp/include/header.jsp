<%@ include file="include.jsp" %>

<header>
	<!-- <img class="logo" src="<c:url value="/resources/rss-50.png"/>" alt="logo"/>-->
	<!-- <div class="logo">séries para download</div>-->
	<details>${param.title}</details>
	<nav>
		<a class="<c:if test="${param.page == 'latest'}">selected</c:if>" href="<c:url value="/latest"/>">latest</a>
		<a class="<c:if test="${param.page == 'shows'}">selected</c:if>" href="<c:url value="/shows"/>">shows</a>
		<a class="<c:if test="${param.page == 'feeds'}">selected</c:if>" href="<c:url value="/feeds"/>">feeds</a>
	</nav>
</header>
