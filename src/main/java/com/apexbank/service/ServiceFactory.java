package com.apexbank.service;

/**
 * Factory class for instantiating Business Services.
 * Demonstrates the Factory Design Pattern.
 */
public class ServiceFactory {

    private static final ATMService atmService = new ATMServiceImpl();
    private static final AdminService adminService = new AdminServiceImpl();

    public static ATMService getATMService() {
        return atmService;
    }

    public static AdminService getAdminService() {
        return adminService;
    }
}
