package com.apexbank.controller;

import com.apexbank.dao.ATMCardDAO;
import com.apexbank.dao.CustomerDAO;
import com.apexbank.dao.DAOFactory;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Customer;
import com.apexbank.service.ATMService;
import com.apexbank.service.ServiceFactory;
import com.apexbank.util.MaskingUtil;
import com.apexbank.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for OTP Verification (Forgot PIN and Card Unlock).
 */
@WebServlet(name = "OtpServlet", urlPatterns = {"/otp", "/forgot-pin"})
public class OtpServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ATMService atmService = ServiceFactory.getATMService();
    private final ATMCardDAO cardDAO = DAOFactory.getATMCardDAO();
    private final CustomerDAO customerDAO = DAOFactory.getCustomerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        forward(req, resp, "auth/forgot-pin");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action"); // "send_otp" or "verify_and_reset"
        String cardNumber = req.getParameter("cardNumber");
        String phone = req.getParameter("phone");
        String enteredOtp = req.getParameter("otp");
        String newPin = req.getParameter("newPin");
        String confirmPin = req.getParameter("confirmPin");

        try {
            if ("send_otp".equalsIgnoreCase(action)) {
                ATMCard card = cardDAO.findByCardNumber(cardNumber);
                if (card == null) {
                    req.setAttribute("errorMessage", "ATM Card number not found.");
                    forward(req, resp, "auth/forgot-pin");
                    return;
                }

                Customer customer = customerDAO.findById(card.getCustomerId());
                if (customer == null || !customer.getPhone().equals(phone.trim())) {
                    req.setAttribute("errorMessage", "Registered phone number does not match customer profile.");
                    forward(req, resp, "auth/forgot-pin");
                    return;
                }

                String generatedOtp = atmService.triggerOtp(customer.getPhone(), "FORGOT_PIN_RESET");

                req.setAttribute("step", "VERIFY");
                req.setAttribute("cardNumber", cardNumber);
                req.setAttribute("phone", customer.getPhone());
                req.setAttribute("maskedPhone", MaskingUtil.maskPhone(customer.getPhone()));
                req.setAttribute("simulatedOtp", generatedOtp); // Display on screen for seamless college demo!
                req.setAttribute("infoMessage", "OTP has been simulated and sent to your registered mobile.");
                forward(req, resp, "auth/forgot-pin");
            } else if ("verify_and_reset".equalsIgnoreCase(action)) {
                if (!atmService.verifyOtp(phone, enteredOtp)) {
                    req.setAttribute("step", "VERIFY");
                    req.setAttribute("cardNumber", cardNumber);
                    req.setAttribute("phone", phone);
                    req.setAttribute("errorMessage", "Invalid or expired OTP. Please check the simulated code.");
                    forward(req, resp, "auth/forgot-pin");
                    return;
                }

                if (newPin == null || confirmPin == null || !newPin.trim().equals(confirmPin.trim())) {
                    req.setAttribute("step", "VERIFY");
                    req.setAttribute("cardNumber", cardNumber);
                    req.setAttribute("phone", phone);
                    req.setAttribute("errorMessage", "PINs do not match.");
                    forward(req, resp, "auth/forgot-pin");
                    return;
                }

                if (!newPin.matches("^\\d{4}$")) {
                    req.setAttribute("step", "VERIFY");
                    req.setAttribute("cardNumber", cardNumber);
                    req.setAttribute("phone", phone);
                    req.setAttribute("errorMessage", "New PIN must be exactly 4 digits.");
                    forward(req, resp, "auth/forgot-pin");
                    return;
                }

                ATMCard card = cardDAO.findByCardNumber(cardNumber);
                String salt = "salt_" + System.currentTimeMillis();
                String newHash = SecurityUtil.hashWithSalt(newPin.trim(), salt);
                cardDAO.updatePin(card.getCardId(), newHash, salt, newPin.trim());

                redirect(resp, req.getContextPath() + "/login?message=PIN+reset+successful!+Please+log+in+with+your+new+PIN.");
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error: " + e.getMessage());
            forward(req, resp, "auth/forgot-pin");
        }
    }
}
