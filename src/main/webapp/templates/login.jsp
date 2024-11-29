<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/login.css'/>">
</head>

<body>
<%@include file="/templates/base/navbar.jsp" %>
<div class="form-container">
    <h2>Вход</h2>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label for="login">Логин:</label>
            <input type="text" id="login" name="login" required>
        </div>

        <div class="form-group">
            <label for="pwd">Пароль:</label>
            <input type="password" id="pwd" name="pwd" required>
        </div>

        <c:if test="${not empty errorList}">
            <div class="error-container">
                <ul class="error-list">
                    <c:forEach var="error" items="${errorList}">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <div class="button-container">
            <button type="submit">Войти</button>
            <a href="sign-up" class="register-button">Зарегистрироваться</a>
        </div>
    </form>
</div>
<%@include file="/templates/base/footer.jsp" %>
</body>
</html>
