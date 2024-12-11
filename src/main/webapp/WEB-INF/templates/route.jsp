<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создание маршрута</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/route.css'/>">
</head>

<body>
<%@include file="/WEB-INF/templates/base/navbar.jsp" %>
<div class="container">
    <h1 class="text-center">Создание маршрута</h1>

    <div id="route-form">
        <div class="form-group">
            <label for="start">Начальная точка:</label>
            <div class="input-group">
                <input type="text" class="form-control" id="start" name="start" readonly>
                <button class="btn btn-secondary" type="button" onclick="openModal('start')">Выбрать</button>
            </div>
        </div>

        <div class="form-group">
            <label for="end">Конечная точка:</label>
            <div class="input-group">
                <input type="text" class="form-control" id="end" name="end" readonly>
                <button class="btn btn-secondary" type="button" onclick="openModal('end')">Выбрать</button>
            </div>
        </div>

        <div class="form-group">
            <label>Промежуточные точки:</label>
            <div id="intermediate-points"></div>
            <button class="btn btn-secondary" type="button" onclick="openModal('intermediate')">Добавить промежуточную
                точку
            </button>
        </div>

        <input type="hidden" id="routeData" name="routeData">
        <button onclick="sendData()" class="btn btn-success">Создать маршрут</button>
        <button onclick="getInfo()" class="btn btn-success">Получить актуальную дистанцию и среднее время поездки
        </button>
    </div>
    <div class="mt-4" id="routeDisplay">
        <h5 class="mb-2">Маршрут:</h5>
        <p style="color: black" id="routeOutput">Не указан</p>
        <h5 class="mb-2">Время:</h5>
        <p style="color: black" id="routeTime">Не указан</p>
        <h5 class="mb-2">Дистанция (км):</h5>
        <p style="color: black" id="routeDistance">Не указан</p>
    </div>
    <div id="error-container" class="error-container" style="display:none;">
        <ul class="error-list">
        </ul>
    </div>

</div>

<div id="myModal" class="modal">
    <div class="modal-content">
        <h2 class="modal-title">Выбор точки</h2>
        <input type="text" class="form-control" id="search-input" placeholder="Введите название точки">
        <button class="btn btn-primary mt-2" onclick="searchPoints()">Поиск</button>
        <div id="results" class="mt-2"></div>
        <button class="btn btn-secondary mt-2" onclick="closeModal()">Закрыть</button>
    </div>
</div>
<input id="contextId" value="${contextPath}" hidden="hidden">

<script src="<c:url value='/js/route.js'/>"></script>
<%--<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>--%>
<%@include file="/WEB-INF/templates/base/footer.jsp" %>


</body>

</html>