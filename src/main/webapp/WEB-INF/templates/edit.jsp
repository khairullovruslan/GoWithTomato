<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование профиля</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/edit.css'/>">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="icon" href="<c:url value='/img/favicon.png'/>" type="image/png">
</head>
<body>
<%@include file="/WEB-INF/templates/base/navbar.jsp" %>
<div class="form-container">
    <div>
        <h4>Загрузить новую фотографию профиля</h4>
        <form id="uploadForm" enctype="multipart/form-data">
            <input type="file" name="image" accept="image/*" required>
            <button type="button" id="uploadButton" onclick="uploadNewPhoto()">Загрузить</button>
        </form>
        <div id="successMessageUserPhoto" class="success-message" style="display:none; color: green; margin-top: 10px;">
        </div>
        <div id="image-error-container" class="error-container" style="display:none;">
            <ul class="error-list">
            </ul>
        </div>
        <br>

        <div class="form-group">
            <label for="login">Логин:</label>
            <input value="<c:if test="${user.login != null}">${user.login}</c:if>" type="text" id="login" name="login"
                   required>
        </div>

        <div class="form-group">
            <label for="email">Email:</label>
            <input value="<c:if test="${user.email != null}">${user.email}</c:if>" type="email" id="email" name="email"
                   required>
        </div>

        <div class="form-group">
            <label for="phone_number">Телефон: </label>
            <input value="<c:if test="${user.phoneNumber != null}">${user.phoneNumber}</c:if>" type="text"
                   id="phone_number" name="phone_number" required>
        </div>

        <div class="button-container">
            <button onclick="sendNewUserData()">Сменить данные</button>
        </div>

        <div id="successMessageUserData" class="success-message" style="display:none; color: green; margin-top: 10px;">
        </div>
        <div id="edit-error-container" class="error-container" style="display:none;">
            <ul class="error-list">
            </ul>
        </div>
        <br>


        <div class="form-group">
            <label for="old_pwd">Старый пароль: </label>
            <input type="password" id="old_pwd" name="old_pwd" required>
        </div>
        <div class="form-group">

            <label for="new_pwd">Новый пароль: </label>
            <input type="password" id="new_pwd" name="new_pwd" required>
        </div>
        <div class="form-group">

            <label for="new_pwd_rep">Повторите новый пароль: </label>
            <input type="password" id="new_pwd_rep" name="new_pwd_rep" required>
        </div>
        <div class="button-container">
            <button onclick="sendNewPasswordData()" style="background-color: #6c757d">Сменить пароль</button>
        </div>
        <div id="password-error-container" class="error-container" style="display:none;">
            <ul class="error-list">
            </ul>
        </div>
        <div id="successMessageUserPasswordData" class="success-message"
             style="display:none; color: green; margin-top: 10px;">
        </div>

        <br>
        <br>
        <form action="${pageContext.request.contextPath}/profile/edit/delete" method="post">
            <div class="button-container">
                <button type="submit" style="background-color: #e70e0e">Удалить аккаунт</button>
            </div>
        </form>


    </div>
</div>
<input id="contextId" value="${contextPath}" hidden="hidden">

<script src="<c:url value='/js/edit.js'/>"></script>
<%@include file="/WEB-INF/templates/base/footer.jsp" %>
</body>
</html>