package com.flipfit.dao.impl;

import com.flipfit.bean.GymSlot;
import com.flipfit.dao.GymSlotDAO;
import com.flipfit.utils.DBConnection;
import com.flipfit.constant.SQLConstants;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class GymSlotDAOImpl.
 * Implementation of GymSlotDAO interface
 * Handles all database operations for gym slots
 * 
 * @author JEDI-BRAVO
 * @ClassName GymSlotDAOImpl
 */
public class GymSlotDAOImpl implements GymSlotDAO {

    /**
     * Insert a new gym slot.
     *
     * @param slot the gym slot object
     * @return true if insertion successful, false otherwise
     */
    @Override
    public boolean insertSlot(GymSlot slot) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.INSERT_SLOT)) {
            
            pstmt.setString(1, slot.getSlotId());
            pstmt.setString(2, slot.getGymId());
            pstmt.setTime(3, Time.valueOf(slot.getStartTime()));
            pstmt.setTime(4, Time.valueOf(slot.getEndTime()));
            pstmt.setInt(5, slot.getTotalSeats());
            pstmt.setInt(6, slot.getAvailableSeats());
            pstmt.setBigDecimal(7, slot.getPrice());
            pstmt.setBoolean(8, slot.isActive());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting slot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get slot by ID.
     *
     * @param slotId the slot ID
     * @return the gym slot object
     */
    @Override
    public GymSlot getSlotById(String slotId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_SLOT_BY_ID)) {
            
            pstmt.setString(1, slotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSlot(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting slot by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all slots for a specific gym.
     *
     * @param gymId the gym ID
     * @return list of gym slots
     */
    @Override
    public List<GymSlot> getSlotsByCenter(String gymId) {
        List<GymSlot> slots = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_SLOTS_BY_GYM)) {
            
            pstmt.setString(1, gymId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    slots.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting slots by gym: " + e.getMessage());
        }
        return slots;
    }

    /**
     * Get available slots for a gym.
     *
     * @param gymId the gym ID
     * @return list of available gym slots
     */
    public List<GymSlot> getAvailableSlotsByGym(String gymId) {
        List<GymSlot> slots = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_AVAILABLE_SLOTS_BY_GYM)) {
            
            pstmt.setString(1, gymId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    slots.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting available slots: " + e.getMessage());
        }
        return slots;
    }

    /**
     * Get all slots for a gym within a time range.
     *
     * @param gymId the gym ID
     * @param startTime the start time
     * @param endTime the end time
     * @return list of gym slots
     */
    public List<GymSlot> getSlotsByGymAndTime(String gymId, LocalTime startTime, LocalTime endTime) {
        List<GymSlot> slots = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_SLOTS_BY_GYM_TIME)) {
            
            pstmt.setString(1, gymId);
            pstmt.setTime(2, Time.valueOf(startTime));
            pstmt.setTime(3, Time.valueOf(endTime));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    slots.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting slots by time range: " + e.getMessage());
        }
        return slots;
    }

    /**
     * Check if seats are available in a slot.
     *
     * @param slotId the slot ID
     * @return true if seats available, false otherwise
     */
    @Override
    public boolean checkSlotAvailability(String slotId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.CHECK_SLOT_AVAILABILITY)) {
            
            pstmt.setString(1, slotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("available_seats") > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking slot availability: " + e.getMessage());
        }
        return false;
    }

    /**
     * Update available seats in a slot.
     *
     * @param slotId the slot ID
     * @param seatsChange the number of seats to add (positive) or remove (negative)
     * @return true if update successful, false otherwise
     */
    @Override
    public boolean updateAvailableSeats(String slotId, int seatsChange) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.UPDATE_AVAILABLE_SEATS)) {
            
            pstmt.setInt(1, seatsChange);
            pstmt.setString(2, slotId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating available seats: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all available slots across all gyms.
     *
     * @return list of available gym slots
     */
    public List<GymSlot> getAllAvailableSlots() {
        List<GymSlot> slots = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_ALL_AVAILABLE_SLOTS)) {
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    slots.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all available slots: " + e.getMessage());
        }
        return slots;
    }

    /**
     * Get slots by city.
     *
     * @param city the city name
     * @return list of gym slots
     */
    public List<GymSlot> getSlotsByCity(String city) {
        List<GymSlot> slots = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_SLOTS_BY_CITY)) {
            
            pstmt.setString(1, city);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    slots.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting slots by city: " + e.getMessage());
        }
        return slots;
    }

    /**
     * Delete a slot.
     *
     * @param slotId the slot ID
     * @return true if deletion successful, false otherwise
     */
    @Override
    public boolean deleteSlot(String slotId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.DELETE_SLOT)) {
            
            pstmt.setString(1, slotId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting slot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update slot details.
     *
     * @param slot the gym slot object with updated information
     * @return true if update successful, false otherwise
     */
    @Override
    public boolean updateSlot(GymSlot slot) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.UPDATE_SLOT)) {
            
            pstmt.setTime(1, Time.valueOf(slot.getStartTime()));
            pstmt.setTime(2, Time.valueOf(slot.getEndTime()));
            pstmt.setInt(3, slot.getTotalSeats());
            pstmt.setInt(4, slot.getAvailableSeats());
            pstmt.setBigDecimal(5, slot.getPrice());
            pstmt.setBoolean(6, slot.isActive());
            pstmt.setString(7, slot.getSlotId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating slot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get slot capacity.
     *
     * @param slotId the slot ID
     * @return the capacity of the slot
     */
    @Override
    public int getSlotCapacity(String slotId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.GET_SLOT_CAPACITY)) {
            
            pstmt.setString(1, slotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_seats");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting slot capacity: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get available seats count for a slot.
     *
     * @param slotId the slot ID
     * @return the number of available seats
     */
    @Override
    public int getAvailableSeats(String slotId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.GET_AVAILABLE_SEATS)) {
            
            pstmt.setString(1, slotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("available_seats");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting available seats: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Helper method to map ResultSet to GymSlot object.
     *
     * @param rs the result set
     * @return the gym slot object
     * @throws SQLException the SQL exception
     */
    private GymSlot mapResultSetToSlot(ResultSet rs) throws SQLException {
        GymSlot slot = new GymSlot();
        slot.setSlotId(rs.getString("slot_id"));
        slot.setGymId(rs.getString("gym_id"));
        slot.setStartTime(rs.getTime("start_time").toLocalTime());
        slot.setEndTime(rs.getTime("end_time").toLocalTime());
        slot.setTotalSeats(rs.getInt("total_seats"));
        slot.setAvailableSeats(rs.getInt("available_seats"));
        slot.setPrice(rs.getBigDecimal("price"));
        slot.setActive(rs.getBoolean("is_active"));
        slot.setCreatedAt(rs.getTimestamp("created_at"));
        return slot;
    }
}
