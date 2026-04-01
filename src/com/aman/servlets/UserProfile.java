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
@WebServlet("/userprofile")
public class UserProfile extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

        // Include base layout (nav, header, footer)
        RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
        rd.include(req, res);

        String userName = TrainUtil.getCurrentUserName(req);

        // Profile card container
        pw.println("<div id='profileCard' style='max-width:850px;margin:3rem auto;"
                + "background-color:white;border-radius:12px;box-shadow:0 6px 20px rgba(0,0,0,0.15);padding:25px;'>");

        pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1.5rem;'>Welcome, " + userName + "!</h2>");
        pw.println("<p style='text-align:center;color:#0066cc;font-size:1rem;margin-bottom:1.8rem;'>"
                + "Hello " + userName + "! Welcome to our new E-Train Website 🚆</p>");

        pw.println("<div style='text-align:center;margin-bottom:2rem;'>"
                + "<a href='viewuserprofile' style='text-decoration:none;background-color:#007BFF;color:white;"
                + "padding:10px 18px;border-radius:6px;margin:6px;display:inline-block;'>View Profile</a>"
                + "<a href='edituserprofile' style='text-decoration:none;background-color:#00AA88;color:white;"
                + "padding:10px 18px;border-radius:6px;margin:6px;display:inline-block;'>Edit Profile</a>"
                + "<a href='changeuserpassword' style='text-decoration:none;background-color:#FF9933;color:white;"
                + "padding:10px 18px;border-radius:6px;margin:6px;display:inline-block;'>Change Password</a>"
                + "</div>");

        pw.println("<div style='background-color:#f0f8ff;border-radius:8px;padding:15px 20px;color:#333;"
                + "line-height:1.6;text-align:center;'>"
                + "<p><strong>Hey " + userName + "!</strong> Here you can edit your details, "
                + "view your profile, and securely change your password.</p>"
                + "<p>Thanks for staying connected with <strong>E-Train</strong> 🚄</p>"
                + "</div>");

        pw.println("</div>");

        // JS to move the card above footer and highlight Profile tab
        pw.println("<script>");
        pw.println("(function(){");
        pw.println("var footer=document.querySelector('footer');"
                + "var card=document.getElementById('profileCard');"
                + "if(footer && card){footer.parentNode.insertBefore(card, footer);"
                + "card.style.marginBottom='3rem';window.scrollTo({top:card.offsetTop-100,behavior:'smooth'});}"); 
        pw.println("var links=document.querySelectorAll('nav button, .main .menu a');"
                + "links.forEach(l=>{if(l.innerText.trim().toLowerCase().includes('profile')){"
                + "l.style.background='white';l.style.color='#003366';l.style.fontWeight='600';}});");
        pw.println("})();");
        pw.println("</script>");

        pw.flush();
    }
}
