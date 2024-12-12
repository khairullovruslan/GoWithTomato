<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/login.css'/>">
    <link rel="icon" href="<c:url value='/img/favicon.png'/>" type="image/png">
</head>

<body>
<%@include file="/WEB-INF/templates/base/navbar.jsp" %>
<div class="form-container">
    <h2>Вход</h2>
    <div>
        <div class="form-group">
            <label for="login">Логин:</label>
            <input type="text" id="login" name="login" required>
        </div>

        <div class="form-group">
            <label for="pwd">Пароль:</label>
            <input type="password" id="pwd" name="pwd" required>
        </div>

        <div class="error-container" style="display:none;">
            <ul class="error-list">
            </ul>
        </div>

        <div class="button-container">
            <button onclick="sendUserLoginData()">Войти</button>
            <a href="${pageContext.request.contextPath}/sign-up" class="register-button">Зарегистрироваться</a>
        </div>
    </div>
</div>
<input id="contextId" value="${contextPath}" hidden="hidden">

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="<c:url value='/js/login.js'/>"></script>

<%@include file="/WEB-INF/templates/base/footer.jsp" %>
</body>
</html>
