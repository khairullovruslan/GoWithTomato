<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создание поездки</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/create-trip.css'/>">
</head>
<body>
<%@include file="/templates/base/navbar.jsp" %>
<div class="container">
    <h1 class="text-center">Создание поездки</h1>

    <form id="route-form" method="POST" action="${pageContext.request.contextPath}/create-trip">
        <div class="form-group">
            <label for="tripDateTime">Дата и время поездки:</label>
            <input type="datetime-local" class="form-control" id="tripDateTime" name="tripDateTime" required>
        </div>

        <div class="form-group">
            <label for="availableSeats">Доступные места:</label>
            <input type="number" class="form-control" id="availableSeats" name="availableSeats" min="1" required>
        </div>

        <div class="form-group">
            <label for="price">Цена:</label>
            <input type="number" class="form-control" id="price" name="price" step="0.01" required>
        </div>

        <button type="submit" class="btn btn-success">Создать маршрут</button>
        <input id="routeId" th:value="${routeId}" hidden="hidden">

    </form>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="<c:url value='/js/create-trip.js'/>"></script>

<%@include file="/templates/base/footer.jsp" %>
</body>
</html>
