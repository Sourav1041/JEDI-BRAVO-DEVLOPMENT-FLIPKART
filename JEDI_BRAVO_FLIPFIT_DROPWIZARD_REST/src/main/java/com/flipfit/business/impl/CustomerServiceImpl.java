package com.flipfit.business.impl;

import com.flipfit.bean.GymUser;
import com.flipfit.business.CustomerService;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.impl.GymCustomerDAOImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of CustomerService interface
 * Uses DAO layer for database operations
 */
public class CustomerServiceImpl implements CustomerService {
    
    private GymCustomerDAO customerDAO;
    
    public CustomerServiceImpl() {
        this.customerDAO = new GymCustomerDAOImpl();
    }
    
    @Override
    public String registerCustomer(String userId, Date dateOfBirth, String fitnessGoal) {
        // Validate input
        if (userId == null || userId.isEmpty()) {
            System.out.println("User ID is required!");
            return null;
        }
        
        // Generate customer ID
        String customerId = generateCustomerId();
        
        // Insert customer
        boolean success = customerDAO.insertGymCustomer(customerId, userId, dateOfBirth, fitnessGoal);
        
        if (success) {
            System.out.println("Customer registered successfully!");
            return customerId;
        } else {
            System.out.println("Failed to register customer!");
            return null;
        }
    }
    
    @Override
    public GymUser getCustomerById(String customerId) {
        return customerDAO.getGymCustomerById(customerId);
    }
    
    @Override
    public GymUser getCustomerByUserId(String userId) {
        return customerDAO.getGymCustomerByUserId(userId);
    }
    
    @Override
    public List<GymUser> getAllCustomers() {
        return customerDAO.getAllGymCustomers();
    }
    
    @Override
    public boolean updateCustomer(String customerId, Date dateOfBirth, String fitnessGoal) {
        // Validate customer exists
        GymUser customer = customerDAO.getGymCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return false;
        }
        
        // Update customer
        return customerDAO.updateGymCustomer(customerId, dateOfBirth, fitnessGoal);
    }
    
    @Override
    public int getBookingCount(String customerId) {
        return customerDAO.getCustomerBookingCount(customerId);
    }
    
    /**
     * Generate unique customer ID
     * @return Generated customer ID
     */
    private String generateCustomerId() {
        return "CUS" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
