<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Профиль</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
          integrity="sha512-9usAa10IRO0HhonpyAIVpjrylPvoDwiPUiKdWk5t3PyolY1cOd4DSE0Ga+ri4AuTroPR5aQvXU9xC6qOPnzFeg=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" type="text/css" href="css/profile.css">
</head>
<body>
<%@include file="/templates/base/navbar.jsp" %>
<div class="container">
    <header class="profile-header">
        <h1>Мой профиль</h1>
        <a href="/edit" class="edit-button"><i class="fas fa-edit"></i> Редактировать</a>

        <a href="<%= request.getContextPath() %>/trips?organize=${user.login}" class="edit-button">
            <c:choose>
                <c:when test="${isOwner}">
                    Посмотреть созданные вами поездки
                </c:when>
                <c:otherwise>
                    Посмотреть поездки пользователя
                </c:otherwise>
            </c:choose>
        </a>
        <a href="<%= request.getContextPath() %>/trips?owner_tickets=${true}" class="edit-button">
            <c:choose>
                <c:when test="${isOwner}">
                    Посмотреть ваши поездки
                </c:when>
            </c:choose>
        </a>


    </header>

    <section class="user-info">
        <div class="info-item">
            <i class="fas fa-user"></i> <span>Логин:</span> <span>${user.login}</span>
        </div>
        <div class="info-item">
            <i class="fas fa-envelope"></i> <span>Email:</span> <span>${user.email}</span>
        </div>
        <div class="info-item">
            <i class="fas fa-phone"></i> <span>Телефон:</span> <span>${user.phoneNumber}</span>
        </div>
    </section>

    <section class="statistics">
        <div class="stat-card">
            <i class="fas fa-route icon"></i>
            <h3>Количество поездок</h3>
            <span>123</span>
        </div>
        <div class="stat-card">
            <i class="fas fa-comments icon"></i>
            <h3>Количество отзывов</h3>
            <span>1234</span>
        </div>
        <div class="stat-card">
            <i class="fas fa-star icon"></i>
            <h3>Рейтинг</h3>
            <span>4.5</span>
        </div>
    </section>


</div>


<%@include file="/templates/base/footer.jsp" %>
</body>
</html>