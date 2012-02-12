<%
    new com.example.todo.web.IndexController().actionMapper(session, request, response);
    if ("ajax".equals(request.getParameter("responseType"))) {
        return;
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Example TODO Task List</title>
        <link rel="stylesheet" type="text/css" href="style.css" />
        <script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
        <script type="text/javascript" charset="UTF-8">
            function setCompleted(taskId) {
                $.post("index.jsp", {responseType: "ajax", action: "setCompleted", taskId: taskId}, function(result) {
                    if (result == "true") {
                        $("#task" + taskId).addClass("done");
                        $("#actions" + taskId).empty().append("Completed!");
                    } else {
                        alert("This task can not be marked completed");
                    }
                });
            }
        </script>
    </head>
    <body>
        <div class="header">
            <div class="header-base banner">Example TODO</div>
            <div class="header-base userpanel">Welcome, ${sessionScope.user.userName}! | <a class="loginlink" href="login.jsp?action=logout">Logout</a></div>
        </div>
        <div class="main">
            <h2 class="sub">Your Tasks</h2>
            <c:if test="${!empty requestScope.errors}">
                <div class="errors">
                    <c:forEach var="error" items="${requestScope.errors}">
                        ${error}<br/>
                    </c:forEach>
                </div>
            </c:if>
            <form method="POST" action="index.jsp">
                <label for="description">Description:</label>
                <input type="text" name="description" id="description" />
                <input type="hidden" name="action" value="addTask" />
                <input type="submit" name="add" value="Add This Task" />
            </form>
            <c:choose>
                <c:when test="${!empty requestScope.tasks}">
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
                                <tr id="task${task.id}" <c:if test="${task.completed}">class="done"</c:if>>
                                    <td>${task.description}</td>
                                    <td><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${task.created}" /></td>
                                    <td id="actions${task.id}">
                                        <c:choose>
                                            <c:when test="${task.completed}">Completed!</c:when>
                                            <c:otherwise><a href="index.jsp?action=setCompleted&amp;taskId=${task.id}" class="actionlink" onclick="setCompleted(${task.id}); return false;">Complete</a></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="tasks">You have no tasks yet</div>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>
