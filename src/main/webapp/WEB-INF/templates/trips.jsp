<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/trips.css'/>">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap">
    <title>Найдите свою поездку</title>
    <link rel="icon" href="<c:url value='/img/favicon.png'/>" type="image/png">
</head>
<body>
<%@include file="/WEB-INF/templates/base/navbar.jsp" %>

<header>
    <div class="container">
        <div id="filter" class="filter-container">
            <input value="<%= request.getParameter("from") != null ? request.getParameter("from") : "" %>"
                   id="from" type="text" placeholder="Откуда"/>
            <input value="<%= request.getParameter("to") != null ? request.getParameter("to") : "" %>"
                   id="to" type="text" placeholder="Куда"/>
            <input value="<%= request.getParameter("date") != null ? request.getParameter("date") : "" %>"
                   id="date" type="date" placeholder="Дата поездки"/>
            <input value="<%= request.getParameter("count") != null ? request.getParameter("count") : "" %>"
                   id="count" type="number" placeholder="Количество пассажиров" min="1" max="30"/>
            <input hidden="hidden"
                   value="<%= request.getParameter("organizer") != null ? request.getParameter("organizer") : "" %>"
                   id="organizer" type="text"/>
            <input hidden="hidden"
                   value="<%= request.getParameter("status") != null ? request.getParameter("status") : "" %>"
                   id="status" type="text"/>
            <input hidden="hidden"
                   value="<%= request.getParameter("owner_tickets") != null ? request.getParameter("owner_tickets") : "" %>"
                   id="owner_tickets" type="text"/>
            <button id="search-btn" onclick="filterSend()">Поиск</button>
        </div>
        <div id="filter-status" class="filter-container">
            <button class="status-btn" onclick="filterStatusSend('available')">Активные</button>
            <button class="status-btn" onclick="filterStatusSend('cancelled')">Отмененные</button>
            <button class="status-btn" onclick="filterStatusSend('completed')">Завершенные</button>
            <button class="status-btn" onclick="clearFilter()">Очистить фильтр</button>
        </div>

    </div>

</header>

<div class="container">
    <div class="trip-list">
        <c:if test="${not empty tripList}">
            <c:forEach var="trip" items="${tripList}">
                <div class="trip-card" style="${trip.status == 'cancelled' ? ' background-color: red;' : ''}" onclick="openTrip(${trip.id})">
                    <div class="details">
                        <h2>From: ${trip.route.start.name} To: ${trip.route.finish.name}</h2>
                        <div class="description">
                            <p>${trip.tripDateTimeFormatted}</p>
                            <p>Свободных мест: ${trip.availableSeats}</p>
                        </div>
                        <div class="route">
                            <p>Маршрут: Старт: ${trip.route.start.name}, Финиш: ${trip.route.finish.name}</p>
                        </div>
                    </div>
                    <div class="price">${trip.price} ₽</div>
                    <input id="id" value="${trip.id}" hidden="hidden">
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${empty tripList}">
            <p class="no-trips">No trips found.</p>
        </c:if>
    </div>

</div>
<div class="pagination">
    <c:if test="${page > 1}">
        <a href="?page=${page - 1}&from=${from}&to=${to}&date=${date}&organize=${organizer}"
           class="pagination-link">« Предыдущая</a>
    </c:if>

    <c:forEach begin="1" end="${totalPages}" var="i">
        <a href="?page=${i}&from=${from}&to=${to}&date=${date}&organize=${organizer}"
           class="pagination-link <c:if test='${i == page}'>active</c:if>">${i}</a>
    </c:forEach>

    <c:if test="${page < totalPages}">
        <a href="?page=${page + 1}&from=${from}&to=${to}&date=${date}&count=${count}&organize=${organizer}"
           class="pagination-link">Следующая »</a>
    </c:if>
</div>

<input id="contextId" value="${contextPath}" hidden="hidden">

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="<c:url value='/js/trips.js'/>"></script>
<%@include file="/WEB-INF/templates/base/footer.jsp" %>
</body>
</html>
