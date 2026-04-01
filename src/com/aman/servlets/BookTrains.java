package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.aman.beans.HistoryBean;
import com.aman.beans.TrainBean;
import com.aman.beans.TrainException;
import com.aman.constant.ResponseCode;
import com.aman.constant.UserRole;
import com.aman.service.BookingService;
import com.aman.service.TrainService;
import com.aman.service.impl.BookingServiceImpl;
import com.aman.service.impl.TrainServiceImpl;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/booktrains")
public class BookTrains extends HttpServlet {

    private TrainService trainService = new TrainServiceImpl();
    private BookingService bookingService = new BookingServiceImpl();

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");
        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

        RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
        rd.include(req, res);

        ServletContext sct = req.getServletContext();

        try {
            int seat = 0;
            Object seatsObj = sct.getAttribute("seats");
            if (seatsObj != null) seat = Integer.parseInt(String.valueOf(seatsObj));

            String trainNo = String.valueOf(sct.getAttribute("trainnumber"));
            String journeyDate = String.valueOf(sct.getAttribute("journeydate"));
            String seatClass = String.valueOf(sct.getAttribute("class"));
            String userMailId = TrainUtil.getCurrentUserEmail(req);
            String dateToStore = (journeyDate != null && !journeyDate.isBlank())
                    ? journeyDate.trim()
                    : java.time.LocalDate.now().toString();

            TrainBean train = trainService.getTrainById(trainNo);

            if (train != null) {
                int avail = train.getSeats();

                if (seat > avail) {
                    pw.println("<div id='bookFailCard' style='max-width:800px;margin:2rem auto;"
                            + "background-color:#fff5f5;border-radius:12px;box-shadow:0 4px 15px rgba(0,0,0,0.1);"
                            + "padding:20px;text-align:center;color:#b30000;font-weight:600;'>"
                            + "⚠️ Only " + avail + " Seats are Available in this Train!"
                            + "</div>");
                } else {
                    avail -= seat;
                    train.setSeats(avail);
                    String responseCode = trainService.updateTrain(train);

                    if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(responseCode)) {

                        HistoryBean bookingDetails = new HistoryBean();
                        bookingDetails.setAmount(train.getFare() * seat);
                        bookingDetails.setFrom_stn(train.getFrom_stn());
                        bookingDetails.setTo_stn(train.getTo_stn());
                        bookingDetails.setTr_no(trainNo);
                        bookingDetails.setSeats(seat);
                        bookingDetails.setMailId(userMailId);
                        bookingDetails.setDate(dateToStore);

                        HistoryBean transaction = bookingService.createHistory(bookingDetails);

                        // ✅ Booking Success Card with message at the bottom
                        pw.println("<div id='bookingSuccessCard' style='max-width:900px;margin:2rem auto;"
                                + "background-color:#ffffff;border-radius:15px;box-shadow:0 8px 24px rgba(0,0,0,0.15);"
                                + "padding:25px;'>");

                        pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1.5rem;'>"
                                + "Booking Details</h2>");

                        pw.println("<table style='width:100%;border-collapse:collapse;font-size:0.95rem;'>");

                        pw.println("<tr><td style='padding:8px;font-weight:600;'>Transaction ID:</td>"
                                + "<td colspan='3' style='color:#0066cc;'>" + transaction.getTransId() + "</td></tr>");

                        pw.println("<tr><td style='padding:8px;font-weight:600;'>PNR No:</td>"
                                + "<td colspan='3' style='color:#007bff;'>" + transaction.getTransId() + "</td></tr>");

                        pw.println("<tr><td style='padding:8px;font-weight:600;'>Train Name:</td><td>"
                                + train.getTr_name() + "</td>"
                                + "<td style='padding:8px;font-weight:600;'>Train No:</td><td>"
                                + transaction.getTr_no() + "</td></tr>");

                        pw.println("<tr><td style='padding:8px;font-weight:600;'>From Station:</td><td>"
                                + transaction.getFrom_stn() + "</td>"
                                + "<td style='padding:8px;font-weight:600;'>To Station:</td><td>"
                                + transaction.getTo_stn() + "</td></tr>");

                        pw.println("<tr><td style='padding:8px;font-weight:600;'>Date Of Journey:</td><td>"
                                + transaction.getDate() + "</td>"
                                + "<td style='padding:8px;font-weight:600;'>Time (HH:MM):</td><td>11:23</td></tr>");

                        pw.println("<tr><td style='padding:8px;font-weight:600;'>Passengers:</td><td>"
                                + transaction.getSeats() + "</td>"
                                + "<td style='padding:8px;font-weight:600;'>Class:</td><td>"
                                + seatClass + "</td></tr>");

                        pw.println("<tr><td style='padding:8px;font-weight:600;'>Booking Status:</td><td style='color:green;'>"
                                + "CNF/S10/35</td><td style='padding:8px;font-weight:600;'>Amount Paid:</td><td>₹ "
                                + transaction.getAmount() + "</td></tr>");

                        pw.println("</table>");

                        // ✅ Success message below the table
                        pw.println("<div style='background-color:#e8f9ee;border-left:5px solid #28a745;"
                                + "color:#155724;padding:10px 15px;margin-top:20px;"
                                + "border-radius:6px;text-align:center;font-weight:600;'>"
                                + "✅ " + seat + " Seats Booked Successfully!</div>");

                        pw.println("</div>");

                        // ✅ Move card before footer
                        pw.println("<script>"
                                + "(function(){"
                                + "var footer=document.querySelector('footer');"
                                + "var card=document.getElementById('bookingSuccessCard');"
                                + "if(footer && card){"
                                + "footer.parentNode.insertBefore(card, footer);"
                                + "card.style.marginBottom='3rem';"
                                + "window.scrollTo({top: card.offsetTop - 100, behavior:'smooth'});"
                                + "}"
                                + "})();"
                                + "</script>");

                    } else {
                        pw.println("<div id='bookFailCard' style='max-width:800px;margin:2rem auto;"
                                + "background-color:#fff5f5;border-radius:12px;box-shadow:0 4px 15px rgba(0,0,0,0.1);"
                                + "padding:20px;text-align:center;color:#b30000;font-weight:600;'>"
                                + "❌ Transaction Declined. Please Try Again!"
                                + "</div>");
                    }
                }
            } else {
                pw.println("<div id='bookFailCard' style='max-width:800px;margin:2rem auto;"
                        + "background-color:#fff5f5;border-radius:12px;box-shadow:0 4px 15px rgba(0,0,0,0.1);"
                        + "padding:20px;text-align:center;color:#b30000;font-weight:600;'>"
                        + "⚠️ Invalid Train Number!"
                        + "</div>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
        } finally {
            try {
                sct.removeAttribute("seats");
                sct.removeAttribute("trainnumber");
                sct.removeAttribute("journeydate");
                sct.removeAttribute("class");
            } catch (Exception ignore) {}
        }
    }
}
