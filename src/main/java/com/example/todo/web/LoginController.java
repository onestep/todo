package com.example.todo.web;

import com.example.todo.model.User;
import com.example.todo.service.UserService;
import com.example.todo.util.UserNotFoundException;
import com.example.todo.util.WrongPasswordException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Val
 */
public class LoginController implements Controller {

    private List<String> errors = new ArrayList<String>();

    public void actionMapper(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute("errors", errors);
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
        if (userName == null || userName.isEmpty()) {
            errors.add("User name can not be empty");
        }
        String password = request.getParameter("password");
        if (password == null || password.isEmpty()) {
            errors.add("Password can not be empty");
        }

        if (userName != null && !userName.isEmpty() && password != null && !password.isEmpty()) {
            User user;
            try {
                user = UserService.getUser(userName, password);
            } catch (UserNotFoundException ex) {
                user = UserService.addUser(userName, password);
                if (user == null) {
                    errors.add("Can not register user with such name");
                }
            } catch (WrongPasswordException ex) {
                errors.add("Password is wrong");
                user = null;
            }

            if (user != null) {
                session.setAttribute("user", user);
                response.sendRedirect("index.jsp");
            }
        }
    }

    public void logoutAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.removeAttribute("user");
    }
}
