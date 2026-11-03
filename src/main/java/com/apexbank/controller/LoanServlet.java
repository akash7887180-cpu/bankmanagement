package com.apexbank.controller;

import com.apexbank.model.Account;
import com.apexbank.model.Customer;
import com.apexbank.model.Loan;
import com.apexbank.service.LoanService;
import com.apexbank.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "LoanServlet", urlPatterns = {"/atm/loans", "/loans"})
public class LoanServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final LoanService loanService = ServiceFactory.getLoanService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Customer customer = getSessionCustomer(req);
        Account account = getSessionAccount(req);

        if (customer != null && account != null) {
            List<Loan> myLoans = loanService.getCustomerLoans(customer.getCustomerId());
            req.setAttribute("myLoans", myLoans);
        }

        // Pass live benchmark gold rates
        req.setAttribute("goldRate24k", loanService.getGoldRatePerGram(24));
        req.setAttribute("goldRate22k", loanService.getGoldRatePerGram(22));
        req.setAttribute("goldRate18k", loanService.getGoldRatePerGram(18));

        forward(req, resp, "atm/loans");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Customer customer = getSessionCustomer(req);
        Account account = getSessionAccount(req);

        if (customer == null || account == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Please+insert+your+ATM+card+and+PIN+to+apply+for+loans.");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("applyMoney".equalsIgnoreCase(action)) {
                String loanType = req.getParameter("loanType");
                String amountStr = req.getParameter("amount");
                String tenureStr = req.getParameter("tenureMonths");
                String rateStr = req.getParameter("interestRate");
                String purpose = req.getParameter("purpose");
                String incomeStr = req.getParameter("monthlyIncome");
                String employmentType = req.getParameter("employmentType");

                BigDecimal amount = new BigDecimal(amountStr.replaceAll("[^0-9.]", ""));
                int tenure = Integer.parseInt(tenureStr.trim());
                BigDecimal rate = (rateStr != null && !rateStr.isEmpty()) ?
                        new BigDecimal(rateStr.trim()) : new BigDecimal("10.50");
                BigDecimal income = (incomeStr != null && !incomeStr.isEmpty()) ?
                        new BigDecimal(incomeStr.replaceAll("[^0-9.]", "")) : BigDecimal.ZERO;

                Loan loan = loanService.applyMoneyLoan(customer.getCustomerId(), account.getAccountId(),
                        loanType, amount, tenure, rate, purpose, income, employmentType);

                if (loan != null) {
                    resp.sendRedirect(req.getContextPath() + "/atm/loans?success=Money+Loan+application+submitted+successfully!+Ref:+<strong>" + loan.getLoanReference() + "</strong>");
                    return;
                }
            } else if ("applyGold".equalsIgnoreCase(action)) {
                String weightStr = req.getParameter("goldWeightGrams");
                String karatStr = req.getParameter("goldKarat");
                String reqAmountStr = req.getParameter("requestedAmount");
                String tenureStr = req.getParameter("tenureMonths");
                String ornamentDetails = req.getParameter("ornamentDetails");

                BigDecimal weight = new BigDecimal(weightStr.trim());
                int karat = Integer.parseInt(karatStr.trim());
                BigDecimal requestedAmount = (reqAmountStr != null && !reqAmountStr.isEmpty()) ?
                        new BigDecimal(reqAmountStr.replaceAll("[^0-9.]", "")) : null;
                int tenure = (tenureStr != null && !tenureStr.isEmpty()) ? Integer.parseInt(tenureStr.trim()) : 12;

                Loan loan = loanService.applyGoldLoan(customer.getCustomerId(), account.getAccountId(),
                        weight, karat, requestedAmount, tenure, ornamentDetails);

                if (loan != null) {
                    resp.sendRedirect(req.getContextPath() + "/atm/loans?success=Gold+Loan+pledge+submitted+successfully!+Ref:+<strong>" + loan.getLoanReference() + "</strong>");
                    return;
                }
            }
            resp.sendRedirect(req.getContextPath() + "/atm/loans?error=Could+not+process+loan+application.+Please+verify+details.");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/atm/loans?error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}
