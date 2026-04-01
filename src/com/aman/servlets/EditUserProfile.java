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
@WebServlet("/edituserprofile")
public class EditUserProfile extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();
        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

        UserBean ub = TrainUtil.getCurrentCustomer(req);

        // ✅ Redirect if session expired
        if (ub == null) {
            res.sendRedirect("UserLogin.html");
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
        rd.include(req, res);

        pw.println("<div id='profileEditCard' style='max-width:850px;margin:3rem auto;"
                + "background-color:white;border-radius:12px;box-shadow:0 6px 20px rgba(0,0,0,0.15);padding:25px;'>");
        pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1.5rem;'>Edit Your Profile</h2>");
        pw.println("<p style='text-align:center;color:#0066cc;font-size:1rem;margin-bottom:1.8rem;'>"
                + "Update your personal details safely below.</p>");

        pw.println("<form action='updateuserprofile' method='post' style='width:100%;'>");
        pw.println("<table style='width:100%;border-collapse:collapse;font-size:0.95rem;'>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>User Name:</td><td><input type='text' name='username' value='" + ub.getMailId() + "' disabled></td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>First Name:</td><td><input type='text' name='firstname' value='" + ub.getFName() + "'></td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>Last Name:</td><td><input type='text' name='lastname' value='" + ub.getLName() + "'></td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>Address:</td><td><input type='text' name='address' value='" + ub.getAddr() + "'></td></tr>");
        pw.println("<tr><td style='padding:10px;font-weight:600;'>Phone No:</td><td><input type='text' name='phone' value='" + ub.getPhNo() + "'></td></tr>");
        pw.println("<tr><td></td><td><input type='hidden' name='mail' value='" + ub.getMailId() + "'>"
                + "<button type='submit' style='background-color:#007BFF;color:white;padding:10px 18px;"
                + "border:none;border-radius:6px;margin-top:10px;cursor:pointer;'>Update Profile</button></td></tr>");
        pw.println("</table></form>");
        pw.println("</div>");

        pw.println("<script>");
        pw.println("(function(){var f=document.querySelector('footer');var c=document.getElementById('profileEditCard');"
                + "if(f&&c){f.parentNode.insertBefore(c,f);c.style.marginBottom='3rem';window.scrollTo({top:c.offsetTop-100,behavior:'smooth'});} "
                + "document.querySelectorAll('nav button,.main .menu a').forEach(e=>{if(e.innerText.toLowerCase().includes('profile'))"
                + "{e.style.background='white';e.style.color='#003366';e.style.fontWeight='600';}});})();");
        pw.println("</script>");
    }
}
