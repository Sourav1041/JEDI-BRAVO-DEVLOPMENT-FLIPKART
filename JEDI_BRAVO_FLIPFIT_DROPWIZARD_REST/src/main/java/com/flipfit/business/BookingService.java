package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.Notification;
import com.flipfit.exception.BookingFailedException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface BookingService.
 * Defines business operations for booking management
 * 
 * @author JEDI-BRAVO
 * @ClassName BookingService
 */
public interface BookingService {
    
    /**
     * Book a slot for a customer on a specific date.
     * Implements all business rules:
     * - Check seat availability
     * - Prevent overbooking
     * - Auto-cancel existing booking in same time slot
     * - Update slot availability
     *
     * @param customerId the customer ID
     * @param slotId the slot ID
     * @param bookingDate the date for the booking
     * @return the created booking
     * @throws BookingFailedException if booking fails
     */
    Booking bookSlot(String customerId, String slotId, LocalDate bookingDate) throws BookingFailedException;
    
    /**
     * Cancel a booking.
     *
     * @param bookingId the booking ID
     * @return true if cancellation successful
     * @throws BookingFailedException if cancellation fails
     */
    boolean cancelBooking(String bookingId) throws BookingFailedException;
    
    /**
     * View all bookings for a customer.
     *
     * @param customerId the customer ID
     * @return list of bookings
     */
    List<Booking> viewMyBookings(String customerId);
    
    /**
     * View customer's plan for a specific date.
     *
     * @param customerId the customer ID
     * @param date the date
     * @return list of bookings for that date
     */
    List<Booking> viewPlanByDate(String customerId, LocalDate date);
    
    /**
     * View all available slots for a gym.
     *
     * @param gymId the gym ID
     * @return list of available slots
     */
    List<GymSlot> viewAvailableSlots(String gymId);
    
    /**
     * View all available slots by city.
     *
     * @param city the city name
     * @return list of available slots
     */
    List<GymSlot> viewAvailableSlotsByCity(String city);
    
    /**
     * View all gym centers by city.
     *
     * @param city the city name
     * @return list of gym centers
     */
    List<GymCenter> viewGymCentersByCity(String city);
    
    /**
     * Check if a slot has available seats.
     *
     * @param slotId the slot ID
     * @return true if seats available
     */
    boolean checkSlotAvailability(String slotId);
    
    /**
     * Get booking by ID.
     *
     * @param bookingId the booking ID
     * @return the booking
     */
    Booking getBookingById(String bookingId);
    
    /**
     * Add customer to waitlist for a full slot.
     *
     * @param customerId the customer ID
     * @param slotId the slot ID
     * @param requestedDate the requested date
     * @return true if added to waitlist
     */
    boolean addToWaitList(String customerId, String slotId, LocalDate requestedDate);
    
    /**
     * Find nearest available slot for a given gym.
     *
     * @param gymId the gym ID
     * @param preferredTime the preferred time
     * @return the nearest available slot
     */
    GymSlot findNearestAvailableSlot(String gymId, LocalTime preferredTime);
    
    /**
     * Get notifications for user.
     *
     * @param userId the user ID
     * @return list of notifications
     */
    List<Notification> getNotifications(String userId);
}
