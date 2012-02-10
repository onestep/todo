package com.example.todo.web;

import com.example.todo.service.TaskService;
import com.example.todo.model.Task;
import com.example.todo.model.User;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Val
 */
public class TaskController implements Controller {

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
    }

    private void addTaskAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String description = request.getParameter("description");

        Date dueDate = null;
        try {
            dueDate = new SimpleDateFormat("dd.MM.yyyy").parse(request.getParameter("dueDate"));
        } catch (ParseException ex) {
        } catch (NullPointerException ex) {
        }
        if (dueDate == null) {
            dueDate = new Date();
        }

        Task task = new Task();
        task.setUser(user);
        task.setDescription(description);
        TaskService.addTask(task);

        if (request.getParameter("addNext") == null) {
            response.sendRedirect("index.jsp");
        }
    }

    private void setCompletedAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int taskId = Integer.parseInt(request.getParameter("id"));

        TaskService.setCompleted(taskId);

        response.sendRedirect("index.jsp");
    }
}
