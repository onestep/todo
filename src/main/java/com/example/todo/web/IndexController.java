package com.example.todo.web;

import com.example.todo.service.TaskService;
import com.example.todo.model.Task;
import com.example.todo.model.User;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        String action = request.getParameter("action");

        if ("addTask".equals(action)) {
            addTaskAction(session, request, response);
        } else if ("setCompleted".equals(action)) {
            setCompletedAction(session, request, response);
        }

        /* always perform default action */
        defaultAction(session, request, response);
    }

    private void defaultAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Task> tasks = TaskService.getTasks(user);

        request.setAttribute("tasks", tasks);
    }

    private void addTaskAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String description = request.getParameter("description");

        Task task = new Task();
        task.setUser(user);
        task.setDescription(description);

        TaskService.addTask(task);
    }

    private void setCompletedAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int taskId = Integer.parseInt(request.getParameter("id"));

        TaskService.setCompleted(taskId);
    }
}
