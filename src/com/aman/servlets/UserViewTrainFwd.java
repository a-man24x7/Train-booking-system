package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
@WebServlet("/userviewtrainfwd")
public class UserViewTrainFwd extends HttpServlet {

    TrainService trainService = new TrainServiceImpl();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();
        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

        try {
            List<TrainBean> trains = trainService.getAllTrains();

            RequestDispatcher rd = req.getRequestDispatcher("UserViewTrains.html");
            rd.include(req, res);

            if (trains != null && !trains.isEmpty()) {
                // 🔹 Use JavaScript injection to display table inside the main section
                pw.println("<script>");
                pw.println("const container = document.getElementById('dynamicTrainsSection');");
                pw.println("if (container) {");
                pw.println("  container.innerHTML = `");
               
                pw.println("<div class='table-container'>");
                pw.println("<table class='train-table'>");
                pw.println("<thead><tr>"
                        + "<th>Train Name</th>"
                        + "<th>Train Number</th>"
                        + "<th>From Station</th>"
                        + "<th>To Station</th>"
                        + "<th>Time</th>"
                        + "<th>Seats Available</th>"
                        + "<th>Fare (INR)</th>"
                        + "<th>Booking</th>"
                        + "</tr></thead><tbody>");

                for (TrainBean train : trains) {
                    int hr = (int) (Math.random() * 24);
                    int min = (int) (Math.random() * 60);
                    String time = (hr < 10 ? ("0" + hr) : hr) + ":" + ((min < 10) ? "0" + min : min);

                    pw.println("<tr>"
                            + "<td><a href='view?trainNo=" + train.getTr_no() + "&fromStn=" + train.getFrom_stn()
                            + "&toStn=" + train.getTo_stn() + "'>" + train.getTr_name() + "</a></td>"
                            + "<td>" + train.getTr_no() + "</td>"
                            + "<td>" + train.getFrom_stn() + "</td>"
                            + "<td>" + train.getTo_stn() + "</td>"
                            + "<td>" + time + "</td>"
                            + "<td>" + train.getSeats() + "</td>"
                            + "<td>" + train.getFare() + " RS</td>"
                            + "<td><a href='booktrainbyref?trainNo=" + train.getTr_no()
                            + "&fromStn=" + train.getFrom_stn() + "&toStn=" + train.getTo_stn()
                            + "'>Book Now</a></td>"
                            + "</tr>");
                }

                pw.println("</tbody></table></div>`;");
                pw.println("}");
                pw.println("</script>");
            } else {
                // No trains case
                pw.println("<script>");
                pw.println("const container = document.getElementById('dynamicTrainsSection');");
                pw.println("if (container) {");
                pw.println("container.innerHTML = `<p style='color:#c0392b; font-weight:600; margin-top:1rem;'>No Running Trains Available!</p>`;");
                pw.println("}");
                pw.println("</script>");
            }

        } catch (Exception e) {
            throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
        }
    }
}
