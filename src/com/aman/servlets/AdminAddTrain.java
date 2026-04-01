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
import com.aman.constant.ResponseCode;
import com.aman.constant.UserRole;
import com.aman.service.TrainService;
import com.aman.service.impl.TrainServiceImpl;
import com.aman.utility.TrainUtil;

@WebServlet("/adminaddtrain")
public class AdminAddTrain extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TrainService trainService = new TrainServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();
        TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);

        try {
            String trainNoParam = req.getParameter("trainno");

            // ✅ Validate that Train Number is numeric
            if (trainNoParam == null || !trainNoParam.matches("\\d+")) {
                RequestDispatcher rd = req.getRequestDispatcher("AddTrains.html");
                rd.include(req, res);
                pw.println("<script>"
                        + "setTimeout(function(){"
                        + "var card=document.querySelector('form')?.parentElement;"
                        + "if(card){"
                        + "var msg=document.createElement('div');"
                        + "msg.style.marginTop='15px';"
                        + "msg.style.padding='12px 16px';"
                        + "msg.style.borderRadius='6px';"
                        + "msg.style.textAlign='center';"
                        + "msg.style.fontSize='0.95rem';"
                        + "msg.style.background='#fff3cd';"
                        + "msg.style.borderLeft='5px solid #ffc107';"
                        + "msg.style.color='#856404';"
                        + "msg.innerHTML='⚠️ Please enter a numeric Train Number!';"
                        + "card.appendChild(msg);"
                        + "}"
                        + "},300);"
                        + "</script>");
                return;
            }

            // ✅ Safe parsing after validation
            TrainBean train = new TrainBean();
            train.setTr_no(Long.parseLong(trainNoParam));
            train.setTr_name(req.getParameter("trainname").toUpperCase());
            train.setFrom_stn(req.getParameter("fromstation").toUpperCase());
            train.setTo_stn(req.getParameter("tostation").toUpperCase());
            train.setSeats(Integer.parseInt(req.getParameter("available")));
            train.setFare(Double.parseDouble(req.getParameter("fare")));

            // Add train
            String message = trainService.addTrain(train);
            RequestDispatcher rd = req.getRequestDispatcher("AddTrains.html");
            rd.include(req, res);

            if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(message)) {
                pw.println("<script>"
                        + "setTimeout(function(){"
                        + "var card=document.querySelector('form')?.parentElement;"
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
                        + "msg.innerHTML='✅ Train Added Successfully!';"
                        + "card.appendChild(msg);"
                        + "}"
                        + "},300);"
                        + "</script>");
            } else {
                pw.println("<script>"
                        + "setTimeout(function(){"
                        + "var card=document.querySelector('form')?.parentElement;"
                        + "if(card){"
                        + "var msg=document.createElement('div');"
                        + "msg.style.marginTop='15px';"
                        + "msg.style.padding='12px 16px';"
                        + "msg.style.borderRadius='6px';"
                        + "msg.style.textAlign='center';"
                        + "msg.style.fontSize='0.95rem';"
                        + "msg.style.background='#ffeaea';"
                        + "msg.style.borderLeft='5px solid #dc3545';"
                        + "msg.style.color='#a30000';"
                        + "msg.innerHTML='⚠️ Error adding train details!';"
                        + "card.appendChild(msg);"
                        + "}"
                        + "},300);"
                        + "</script>");
            }

        } catch (Exception e) {
            throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
        }
    }
}
