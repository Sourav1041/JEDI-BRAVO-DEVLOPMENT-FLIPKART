package com.flipfit.dao.impl;

import com.flipfit.bean.GymCenter;
import com.flipfit.dao.GymCenterDAO;
import com.flipfit.utils.DBConnection;
import com.flipfit.constant.SQLConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class GymCenterDAOImpl.
 * Implementation of GymCenterDAO interface
 * Handles all database operations for gym centers
 * 
 * @author JEDI-BRAVO
 */
public class GymCenterDAOImpl implements GymCenterDAO {

    /**
     * Insert a new gym center.
     *
     * @param center the gym center object
     * @return true if insertion successful, false otherwise
     */
    @Override
    public boolean insertGymCenter(GymCenter center) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.INSERT_GYM_CENTER)) {
            
            pstmt.setString(1, center.getGymId());
            pstmt.setString(2, center.getOwnerId());
            pstmt.setString(3, center.getGymName());
            pstmt.setString(4, center.getGymAddress());
            pstmt.setString(5, center.getCity());
            pstmt.setString(6, center.getState());
            pstmt.setString(7, center.getPincode());
            pstmt.setString(8, center.getPhoneNumber());
            pstmt.setString(9, center.getEmail());
            pstmt.setInt(10, center.getTotalSlots());
            pstmt.setBoolean(11, center.isApproved());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting gym center: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get gym center by ID.
     *
     * @param gymId the gym ID
     * @return the gym center object
     */
    @Override
    public GymCenter getGymCenterById(String gymId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_GYM_CENTER_BY_ID)) {
            
            pstmt.setString(1, gymId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGymCenter(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting gym center by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all gym centers for an owner.
     *
     * @param ownerId the owner ID
     * @return list of gym centers
     */
    @Override
    public List<GymCenter> getGymCentersByOwner(String ownerId) {
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_GYM_CENTERS_BY_OWNER)) {
            
            pstmt.setString(1, ownerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    centers.add(mapResultSetToGymCenter(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting gym centers by owner: " + e.getMessage());
        }
        return centers;
    }

    /**
     * Get all gym centers in a city.
     *
     * @param city the city name
     * @return list of gym centers
     */
    @Override
    public List<GymCenter> getGymCentersByCity(String city) {
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_GYM_CENTERS_BY_CITY)) {
            
            pstmt.setString(1, city);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    centers.add(mapResultSetToGymCenter(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting gym centers by city: " + e.getMessage());
        }
        return centers;
    }

    /**
     * Get all approved gym centers.
     *
     * @return list of all approved gym centers
     */
    @Override
    public List<GymCenter> getAllGymCenters() {
        List<GymCenter> centers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_ALL_GYM_CENTERS)) {
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    centers.add(mapResultSetToGymCenter(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all gym centers: " + e.getMessage());
        }
        return centers;
    }

    /**
     * Update gym center details.
     *
     * @param center the gym center object with updated information
     * @return true if update successful, false otherwise
     */
    @Override
    public boolean updateGymCenter(GymCenter center) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.UPDATE_GYM_CENTER)) {
            
            pstmt.setString(1, center.getGymName());
            pstmt.setString(2, center.getGymAddress());
            pstmt.setString(3, center.getCity());
            pstmt.setString(4, center.getState());
            pstmt.setString(5, center.getPincode());
            pstmt.setString(6, center.getPhoneNumber());
            pstmt.setString(7, center.getEmail());
            pstmt.setInt(8, center.getTotalSlots());
            pstmt.setString(9, center.getGymId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating gym center: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a gym center.
     *
     * @param gymId the gym ID
     * @return true if deletion successful, false otherwise
     */
    @Override
    public boolean deleteGymCenter(String gymId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.DELETE_GYM_CENTER)) {
            
            pstmt.setString(1, gymId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting gym center: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to map ResultSet to GymCenter object.
     *
     * @param rs the result set
     * @return the gym center object
     * @throws SQLException the SQL exception
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
