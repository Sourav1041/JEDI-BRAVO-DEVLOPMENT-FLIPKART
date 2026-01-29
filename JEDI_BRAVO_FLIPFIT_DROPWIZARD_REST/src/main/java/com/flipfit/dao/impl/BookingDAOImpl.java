package com.flipfit.dao.impl;

import com.flipfit.bean.Booking;
import com.flipfit.dao.BookingDAO;
import com.flipfit.dao.GymSlotDAO;
import com.flipfit.enums.BookingStatus;
import com.flipfit.utils.DBConnection;
import com.flipfit.constant.SQLConstants;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class BookingDAOImpl.
 * Implementation of BookingDAO interface
 * Handles all database operations for bookings
 * 
 * @author FlipFit Team
 * @ClassName BookingDAOImpl
 */
public class BookingDAOImpl implements BookingDAO {
    
    private final GymSlotDAO slotDAO = new GymSlotDAOImpl();

    /**
     * Insert a new booking.
     *
     * @param booking the booking object
     * @return true if insertion successful, false otherwise
     */
    @Override
    public boolean insertBooking(Booking booking) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.INSERT_BOOKING)) {
            
            pstmt.setString(1, booking.getBookingId());
            pstmt.setString(2, booking.getCustomerId());
            pstmt.setString(3, booking.getSlotId());
            pstmt.setDate(4, Date.valueOf(booking.getBookingDate()));
            pstmt.setString(5, booking.getBookingStatus().toString());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting booking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get booking by ID.
     *
     * @param bookingId the booking ID
     * @return the booking object
     */
    @Override
    public Booking getBookingById(String bookingId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_BOOKING_BY_ID)) {
            
            pstmt.setString(1, bookingId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting booking by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all bookings for a customer.
     *
     * @param customerId the customer ID
     * @return list of bookings
     */
    @Override
    public List<Booking> getBookingsByCustomer(String customerId) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_BOOKINGS_BY_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting bookings by customer: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Get all bookings for a slot.
     *
     * @param slotId the slot ID
     * @return list of bookings
     */
    @Override
    public List<Booking> getBookingsBySlot(String slotId) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_BOOKINGS_BY_SLOT)) {
            
            pstmt.setString(1, slotId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting bookings by slot: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Get customer bookings for a specific date.
     *
     * @param customerId the customer ID
     * @param date the date
     * @return list of bookings
     */
    @Override
    public List<Booking> getCustomerBookingsByDate(String customerId, LocalDate date) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_CUSTOMER_BOOKINGS_BY_DATE)) {
            
            pstmt.setString(1, customerId);
            pstmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting customer bookings by date: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Check if customer has existing bookings on a given date.
     *
     * @param customerId the customer ID
     * @param date the booking date
     * @return list of existing bookings for that date
     */
    @Override
    public List<Booking> checkExistingBookingsOnDate(String customerId, LocalDate date) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.CHECK_EXISTING_BOOKING_SAME_TIME)) {
            
            pstmt.setString(1, customerId);
            pstmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking existing bookings: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Update booking status.
     *
     * @param bookingId the booking ID
     * @param status the new booking status
     * @return true if update successful, false otherwise
     */
    @Override
    public boolean updateBookingStatus(String bookingId, BookingStatus status) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.UPDATE_BOOKING_STATUS)) {
            
            pstmt.setString(1, status.toString());
            pstmt.setString(2, bookingId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating booking status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a booking.
     *
     * @param bookingId the booking ID
     * @return true if deletion successful, false otherwise
     */
    @Override
    public boolean deleteBooking(String bookingId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.DELETE_BOOKING)) {
            
            pstmt.setString(1, bookingId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Count total bookings for a customer.
     *
     * @param customerId the customer ID
     * @return the booking count
     */
    @Override
    public int countBookingsByCustomer(String customerId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.COUNT_BOOKINGS_BY_CUSTOMER)) {
            
            pstmt.setString(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting bookings: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Cancel a booking and update slot availability.
     *
     * @param bookingId the booking ID
     * @return true if cancellation successful, false otherwise
     */
    @Override
    public boolean cancelBooking(String bookingId) {
        // Get booking details first
        Booking booking = getBookingById(bookingId);
        if (booking == null || booking.getBookingStatus() == BookingStatus.CANCELLED) {
            return false;
        }
        
        // Simply update booking status to CANCELLED
        // No need to update available_seats since we calculate availability per date dynamically
        return updateBookingStatus(bookingId, BookingStatus.CANCELLED);
    }

    /**
     * Count confirmed bookings for a specific slot on a specific date.
     * Used to calculate date-specific availability.
     *
     * @param slotId the slot ID
     * @param date the booking date
     * @return count of confirmed bookings
     */
    @Override
    public int countBookingsForSlotOnDate(String slotId, LocalDate date) {
        String query = "SELECT COUNT(*) as count FROM Booking " +
                      "WHERE slot_id = ? AND booking_date = ? AND booking_status = 'CONFIRMED'";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, slotId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        } catch (SQLException e) {
            System.err.println("Error counting bookings for slot " + slotId + " on " + date + ": " + e.getMessage());
            return 0;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Helper method to map ResultSet to Booking object.
     *
     * @param rs the result set
     * @return the booking object
     * @throws SQLException the SQL exception
     */
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getString("booking_id"));
        booking.setCustomerId(rs.getString("customer_id"));
        booking.setSlotId(rs.getString("slot_id"));
        booking.setBookingDate(rs.getDate("booking_date").toLocalDate());
        booking.setBookingStatus(BookingStatus.valueOf(rs.getString("booking_status")));
        return booking;
    }
}
