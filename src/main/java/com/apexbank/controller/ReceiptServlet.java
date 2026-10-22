package com.apexbank.controller;

import com.apexbank.model.Receipt;
import com.apexbank.util.CurrencyUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller displaying the realistic ATM paper thermal receipt.
 */
@WebServlet(name = "ReceiptServlet", urlPatterns = {"/atm/receipt"})
public class ReceiptServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Receipt receipt = (Receipt) req.getSession().getAttribute("LAST_RECEIPT");

        if (receipt == null) {
            redirect(resp, req.getContextPath() + "/atm/dashboard");
            return;
        }

        req.setAttribute("receipt", receipt);
        req.setAttribute("formattedAmount", CurrencyUtil.format(receipt.getAmount()));
        req.setAttribute("formattedBalance", CurrencyUtil.format(receipt.getAvailableBalance()));
        req.setAttribute("amountInWords", CurrencyUtil.convertToWords(receipt.getAmount()));

        forward(req, resp, "atm/receipt");
    }
}
