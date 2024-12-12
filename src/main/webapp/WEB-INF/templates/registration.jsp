<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/registration.css'/>">
    <link rel="icon" href="<c:url value='/img/favicon.png'/>" type="image/png">
</head>

<body>
<%@include file="/WEB-INF/templates/base/navbar.jsp" %>
<div class="form-container">
    <h2>Регистрация</h2>
    <div>
        <div class="form-group">
            <label for="login">Логин:</label>
            <input type="text" id="login" name="login" required>
        </div>

        <div class="form-group">
            <label for="pwd">Пароль:</label>
            <input type="password" id="pwd" name="pwd" required>
        </div>

        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
        </div>

        <div class="form-group">
            <label for="phone">Телефон:</label>
            <input type="text" id="phone" name="phone" required>
        </div>

        <div class="error-container" style="display:none;">
            <ul class="error-list">
            </ul>
        </div>

        <div class="button-container">
            <button onclick="sendUserRegData()">Зарегистрироваться</button>
            <a href="${pageContext.request.contextPath}/login" class="register-button">Уже есть аккаунт</a>
        </div>
    </div>
</div>

<input id="contextId" value="${contextPath}" hidden="hidden">

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="<c:url value='/js/registration.js'/>"></script>

<%@include file="/WEB-INF/templates/base/footer.jsp" %>
</body>
</html>
