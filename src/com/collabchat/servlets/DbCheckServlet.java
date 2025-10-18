package com.collabchat.servlets;

import com.collabchat.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "DbCheckServlet", urlPatterns = {"/dbinfo"})
public class DbCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (Connection c = DBUtil.getConnection()) {
            int users = 0;
            int groups = 0;
            try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) AS cnt FROM users"); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) users = rs.getInt("cnt");
            }
            try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) AS cnt FROM `groups`"); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) groups = rs.getInt("cnt");
            }
            PrintWriter out = resp.getWriter();
            out.print("{\"ok\":true,\"users\":"+users+",\"groups\":"+groups+"}");
        } catch (Exception e) {
            resp.setStatus(500);
            PrintWriter out = resp.getWriter();
            String msg = e.getMessage() == null ? "unknown" : e.getMessage().replaceAll("\"","\\\"");
            out.print("{\"ok\":false,\"error\":\""+msg+"\"}");
        }
    }
}
