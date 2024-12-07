<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/base/navbar.css'/>">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <img src="<c:url value='/img/logo.png'/>" alt="Logo"
         onclick="window.location.href='<%=request.getContextPath()%>/'">
    <h1>GoWithTomato</h1>
    <div class="search">

        <a href="<%= request.getContextPath() %>/trips">
            <i class="fas fa-search"></i> Поиск
        </a>
        <a href="<%= request.getContextPath() %>/profile/routes">
            <i class="fas fa-plus"></i> Опубликовать поездку
        </a>


        <c:choose>

            <c:when test="${userIsAuthorized}">
                <a href="<%= request.getContextPath()%>/profile">
                    <i class="fas fa-user"></i> Профиль
                </a>
            </c:when>

            <c:otherwise>
                <a href="<%= request.getContextPath()%>/login">
                    <i class="fas fa-user"></i> Войти
                </a>
            </c:otherwise>

        </c:choose>
    </div>
</nav>