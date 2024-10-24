<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to GoWithTomato</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/welcome.css'/>">
    <link rel="icon" href="<c:url value='/img/favicon.png'/>" type="image/png">

</head>
<body>

<%@include file="/templates/base/navbar.jsp" %>
<header style=" background-image: url(<c:url value='/img/header-image.png'/>);">
    <h1>Платформа для Совместных Поездок</h1>
    <h2>Путешествуйте комфортно и дешево!</h2>
</header>

<div class="container">
    <div class="features">
        <div class="feature">
            <i class="fas fa-car"></i>
            <h2>Предложите поездку</h2>
            <p>Укажите свой маршрут, дату и время выезда, количество свободных мест и цену. Найдите попутчиков и делитесь затратами на дорогу!</p>
        </div>
        <div class="feature">
            <i class="fas fa-users"></i>
            <h2>Найдите попутчика</h2>
            <p>Укажите свой маршрут, дату и время поездки. GoWithTomato подберет для вас подходящие варианты от других пользователей.</p>
        </div>
    </div>
</div>

<%@include file="/templates/base/footer.jsp" %>

</body>
</html>
