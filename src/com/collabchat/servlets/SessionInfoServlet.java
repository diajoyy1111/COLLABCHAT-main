package com.collabchat.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SessionInfoServlet", urlPatterns = {"/session"})
public class SessionInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String username = (String) req.getSession().getAttribute("username");
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (username == null || userId == null) {
            resp.setStatus(200);
            resp.getWriter().print("{\"ok\":false}");
            return;
        }
        resp.getWriter().print("{\"ok\":true,\"username\":\""+username+"\",\"userId\":"+userId+"}");
    }
}
