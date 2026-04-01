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
@WebServlet("/fareenq")
public class FareEnq extends HttpServlet {
	TrainService trainService = new TrainServiceImpl();

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();

		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

		try {
			String fromStation = req.getParameter("fromstation");
			String toStation = req.getParameter("tostation");
			List<TrainBean> trains = trainService.getTrainsBetweenStations(fromStation, toStation);

			// Include main layout
			RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
			rd.include(req, res);

			// Card container for fare enquiry results
			pw.println("<div id='fareCard' style='max-width:950px;margin:3rem auto;"
					+ "background-color:white;border-radius:12px;box-shadow:0 6px 20px rgba(0,0,0,0.15);padding:25px;'>");

			if (trains != null && !trains.isEmpty()) {

				pw.println("<h2 style='text-align:center;color:#00264d;margin-bottom:1rem;'>"
						+ "Fare for Trains Between Stations: " + fromStation + " to " + toStation + "</h2>");
				pw.println("<p style='text-align:center;color:#555;margin-bottom:1.5rem;'>Below are the available trains and fares for your selected route.</p>");

				pw.println("<div style='overflow-x:auto;'>");
				pw.println("<table style='width:100%;border-collapse:collapse;font-size:0.95rem;'>");
				pw.println("<thead style='background-color:#00264d;color:white;'>"
						+ "<tr><th style='padding:10px;'>Train Name</th><th style='padding:10px;'>Train No</th>"
						+ "<th style='padding:10px;'>From Stn</th><th style='padding:10px;'>To Stn</th>"
						+ "<th style='padding:10px;'>Time</th><th style='padding:10px;'>Seats</th>"
						+ "<th style='padding:10px;'>Fare (INR)</th><th style='padding:10px;'>Action</th></tr></thead><tbody>");

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
							+ "<td style='padding:10px;'>"
							+ "<a href='booktrainbyref?trainNo=" + train.getTr_no() + "&fromStn="
							+ train.getFrom_stn() + "&toStn=" + train.getTo_stn()
							+ "' style='background-color:#00a8e8;color:white;padding:6px 14px;"
							+ "border-radius:6px;text-decoration:none;font-weight:600;'>Book Now</a>"
							+ "</td></tr>");
				}

				pw.println("</tbody></table></div>");
			} else {
				pw.println("<p style='text-align:center;color:#c00;font-weight:500;'>"
						+ "No trains available between " + fromStation + " and " + toStation + "!</p>");
			}

			pw.println("</div>"); // close card

			// Inject JavaScript: Move fare card above footer and highlight navbar
			pw.println("<script>");
			pw.println("(function(){");
			// Hide header section if any
			pw.println("var header=document.querySelector('.section-header');if(header){header.style.display='none';}");
			// Move the card before footer
			pw.println("var footer=document.querySelector('footer');var card=document.getElementById('fareCard');"
					+ "if(footer&&card){footer.parentNode.insertBefore(card,footer);card.style.marginBottom='3rem';}");
			// Highlight the Fare Enquiry navbar item
			pw.println("var links=document.querySelectorAll('nav button, .main .menu a');"
					+ "links.forEach(l=>{if(l.innerText.trim().toLowerCase().includes('fare enquiry')){"
					+ "l.style.background='white';l.style.color='#003366';l.style.fontWeight='600';}});");
			pw.println("})();");
			pw.println("</script>");

			pw.flush();

		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}
	}
}
