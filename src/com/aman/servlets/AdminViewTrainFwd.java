package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
@WebServlet("/adminviewtrainfwd")
public class AdminViewTrainFwd extends HttpServlet {

    private TrainService trainService = new TrainServiceImpl();

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();
        TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);

        try {
            List<TrainBean> trains = trainService.getAllTrains();

            // Include base admin layout
            RequestDispatcher rd = req.getRequestDispatcher("ViewTrains.html");
            rd.include(req, res);

            if (trains != null && !trains.isEmpty()) {

                // ✅ Reduced top margin to 0.2rem for near zero gap
                pw.println("<div id='adminRunningTrainsCard' style='max-width:1000px;"
                        + "margin:0.2rem auto 2rem auto;"
                        + "background-color:white;border-radius:12px;"
                        + "box-shadow:0 6px 15px rgba(0,0,0,0.1);padding:20px;'>");

                pw.println("<div style='overflow-x:auto;'>");
                pw.println("<table style='width:100%;border-collapse:collapse;font-size:0.95rem;'>");
                pw.println("<thead style='background-color:#003366;color:white;'>"
                        + "<tr>"
                        + "<th style='padding:10px;'>Train Name</th>"
                        + "<th style='padding:10px;'>Train Number</th>"
                        + "<th style='padding:10px;'>From Station</th>"
                        + "<th style='padding:10px;'>To Station</th>"
                        + "<th style='padding:10px;'>Seats Available</th>"
                        + "<th style='padding:10px;'>Fare (INR)</th>"
                        + "<th style='padding:10px;'>Action</th>"
                        + "</tr></thead><tbody>");

                for (TrainBean train : trains) {
                    pw.println("<tr style='text-align:center;border-bottom:1px solid #ddd;'>"
                            + "<td style='padding:8px;'><a href='viewadmin?trainNo=" + train.getTr_no()
                            + "&fromStn=" + train.getFrom_stn() + "&toStn=" + train.getTo_stn()
                            + "' style='color:#007bff;text-decoration:none;'>" + train.getTr_name() + "</a></td>"
                            + "<td style='padding:8px;'>" + train.getTr_no() + "</td>"
                            + "<td style='padding:8px;'>" + train.getFrom_stn() + "</td>"
                            + "<td style='padding:8px;'>" + train.getTo_stn() + "</td>"
                            + "<td style='padding:8px;'>" + train.getSeats() + "</td>"
                            + "<td style='padding:8px;'>" + train.getFare() + " RS</td>"
                            + "<td style='padding:8px;'><a href='adminupdatetrain?trainnumber=" + train.getTr_no()
                            + "' style='color:#28a745;font-weight:600;'>Update</a></td>"
                            + "</tr>");
                }

                pw.println("</tbody></table></div></div>");

                // ✅ Move card before footer, now with tighter spacing
                pw.println("<script>"
                        + "(function(){"
                        + "var footer=document.querySelector('footer');"
                        + "var card=document.getElementById('adminRunningTrainsCard');"
                        + "if(footer && card){"
                        + "footer.parentNode.insertBefore(card, footer);"
                        + "card.style.marginTop='0.3rem';"
                        + "card.style.marginBottom='1.8rem';"
                        + "}"
                        + "})();"
                        + "</script>");

            } else {
                pw.println("<div id='noTrainsMsg' style='text-align:center;margin:1.5rem auto;"
                        + "color:#dc3545;font-weight:600;font-size:1rem;'>No Running Trains Available!</div>");
                pw.println("<script>"
                        + "(function(){var f=document.querySelector('footer');var c=document.getElementById('noTrainsMsg');"
                        + "if(f&&c){f.parentNode.insertBefore(c,f);} })();"
                        + "</script>");
            }

        } catch (Exception e) {
            throw new TrainException(422,
                    this.getClass().getName() + "_FAILED",
                    e.getMessage());
        }
    }
}
