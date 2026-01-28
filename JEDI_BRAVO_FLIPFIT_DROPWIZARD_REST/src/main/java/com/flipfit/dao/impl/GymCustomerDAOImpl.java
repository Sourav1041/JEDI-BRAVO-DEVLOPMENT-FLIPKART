package com.flipfit.dao.impl;

import com.flipfit.bean.GymUser;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.constants.DatabaseConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of GymCustomerDAO interface
 * Handles all database operations for Customer management
 */
public class GymCustomerDAOImpl implements GymCustomerDAO {
    
    private GymUserDAO gymUserDAO;
    
    public GymCustomerDAOImpl() {
        this.gymUserDAO = new GymUserDAOImpl();
    }
    
    /**
     * Get database connection
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            DatabaseConstants.DB_URL,
            DatabaseConstants.DB_USER,
            DatabaseConstants.DB_PASSWORD
        );
    }
    
    @Override
    public boolean insertGymCustomer(String customerId, String userId, 
                                      Date dateOfBirth, String fitnessGoal) {
        String sql = "INSERT INTO GymCustomer (customer_id, user_id, date_of_birth, fitness_goal) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            pstmt.setString(2, userId);
            pstmt.setDate(3, dateOfBirth != null ? new java.sql.Date(dateOfBirth.getTime()) : null);
            pstmt.setString(4, fitnessGoal);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public GymUser getGymCustomerById(String customerId) {
        String sql = "SELECT user_id FROM GymCustomer WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String userId = rs.getString("user_id");
                return gymUserDAO.getUserById(userId);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public GymUser getGymCustomerByUserId(String userId) {
        String sql = "SELECT user_id FROM GymCustomer WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return gymUserDAO.getUserById(userId);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<GymUser> getAllGymCustomers() {
        List<GymUser> customers = new ArrayList<>();
        String sql = "SELECT user_id FROM GymCustomer";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String userId = rs.getString("user_id");
                GymUser user = gymUserDAO.getUserById(userId);
                if (user != null) {
                    customers.add(user);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    @Override
    public boolean updateGymCustomer(String customerId, Date dateOfBirth, String fitnessGoal) {
        String sql = "UPDATE GymCustomer SET date_of_birth = ?, fitness_goal = ? " +
                     "WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, dateOfBirth != null ? new java.sql.Date(dateOfBirth.getTime()) : null);
            pstmt.setString(2, fitnessGoal);
            pstmt.setString(3, customerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateMembership(String customerId, Date membershipStartDate, 
                                     Date membershipEndDate, boolean isPremium) {
        String sql = "UPDATE GymCustomer SET membership_start_date = ?, " +
                     "membership_end_date = ?, is_premium = ? WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, membershipStartDate != null ? new java.sql.Date(membershipStartDate.getTime()) : null);
            pstmt.setDate(2, membershipEndDate != null ? new java.sql.Date(membershipEndDate.getTime()) : null);
            pstmt.setBoolean(3, isPremium);
            pstmt.setString(4, customerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteGymCustomer(String customerId) {
        String sql = "DELETE FROM GymCustomer WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public int getCustomerBookingCount(String customerId) {
        String sql = "SELECT COUNT(*) FROM Booking WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
