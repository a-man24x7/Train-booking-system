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
@WebServlet("/userhome")
public class UserHome extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        // Ensure authorized user
        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

        // Include main page first
        RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
        rd.include(req, res);

        // ✅ Instead of printing at the end, inject the message into the placeholder div using JS
        String userName = TrainUtil.getCurrentUserName(req);

        pw.println("<script>");
        pw.println("document.getElementById('welcomeMessageContainer').innerHTML = `"
                + "<div style='background:#e8f4ff; color:#00264d; padding:1.5rem; margin:2rem auto; "
                + "max-width:900px; border-radius:10px; text-align:center; box-shadow:0 3px 8px rgba(0,0,0,0.1); line-height:1.6;'>"
                + "Hello " + userName + "! Welcome to our new E-Train Website.<br/>"
                + "User Home<br/>"
                + "Hello " + userName + "! Good to See You here.<br/>"
                + "Here you can Check up the train details, train schedule, fare Enquiry and many more information.<br/>"
                + "Just go to the Side Menu Links and Explore the Advantages.<br/><br/>"
                + "Thanks For Being Connected with us!"
                + "</div>`;");
        pw.println("</script>");
    }
}
