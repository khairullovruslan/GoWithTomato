<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Поездка</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/trip.css'/>">
</head>
<body>
<%@include file="/WEB-INF/templates/base/navbar.jsp" %>
<div class="container">
    <div class="header">
        <h1>${trip.tripDateTimeFormatted}</h1>
        <span class="date">${tripInfo}</span>
        <br>
        <span class="date">Свободных мест - ${trip.availableSeats}</span>
    </div>
    <c:if test="${trip.status == 'cancelled'}">
        <h1 style="color: red">Поездка отменена</h1>
    </c:if>
    <div class="trip-info">
        <h2>Поездка из ${trip.route.start.name} в ${trip.route.finish.name}</h2>
        <div class="timeline">
            <ul>
                <li>
                    <span>${trip.route.start.name}</span>
                </li>
                <c:forEach var="waypoint" items="${trip.route.others}">
                    <li class="event">
                        <span>${waypoint.name}</span>
                    </li>
                </c:forEach>
                <li>
                    <span>${trip.route.finish.name}</span>
                </li>
            </ul>
        </div>
    </div>

    <div class="creator-info">
        <h2>Организатор:</h2>
        <div class="creator-details">
            <c:choose>
                <c:when test="${not empty ownerTrip}">
                    <span>Никнейм: ${ownerTrip.login}</span><br>
                    <span>Почта: ${ownerTrip.email}</span><br>
                    <span>Телефон: ${ownerTrip.phoneNumber}</span>
                </c:when>
                <c:otherwise>
                    <span>Видимо, организатор решил удалить свой аккаунт. Пу-пу-пу</span>
                </c:otherwise>
            </c:choose>

        </div>
    </div>

    <div class="passenger-info">
        <h2>Пассажиры</h2>
        <div class="trip-details">
            <c:choose>
                <c:when test="${not empty members}">
                    <c:forEach var="member" items="${members}">
                        <span>${member.login} (${member.email})</span><br>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <span>Видимо, еще никого нет. Будьте первыми!</span>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <c:choose>

        <c:when test="${trip.status == 'cancelled'}">
        </c:when>
        <c:when test="${owner}">
            <input id="tripId" value="${trip.id}" hidden="hidden">
            <button style="color: white" class="button cancellation" onclick="sendPutMethod()">Отменить поездку</button>
        </c:when>
        <c:when test="${chosen}">
            <c:if test="${trip.status == 'completed'}">
                <button style="color: white" class="button" type="submit"
                        onclick="window.location.href='<%=request.getContextPath()%>/review?trip=${trip.id}'">
                    Братанчик, оставь отзыв!
                </button>
            </c:if>
            <c:if test="${trip.status == 'available' && leaveFeedback}">
                <button style="color: white" class="button" type="submit" disabled>Вы уже забронировали</button>
            </c:if>
        </c:when>
        <c:when test="${trip.availableSeats > 0 && !chosen && trip.status == 'available'}">
            <form action="${pageContext.request.contextPath}/trip/${trip.id}" method="post" class="price-section">
                <span class="price">${trip.price} ₽</span>
                <button class="button" type="submit">Забронировать</button>
            </form>
        </c:when>
        <c:otherwise>
            <span>Мест не осталось. Попробуйте позже</span>
        </c:otherwise>

    </c:choose>


</div>
<input id="contextId" value="${contextPath}" hidden="hidden">

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="<c:url value='/js/trip.js'/>"></script>
<%@include file="/WEB-INF/templates/base/footer.jsp" %>
</body>
</html>
