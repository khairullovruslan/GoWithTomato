<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/base/navbar.css'/>">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <img src="<c:url value='/img/logo.png'/>" alt="Logo" onclick="window.location.href='<%=request.getContextPath()%>/'">
    <h1>GoWithTomato</h1>
    <div class="search">

        <a href="<%= request.getContextPath() %>/trips">
            <i class="fas fa-search"></i> Поиск
        </a>
        <a href="#">
            <i class="fas fa-plus"></i> Опубликовать поездку
        </a>
        <a href="<%= request.getContextPath()%>/profile">
            <i class="fas fa-user"></i> Профиль
        </a>
    </div>
</nav>