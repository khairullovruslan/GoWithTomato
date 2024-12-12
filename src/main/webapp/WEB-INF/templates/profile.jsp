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
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/profile.css'/>">
    <link rel="icon" href="<c:url value='/img/favicon.png'/>" type="image/png">
</head>
<body>
<%@include file="/WEB-INF/templates/base/navbar.jsp" %>
<div class="container">
    <h1>Мой профиль</h1>
    <header class="profile-header">
        <img src="<c:url value='${photo}'/>" alt="" width="200" height="200">

        <c:if test="${isOwner}">
            <a href="${contextPath}/profile/edit" class="edit-button"><i class="fas fa-edit"></i>
                Редактировать</a>
        </c:if>


        <a href="${contextPath}/trips?organizer=${user.login}" class="edit-button">
            <c:choose>
                <c:when test="${isOwner}">
                    Посмотреть созданные вами поездки
                </c:when>
                <c:otherwise>
                    Посмотреть поездки пользователя
                </c:otherwise>
            </c:choose>
        </a>
        <c:if test="${isOwner}">
            <a href="${contextPath}/trips?owner_tickets=${true}" class="edit-button">
                Посмотреть ваши поездки
            </a>
            <a style="background-color: red" href="${contextPath}/logout" class="edit-button">
                Выйти
            </a>

        </c:if>


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
            <span>${tripCount}</span>
        </div>
        <div class="stat-card">
            <i class="fas fa-comments icon"></i>
            <h3>Количество отзывов</h3>
            <span>${reviewCount}</span>
        </div>
        <div class="stat-card">
            <i class="fas fa-star icon"></i>
            <h3>Рейтинг</h3>
            <span>${rating}</span>
        </div>
    </section>
    <h2>Отзывы:</h2>

    <div class="review-list">
        <c:if test="${not empty reviewList}">
            <c:forEach var="review" items="${reviewList}" varStatus="iterStat">
                <div class="review-card">
                    <div class="review-body">
                        <div class="review-author">
                            <p><strong style="color: black">Автор: </strong> <c:out value="${review.owner.login}"/></p>
                        </div>
                        <div class="review-details">
                            <p><strong style="color: black">Описание:</strong></p>
                            <p style="color: black">
                                <c:out value="${review.description}"/>,
                            </p>
                            <p><strong style="color: black">Рейтинг: </strong> <c:out value="${review.rating}"/></p>

                        </div>
                    </div>

                </div>


            </c:forEach>


        </c:if>

        <c:if test="${empty reviewList}">
            <p class="no-review">Нет отзывов.</p>
        </c:if>
    </div>


</div>

<div class="pagination">

    <c:choose>
        <c:when test="${isOwner}">
            <c:if test="${page > 1}">
                <a href="?page=${page - 1}" class="pagination-link">« Предыдущая</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="?page=${i}" class="pagination-link <c:if test='${i == page}'>active</c:if>">${i}</a>
            </c:forEach>

            <c:if test="${page < totalPages}">
                <a href="?page=${page + 1}" class="pagination-link">Следующая »</a>
            </c:if>
        </c:when>
        <c:otherwise>
            <c:if test="${page > 1}">
                <a href="?page=${page - 1}&u=${user.login}" class="pagination-link">« Предыдущая</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="?page=${i}&u=${user.login}"
                   class="pagination-link <c:if test='${i == page}'>active</c:if>">${i}</a>
            </c:forEach>

            <c:if test="${page < totalPages}">
                <a href="?page=${page + 1}&u=${user.login}" class="pagination-link">Следующая »</a>
            </c:if>
        </c:otherwise>
    </c:choose>


</div>


<%@include file="/WEB-INF/templates/base/footer.jsp" %>
</body>
</html>