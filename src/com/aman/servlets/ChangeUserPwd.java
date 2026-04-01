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
import com.aman.beans.UserBean;
import com.aman.constant.UserRole;
import com.aman.service.UserService;
import com.aman.service.impl.UserServiceImpl;
import com.aman.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/changeuserpwd")
public class ChangeUserPwd extends HttpServlet {

    private UserService userService = new UserServiceImpl(UserRole.CUSTOMER);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        UserBean currentUser = TrainUtil.getCurrentCustomer(req);

        try {
            String email = req.getParameter("username");
            String oldPwd = req.getParameter("oldpassword");
            String newPwd = req.getParameter("newpassword");

            if (email != null) email = email.trim();
            if (oldPwd != null) oldPwd = oldPwd.trim();
            if (newPwd != null) newPwd = newPwd.trim();

            currentUser = userService.getUserByEmailId(email);

            if (currentUser == null || currentUser.getPWord() == null) {
                pw.println("<div class='tab red'>Invalid user credentials!</div>");
                return;
            }

            if (currentUser.getPWord().equals(oldPwd)) {
                currentUser.setPWord(newPwd);
                String message = userService.updateUser(currentUser);

                if ("SUCCESS".equalsIgnoreCase(message)) {
                    // logout to clear old session
                    TrainUtil.logout(res);

                    // Include the login page first
                    RequestDispatcher rd = req.getRequestDispatcher("UserLogin.html");
                    rd.include(req, res);

                    // JavaScript to insert the message inside the login card
                    pw.println("<script>");
                    pw.println("setTimeout(function(){"
                            + "var card=document.querySelector('.login-card, form, div[style*=\"border-radius\"]');"
                            + "if(card){"
                            + "var msg=document.createElement('div');"
                            + "msg.style.marginTop='15px';"
                            + "msg.style.padding='12px 16px';"
                            + "msg.style.borderRadius='6px';"
                            + "msg.style.textAlign='center';"
                            + "msg.style.fontSize='0.95rem';"
                            + "msg.style.background='#e9f9ee';"
                            + "msg.style.borderLeft='5px solid #28a745';"
                            + "msg.style.color='#155724';"
                            + "msg.innerHTML='<strong>✅ Your password has been updated successfully!</strong><br>Please login again.';"
                            + "card.appendChild(msg);"
                            + "}"
                            + "},400);");
                    pw.println("</script>");

                } else {
                    pw.println("<div class='tab red'>Update failed: " + message + "</div>");
                }

            } else {
                pw.println("<div class='tab red'>Wrong old password entered!</div>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
        }
    }
}
