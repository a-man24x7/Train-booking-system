package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aman.beans.TrainBean;
import com.aman.beans.TrainException;
import com.aman.constant.UserRole;
import com.aman.service.TrainService;
import com.aman.service.impl.TrainServiceImpl;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/searchtrainservlet")
public class UserSearchTrain extends HttpServlet {
    private TrainService trainService = new TrainServiceImpl();

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

        try {
            String trainNo = req.getParameter("trainnumber");
            TrainBean train = trainService.getTrainById(trainNo);

            // Include layout
            RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
            rd.include(req, res);

            pw.println("<div id='searchCard' style='max-width:850px;margin:3rem auto;"
                    + "background-color:white;border-radius:12px;box-shadow:0 6px 20px rgba(0,0,0,0.15);padding:25px;'>");

            if (train != null) {
                pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1.5rem;'>Searched Train Detail</h2>");
                pw.println("<p style='text-align:center;color:#0066cc;font-size:1rem;margin-bottom:1.2rem;'>"
                        + "Here are the details for Train Number <strong>" + trainNo + "</strong>.</p>");

                pw.println("<div style='overflow-x:auto;'>");
                pw.println("<table style='width:100%;border-collapse:collapse;font-size:0.95rem;'>");

                pw.println("<tr><td style='padding:10px;font-weight:600;'>Train Name:</td>"
                        + "<td style='padding:10px;'>" + train.getTr_name() + "</td></tr>");
                pw.println("<tr><td style='padding:10px;font-weight:600;'>Train Number:</td>"
                        + "<td style='padding:10px;'>" + train.getTr_no() + "</td></tr>");
                pw.println("<tr><td style='padding:10px;font-weight:600;'>From Station:</td>"
                        + "<td style='padding:10px;'>" + train.getFrom_stn() + "</td></tr>");
                pw.println("<tr><td style='padding:10px;font-weight:600;'>To Station:</td>"
                        + "<td style='padding:10px;'>" + train.getTo_stn() + "</td></tr>");
                pw.println("<tr><td style='padding:10px;font-weight:600;'>Available Seats:</td>"
                        + "<td style='padding:10px;color:#008000;font-weight:600;'>" + train.getSeats()
                        + " Seats</td></tr>");
                pw.println("<tr><td style='padding:10px;font-weight:600;'>Fare (INR):</td>"
                        + "<td style='padding:10px;'>" + train.getFare() + " RS</td></tr>");
                pw.println("</table></div>");

            } else {
                pw.println("<h2 style='text-align:center;color:#c00;'>Train Number " + trainNo + " Not Found!</h2>");
                pw.println("<p style='text-align:center;color:#555;'>Please check the number and try again.</p>");
            }

            pw.println("</div>");

            // JavaScript: move card above footer and highlight navbar
            pw.println("<script>");
            pw.println("(function(){");
            pw.println("var footer=document.querySelector('footer');"
                    + "var card=document.getElementById('searchCard');"
                    + "if(footer && card){footer.parentNode.insertBefore(card, footer);"
                    + "card.style.marginBottom='3rem';window.scrollTo({top:card.offsetTop-100,behavior:'smooth'});}"); 
            pw.println("var links=document.querySelectorAll('nav button, .main .menu a');"
                    + "links.forEach(l=>{if(l.innerText.trim().toLowerCase().includes('search train')){"
                    + "l.style.background='white';l.style.color='#003366';l.style.fontWeight='600';}});");
            pw.println("})();");
            pw.println("</script>");

            pw.flush();

        } catch (Exception e) {
            throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
        }
    }
}
