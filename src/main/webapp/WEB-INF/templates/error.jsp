<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Ошибочка</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/error.css'/>">
    м
</head>

<body>
<%@include file="/WEB-INF/templates/base/navbar.jsp" %>
<div class="error-container">
    <h1 class="error-title">Упс! Что-то пошло не так...</h1>
    <p class="error-message">
        <c:choose>
            <c:when test="${not empty errorMessage}">
                ${errorMessage}
            </c:when>
            <c:otherwise>
                Ошибка не указана
            </c:otherwise>
        </c:choose>
    </p>
    <a class="btn-home" href="${pageContext.request.contextPath}/">Вернуться на главную</a>
</div>
<%@include file="/WEB-INF/templates/base/footer.jsp" %>
</body>
</html>
