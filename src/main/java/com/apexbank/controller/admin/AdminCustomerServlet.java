package com.apexbank.controller.admin;

import com.apexbank.controller.BaseServlet;
import com.apexbank.exception.BankingException;
import com.apexbank.model.Account;
import com.apexbank.model.Admin;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Customer;
import com.apexbank.service.AdminService;
import com.apexbank.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for Customer Management in Admin Console.
 */
@WebServlet(name = "AdminCustomerServlet", urlPatterns = {"/admin/customers"})
public class AdminCustomerServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final AdminService adminService = ServiceFactory.getAdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");

        try {
            if ("delete".equalsIgnoreCase(action) && idStr != null) {
                int customerId = Integer.parseInt(idStr);
                adminService.deleteCustomer(customerId);
                redirect(resp, req.getContextPath() + "/admin/customers?message=Customer+deleted+successfully.");
                return;
            }

            if ("edit".equalsIgnoreCase(action) && idStr != null) {
                int customerId = Integer.parseInt(idStr);
                Customer c = adminService.getCustomerById(customerId);
                req.setAttribute("customerToEdit", c);
            }

            List<Customer> customers = adminService.getAllCustomers();
            List<Account> accounts = adminService.getAllAccounts();
            List<ATMCard> cards = adminService.getAllCards();

            Map<Integer, Account> accountMap = new HashMap<>();
            for (Account a : accounts) {
                accountMap.put(a.getCustomerId(), a);
            }

            Map<Integer, ATMCard> cardMap = new HashMap<>();
            for (ATMCard c : cards) {
                cardMap.put(c.getCustomerId(), c);
            }

            req.setAttribute("customers", customers);
            req.setAttribute("accountMap", accountMap);
            req.setAttribute("cardMap", cardMap);

            String msg = req.getParameter("message");
            if (msg != null) req.setAttribute("successMessage", msg);

            forward(req, resp, "admin/customers");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error: " + e.getMessage());
            req.setAttribute("customers", adminService.getAllCustomers());
            forward(req, resp, "admin/customers");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action"); // "add" or "update"

        try {
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String dobStr = req.getParameter("dob");
            String gender = req.getParameter("gender");
            String address = req.getParameter("address");
            String city = req.getParameter("city");
            String state = req.getParameter("state");
            String pincode = req.getParameter("pincode");
            String idProofType = req.getParameter("idProofType");
            String idProofNumber = req.getParameter("idProofNumber");

            Date dob = (dobStr != null && !dobStr.isEmpty()) ? Date.valueOf(dobStr) : new Date(System.currentTimeMillis() - 600000000000L);

            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setDob(dob);
            customer.setGender(gender != null ? gender : "MALE");
            customer.setAddress(address);
            customer.setCity(city);
            customer.setState(state);
            customer.setPincode(pincode);
            customer.setIdProofType(idProofType != null ? idProofType : "AADHAAR");
            customer.setIdProofNumber(idProofNumber != null ? idProofNumber : "PENDING");

            if ("update".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("customerId"));
                customer.setCustomerId(id);
                adminService.updateCustomer(customer);
                redirect(resp, req.getContextPath() + "/admin/customers?message=" + URLEncoder.encode("Customer " + customer.getFullName() + " updated successfully.", "UTF-8"));
            } else {
                int newCustId = adminService.createCustomer(customer);
                String accountNumber = req.getParameter("accountNumber");
                String cardNumber = req.getParameter("cardNumber");
                String accountType = req.getParameter("accountType");
                if (accountType == null || accountType.isEmpty()) accountType = "SAVINGS";
                String initDepStr = req.getParameter("initialDeposit");
                double initDep = (initDepStr != null && !initDepStr.isEmpty()) ? Double.parseDouble(initDepStr) : 5000.0;
                String pin = req.getParameter("atmPin");
                if (pin == null || pin.trim().isEmpty()) pin = "1234";
                String cardType = req.getParameter("cardType");
                if (cardType == null || cardType.isEmpty()) cardType = "DEBIT_RUPAY";

                Admin admin = getSessionAdmin(req);
                String adminUser = (admin != null) ? admin.getUsername() : "ADMIN";

                adminService.openAccountAndIssueCard(newCustId, accountNumber, accountType, initDep, pin, cardNumber, cardType, adminUser);
                redirect(resp, req.getContextPath() + "/admin/customers?message=" + URLEncoder.encode("Customer enrolled successfully! Account and ATM Card (PIN: " + pin + ") created.", "UTF-8"));
                return;
            }
        } catch (BankingException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("customers", adminService.getAllCustomers());
            forward(req, resp, "admin/customers");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            req.setAttribute("customers", adminService.getAllCustomers());
            forward(req, resp, "admin/customers");
        }
    }
}
