<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/registration.css'/>">
</head>

<body>
<%@include file="/templates/base/navbar.jsp" %>
<div class="form-container">
    <h2>Регистрация</h2>
    <form action="${pageContext.request.contextPath}/sign-up" method="post">
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
            <button type="submit">Зарегистрироваться</button>
            <a href="${pageContext.request.contextPath}/login" class="register-button">Уже есть аккаунт</a>
        </div>
    </form>
</div>

<%@include file="/templates/base/footer.jsp" %>
</body>
</html>
