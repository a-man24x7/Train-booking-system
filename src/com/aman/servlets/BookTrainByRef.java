package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.aman.constant.UserRole;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/booktrainbyref")
public class BookTrainByRef extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        // ✅ Validate user
        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

        String emailId = TrainUtil.getCurrentUserEmail(req);
        long trainNo = Long.parseLong(req.getParameter("trainNo"));
        int seat = 1;
        String fromStn = req.getParameter("fromStn");
        String toStn = req.getParameter("toStn");

        // ✅ Include full layout
        RequestDispatcher rd = req.getRequestDispatcher("UserViewTrains.html");
        rd.include(req, res);

        // ✅ Booking Card (styled + spaced nicely)
        pw.println("<div id='bookingCard' style='max-width:850px;margin:3.5rem auto 2rem auto;padding:25px;"
                + "background-color:white;border-radius:12px;box-shadow:0 6px 18px rgba(0,0,0,0.15);'>");

        pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1.5rem;'>Your Ticket Booking Information</h2>");
        pw.println("<form action='payment' method='post' style='font-size:0.95rem;'>");
        pw.println("<table style='width:100%;border-collapse:collapse;'>");

        pw.println("<tr>"
                + "<td style='padding:8px;'><strong>User ID:</strong></td><td style='padding:8px;'>" + emailId + "</td>"
                + "<td style='padding:8px;'><strong>Train No:</strong></td><td style='padding:8px;'>" + trainNo + "</td>"
                + "</tr>");

        pw.println("<tr>"
                + "<td style='padding:8px;'><strong>From Station:</strong></td><td style='padding:8px;'>" + fromStn + "</td>"
                + "<td style='padding:8px;'><strong>To Station:</strong></td><td style='padding:8px;'>" + toStn + "</td>"
                + "</tr>");

        pw.println("<tr>"
                + "<td style='padding:8px;'><strong>Journey Date:</strong></td>"
                + "<td style='padding:8px;'><input type='hidden' name='trainnumber' value='" + trainNo + "'>"
                + "<input type='date' name='journeydate' value='" + LocalDate.now() + "' "
                + "min='" + LocalDate.now() + "' required "
                + "style='padding:6px 8px;border-radius:6px;border:1px solid #ccc;'></td>"
                + "<td style='padding:8px;'><strong>No of Seats:</strong></td>"
                + "<td style='padding:8px;'><input type='number' name='seats' value='" + seat + "' min='1' "
                + "style='padding:6px 8px;border-radius:6px;border:1px solid #ccc;width:100px;'></td>"
                + "</tr>");

        pw.println("<tr>"
                + "<td style='padding:8px;'><strong>Select Class:</strong></td>"
                + "<td style='padding:8px;'><select name='class' required "
                + "style='padding:6px 8px;border-radius:6px;border:1px solid #ccc;'>"
                + "<option value='Sleeper(SL)'>Sleeper (SL)</option>"
                + "<option value='Second Sitting(2S)'>Second Sitting (2S)</option>"
                + "<option value='AC First Class(1A)'>AC First Class (1A)</option>"
                + "<option value='AC 2 Tier(2A)'>AC 2 Tier (2A)</option>"
                + "</select></td>"
                + "<td style='padding:8px;'><strong>Berth Preference:</strong></td>"
                + "<td style='padding:8px;'><select name='berth' "
                + "style='padding:6px 8px;border-radius:6px;border:1px solid #ccc;'>"
                + "<option value='NO'>No Preference</option>"
                + "<option value='LB'>Lower Berth (LB)</option>"
                + "<option value='UB'>Upper Berth (UB)</option>"
                + "<option value='C'>Cabin</option>"
                + "</select></td>"
                + "</tr>");

        pw.println("</table>");

        pw.println("<div style='text-align:center;margin-top:1.8rem;'>"
                + "<input type='submit' value='Pay And Book' "
                + "style='background-color:#00a8e8;color:white;border:none;"
                + "padding:10px 25px;border-radius:6px;font-weight:600;"
                + "cursor:pointer;transition:0.3s;'>"
                + "</div>");

        pw.println("</form>");
        pw.println("</div>");

        // ✅ JS — Hide header + move card slightly upward before footer
        pw.println("<script>");
        pw.println("(function(){");
        pw.println("  var sh = document.querySelector('.section-header'); if(sh){ sh.style.display='none'; }");
        pw.println("  var placeholder = document.getElementById('dynamicTrainsSection'); if(placeholder){ placeholder.style.display='none'; }");
        pw.println("  var booking = document.getElementById('bookingCard');");
        pw.println("  var footer = document.querySelector('footer');");
        pw.println("  if(booking && footer){ ");
        pw.println("      footer.parentNode.insertBefore(booking, footer);");
        // Add smooth scroll and spacing adjustment
        pw.println("      booking.style.marginBottom = '3rem';");
        pw.println("      booking.style.marginTop = '2.5rem';");
        pw.println("      window.scrollTo({ top: booking.offsetTop - 120, behavior: 'smooth' });");
        pw.println("  }");
        pw.println("})();");
        pw.println("</script>");

        pw.flush();
    }
}
