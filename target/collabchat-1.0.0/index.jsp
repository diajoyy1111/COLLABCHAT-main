<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // If the user already has a session, send them to the home screen.
    // Otherwise, show the login page.
    String username = (String) session.getAttribute("username");
    if (username != null) {
        response.sendRedirect("home.jsp");
    } else {
        response.sendRedirect("login.jsp");
    }
%>
