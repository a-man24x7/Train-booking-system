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
import com.aman.service.TrainService;
import com.aman.service.impl.TrainServiceImpl;

@SuppressWarnings("serial")
@WebServlet("/adminupdatetrain")
public class AdminTrainUpdate extends HttpServlet {

    private TrainService trainService = new TrainServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        try {
            String trainNo = req.getParameter("trainnumber");
            TrainBean train = trainService.getTrainById(trainNo);

            if (train != null) {
                RequestDispatcher rd = req.getRequestDispatcher("AdminHome.html");
                rd.include(req, res);

                // ✅ Card Container
                pw.println("<div id='trainUpdateCard' "
                        + "style='max-width:800px;margin:2rem auto;"
                        + "background-color:#ffffff;border-radius:12px;"
                        + "box-shadow:0 8px 24px rgba(0,0,0,0.15);padding:25px;'>");

                pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1.5rem;'>"
                        + "Train Schedule Update</h2>");

                // ✅ Begin Form
                pw.println("<form action='updatetrainschedule' method='post' "
                        + "style='width:100%;font-size:0.95rem;'>");

                pw.println("<table style='width:100%;border-collapse:collapse;'>");

                pw.println("<tr><td style='padding:10px;font-weight:600;'>Train No :</td>"
                        + "<td><input type='text' name='trainno' value='" + train.getTr_no()
                        + "' readonly style='width:100%;padding:8px;border-radius:6px;border:1px solid #ccc;'></td></tr>");

                pw.println("<tr><td style='padding:10px;font-weight:600;'>Train Name :</td>"
                        + "<td><input type='text' name='trainname' value='" + train.getTr_name()
                        + "' style='width:100%;padding:8px;border-radius:6px;border:1px solid #ccc;'></td></tr>");

                pw.println("<tr><td style='padding:10px;font-weight:600;'>From Station :</td>"
                        + "<td><input type='text' name='fromstation' value='" + train.getFrom_stn()
                        + "' style='width:100%;padding:8px;border-radius:6px;border:1px solid #ccc;'></td></tr>");

                pw.println("<tr><td style='padding:10px;font-weight:600;'>To Station :</td>"
                        + "<td><input type='text' name='tostation' value='" + train.getTo_stn()
                        + "' style='width:100%;padding:8px;border-radius:6px;border:1px solid #ccc;'></td></tr>");

                pw.println("<tr><td style='padding:10px;font-weight:600;'>Available Seats :</td>"
                        + "<td><input type='text' name='available' value='" + train.getSeats()
                        + "' style='width:100%;padding:8px;border-radius:6px;border:1px solid #ccc;'></td></tr>");

                pw.println("<tr><td style='padding:10px;font-weight:600;'>Fare (INR) :</td>"
                        + "<td><input type='text' name='fare' value='" + train.getFare()
                        + "' style='width:100%;padding:8px;border-radius:6px;border:1px solid #ccc;'></td></tr>");

                pw.println("<tr><td></td><td style='text-align:right;padding-top:15px;'>"
                        + "<input type='submit' name='submit' value='Update Train Schedule' "
                        + "style='background-color:#0078d7;color:white;padding:10px 20px;"
                        + "border:none;border-radius:6px;font-weight:600;cursor:pointer;'>"
                        + "</td></tr>");

                pw.println("</table>");
                pw.println("</form>");

                // ✅ Move success message BELOW the button
                String updateStatus = req.getParameter("updateStatus");
                if ("success".equalsIgnoreCase(updateStatus)) {
                    pw.println("<div style='background-color:#e8f9ee;border-left:5px solid #28a745;"
                            + "color:#155724;padding:10px 15px;margin-top:20px;"
                            + "border-radius:6px;text-align:center;font-weight:600;'>"
                            + "✅ Train Updated Successfully!</div>");
                } else if ("failed".equalsIgnoreCase(updateStatus)) {
                    pw.println("<div style='background-color:#ffeaea;border-left:5px solid #dc3545;"
                            + "color:#a30000;padding:10px 15px;margin-top:20px;"
                            + "border-radius:6px;text-align:center;font-weight:600;'>"
                            + "⚠️ Error updating train. Please try again.</div>");
                }

                pw.println("</div>"); // close card

                // ✅ Move card before footer
                pw.println("<script>"
                        + "(function(){"
                        + "var footer=document.querySelector('footer');"
                        + "var card=document.getElementById('trainUpdateCard');"
                        + "if(footer && card){"
                        + "footer.parentNode.insertBefore(card, footer);"
                        + "card.style.marginBottom='2.5rem';"
                        + "window.scrollTo({top: card.offsetTop - 100, behavior:'smooth'});"
                        + "}"
                        + "})();"
                        + "</script>");

            } else {
                RequestDispatcher rd = req.getRequestDispatcher("AdminUpdateTrain.html");
                rd.include(req, res);
                pw.println("<div id='noTrainMsg' style='text-align:center;margin:2rem auto;"
                        + "color:#dc3545;font-weight:600;'>Train Not Available!</div>");
                pw.println("<script>"
                        + "(function(){var f=document.querySelector('footer');var c=document.getElementById('noTrainMsg');"
                        + "if(f&&c){f.parentNode.insertBefore(c,f);}})();"
                        + "</script>");
            }

        } catch (Exception e) {
            throw new TrainException(422,
                    this.getClass().getName() + "_FAILED",
                    e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        doPost(req, res);
    }
}
