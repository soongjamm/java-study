<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: soongjamm
  Date: 2021/01/28
  Time: 12:15 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><spring:message code="login.title"/></title>
</head>
<body>
<p>
    <spring:message code="login.done"/>
</p>
<p>
    <a href="<c:url value='/main' />">
        [<spring:message code="go.main" />]
    </a>
</p>
</body>
</html>
