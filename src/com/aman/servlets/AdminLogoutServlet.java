package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.aman.constant.UserRole;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/adminlogout")
public class AdminLogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        RequestDispatcher rd = req.getRequestDispatcher("AdminLogin.html");
        rd.include(req, res);

        // ✅ Check login status
        if (TrainUtil.isLoggedIn(req, UserRole.ADMIN)) {
            TrainUtil.logout(res);

            // ✅ Add logout success message inside login card
            pw.println("<script>"
                    + "document.addEventListener('DOMContentLoaded', function() {"
                    + " var loginCard = document.querySelector('form');"
                    + " if(loginCard) {"
                    + "   var msgDiv = document.createElement('div');"
                    + "   msgDiv.innerHTML = `"
                    + "<div style='margin-top:10px;background:#d4edda;"
                    + "color:#155724;padding:10px 15px;border-radius:6px;"
                    + "font-weight:600;text-align:center;border-left:5px solid #28a745;'>"
                    + "You have been successfully logged out!</div>`;"
                    + "   loginCard.parentNode.insertBefore(msgDiv, loginCard.nextSibling);"
                    + " }"
                    + "});"
                    + "</script>");

        } else {
            // ✅ Already logged out message inside card
            pw.println("<script>"
                    + "document.addEventListener('DOMContentLoaded', function() {"
                    + " var loginCard = document.querySelector('form');"
                    + " if(loginCard) {"
                    + "   var msgDiv = document.createElement('div');"
                    + "   msgDiv.innerHTML = `"
                    + "<div style='margin-top:10px;background:#fff3cd;"
                    + "color:#856404;padding:10px 15px;border-radius:6px;"
                    + "font-weight:600;text-align:center;border-left:5px solid #ffc107;'>"
                    + "You are already logged out!</div>`;"
                    + "   loginCard.parentNode.insertBefore(msgDiv, loginCard.nextSibling);"
                    + " }"
                    + "});"
                    + "</script>");
        }
    }
}
