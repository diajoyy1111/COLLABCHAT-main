package com.collabchat.servlets;

import com.collabchat.dao.MessageDAO;
import com.collabchat.dao.UserDAO;
import com.collabchat.models.Message;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import com.collabchat.util.StringUtil;
import java.util.List;

@WebServlet(name = "MessageServlet", urlPatterns = {"/message"})
public class MessageServlet extends HttpServlet {
    private MessageDAO messageDAO = new MessageDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        String gidStr = req.getParameter("groupId");
        String message = req.getParameter("message");
        if (userId == null) { resp.setStatus(401); resp.setContentType("application/json"); resp.getWriter().print("{\"error\":\"unauthenticated\"}"); return; }
        if (gidStr == null || message == null) { resp.setStatus(400); resp.setContentType("application/json"); resp.getWriter().print("{\"error\":\"missing_parameters\"}"); return; }
        try {
            int groupId = Integer.parseInt(gidStr);
            int msgId = messageDAO.saveMessage(groupId, userId, message);
            if (msgId > 0) {
                resp.setStatus(201);
                resp.setContentType("application/json");
                resp.getWriter().print("{\"messageId\":"+msgId+",\"groupId\":"+groupId+"}");
                return;
            }
        } catch (SQLException | NumberFormatException e) { e.printStackTrace(); }
        resp.setStatus(500);
        resp.setContentType("application/json");
        resp.getWriter().print("{\"error\":\"server_error\"}");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String gidStr = req.getParameter("groupId");
        if (gidStr == null || gidStr.trim().isEmpty()) { resp.setStatus(400); resp.setContentType("application/json"); resp.getWriter().print("{\"error\":\"missing_groupId\"}"); return; }
        int limit = 200;
        try {
            int groupId = Integer.parseInt(gidStr);
            List<Message> msgs = messageDAO.fetchRecentForGroup(groupId, limit);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print("[");
            boolean first=true;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Message m: msgs) {
                if (!first) out.print(",");
                String ts = m.getTimestamp()!=null?df.format(m.getTimestamp()):"";
                out.print("{\"id\":"+m.getMessageId()+",\"userId\":"+m.getUserId()+",\"username\":"+StringUtil.escapeJson(m.getUsername())+",\"text\":"+StringUtil.escapeJson(m.getMessage())+",\"ts\":\""+ts+"\",\"timestamp\":"+(m.getTimestamp()!=null?m.getTimestamp().getTime():System.currentTimeMillis())+"}");
                first=false;
            }
            out.print("]");
        } catch (SQLException | NumberFormatException e) { e.printStackTrace(); resp.setStatus(500); resp.setContentType("application/json"); resp.getWriter().print("{\"error\":\"server_error\"}"); }
    }
}
