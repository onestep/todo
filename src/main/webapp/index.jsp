<%@page import="com.example.todo.web.Controller"%>
<%@page import="com.example.todo.web.IndexController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    Controller controller = new IndexController();
    controller.actionMapper(session, request, response);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Example TODO</title>
        <link rel="stylesheet" type="text/css" href="style.css" />
    </head>
    <body class="main">
        <h1 class="header">Example TODO</h1>
        <div class="subheader">
            Welcome, ${sessionScope.user.userName}! |
            <a href="login.jsp?action=logout">Logout</a>
        </div>
        <h2 class="subheader">Your Tasks</h2>
        <form method="POST" action="index.jsp">
            <label for="description">Description:</label>
            <input type="text" name="description" id="description" />
            <input type="hidden" name="action" value="addTask" />
            <input type="submit" name="add" value="Add This Task" />
        </form>
        <br/>
        <table class="tasks">
            <thead>
                <tr>
                    <th>Description</th>
                    <th>Created</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="task" items="${requestScope.tasks}">
                    <tr<c:if test="${task.completed}"> class="done"</c:if>>
                        <td>${task.description}</td>
                        <td><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${task.created}" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${task.completed}">Completed!</c:when>
                                <c:otherwise><a href="index.jsp?action=setCompleted&amp;id=${task.id}">✓ Complete</a></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
