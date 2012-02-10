<%@page import="com.example.todo.web.Controller"%>
<%@page import="com.example.todo.web.LoginController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
    <body>
        <div class="header">
            <div class="header-base banner">Example TODO</div>
            <div class="header-base userpanel">Not logged in | <a class="loginlink" href="login.jsp">Login</a></div>
        </div>
        <div class="main">
            <h2 class="sub">Login Form</h2>
            <c:if test="${!empty requestScope.errors}">
                <div class="errors">
                    <c:forEach var="error" items="${requestScope.errors}">
                        ${error}<br/>
                    </c:forEach>
                </div>
            </c:if>
            <div class="center">
                <form method="POST" action="login.jsp">
                    <label for="userName">User Name:</label>
                    <input type="text" name="userName" id="userName" /><br />
                    <label for="password">Password:</label>
                    <input type="password" name="password" id="password" /><br />
                    <input type="hidden" name="action" value="login" />
                    <input type="submit" name="login" value="Log In"/>
                </form>
            </div>
        </div>
    </body>
</html>
