<%@page import="com.example.todo.web.Controller"%>
<%@page import="com.example.todo.web.TaskController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    Controller controller = new TaskController();
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
            <a href="index.jsp">Index</a> |
            Add Task |
            <a href="login.jsp?action=logout">Logout</a>
        </div>
        <h2 class="subheader">Add Task</h2>
        <form method="POST" action="task.jsp">
            <label for="description">Description:</label>
            <input type="text" name="description" id="description" /><br />
            <input type="hidden" name="action" value="addTask" />
            <input type="submit" name="add" value="Add and Return" />
            <input type="submit" name="addNext" value="Add More" />
        </form>
    </body>
</html>
