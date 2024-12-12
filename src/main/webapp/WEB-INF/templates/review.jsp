<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Отзыв</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/review.css'/>">
    <link rel="icon" href="<c:url value='/img/favicon.png'/>" type="image/png">
</head>

<body>
<%@include file="/WEB-INF/templates/base/navbar.jsp" %>
<div class="container">
    <c:if test="${not empty trip}">
        <div class="trip-card" style="${trip.status == 'cancelled' ? ' background-color: red;' : ''}">
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
            <input id="tripId" value="${trip.id}" hidden="hidden">
            <div class="price">${trip.price} ₽</div>
        </div>

        <div class="review-form">
            <h3>Оставьте отзыв</h3>
            <div>

                <div class="rating">
                    <div class="rating-area">
                        <input type="radio" id="star-5" name="rating" value="1"
                            <c:if test="${review != null}">
                                   disabled
                                <c:if test="${review.rating == 5}">
                                       checked
                                </c:if>
                            </c:if>
                        >
                        <label for="star-5"></label>

                        <input type="radio" id="star-4" name="rating" value="2"
                            <c:if test="${review != null}">
                                   disabled
                                <c:if test="${review.rating == 4}">
                                       checked
                                </c:if>
                            </c:if>
                        >
                        <label for="star-4"></label>

                        <input type="radio" id="star-3" name="rating" value="3"
                            <c:if test="${review != null}">
                                   disabled
                                <c:if test="${review.rating == 3}">
                                       checked
                                </c:if>
                            </c:if>
                        >
                        <label for="star-3"></label>

                        <input type="radio" id="star-2" name="rating" value="4"
                            <c:if test="${review != null}">
                                   disabled
                                <c:if test="${review.rating == 2}">
                                       checked
                                </c:if>
                            </c:if>
                        >
                        <label for="star-2"></label>

                        <input type="radio" id="star-1" name="rating" value="5"
                            <c:if test="${review != null}">
                                   disabled
                                <c:if test="${review.rating == 1}">
                                       checked
                                </c:if>
                            </c:if>
                        >
                        <label for="star-1"></label>
                    </div>
                </div>

                <textarea class="review-text" name="review" placeholder="Ваш отзыв..." required
                          <c:if test="${review != null}">disabled</c:if>>${review != null ? review.description : ""}</textarea>
                <button class="submit-button" onclick="sendReview()"
                        <c:if test="${review != null}">style="display:none;"
                </c:if>
                > Отправить отзыв
                </button>
                <div id="error-container" class="error-container" style="display:none;">
                    <ul class="error-list">
                    </ul>
                </div>
            </div>

        </div>

    </c:if>

    <c:if test="${empty trip}">
        <p class="no-trips">Поездка не найдена.</p>
    </c:if>
</div>
<input id="contextId" value="${contextPath}" hidden="hidden">

<script src="<c:url value='/js/review.js'/>"></script>
<%@include file="/WEB-INF/templates/base/footer.jsp" %>
</body>

</html>
