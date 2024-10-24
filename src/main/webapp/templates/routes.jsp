<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Список маршрутов</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
          integrity="sha512-iecdLmaskl7CVkqk1w27AHzTy9FJWbW+r/l51NXP+NfJ2o+hW7u3I5pHU5Kz+0lEkmzupR+s/gTDVEuU99l/oQ=="
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/routes.css'/>">
</head>
<body>

<%@include file="/templates/base/navbar.jsp" %>
<header style=" background-image: url(<c:url value='/img/route-header.png'/>);">
</header>
<div class="container">
    <div class="routes-list">
        <c:if test="${not empty routeList}">
            <c:forEach var="route" items="${routeList}" varStatus="iterStat">
                <div class="route-card">
                    <div class="route-body">
                        <div class="route-locations">
                            <p><i class="fas fa-map-marker-alt start-icon"></i> <c:out value="${route.start.name}"/>,
                                <c:out value="${route.start.country}"/>
                                <i class="fas fa-arrow-right"></i>
                                <i class="fas fa-map-marker-alt end-icon"></i> <c:out value="${route.finish.name}"/>,
                                <c:out value="${route.finish.country}"/></p>
                        </div>
                        <div class="route-details">
                            <p><strong>Полный маршрут:</strong></p>
                            <p class="full-route">
                                <c:out value="${route.start.name}"/>
                                <c:forEach var="point" items="${route.others}">
                                    - <c:out value="${point.name}"/>
                                </c:forEach>
                                - <c:out value="${route.finish.name}"/>
                            </p>
                            <p><c:out value="${route.distance}"/> км</p>
                        </div>
                        <input id="id" value="${route.id}" hidden="hidden">
                    </div>
                    ${route.id}

                </div>
            </c:forEach>



        </c:if>

        <c:if test="${empty routeList}">
            <p class="no-trips">Нет доступных маршрутов.</p>
        </c:if>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="<c:url value='/js/routes.js'/>"></script>
<%@include file="/templates/base/footer.jsp" %>

</body>
</html>




