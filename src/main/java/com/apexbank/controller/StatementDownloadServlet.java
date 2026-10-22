package com.apexbank.controller;

import com.apexbank.model.Account;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Customer;
import com.apexbank.model.Transaction;
import com.apexbank.service.ATMService;
import com.apexbank.service.ServiceFactory;
import com.apexbank.util.PDFReceiptGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller providing binary PDF download for Account Statements.
 */
@WebServlet(name = "StatementDownloadServlet", urlPatterns = {"/atm/statement/pdf"})
public class StatementDownloadServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ATMService atmService = ServiceFactory.getATMService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ATMCard card = getSessionCard(req);
        Account account = getSessionAccount(req);
        Customer customer = getSessionCustomer(req);

        if (card == null || account == null) {
            redirect(resp, req.getContextPath() + "/login");
            return;
        }

        String type = req.getParameter("type");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String search = req.getParameter("search");

        resp.setContentType("application/pdf");
        String filename = "Statement_" + account.getAccountNumber() + ".pdf";
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try {
            List<Transaction> txns = atmService.getFullStatement(card.getCardNumber(), type, startDate, endDate, search);
            PDFReceiptGenerator.generateStatementPdf(
                    account.getAccountNumber(),
                    customer != null ? customer.getFullName() : "Valued Customer",
                    account.getBalance(),
                    txns,
                    resp.getOutputStream()
            );
            resp.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to render statement PDF: " + e.getMessage());
        }
    }
}
