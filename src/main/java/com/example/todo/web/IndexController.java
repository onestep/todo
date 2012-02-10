package com.example.todo.web;

import com.example.todo.service.TaskService;
import com.example.todo.model.Task;
import com.example.todo.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Val
 */
public class IndexController implements Controller {

    private User user;

    public void actionMapper(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        request.setCharacterEncoding("UTF-8");

        indexAction(session, request, response);
    }

    private void indexAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Task> tasks = TaskService.getTasks(user);

        request.setAttribute("tasks", tasks);
    }
}
