package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aman.beans.UserBean;
import com.aman.constant.UserRole;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/viewuserprofile")
public class ViewUserProfile extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);
        UserBean ub = TrainUtil.getCurrentCustomer(req);

        // ✅ If session expired, redirect to login page
        if (ub == null) {
            res.sendRedirect("UserLogin.html");
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
        rd.include(req, res);

        pw.println("<div id='profileViewCard' style='max-width:850px;margin:3rem auto;"
                + "background-color:white;border-radius:12px;box-shadow:0 6px 20px rgba(0,0,0,0.15);padding:25px;'>");

        pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1.5rem;'>User Profile</h2>");
        pw.println("<p style='text-align:center;color:#0066cc;font-size:1rem;margin-bottom:1.8rem;'>"
                + "Hello " + TrainUtil.getCurrentUserName(req) + "! Welcome to your NITRTC profile details.</p>");

        pw.println("<div style='overflow-x:auto;'>");
        pw.println("<table style='width:100%;border-collapse:collapse;font-size:0.95rem;'>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>Profile Photo:</td><td>Not Available</td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>User Name:</td><td>" + ub.getMailId() + "</td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>Password:</td><td><input type='password' disabled value='" + ub.getPWord() + "'/></td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>First Name:</td><td>" + ub.getFName() + "</td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>Last Name:</td><td>" + ub.getLName() + "</td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>Address:</td><td>" + ub.getAddr() + "</td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>Phone No:</td><td>" + ub.getPhNo() + "</td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>Mail ID:</td><td>" + ub.getMailId() + "</td></tr>");
        pw.println("</table></div>");
        pw.println("</div>");

        pw.println("<script>");
        pw.println("(function(){var f=document.querySelector('footer');var c=document.getElementById('profileViewCard');"
                + "if(f&&c){f.parentNode.insertBefore(c,f);c.style.marginBottom='3rem';window.scrollTo({top:c.offsetTop-100,behavior:'smooth'});} "
                + "document.querySelectorAll('nav button,.main .menu a').forEach(e=>{if(e.innerText.toLowerCase().includes('profile'))"
                + "{e.style.background='white';e.style.color='#003366';e.style.fontWeight='600';}});})();");
        pw.println("</script>");
    }
}
