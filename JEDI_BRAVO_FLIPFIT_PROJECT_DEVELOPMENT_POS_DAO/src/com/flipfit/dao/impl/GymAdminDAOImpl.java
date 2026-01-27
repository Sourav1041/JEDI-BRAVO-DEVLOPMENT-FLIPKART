package com.flipfit.dao.impl;

import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymCenter;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.constants.DatabaseConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of GymAdminDAO interface
 * Handles all database operations for Admin functionalities
 */
public class GymAdminDAOImpl implements GymAdminDAO {
    
    private GymOwnerDAO gymOwnerDAO;
    
    public GymAdminDAOImpl() {
        this.gymOwnerDAO = new GymOwnerDAOImpl();
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
    public List<GymOwner> getPendingGymOwnerApprovals() {
        return gymOwnerDAO.getPendingGymOwners();
    }
    
    @Override
    public List<GymCenter> getPendingGymCenterApprovals() {
        List<GymCenter> centers = new ArrayList<>();
        String sql = "SELECT * FROM GymCenter WHERE is_approved = FALSE";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                centers.add(mapResultSetToGymCenter(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return centers;
    }
    
    @Override
    public boolean approveGymOwner(String ownerId) {
        return gymOwnerDAO.approveGymOwner(ownerId);
    }
    
    @Override
    public boolean rejectGymOwner(String ownerId, String remarks) {
        String sql = "UPDATE Registration SET status = 'REJECTED', reviewed_date = CURRENT_TIMESTAMP, " +
                     "remarks = ? WHERE user_id = (SELECT user_id FROM GymOwner WHERE owner_id = ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, remarks);
            pstmt.setString(2, ownerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean approveGymCenter(String gymId) {
        String sql = "UPDATE GymCenter SET is_approved = TRUE, approval_date = CURRENT_TIMESTAMP " +
                     "WHERE gym_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, gymId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean rejectGymCenter(String gymId, String remarks) {
        String sql = "UPDATE GymCenter SET is_approved = FALSE WHERE gym_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, gymId);
            int rowsAffected = pstmt.executeUpdate();
            
            // Also update registration table
            if (rowsAffected > 0) {
                String regSql = "INSERT INTO Registration (registration_id, user_id, registration_type, " +
                               "status, remarks) VALUES (?, ?, 'GYM_CENTER', 'REJECTED', ?)";
                try (PreparedStatement regPstmt = conn.prepareStatement(regSql)) {
                    regPstmt.setString(1, "REG_" + System.currentTimeMillis());
                    regPstmt.setString(2, "ADMIN");
                    regPstmt.setString(3, remarks);
                    regPstmt.executeUpdate();
                }
            }
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Object> getAllUsers() {
        List<Object> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(rs.getString("user_id"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    @Override
    public List<Object> getAllBookings() {
        List<Object> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Booking";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                bookings.add(rs.getString("booking_id"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    @Override
    public List<GymCenter> getAllGymCenters() {
        List<GymCenter> centers = new ArrayList<>();
        String sql = "SELECT * FROM GymCenter";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                centers.add(mapResultSetToGymCenter(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return centers;
    }
    
    @Override
    public Map<String, Integer> getSystemStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        try (Connection conn = getConnection()) {
            // Get total users
            String sql = "SELECT COUNT(*) as count FROM User";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("total_users", rs.getInt("count"));
                }
            }
            
            // Get total gym owners
            sql = "SELECT COUNT(*) as count FROM GymOwner";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("total_gym_owners", rs.getInt("count"));
                }
            }
            
            // Get total customers
            sql = "SELECT COUNT(*) as count FROM GymCustomer";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("total_customers", rs.getInt("count"));
                }
            }
            
            // Get total bookings
            sql = "SELECT COUNT(*) as count FROM Booking";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("total_bookings", rs.getInt("count"));
                }
            }
            
            // Get total gym centers
            sql = "SELECT COUNT(*) as count FROM GymCenter";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("total_gym_centers", rs.getInt("count"));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return stats;
    }
    
    /**
     * Helper method to map ResultSet to GymCenter object
     */
    private GymCenter mapResultSetToGymCenter(ResultSet rs) throws SQLException {
        GymCenter center = new GymCenter();
        center.setGymId(rs.getString("gym_id"));
        center.setOwnerId(rs.getString("owner_id"));
        center.setGymName(rs.getString("gym_name"));
        center.setGymAddress(rs.getString("gym_address"));
        center.setCity(rs.getString("city"));
        center.setState(rs.getString("state"));
        center.setPincode(rs.getString("pincode"));
        center.setPhoneNumber(rs.getString("phone_number"));
        center.setEmail(rs.getString("email"));
        center.setTotalSlots(rs.getInt("total_slots"));
        center.setApproved(rs.getBoolean("is_approved"));
        center.setApprovalDate(rs.getTimestamp("approval_date"));
        center.setCreatedAt(rs.getTimestamp("created_at"));
        center.setUpdatedAt(rs.getTimestamp("updated_at"));
        return center;
    }
}
