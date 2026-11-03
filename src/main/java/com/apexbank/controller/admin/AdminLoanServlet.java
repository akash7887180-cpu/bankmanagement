package com.apexbank.controller.admin;

import com.apexbank.controller.BaseServlet;
import com.apexbank.model.Loan;
import com.apexbank.model.LoanStatus;
import com.apexbank.service.LoanService;
import com.apexbank.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@WebServlet(name = "AdminLoanServlet", urlPatterns = {"/admin/loans"})
public class AdminLoanServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final LoanService loanService = ServiceFactory.getLoanService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Loan> allLoans = loanService.getAllLoans();
        req.setAttribute("loans", allLoans);

        forward(req, resp, "admin/loans");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String loanIdStr = req.getParameter("loanId");
        String remarks = req.getParameter("adminRemarks");

        if (loanIdStr == null || loanIdStr.trim().isEmpty()) {
            redirect(resp, req.getContextPath() + "/admin/loans?error=Invalid+loan+ID.");
            return;
        }

        try {
            int loanId = Integer.parseInt(loanIdStr.trim());

            if ("approve".equalsIgnoreCase(action)) {
                loanService.updateLoanStatus(loanId, LoanStatus.APPROVED,
                        remarks != null ? remarks : "Underwriting approved by Bank Manager.");
                redirect(resp, req.getContextPath() + "/admin/loans?message=Loan+application+APPROVED+successfully.");
                return;
            } else if ("reject".equalsIgnoreCase(action)) {
                loanService.updateLoanStatus(loanId, LoanStatus.REJECTED,
                        remarks != null ? remarks : "Does not meet credit eligibility criteria.");
                redirect(resp, req.getContextPath() + "/admin/loans?message=Loan+application+REJECTED.");
                return;
            } else if ("disburse".equalsIgnoreCase(action)) {
                boolean ok = loanService.disburseLoan(loanId,
                        remarks != null ? remarks : "Funds disbursed directly to customer core bank account.");
                if (ok) {
                    redirect(resp, req.getContextPath() + "/admin/loans?message=Loan+funds+DISBURSED+and+credited+to+customer+account+ledger+successfully!");
                } else {
                    redirect(resp, req.getContextPath() + "/admin/loans?error=Could+not+disburse+loan.+Account+may+be+frozen+or+invalid.");
                }
                return;
            }
            redirect(resp, req.getContextPath() + "/admin/loans");
        } catch (Exception e) {
            redirect(resp, req.getContextPath() + "/admin/loans?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}
