package com.collabchat.servlets;

import com.collabchat.dao.GroupDAO;
import com.collabchat.dao.UserDAO;
import com.collabchat.models.Group;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "GroupServlet", urlPatterns = {"/group"})
public class GroupServlet extends HttpServlet {
    private GroupDAO groupDAO = new GroupDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // create group or join via POST (action=join)
        String action = req.getParameter("action");
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) { resp.setStatus(401); return; }

        if ("join".equals(action)) {
            // join existing group
            String gid = req.getParameter("groupId");
            if (gid == null) { resp.setStatus(400); return; }
            try {
                int groupId = Integer.parseInt(gid);
                boolean ok = groupDAO.addMember(groupId, userId);
                resp.setStatus(ok?200:400);
            } catch (SQLException | NumberFormatException e) { resp.setStatus(500); }
            return;
        }

        // default: create group
        String name = req.getParameter("name");
        if (name == null) { resp.setStatus(400); return; }
        try {
            int groupId = groupDAO.createGroup(name, userId);
            groupDAO.addMember(groupId, userId);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print("{\"groupId\":"+groupId+"}");
            resp.setStatus(201);
        } catch (SQLException e) { resp.setStatus(500); }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // join group (expects groupId)
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        int groupId = Integer.parseInt(req.getParameter("groupId"));
        try {
            boolean ok = groupDAO.addMember(groupId, userId);
            resp.setStatus(ok?200:400);
        } catch (SQLException e) { resp.setStatus(500); }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) { resp.setStatus(401); return; }
        
        String groupIdParam = req.getParameter("groupId");
        
        try {
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            
            if (groupIdParam != null) {
                // Return specific group information
                int groupId = Integer.parseInt(groupIdParam);
                Group group = groupDAO.findById(groupId);
                if (group != null) {
                    out.print("{\"groupId\":"+group.getGroupId()+",\"name\":\""+group.getName()+"\"}");
                } else {
                    resp.setStatus(404);
                }
            } else {
                // Return all groups for user
                List<Group> groups = groupDAO.findGroupsForUser(userId);
                out.print("[");
                boolean first=true;
                for (Group g: groups) {
                    if (!first) out.print(",");
                    out.print("{\"groupId\":"+g.getGroupId()+",\"name\":\""+g.getName()+"\"}");
                    first=false;
                }
                out.print("]");
            }
        } catch (SQLException e) { 
            resp.setStatus(500); 
        } catch (NumberFormatException e) {
            resp.setStatus(400);
        }
    }
}
