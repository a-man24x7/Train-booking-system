package com.aman.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aman.beans.TrainBean;
import com.aman.beans.TrainException;
import com.aman.service.TrainService;
import com.aman.service.impl.TrainServiceImpl;

@SuppressWarnings("serial")
@WebServlet("/updatetrainschedule")
public class UpdateTrainSchedule extends HttpServlet {

    private TrainService trainService = new TrainServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");

        try {
            TrainBean train = new TrainBean();
            train.setTr_no(Long.parseLong(req.getParameter("trainno")));
            train.setTr_name(req.getParameter("trainname"));
            train.setFrom_stn(req.getParameter("fromstation"));
            train.setTo_stn(req.getParameter("tostation"));
            train.setSeats(Integer.parseInt(req.getParameter("available")));
            train.setFare(Double.parseDouble(req.getParameter("fare")));

            String message = trainService.updateTrain(train);

            if ("SUCCESS".equalsIgnoreCase(message)) {
                // ✅ Redirect to adminupdatetrain with success flag
                res.sendRedirect("adminupdatetrain?trainnumber=" + train.getTr_no() + "&updateStatus=success");
            } else {
                res.sendRedirect("adminupdatetrain?trainnumber=" + train.getTr_no() + "&updateStatus=failed");
            }

        } catch (Exception e) {
            throw new TrainException(422,
                    this.getClass().getName() + "_FAILED",
                    e.getMessage());
        }
    }
}
