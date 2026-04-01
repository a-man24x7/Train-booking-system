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
@WebServlet("/trainbwstn")
public class TrainBwStn extends HttpServlet {
    private TrainService trainService = new TrainServiceImpl();

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();
        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

        try {
            String fromStation = req.getParameter("fromstation");
            String toStation = req.getParameter("tostation");
            List<TrainBean> trains = trainService.getTrainsBetweenStations(fromStation, toStation);

            // Include full page layout first
            RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
            rd.include(req, res);

            // Create the container card (positioned dynamically before footer)
            pw.println("<div id='trainResultsCard' style='max-width:950px;margin:3rem auto 2rem auto;"
                    + "background-color:white;border-radius:12px;box-shadow:0 6px 20px rgba(0,0,0,0.15);padding:25px;'>");

            if (trains != null && !trains.isEmpty()) {
                pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1rem;'>"
                        + "Trains Between Stations: " + fromStation + " ➜ " + toStation + "</h2>");
                pw.println("<p style='text-align:center;color:#555;margin-bottom:1.5rem;'>"
                        + "Below are the available trains between your selected stations.</p>");

                pw.println("<div style='overflow-x:auto;'>");
                pw.println("<table style='width:100%;border-collapse:collapse;font-size:0.95rem;'>");
                pw.println("<thead style='background-color:#00264d;color:white;'>"
                        + "<tr><th style='padding:10px;'>Train Name</th>"
                        + "<th style='padding:10px;'>Train No</th>"
                        + "<th style='padding:10px;'>From</th>"
                        + "<th style='padding:10px;'>To</th>"
                        + "<th style='padding:10px;'>Time</th>"
                        + "<th style='padding:10px;'>Seats</th>"
                        + "<th style='padding:10px;'>Fare (INR)</th>"
                        + "<th style='padding:10px;'>Action</th></tr></thead><tbody>");

                for (TrainBean train : trains) {
                    int hr = (int) (Math.random() * 24);
                    int min = (int) (Math.random() * 60);
                    String time = (hr < 10 ? ("0" + hr) : hr) + ":" + ((min < 10) ? "0" + min : min);

                    pw.println("<tr style='border-bottom:1px solid #ddd;text-align:center;'>"
                            + "<td style='padding:10px;color:#004c99;font-weight:500;'>" + train.getTr_name() + "</td>"
                            + "<td style='padding:10px;'>" + train.getTr_no() + "</td>"
                            + "<td style='padding:10px;'>" + train.getFrom_stn() + "</td>"
                            + "<td style='padding:10px;'>" + train.getTo_stn() + "</td>"
                            + "<td style='padding:10px;'>" + time + "</td>"
                            + "<td style='padding:10px;'>" + train.getSeats() + "</td>"
                            + "<td style='padding:10px;'>" + train.getFare() + " RS</td>"
                            + "<td style='padding:10px;'><a href='booktrainbyref?trainNo=" + train.getTr_no()
                            + "&fromStn=" + train.getFrom_stn() + "&toStn=" + train.getTo_stn()
                            + "' style='background-color:#00a8e8;color:white;padding:6px 14px;"
                            + "border-radius:6px;text-decoration:none;font-weight:600;'>Book Now</a></td></tr>");
                }

                pw.println("</tbody></table></div>");
            } else {
                pw.println("<h3 style='text-align:center;color:#c00;'>No trains available between "
                        + fromStation + " and " + toStation + ".</h3>");
            }

            pw.println("</div>"); // close card

            // Move card before footer and hide welcome area
            pw.println("<script>");
            pw.println("(function(){");
            pw.println("var welcome = document.querySelector('.welcome-section'); if(welcome){ welcome.style.display='none'; }");
            pw.println("var footer = document.querySelector('footer'); var card = document.getElementById('trainResultsCard');");
            pw.println("if(footer && card){ footer.parentNode.insertBefore(card, footer); card.style.marginBottom='3rem'; window.scrollTo({top: card.offsetTop-120, behavior:'smooth'}); }");
            pw.println("})();");
            pw.println("</script>");

            pw.flush();

        } catch (Exception e) {
            throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
        }
    }
}
