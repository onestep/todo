package com.example.todo.web;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Val
 */
public interface Controller {

    void actionMapper(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
