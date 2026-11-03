package com.apexbank.service;

/**
 * Factory class for instantiating Business Services.
 * Demonstrates the Factory Design Pattern.
 */
public class ServiceFactory {

    private static final ATMService atmService = new ATMServiceImpl();
    private static final AdminService adminService = new AdminServiceImpl();
    private static final LoanService loanService = new LoanServiceImpl();
    private static final SupportService supportService = new SupportServiceImpl();
    private static final ChatbotService chatbotService = new ChatbotService();

    public static ATMService getATMService() {
        return atmService;
    }

    public static AdminService getAdminService() {
        return adminService;
    }

    public static LoanService getLoanService() {
        return loanService;
    }

    public static SupportService getSupportService() {
        return supportService;
    }

    public static ChatbotService getChatbotService() {
        return chatbotService;
    }
}
