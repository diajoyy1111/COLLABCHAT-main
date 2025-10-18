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

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With")) || "true".equals(req.getParameter("ajax"));
        if (username == null || password == null) {
            if (isAjax) { resp.setStatus(400); resp.setContentType("application/json"); resp.getWriter().print("{\"error\":\"missing_parameters\"}"); return; }
            resp.sendRedirect("register.jsp"); return;
        }
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        try {
            int userId = userDAO.createUser(username, hash);
            if (userId > 0) {
                req.getSession().setAttribute("username", username);
                req.getSession().setAttribute("userId", userId);
                if (isAjax) {
                    resp.setStatus(201);
                    resp.setContentType("application/json");
                    resp.getWriter().print("{\"userId\":"+userId+",\"username\":\""+username+"\"}");
                    return;
                }
                resp.sendRedirect("home.jsp");
                return;
            }
        } catch (SQLException e) {
            // duplicate or error
        }
        if (isAjax) { resp.setStatus(400); resp.setContentType("application/json"); resp.getWriter().print("{\"error\":\"could_not_create\"}"); return; }
        resp.sendRedirect("register.jsp");
    }
}
