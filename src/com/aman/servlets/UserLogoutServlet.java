package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aman.constant.UserRole;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/userlogout")
public class UserLogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        // Include the login page first
        RequestDispatcher rd = req.getRequestDispatcher("UserLogin.html");
        rd.include(req, res);

        // Determine which message to show
        String message;
        if (TrainUtil.isLoggedIn(req, UserRole.CUSTOMER)) {
            TrainUtil.logout(res);
            message = "You have been successfully logged out!";
        } else {
            message = "You are already logged out!";
        }

        // ✅ Inject the message into the correct placeholder (before footer)
        pw.println("<script>");
        pw.println("const msgBox = document.getElementById('logoutMessageContainer');");
        pw.println("if (msgBox) {");
        pw.println("  msgBox.textContent = '" + message + "';");
        pw.println("  msgBox.style.display = 'block';");
        pw.println("}");
        pw.println("</script>");
    }
}
