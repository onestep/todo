<%@page import="com.example.todo.web.Controller"%>
<%@page import="com.example.todo.web.LoginController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Controller controller = new LoginController();
    controller.actionMapper(session, request, response);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Example TODO Login Page</title>
        <link rel="stylesheet" type="text/css" href="style.css" />
    </head>
    <body class="main">
        <h1 class="header">Example TODO</h1>
        <h2 class="subheader">Login Form</h2>
        <form method="POST" action="login.jsp">
            <label for="userName">User Name:</label>
            <input type="text" name="userName" id="userName" /><br />
            <label for="password">Password:</label>
            <input type="password" name="password" id="password" /><br />
            <input type="hidden" name="action" value="login" />
            <input type="submit" name="login" value="Log In"/>
        </form>
    </body>
</html>
