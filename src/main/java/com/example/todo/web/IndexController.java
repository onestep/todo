package com.example.todo.web;

import com.example.todo.model.Task;
import com.example.todo.model.User;
import com.example.todo.service.TaskService;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
    private List<String> errors = new ArrayList<String>();

    public void actionMapper(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.setAttribute("errors", errors);
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("addTask".equals(action)) {
            addTaskAction(session, request, response);
        } else if ("setCompleted".equals(action)) {
            setCompletedAction(session, request, response);
        }

        /*
         * always perform default action
         */
        defaultAction(session, request, response);
    }

    private void defaultAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Task> tasks = TaskService.getTasks(user);

        request.setAttribute("tasks", tasks);
    }

    private void addTaskAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String description = request.getParameter("description");

        if (description != null && !description.isEmpty()) {
            Task task = TaskService.addTask(user, description);
            if (task == null) {
                errors.add("Can not add this task");
            }
        } else {
            errors.add("Task description is not specified");
        }
    }

    private void setCompletedAction(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer taskId;
        try {
            taskId = new Integer(request.getParameter("taskId"));
        } catch (NumberFormatException ex) {
            taskId = null;
        } catch (NullPointerException ex) {
            taskId = null;
        }

        boolean result;
        if (taskId != null) {
            result = TaskService.setCompleted(taskId.intValue());
        } else {
            result = false;
        }

        if ("ajax".equals(request.getParameter("responseType"))) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");

            OutputStreamWriter outsw = new OutputStreamWriter(response.getOutputStream());
            outsw.write(Boolean.toString(result));
            outsw.flush();
        }
    }
}
