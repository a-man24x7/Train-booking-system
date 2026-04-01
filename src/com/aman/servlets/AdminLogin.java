package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.aman.beans.TrainException;
import com.aman.constant.UserRole;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/adminlogin")
public class AdminLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        String uName = req.getParameter("uname");
        String pWord = req.getParameter("pword");

        try {
            String message = TrainUtil.login(req, res, UserRole.ADMIN, uName, pWord);

            if ("SUCCESS".equalsIgnoreCase(message)) {

                // ✅ On successful login
                RequestDispatcher rd = req.getRequestDispatcher("AdminHome.html");
                rd.include(req, res);

                pw.println("<div id='adminWelcomeCard' style='max-width:800px;margin:2rem auto;"
                        + "background:white;border-radius:10px;box-shadow:0 6px 18px rgba(0,0,0,0.1);"
                        + "padding:20px;text-align:center;'>"
                        + "<h3 style='color:#003366;margin-bottom:8px;'>Hello, " + uName + "!</h3>"
                        + "<p style='color:#0066cc;font-size:1rem;'>Hi! You can manage train information from your dashboard.</p>"
                        + "</div>");

                pw.println("<script>"
                        + "(function(){var f=document.querySelector('footer');"
                        + "var c=document.getElementById('adminWelcomeCard');"
                        + "if(f&&c){f.parentNode.insertBefore(c,f);"
                        + "c.style.marginBottom='3rem';"
                        + "window.scrollTo({top:c.offsetTop-100,behavior:'smooth'});} })();"
                        + "</script>");

            } else {
                // ❌ Invalid credentials — show inside login card
                RequestDispatcher rd = req.getRequestDispatcher("AdminLogin.html");
                rd.include(req, res);

                // Add error message directly inside the login form
                pw.println("<script>"
                        + "document.addEventListener('DOMContentLoaded', function() {"
                        + " var loginCard = document.querySelector('form');"
                        + " if(loginCard) {"
                        + "   var msgDiv = document.createElement('div');"
                        + "   msgDiv.innerHTML = `" 
                        + "<div style='margin-top:10px;background:#f8d7da;"
                        + "color:#721c24;padding:10px 15px;border-radius:6px;"
                        + "font-weight:600;text-align:center;border-left:5px solid #dc3545;'>"
                        + "UNAUTHORIZED : Invalid Credentials, Try Again</div>`;"
                        + "   loginCard.parentNode.insertBefore(msgDiv, loginCard.nextSibling);"
                        + " }"
                        + "});"
                        + "</script>");
            }

        } catch (Exception e) {
            throw new TrainException(422,
                    this.getClass().getName() + "_FAILED",
                    e.getMessage());
        }
    }
}
