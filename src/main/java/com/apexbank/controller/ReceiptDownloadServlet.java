package com.apexbank.controller;

import com.apexbank.model.Receipt;
import com.apexbank.util.PDFReceiptGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller providing binary PDF download for ATM receipts.
 */
@WebServlet(name = "ReceiptDownloadServlet", urlPatterns = {"/atm/receipt/pdf"})
public class ReceiptDownloadServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Receipt receipt = (Receipt) req.getSession().getAttribute("LAST_RECEIPT");

        if (receipt == null) {
            redirect(resp, req.getContextPath() + "/atm/dashboard");
            return;
        }

        resp.setContentType("application/pdf");
        String filename = "ATM_Receipt_" + receipt.getReceiptNumber() + ".pdf";
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try {
            PDFReceiptGenerator.generateAtmReceipt(receipt, resp.getOutputStream());
            resp.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to render PDF: " + e.getMessage());
        }
    }
}
