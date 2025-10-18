package com.collabchat.servlets;

import com.collabchat.dao.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With")) || "true".equals(req.getParameter("ajax"));
        if (username == null || password == null) { if (isAjax) { resp.setStatus(400); resp.setContentType("application/json"); resp.getWriter().print("{\"error\":\"missing_parameters\"}"); return; } resp.sendRedirect("login.jsp"); return; }
        try {
            String hash = userDAO.getPasswordHashByUsername(username);
            if (hash != null && BCrypt.checkpw(password, hash)) {
                // load user id
                int userId = userDAO.findByUsername(username).getUserId();
                req.getSession().setAttribute("username", username);
                req.getSession().setAttribute("userId", userId);
                if (isAjax) { resp.setStatus(200); resp.setContentType("application/json"); resp.getWriter().print("{\"userId\":"+userId+",\"username\":\""+username+"\"}"); return; }
                resp.sendRedirect("home.jsp");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (isAjax) { resp.setStatus(401); resp.setContentType("application/json"); resp.getWriter().print("{\"error\":\"invalid_credentials\"}"); return; }
        resp.sendRedirect("login.jsp");
    }
}
