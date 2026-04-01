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
import com.aman.beans.UserBean;
import com.aman.constant.UserRole;
import com.aman.service.UserService;
import com.aman.service.impl.UserServiceImpl;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/updateuserprofile")
public class UpdateUserProfile extends HttpServlet {

    private UserService userService = new UserServiceImpl(UserRole.CUSTOMER);

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);
        UserBean ub = TrainUtil.getCurrentCustomer(req);

        String fName = req.getParameter("firstname");
        String lName = req.getParameter("lastname");
        String addR = req.getParameter("address");
        long phNo = Long.parseLong(req.getParameter("phone"));
        String mailId = req.getParameter("mail");

        ub.setFName(fName);
        ub.setLName(lName);
        ub.setAddr(addR);
        ub.setPhNo(phNo);
        ub.setMailId(mailId);

        try {
            String message = userService.updateUser(ub);

            // Include base layout
            RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
            rd.include(req, res);

            pw.println("<div id='profileUpdateCard' style='max-width:850px;margin:3rem auto;"
                    + "background-color:white;border-radius:12px;box-shadow:0 6px 20px rgba(0,0,0,0.15);padding:25px;'>");

            pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1.5rem;'>Profile Update Status</h2>");
            pw.println("<p style='text-align:center;color:#0066cc;font-size:1rem;margin-bottom:1.8rem;'>"
                    + "Hello " + ub.getFName() + "! Welcome back to your NITRTC Dashboard.</p>");

            if ("SUCCESS".equalsIgnoreCase(message)) {
                pw.println("<div style='background-color:#e9f9ee;border-left:6px solid #28a745;"
                        + "padding:15px 20px;border-radius:8px;color:#155724;font-size:1rem;'>"
                        + "<strong>✅ Your profile has been successfully updated!</strong>"
                        + "<p style='margin-top:8px;'>All changes are now saved in our system.</p>"
                        + "</div>");
            } else {
                pw.println("<div style='background-color:#fff3cd;border-left:6px solid #ff9800;"
                        + "padding:15px 20px;border-radius:8px;color:#856404;font-size:1rem;'>"
                        + "<strong>⚠️ Please enter valid information!</strong>"
                        + "<p style='margin-top:8px;'>Your profile could not be updated. Try again.</p>"
                        + "</div>");
            }

            pw.println("<div style='text-align:center;margin-top:2rem;'>"
                    + "<a href='viewuserprofile' style='text-decoration:none;background-color:#007BFF;color:white;"
                    + "padding:10px 18px;border-radius:6px;margin:6px;display:inline-block;'>View Profile</a>"
                    + "<a href='edituserprofile' style='text-decoration:none;background-color:#00AA88;color:white;"
                    + "padding:10px 18px;border-radius:6px;margin:6px;display:inline-block;'>Edit Again</a>"
                    + "<a href='userprofile' style='text-decoration:none;background-color:#555;color:white;"
                    + "padding:10px 18px;border-radius:6px;margin:6px;display:inline-block;'>Back to Profile</a>"
                    + "</div>");

            pw.println("</div>");

            // JavaScript for positioning and highlighting
            pw.println("<script>");
            pw.println("(function(){"
                    + "var footer=document.querySelector('footer');"
                    + "var card=document.getElementById('profileUpdateCard');"
                    + "if(footer&&card){footer.parentNode.insertBefore(card,footer);"
                    + "card.style.marginBottom='3rem';window.scrollTo({top:card.offsetTop-100,behavior:'smooth'});} "
                    + "document.querySelectorAll('nav button,.main .menu a').forEach(l=>{if(l.innerText.toLowerCase().includes('profile'))"
                    + "{l.style.background='white';l.style.color='#003366';l.style.fontWeight='600';}});"
                    + "})();");
            pw.println("</script>");

            pw.flush();

        } catch (Exception e) {
            throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
        }
    }
}
