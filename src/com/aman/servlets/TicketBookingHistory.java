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

import com.aman.beans.HistoryBean;
import com.aman.beans.TrainException;
import com.aman.constant.UserRole;
import com.aman.service.BookingService;
import com.aman.service.impl.BookingServiceImpl;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/bookingdetails")
public class TicketBookingHistory extends HttpServlet {

    BookingService bookingService = new BookingServiceImpl();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

        try {
            String customerId = TrainUtil.getCurrentUserEmail(req);
            List<HistoryBean> details = bookingService.getAllBookingsByCustomerId(customerId);

            // include full layout (navbar, header, footer)
            RequestDispatcher rd = req.getRequestDispatcher("UserViewTrains.html");
            rd.include(req, res);

            // Booking History Card
            pw.println("<div id='bookingHistoryCard' style='max-width:950px;margin:3rem auto 2rem auto;"
                    + "background-color:white;border-radius:12px;box-shadow:0 6px 20px rgba(0,0,0,0.15);padding:25px;'>");

            pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1.5rem;'>Booked Ticket History</h2>");

            if (details != null && !details.isEmpty()) {

                pw.println("<div style='overflow-x:auto;'>");
                pw.println("<table style='width:100%;border-collapse:collapse;font-size:0.95rem;'>");
                pw.println("<thead style='background-color:#00264d;color:white;'>"
                        + "<tr>"
                        + "<th style='padding:10px;'>Transaction ID</th>"
                        + "<th style='padding:10px;'>Train Number</th>"
                        + "<th style='padding:10px;'>From Station</th>"
                        + "<th style='padding:10px;'>To Station</th>"
                        + "<th style='padding:10px;'>Journey Date</th>"
                        + "<th style='padding:10px;'>Seat</th>"
                        + "<th style='padding:10px;'>Amount Paid</th>"
                        + "</tr></thead><tbody>");

                for (HistoryBean trans : details) {
                    pw.println("<tr style='border-bottom:1px solid #ddd;text-align:center;'>"
                            + "<td style='padding:10px;'>" + trans.getTransId() + "</td>"
                            + "<td style='padding:10px;'>" + trans.getTr_no() + "</td>"
                            + "<td style='padding:10px;'>" + trans.getFrom_stn() + "</td>"
                            + "<td style='padding:10px;'>" + trans.getTo_stn() + "</td>"
                            + "<td style='padding:10px;'>" + trans.getDate() + "</td>"
                            + "<td style='padding:10px;'>" + trans.getSeats() + "</td>"
                            + "<td style='padding:10px;'>" + trans.getAmount() + "</td>"
                            + "</tr>");
                }

                pw.println("</tbody></table></div>");
            } else {
                pw.println("<p style='text-align:center;color:#c00;font-weight:500;'>"
                        + "No tickets booked yet! Book your first ticket now 🚆"
                        + "</p>");
            }

            pw.println("</div>"); // close card

            // Inject JS: hide "Available Trains" + move card before footer + highlight navbar
            pw.println("<script>");
            pw.println("(function(){");
            // Hide old Available Trains header if present
            pw.println("var header=document.querySelector('.section-header');if(header){header.style.display='none';}");
            // Move booking card before footer
            pw.println("var card=document.getElementById('bookingHistoryCard');var footer=document.querySelector('footer');"
                    + "if(footer&&card){footer.parentNode.insertBefore(card,footer);card.style.marginBottom='3rem';}");
            // Highlight active navbar
            pw.println("var links=document.querySelectorAll('nav button, .main .menu a');"
                    + "links.forEach(l=>{if(l.innerText.trim().toLowerCase().includes('booking history')){"
                    + "l.style.background='white';l.style.color='#003366';l.style.fontWeight='600';}});");
            pw.println("})();");
            pw.println("</script>");

            pw.flush();

        } catch (Exception e) {
            throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
        }
    }
}
