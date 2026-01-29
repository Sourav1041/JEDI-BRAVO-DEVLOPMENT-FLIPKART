package com.flipfit.business.impl;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymWaitList;
import com.flipfit.bean.Notification;
import com.flipfit.business.BookingService;
import com.flipfit.dao.BookingDAO;
import com.flipfit.dao.GymSlotDAO;
import com.flipfit.dao.GymCenterDAO;
import com.flipfit.dao.WaitListDAO;
import com.flipfit.dao.NotificationDAO;
import com.flipfit.dao.impl.BookingDAOImpl;
import com.flipfit.dao.impl.GymSlotDAOImpl;
import com.flipfit.dao.impl.GymCenterDAOImpl;
import com.flipfit.dao.impl.WaitListDAOImpl;
import com.flipfit.dao.impl.NotificationDAOImpl;
import com.flipfit.enums.BookingStatus;
import com.flipfit.exception.BookingFailedException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class BookingServiceImpl.
 * Implementation of BookingService interface
 * Contains all business logic for booking operations
 * 
 * @author JEDI-BRAVO
 * @ClassName BookingServiceImpl
 */
public class BookingServiceImpl implements BookingService {
    
    private final BookingDAO bookingDAO = new BookingDAOImpl();
    private final GymSlotDAO slotDAO = new GymSlotDAOImpl();
    private final GymCenterDAO centerDAO = new GymCenterDAOImpl();
    private final WaitListDAO waitListDAO = new WaitListDAOImpl();
    private final NotificationDAO notificationDAO = new NotificationDAOImpl();

    /**
     * Book a slot for a customer on a specific date.
     * USER STORY 2, 3, 4, 5 IMPLEMENTATION:
     * - Successfully book a slot if seats available
     * - Auto-cancel old booking in same time slot
     * - Error if slot already full
     * - Prevent overbooking
     *
     * @param customerId the customer ID
     * @param slotId the slot ID
     * @param bookingDate the date for the booking
     * @return the created booking
     * @throws BookingFailedException if booking fails
     */
    @Override
    public synchronized Booking bookSlot(String customerId, String slotId, LocalDate bookingDate) 
            throws BookingFailedException {
        
        // USER STORY 5: Check seat availability and prevent overbooking
        GymSlot slot = slotDAO.getSlotById(slotId);
        if (slot == null) {
            throw new BookingFailedException("Slot not found");
        }
        
        if (!slot.isActive()) {
            throw new BookingFailedException("Slot is not active");
        }
        
        // USER STORY 4: Check date-specific availability
        // Count confirmed bookings for this slot on this specific date
        int bookedSeatsForDate = bookingDAO.countBookingsForSlotOnDate(slotId, bookingDate);
        int availableSeatsForDate = slot.getTotalSeats() - bookedSeatsForDate;
        
        System.out.println("Slot " + slotId + " on " + bookingDate + ": Total=" + slot.getTotalSeats() + 
                          ", Booked=" + bookedSeatsForDate + ", Available=" + availableSeatsForDate);
        
        // Error if no seats available for this specific date
        if (availableSeatsForDate <= 0) {
            throw new BookingFailedException("No seats available in this slot for " + bookingDate + ". Slot is fully booked!");
        }
        
        // USER STORY 3: Check if customer has existing CONFIRMED booking in same time slot on this date
        List<Booking> existingBookings = bookingDAO.checkExistingBookingsOnDate(customerId, bookingDate);
        
        System.out.println("\n===== CONFLICT DETECTION START =====");
        System.out.println("Customer: " + customerId + " | Date: " + bookingDate);
        System.out.println("New Slot: " + slotId + " | Time: " + slot.getStartTime() + " - " + slot.getEndTime());
        System.out.println("Existing bookings found: " + existingBookings.size());
        
        // Check if any existing CONFIRMED booking overlaps with the new slot time
        for (Booking existing : existingBookings) {
            System.out.println("\n--- Checking existing booking: " + existing.getBookingId());
            System.out.println("    Status: " + existing.getBookingStatus());
            System.out.println("    Slot ID: " + existing.getSlotId());
            
            // Skip cancelled bookings
            if (existing.getBookingStatus() == BookingStatus.CANCELLED) {
                System.out.println("    SKIPPED: Already cancelled");
                continue;
            }
            
            GymSlot existingSlot = slotDAO.getSlotById(existing.getSlotId());
            if (existingSlot != null) {
                System.out.println("    Existing slot time: " + existingSlot.getStartTime() + " - " + existingSlot.getEndTime());
                boolean overlaps = timeSlotsOverlap(existingSlot, slot);
                System.out.println("    Time overlap: " + overlaps);
                
                if (overlaps) {
                    System.out.println("    CONFLICT DETECTED! Cancelling old booking: " + existing.getBookingId());
                    // Use cancelBookingInternal to avoid waitlist promotion during conflict resolution
                    try {
                        cancelBookingInternal(existing.getBookingId(), existing.getSlotId(), false);
                        System.out.println("    Old booking cancelled successfully");
                    } catch (BookingFailedException e) {
                        System.err.println("    Failed to cancel conflicting booking: " + e.getMessage());
                        throw new BookingFailedException("Could not cancel conflicting booking: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("    ERROR: Could not fetch existing slot details");
            }
        }
        System.out.println("===== CONFLICT DETECTION END =====\n");
        
        // USER STORY 2 & 5: Create new booking (with overbooking prevention)
        // Double-check date-specific availability (in case of concurrent bookings)
        int finalBookedSeats = bookingDAO.countBookingsForSlotOnDate(slotId, bookingDate);
        int finalAvailableSeats = slot.getTotalSeats() - finalBookedSeats;
        
        if (finalAvailableSeats <= 0) {
            throw new BookingFailedException("No seats available. Slot just got fully booked for " + bookingDate + "!");
        }
        
        // Check if there's an existing CANCELLED booking for this customer/slot/date
        // If so, reuse it instead of creating a new one (avoids unique constraint violation)
        Booking booking = null;
        boolean isReusingCancelledBooking = false;
        
        for (Booking existing : existingBookings) {
            if (existing.getBookingStatus() == BookingStatus.CANCELLED && 
                existing.getSlotId().equals(slotId)) {
                // Found a cancelled booking for the same slot - reuse it
                booking = existing;
                isReusingCancelledBooking = true;
                System.out.println("Reusing cancelled booking: " + booking.getBookingId());
                break;
            }
        }
        
        boolean bookingCreated;
        if (isReusingCancelledBooking) {
            // Update existing cancelled booking to CONFIRMED
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingCreated = bookingDAO.updateBookingStatus(booking.getBookingId(), BookingStatus.CONFIRMED);
        } else {
            // Create new booking
            booking = new Booking();
            booking.setBookingId("BKG" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            booking.setCustomerId(customerId);
            booking.setSlotId(slotId);
            booking.setBookingDate(bookingDate);
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            
            // Insert booking (no need to update global seat count - we calculate per date)
            bookingCreated = bookingDAO.insertBooking(booking);
        }
        
        if (bookingCreated) {
            // Send notification (wrapped in try-catch to not break booking if notification fails)
            try {
                String userId = getUserIdFromCustomerId(customerId);
                Notification notification = new Notification();
                notification.setNotificationId("NOT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                notification.setUserId(userId);
                notification.setTitle("Booking Confirmed");
                notification.setMessage("Your booking (ID: " + booking.getBookingId() + ") has been confirmed for " + 
                                       bookingDate + " at " + slot.getStartTime());
                notification.setNotificationType("BOOKING");
                notification.setRead(false);
                notificationDAO.insertNotification(notification);
                System.out.println("Notification sent to user: " + userId);
            } catch (Exception e) {
                System.err.println("Warning: Failed to send notification: " + e.getMessage());
            }
            
            System.out.println("Booking successful! Booking ID: " + booking.getBookingId());
            return booking;
        } else {
            throw new BookingFailedException("Failed to create booking");
        }
    }

    /**
     * Cancel a booking.
     * BONUS STORY: Cancel a workout booking
     *
     * @param bookingId the booking ID
     * @return true if cancellation successful
     * @throws BookingFailedException if cancellation fails
     */
    @Override
    public boolean cancelBooking(String bookingId) throws BookingFailedException {
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking == null) {
            throw new BookingFailedException("Booking not found");
        }
        
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BookingFailedException("Booking is already cancelled");
        }
        
        return cancelBookingInternal(bookingId, booking.getSlotId(), true);
    }
    
    /**
     * Internal method to cancel a booking with optional waitlist promotion.
     *
     * @param bookingId the booking ID
     * @param slotId the slot ID
     * @param promoteWaitlist whether to promote waitlist customers
     * @return true if cancellation successful
     * @throws BookingFailedException if cancellation fails
     */
    private boolean cancelBookingInternal(String bookingId, String slotId, boolean promoteWaitlist) 
            throws BookingFailedException {
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking == null) {
            throw new BookingFailedException("Booking not found");
        }
        
        boolean cancelled = bookingDAO.cancelBooking(bookingId);
        if (!cancelled) {
            throw new BookingFailedException("Failed to cancel booking");
        }
        
        // No need to update global seats - availability is calculated per date
        System.out.println("Booking cancelled for slot: " + slotId + " on date: " + booking.getBookingDate());
        
        // Send cancellation notification (wrapped in try-catch)
        try {
            String userId = getUserIdFromCustomerId(booking.getCustomerId());
            Notification notification = new Notification();
            notification.setNotificationId("NOT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            notification.setUserId(userId); // Use actual user_id from User table
            notification.setTitle("Booking Cancelled");
            notification.setMessage("Your booking (ID: " + bookingId + ") has been cancelled successfully.");
            notification.setNotificationType("CANCELLATION");
            notification.setRead(false);
            notificationDAO.insertNotification(notification);
            System.out.println("Cancellation notification sent to user: " + userId);
        } catch (Exception e) {
            System.err.println("Warning: Failed to send cancellation notification: " + e.getMessage());
        }
        
        // BONUS STORY: Check waitlist and promote first customer (DATE-SPECIFIC)
        if (promoteWaitlist) {
            System.out.println("\n===== WAITLIST PROMOTION START =====");
            System.out.println("Checking waitlist for slot: " + slotId + " on date: " + booking.getBookingDate());
            
            // IMPORTANT: Get the first waiting customer for THIS SPECIFIC DATE
            GymWaitList firstWaiting = waitListDAO.getFirstWaitingCustomerByDate(slotId, booking.getBookingDate());
            
            if (firstWaiting != null) {
                System.out.println("Found waitlisted customer: " + firstWaiting.getCustomerId());
                System.out.println("Requested date: " + firstWaiting.getRequestedDate());
                System.out.println("Attempting to promote...");
                
                try {
                    // Book the slot for the waitlisted customer
                    Booking promotedBooking = bookSlot(firstWaiting.getCustomerId(), slotId, firstWaiting.getRequestedDate());
                    
                    // Update waitlist status to ALLOCATED
                    waitListDAO.updateWaitListStatus(firstWaiting.getWaitlistId(), "ALLOCATED");
                    
                    // Send promotion notification (wrapped in try-catch)
                    try {
                        String userId = getUserIdFromCustomerId(firstWaiting.getCustomerId());
                        Notification promoNotif = new Notification();
                        promoNotif.setNotificationId("NOT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                        promoNotif.setUserId(userId); // Use actual user_id from User table
                        promoNotif.setTitle("Promoted from Waitlist!");
                        promoNotif.setMessage("Great news! You have been promoted from waitlist and booked for slot " + 
                                             slotId + " on " + firstWaiting.getRequestedDate() + 
                                             ". Booking ID: " + promotedBooking.getBookingId());
                        promoNotif.setNotificationType("PROMOTION");
                        promoNotif.setRead(false);
                        notificationDAO.insertNotification(promoNotif);
                        System.out.println("Promotion notification sent to user: " + userId);
                    } catch (Exception e) {
                        System.err.println("Warning: Failed to send promotion notification: " + e.getMessage());
                    }
                    
                    System.out.println("Waitlist customer promoted successfully! Booking ID: " + promotedBooking.getBookingId());
                } catch (BookingFailedException e) {
                    System.err.println("Failed to promote waitlist customer: " + e.getMessage());
                    // If promotion fails, seat remains available for others
                }
            } else {
                System.out.println("No customers in waitlist for this slot on " + booking.getBookingDate());
            }
            System.out.println("===== WAITLIST PROMOTION END =====\n");
        }
        
        return true;
    }

    /**
     * View all bookings for a customer.
     *
     * @param customerId the customer ID
     * @return list of bookings
     */
    @Override
    public List<Booking> viewMyBookings(String customerId) {
        return bookingDAO.getBookingsByCustomer(customerId);
    }

    /**
     * View customer's plan for a specific date.
     * BONUS STORY: View plan by day
     *
     * @param customerId the customer ID
     * @param date the date
     * @return list of bookings for that date
     */
    @Override
    public List<Booking> viewPlanByDate(String customerId, LocalDate date) {
        return bookingDAO.getCustomerBookingsByDate(customerId, date);
    }

    /**
     * View all available slots for a gym.
     *
     * @param gymId the gym ID
     * @return list of available slots
     */
    @Override
    public List<GymSlot> viewAvailableSlots(String gymId) {
        return slotDAO.getSlotsByCenter(gymId);
    }

    /**
     * View all available slots by city.
     * USER STORY 1: View all centers with details for a given city
     *
     * @param city the city name
     * @return list of available slots
     */
    @Override
    public List<GymSlot> viewAvailableSlotsByCity(String city) {
        return slotDAO.getSlotsByCity(city);
    }

    /**
     * View all gym centers by city.
     * USER STORY 1: View all centers with details for a given city
     *
     * @param city the city name
     * @return list of gym centers
     */
    @Override
    public List<GymCenter> viewGymCentersByCity(String city) {
        return centerDAO.getGymCentersByCity(city);
    }

    /**
     * Check if a slot has available seats.
     *
     * @param slotId the slot ID
     * @return true if seats available
     */
    @Override
    public boolean checkSlotAvailability(String slotId) {
        return slotDAO.checkSlotAvailability(slotId);
    }

    /**
     * Get booking by ID.
     *
     * @param bookingId the booking ID
     * @return the booking
     */
    @Override
    public Booking getBookingById(String bookingId) {
        return bookingDAO.getBookingById(bookingId);
    }

    /**
     * Add customer to waitlist for a full slot on a specific date.
     * BONUS STORY: Waiting list implementation (DATE-SPECIFIC)
     *
     * @param customerId the customer ID
     * @param slotId the slot ID
     * @param requestedDate the requested date
     * @return true if added to waitlist
     */
    @Override
    public boolean addToWaitList(String customerId, String slotId, LocalDate requestedDate) {
        System.out.println("\n===== ADD TO WAITLIST =====");
        System.out.println("Customer: " + customerId);
        System.out.println("Slot: " + slotId);
        System.out.println("Requested Date: " + requestedDate);
        
        // Check if customer is already in waitlist for this slot on this date
        if (waitListDAO.isCustomerInWaitlistByDate(customerId, slotId, requestedDate)) {
            System.out.println("Customer is already in waitlist for this slot on this date");
            return false;
        }
        
        // Check if slot is actually full for this date
        GymSlot slot = slotDAO.getSlotById(slotId);
        if (slot == null) {
            System.out.println("Slot not found");
            return false;
        }
        
        int bookedSeatsForDate = bookingDAO.countBookingsForSlotOnDate(slotId, requestedDate);
        int availableSeatsForDate = slot.getTotalSeats() - bookedSeatsForDate;
        
        if (availableSeatsForDate > 0) {
            System.out.println("Slot is not full (" + availableSeatsForDate + " seats available). Customer should book directly.");
            return false;
        }
        
        // Add to waitlist
        GymWaitList waitList = new GymWaitList();
        waitList.setWaitlistId("WL" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        waitList.setCustomerId(customerId);
        waitList.setSlotId(slotId);
        waitList.setRequestedDate(requestedDate);
        waitList.setStatus("WAITING");
        
        boolean added = waitListDAO.insertWaitList(waitList);
        if (added) {
            System.out.println("Added to waitlist successfully! Waitlist ID: " + waitList.getWaitlistId());
            
            // Send notification (wrapped in try-catch)
            try {
                String userId = getUserIdFromCustomerId(customerId);
                Notification notification = new Notification();
                notification.setNotificationId("NOT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                notification.setUserId(userId); // Use actual user_id from User table
                notification.setTitle("Added to Waitlist");
                notification.setMessage("You have been added to waitlist for slot " + slotId + " on " + requestedDate + 
                                       ". You'll be notified when a seat becomes available.");
                notification.setNotificationType("GENERAL");
                notification.setRead(false);
                notificationDAO.insertNotification(notification);
                System.out.println("Waitlist notification sent to user: " + userId);
            } catch (Exception e) {
                System.err.println("Warning: Failed to send waitlist notification: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to add to waitlist");
        }
        System.out.println("===== ADD TO WAITLIST END =====\n");
        return added;
    }

    /**
     * Find nearest available slot for a given gym.
     * BONUS STORY: Return nearest time slot
     *
     * @param gymId the gym ID
     * @param preferredTime the preferred time
     * @return the nearest available slot
     */
    @Override
    public GymSlot findNearestAvailableSlot(String gymId, LocalTime preferredTime) {
        List<GymSlot> allSlots = slotDAO.getSlotsByCenter(gymId);
        
        if (allSlots.isEmpty()) {
            return null;
        }
        
        GymSlot nearestSlot = null;
        long minTimeDiff = Long.MAX_VALUE;
        
        for (GymSlot slot : allSlots) {
            if (slot.getAvailableSeats() > 0 && slot.isActive()) {
                long timeDiff = Math.abs(
                    slot.getStartTime().toSecondOfDay() - preferredTime.toSecondOfDay()
                );
                
                if (timeDiff < minTimeDiff) {
                    minTimeDiff = timeDiff;
                    nearestSlot = slot;
                }
            }
        }
        
        return nearestSlot;
    }

    /**
     * Get notifications for user.
     *
     * @param userId the user ID
     * @return list of notifications
     */
    @Override
    public List<Notification> getNotifications(String userId) {
        return notificationDAO.getNotificationsByUser(userId);
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
        return bookingDAO.countBookingsForSlotOnDate(slotId, date);
    }

    /**
     * Helper method to check if two time slots overlap.
     *
     * @param slot1 the first slot
     * @param slot2 the second slot
     * @return true if slots overlap
     */
    private boolean timeSlotsOverlap(GymSlot slot1, GymSlot slot2) {
        return !(slot1.getEndTime().isBefore(slot2.getStartTime()) || 
                 slot1.getStartTime().isAfter(slot2.getEndTime()));
    }
    
    /**
     * Helper method to get user_id from customer_id for notifications.
     * Notifications require user_id (from User table), not customer_id.
     *
     * @param customerId the customer ID
     * @return the corresponding user ID, or customerId as fallback
     */
    private String getUserIdFromCustomerId(String customerId) {
        try (java.sql.Connection conn = com.flipfit.utils.DBConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT user_id FROM GymCustomer WHERE customer_id = ?")) {
            
            pstmt.setString(1, customerId);
            java.sql.ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String userId = rs.getString("user_id");
                rs.close();
                return userId;
            }
            rs.close();
        } catch (Exception e) {
            System.err.println("Error fetching user_id from customer_id: " + e.getMessage());
        }
        // Fallback to customerId if fetch fails
        return customerId;
    }
}
