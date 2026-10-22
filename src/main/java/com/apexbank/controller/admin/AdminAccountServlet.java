package com.apexbank.controller.admin;

import com.apexbank.controller.BaseServlet;
import com.apexbank.model.Account;
import com.apexbank.model.AccountStatus;
import com.apexbank.model.Admin;
import com.apexbank.model.ATMCard;
import com.apexbank.model.CardStatus;
import com.apexbank.service.AdminService;
import com.apexbank.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller for Account and Card management (Freeze, Activate, PIN unlock).
 */
@WebServlet(name = "AdminAccountServlet", urlPatterns = {"/admin/accounts"})
public class AdminAccountServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final AdminService adminService = ServiceFactory.getAdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Admin admin = getSessionAdmin(req);
        String adminUser = admin != null ? admin.getUsername() : "ADMIN";

        try {
            if ("freeze".equalsIgnoreCase(action) && idStr != null) {
                int accountId = Integer.parseInt(idStr);
                adminService.changeAccountStatus(accountId, AccountStatus.FROZEN, adminUser);
                redirect(resp, req.getContextPath() + "/admin/accounts?message=Account+frozen+successfully.");
                return;
            } else if ("activate".equalsIgnoreCase(action) && idStr != null) {
                int accountId = Integer.parseInt(idStr);
                adminService.changeAccountStatus(accountId, AccountStatus.ACTIVE, adminUser);
                redirect(resp, req.getContextPath() + "/admin/accounts?message=Account+activated+successfully.");
                return;
            } else if ("unlock_card".equalsIgnoreCase(action) && idStr != null) {
                int cardId = Integer.parseInt(idStr);
                adminService.resetCardPinAttempts(cardId, adminUser);
                redirect(resp, req.getContextPath() + "/admin/accounts?message=ATM+Card+unlocked+and+reset+successfully.");
                return;
            } else if ("block_card".equalsIgnoreCase(action) && idStr != null) {
                int cardId = Integer.parseInt(idStr);
                adminService.changeCardStatus(cardId, CardStatus.BLOCKED, adminUser);
                redirect(resp, req.getContextPath() + "/admin/accounts?message=ATM+Card+blocked.");
                return;
            }

            List<Account> accounts = adminService.getAllAccounts();
            List<ATMCard> cards = adminService.getAllCards();

            req.setAttribute("accounts", accounts);
            req.setAttribute("cards", cards);
            req.setAttribute("customers", adminService.getAllCustomers());

            String msg = req.getParameter("message");
            if (msg != null) req.setAttribute("successMessage", msg);

            forward(req, resp, "admin/accounts");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error: " + e.getMessage());
            req.setAttribute("accounts", adminService.getAllAccounts());
            req.setAttribute("cards", adminService.getAllCards());
            req.setAttribute("customers", adminService.getAllCustomers());
            forward(req, resp, "admin/accounts");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        Admin admin = getSessionAdmin(req);
        String adminUser = admin != null ? admin.getUsername() : "ADMIN";

        try {
            if ("create_account".equalsIgnoreCase(action)) {
                int customerId = Integer.parseInt(req.getParameter("customerId"));
                String accountNumber = req.getParameter("accountNumber");
                String cardNumber = req.getParameter("cardNumber");
                String accountType = req.getParameter("accountType");
                double initialDeposit = Double.parseDouble(req.getParameter("initialDeposit"));
                String pin = req.getParameter("pin");
                String cardType = req.getParameter("cardType");

                adminService.openAccountAndIssueCard(customerId, accountNumber, accountType, initialDeposit, pin, cardNumber, cardType, adminUser);
                redirect(resp, req.getContextPath() + "/admin/accounts?message=Account+successfully+opened+and+ATM+Card+issued!");
                return;
            }
            redirect(resp, req.getContextPath() + "/admin/accounts");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Failed to create account: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
