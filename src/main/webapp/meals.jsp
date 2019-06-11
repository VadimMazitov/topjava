<%--
  Created by IntelliJ IDEA.
  User: Vadim
  Date: 07.06.2019
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Time</th>
        <th>Update</th>
        <th>Delete</th>
    </tr>
    <c:if test="${not empty meals}">
    <c:forEach var="meal" items="${meals}">
        <c:if test="${meal.excess}">
            <c:set var="excess" value="RED"/>
        </c:if>
        <c:if test="${not meal.excess}">
            <c:set var="excess" value="GREEN"/>
        </c:if>
            <tr>
                <td style="color: ${excess}">${meal.id}</td>
                <td style="color: ${excess}">${meal.description}</td>
                <td style="color: ${excess}">${meal.calories}</td>
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" var="date"/>
                <td style="color: ${excess}">${date}</td>
                <td style="color: ${excess}"><a href="meals?action=update&id=<c:out value="${meal.id}"/>">Update</a></td>
                <td style="color: ${excess}"><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
            </tr>
    </c:forEach>
    </c:if>
</table>

<a href="meals?action=add">Add Meal</a>
</form>
</body>
</html>
