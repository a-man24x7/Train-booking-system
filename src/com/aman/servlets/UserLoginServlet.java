package com.aman.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aman.constant.ResponseCode;
import com.aman.constant.UserRole;
import com.aman.utility.TrainUtil;

@WebServlet("/userlogin")
public class UserLoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		PrintWriter pw = res.getWriter();
		res.setContentType("text/html");

		String uName = req.getParameter("uname");
		String pWord = req.getParameter("pword");

		String responseMsg = TrainUtil.login(req, res, UserRole.CUSTOMER, uName, pWord);

		// ✅ Successful login
		if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(responseMsg)) {

			RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
			rd.include(req, res);

			pw.println("<script>");
			pw.println("const msgBox = document.createElement('div');");
			pw.println("msgBox.className = 'welcome-message';");
			pw.println("msgBox.style.textAlign = 'center';");
			pw.println("msgBox.style.margin = '30px auto';");
			pw.println("msgBox.style.maxWidth = '800px';");
			pw.println("msgBox.style.backgroundColor = '#e6f9ff';");
			pw.println("msgBox.style.borderLeft = '4px solid #00a8e8';");
			pw.println("msgBox.style.padding = '20px';");
			pw.println("msgBox.style.borderRadius = '10px';");
			pw.println("msgBox.style.boxShadow = '0 4px 10px rgba(0,0,0,0.1)';");
			pw.println("msgBox.style.fontSize = '15px';");
			pw.println("msgBox.innerHTML = "
					+ "'<strong>Hello " + uName
					+ "!</strong> Welcome to our new E-Train Website.<br><br>"
					+ "Hello " + uName
					+ ", good to see you here.<br> Here you can check train details, schedules, fare enquiries, and more.<br>"
					+ "Just explore the menu links above and make the most of your travel experience.<br><br>"
					+ "<em>Thanks for being connected with us!</em>';");

			pw.println("const footer = document.querySelector('footer');");
			pw.println("footer.parentNode.insertBefore(msgBox, footer);");
			pw.println("</script>");

		} 
		// ❌ Invalid login
		else {
			RequestDispatcher rd = req.getRequestDispatcher("UserLogin.html");
			rd.include(req, res);

			pw.println("<script>");
			pw.println("const msgBox = document.getElementById('logoutMessageContainer');");
			pw.println("if (msgBox) {");
			pw.println("  msgBox.textContent = '" + responseMsg.replace("'", "\\'") + "';");
			pw.println("  msgBox.style.display = 'block';");
			pw.println("  msgBox.style.backgroundColor = '#ffe6e6';");
			pw.println("  msgBox.style.borderLeft = '4px solid #e74c3c';");
			pw.println("  msgBox.style.color = '#b30000';");
			pw.println("}");
			pw.println("</script>");
		}
	}
}
