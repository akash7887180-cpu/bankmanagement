package com.apexbank.dao;

import com.apexbank.model.Customer;
import java.util.List;

/**
 * Data Access Object Interface for Customer entities.
 * Demonstrates OOP Interface Abstraction and DAO Design Pattern.
 */
public interface CustomerDAO {
    Customer findById(int customerId);
    Customer findByEmail(String email);
    Customer findByPhone(String phone);
    List<Customer> findAll();
    int create(Customer customer);
    boolean update(Customer customer);
    boolean delete(int customerId);
    int countTotalCustomers();
}
