package com.flipfit.dao;

import com.flipfit.bean.Booking;
import com.flipfit.enums.BookingStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface BookingDAO.
 * Defines operations for booking management
 * 
 * @author JEDI-BRAVO
 * @ClassName BookingDAO
 */
public interface BookingDAO {
    
    /**
     * Insert a new booking.
     *
     * @param booking the booking object
     * @return true if insertion successful, false otherwise
     */
    boolean insertBooking(Booking booking);
    
    /**
     * Get booking by ID.
     *
     * @param bookingId the booking ID
     * @return the booking object
     */
    Booking getBookingById(String bookingId);
    
    /**
     * Get all bookings for a customer.
     *
     * @param customerId the customer ID
     * @return list of bookings
     */
    List<Booking> getBookingsByCustomer(String customerId);
    
    /**
     * Get all bookings for a slot.
     *
     * @param slotId the slot ID
     * @return list of bookings
     */
    List<Booking> getBookingsBySlot(String slotId);
    
    /**
     * Get customer bookings for a specific date.
     *
     * @param customerId the customer ID
     * @param date the date
     * @return list of bookings
     */
    List<Booking> getCustomerBookingsByDate(String customerId, LocalDate date);
    
    /**
     * Check if customer has existing booking in same time slot on a given date.
     *
     * @param customerId the customer ID
     * @param date the booking date
     * @return list of existing bookings for that date
     */
    List<Booking> checkExistingBookingsOnDate(String customerId, LocalDate date);
    
    /**
     * Update booking status.
     *
     * @param bookingId the booking ID
     * @param status the new booking status
     * @return true if update successful, false otherwise
     */
    boolean updateBookingStatus(String bookingId, BookingStatus status);
    
    /**
     * Delete a booking.
     *
     * @param bookingId the booking ID
     * @return true if deletion successful, false otherwise
     */
    boolean deleteBooking(String bookingId);
    
    /**
     * Count total bookings for a customer.
     *
     * @param customerId the customer ID
     * @return the booking count
     */
    int countBookingsByCustomer(String customerId);
    
    /**
     * Cancel a booking and update slot availability.
     *
     * @param bookingId the booking ID
     * @return true if cancellation successful, false otherwise
     */
    boolean cancelBooking(String bookingId);
}
