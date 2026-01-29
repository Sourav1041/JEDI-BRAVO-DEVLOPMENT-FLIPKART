package com.flipfit.dao.impl;

import com.flipfit.bean.GymWaitList;
import com.flipfit.dao.WaitListDAO;
import com.flipfit.utils.DBConnection;
import com.flipfit.constant.SQLConstants;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class WaitListDAOImpl.
 * Implementation of WaitListDAO interface
 * Handles all database operations for waitlist
 * 
 * @author JEDI-BRAVO
 */
public class WaitListDAOImpl implements WaitListDAO {
    
    /**
     * Insert a new waitlist entry.
     *
     * @param waitList the waitlist object
     * @return true if insertion successful, false otherwise
     */
    @Override
    public boolean insertWaitList(GymWaitList waitList) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.INSERT_WAITLIST)) {
            
            pstmt.setString(1, waitList.getWaitlistId());
            pstmt.setString(2, waitList.getCustomerId());
            pstmt.setString(3, waitList.getSlotId());
            pstmt.setDate(4, Date.valueOf(waitList.getRequestedDate()));
            pstmt.setString(5, "WAITING");
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting waitlist: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all waitlist entries for a slot.
     *
     * @param slotId the slot ID
     * @return list of waitlist entries
     */
    @Override
    public List<GymWaitList> getWaitListBySlot(String slotId) {
        List<GymWaitList> waitList = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_WAITLIST_BY_SLOT)) {
            
            pstmt.setString(1, slotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    waitList.add(mapResultSetToWaitList(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting waitlist: " + e.getMessage());
        }
        return waitList;
    }
    
    /**
     * Update waitlist status.
     *
     * @param waitlistId the waitlist ID
     * @param status the new status
     * @return true if update successful, false otherwise
     */
    @Override
    public boolean updateWaitListStatus(String waitlistId, String status) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.UPDATE_WAITLIST_STATUS)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, waitlistId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating waitlist: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a waitlist entry.
     *
     * @param waitlistId the waitlist ID
     * @return true if deletion successful, false otherwise
     */
    @Override
    public boolean deleteWaitList(String waitlistId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.DELETE_WAITLIST)) {
            
            pstmt.setString(1, waitlistId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting waitlist: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get first waiting customer for a slot.
     *
     * @param slotId the slot ID
     * @return the first waitlist entry or null
     */
    @Override
    public GymWaitList getFirstWaitingCustomer(String slotId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_FIRST_WAITLIST)) {
            
            pstmt.setString(1, slotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToWaitList(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting first waiting customer: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get first waiting customer for a specific slot on a specific date.
     * This is date-specific to ensure proper waitlist promotion.
     *
     * @param slotId the slot ID
     * @param requestedDate the requested date
     * @return the first waitlist entry for that slot and date, or null
     */
    @Override
    public GymWaitList getFirstWaitingCustomerByDate(String slotId, java.time.LocalDate requestedDate) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_FIRST_WAITLIST_BY_DATE)) {
            
            pstmt.setString(1, slotId);
            pstmt.setDate(2, Date.valueOf(requestedDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToWaitList(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting first waiting customer by date: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Check if customer is already in waitlist for a specific slot on a specific date.
     *
     * @param customerId the customer ID
     * @param slotId the slot ID
     * @param requestedDate the requested date
     * @return true if customer is already in waitlist
     */
    @Override
    public boolean isCustomerInWaitlistByDate(String customerId, String slotId, java.time.LocalDate requestedDate) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.CHECK_CUSTOMER_IN_WAITLIST_BY_DATE)) {
            
            pstmt.setString(1, customerId);
            pstmt.setString(2, slotId);
            pstmt.setDate(3, Date.valueOf(requestedDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking customer in waitlist by date: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Helper method to map ResultSet to GymWaitList object.
     *
     * @param rs the result set
     * @return the waitlist object
     * @throws SQLException the SQL exception
     */
    private GymWaitList mapResultSetToWaitList(ResultSet rs) throws SQLException {
        GymWaitList waitList = new GymWaitList();
        waitList.setWaitlistId(rs.getString("waitlist_id"));
        waitList.setCustomerId(rs.getString("customer_id"));
        waitList.setSlotId(rs.getString("slot_id"));
        waitList.setRequestedDate(rs.getDate("requested_date").toLocalDate());
        waitList.setPriority(rs.getInt("priority"));
        waitList.setStatus(rs.getString("status"));
        waitList.setCreatedAt(rs.getTimestamp("created_at"));
        return waitList;
    }
}

