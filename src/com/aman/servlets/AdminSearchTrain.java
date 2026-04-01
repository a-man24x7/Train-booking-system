package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.aman.beans.TrainBean;
import com.aman.beans.TrainException;
import com.aman.constant.UserRole;
import com.aman.service.TrainService;
import com.aman.service.impl.TrainServiceImpl;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/adminsearchtrain")
public class AdminSearchTrain extends HttpServlet {

    private TrainService trainService = new TrainServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();
        TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);

        try {
            String trainNo = req.getParameter("trainnumber");
            TrainBean train = trainService.getTrainById(trainNo);

            // Include the base search page
            RequestDispatcher rd = req.getRequestDispatcher("AdminSearchTrain.html");
            rd.include(req, res);

            if (train != null) {

                // ✅ Beautiful styled card for "Searched Train Detail"
                pw.println("<div id='searchedTrainCard' style='max-width:800px;"
                        + "margin:1rem auto 2.5rem auto;background-color:white;"
                        + "border-radius:12px;box-shadow:0 6px 20px rgba(0,0,0,0.1);padding:25px;'>");

                pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1rem;'>Searched Train Detail</h2>");
                pw.println("<table style='width:100%;border-collapse:collapse;font-size:0.95rem;'>");
                pw.println("<tr><td style='padding:8px;font-weight:600;color:#003366;'>Train Name :</td>"
                        + "<td>" + train.getTr_name() + "</td></tr>");
                pw.println("<tr><td style='padding:8px;font-weight:600;color:#003366;'>Train Number :</td>"
                        + "<td>" + train.getTr_no() + "</td></tr>");
                pw.println("<tr><td style='padding:8px;font-weight:600;color:#003366;'>From Station :</td>"
                        + "<td>" + train.getFrom_stn() + "</td></tr>");
                pw.println("<tr><td style='padding:8px;font-weight:600;color:#003366;'>To Station :</td>"
                        + "<td>" + train.getTo_stn() + "</td></tr>");
                pw.println("<tr><td style='padding:8px;font-weight:600;color:#003366;'>Available Seats :</td>"
                        + "<td>" + train.getSeats() + "</td></tr>");
                pw.println("<tr><td style='padding:8px;font-weight:600;color:#003366;'>Fare (INR) :</td>"
                        + "<td>" + train.getFare() + " RS</td></tr>");
                pw.println("</table></div>");

                // ✅ JavaScript to move the card above footer
                pw.println("<script>"
                        + "(function(){"
                        + "var footer=document.querySelector('footer');"
                        + "var card=document.getElementById('searchedTrainCard');"
                        + "if(footer && card){"
                        + "footer.parentNode.insertBefore(card, footer);"
                        + "card.style.marginTop='0.5rem';"
                        + "card.style.marginBottom='2rem';"
                        + "}"
                        + "})();"
                        + "</script>");

            } else {
                // ❌ When no train is found
                pw.println("<div id='noTrainCard' style='max-width:600px;margin:1rem auto;"
                        + "background-color:#fff3f3;color:#b30000;"
                        + "border-left:6px solid #e60000;border-radius:8px;"
                        + "padding:15px 20px;text-align:center;font-weight:600;'>"
                        + "Train No. " + trainNo + " is not available!</div>");

                pw.println("<script>"
                        + "(function(){"
                        + "var footer=document.querySelector('footer');"
                        + "var msg=document.getElementById('noTrainCard');"
                        + "if(footer && msg){footer.parentNode.insertBefore(msg, footer);}"
                        + "})();"
                        + "</script>");
            }

        } catch (Exception e) {
            throw new TrainException(422,
                    this.getClass().getName() + "_FAILED",
                    e.getMessage());
        }
    }
}
