
<%--
  Created by IntelliJ IDEA.
  User: Vadim
  Date: 11.06.2019
  Time: 0:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form method="post" action="meals" name="frmMeal">
    Meal ID : <input type="text" readonly="readonly" name="id" value="<c:out value="${meal.id}" />" /><br/>
    Description : <input type="text" name="description" value="<c:out value="${meal.description}" />"/><br/>
    Calories : <input type="text" name="calories" value="<c:out value="${meal.calories}"/>"/><br/>
    Time : <input type="datetime-local" name="dateTime" value="<c:out value="${meal.dateTime}"/>"/><br/>
    <c:if test="${empty meal.description}">
        <input type="submit" value="Add"/>
    </c:if>
    <c:if test="${!empty meal.description}">
        <input type="submit" value="Update"/>
    </c:if>
</form>

</body>
</html>
