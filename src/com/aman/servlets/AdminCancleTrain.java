package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.aman.beans.TrainException;
import com.aman.constant.ResponseCode;
import com.aman.constant.UserRole;
import com.aman.service.TrainService;
import com.aman.service.impl.TrainServiceImpl;
import com.aman.utility.TrainUtil;

@WebServlet("/admincancletrain")
public class AdminCancleTrain extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private TrainService trainService = new TrainServiceImpl();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);

		try {
			String trainNo = req.getParameter("trainno");
			String message = trainService.deleteTrainById(trainNo);

			// Include main delete train page
			RequestDispatcher rd = req.getRequestDispatcher("CancleTrain.html");
			rd.include(req, res);

			if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(message)) {

				// ✅ SUCCESS message card
				pw.println("<div id='deleteSuccessMsg' style='max-width:650px;margin:1rem auto;"
						+ "background-color:#e9f9ee;border-left:6px solid #28a745;border-radius:8px;"
						+ "padding:15px 20px;text-align:center;font-size:1rem;color:#155724;font-weight:600;'>"
						+ "✅ Train number " + trainNo + " has been Deleted Successfully."
						+ "</div>");

				// Move message before footer
				pw.println("<script>"
						+ "(function(){"
						+ "var footer=document.querySelector('footer');"
						+ "var msg=document.getElementById('deleteSuccessMsg');"
						+ "if(footer && msg){"
						+ "footer.parentNode.insertBefore(msg, footer);"
						+ "msg.style.marginBottom='2rem';"
						+ "}"
						+ "})();"
						+ "</script>");

			} else {

				// ❌ ERROR message card
				pw.println("<div id='deleteErrorMsg' style='max-width:650px;margin:1rem auto;"
						+ "background-color:#ffeaea;border-left:6px solid #dc3545;border-radius:8px;"
						+ "padding:15px 20px;text-align:center;font-size:1rem;color:#a30000;font-weight:600;'>"
						+ "⚠️ Train No. " + trainNo + " is Not Available!"
						+ "</div>");

				pw.println("<script>"
						+ "(function(){"
						+ "var footer=document.querySelector('footer');"
						+ "var msg=document.getElementById('deleteErrorMsg');"
						+ "if(footer && msg){"
						+ "footer.parentNode.insertBefore(msg, footer);"
						+ "msg.style.marginBottom='2rem';"
						+ "}"
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
