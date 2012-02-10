package com.example.todo.web;

import com.example.todo.service.UserService;
import com.example.todo.model.User;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Val
 */
public class LoginController implements Controller {

    public void actionMapper(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if ("login".equals(action)) {
            loginAction(session, request, response);
        } else if ("logout".equals(action)) {
            logoutAction(session, request, response);
        }
    }

    public void loginAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (session.getAttribute("user") != null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        User user = UserService.getUser(userName, password);
        if (user == null) {
            user = new User();
            user.setUserName(userName);
            if (!UserService.addUser(user) || !UserService.changePassword(user, password)) {
                user = null;
            }
        }
        
        if (user != null) {
            session.setAttribute("user", user);
            response.sendRedirect("index.jsp");            
        }
    }

    public void logoutAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.removeAttribute("user");
    }
}
