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
        
        // USER STORY 4: Error if no seats available (already booked by others)
        if (slot.getAvailableSeats() <= 0) {
            throw new BookingFailedException("No seats available in this slot. Slot is fully booked!");
        }
        
        // USER STORY 3: Check if customer has existing booking in same time slot on this date
        List<Booking> existingBookings = bookingDAO.checkExistingBookingsOnDate(customerId, bookingDate);
        
        // Check if any existing booking overlaps with the new slot time
        for (Booking existing : existingBookings) {
            GymSlot existingSlot = slotDAO.getSlotById(existing.getSlotId());
            if (existingSlot != null && timeSlotsOverlap(existingSlot, slot)) {
                System.out.println("Found existing booking in same time slot. Cancelling old booking...");
                boolean cancelled = bookingDAO.cancelBooking(existing.getBookingId());
                if (!cancelled) {
                    throw new BookingFailedException("Failed to cancel existing booking");
                }
                System.out.println("Old booking cancelled successfully");
            }
        }
        
        // USER STORY 2 & 5: Create new booking (with overbooking prevention)
        // Double-check availability (in case of concurrent bookings)
        int currentSeats = slotDAO.getAvailableSeats(slotId);
        if (currentSeats <= 0) {
            throw new BookingFailedException("No seats available. Slot just got fully booked!");
        }
        
        // Create booking
        Booking booking = new Booking();
        booking.setBookingId("BKG" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setCustomerId(customerId);
        booking.setSlotId(slotId);
        booking.setBookingDate(bookingDate);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        
        // Insert booking and decrease available seats
        boolean bookingCreated = bookingDAO.insertBooking(booking);
        boolean seatsUpdated = slotDAO.updateAvailableSeats(slotId, -1);
        
        if (bookingCreated && seatsUpdated) {
            // Send notification
            Notification notification = new Notification();
            notification.setNotificationId("NOT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            notification.setUserId(customerId);
            notification.setTitle("Booking Confirmed");
            notification.setMessage("Your booking (ID: " + booking.getBookingId() + ") has been confirmed for " + 
                                   bookingDate + " at " + slot.getStartTime());
            notification.setNotificationType("BOOKING");
            notification.setRead(false);
            notificationDAO.insertNotification(notification);
            
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
        
        boolean cancelled = bookingDAO.cancelBooking(bookingId);
        if (!cancelled) {
            throw new BookingFailedException("Failed to cancel booking");
        }
        
        // Send cancellation notification
        Notification notification = new Notification();
        notification.setNotificationId("NOT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        notification.setUserId(booking.getCustomerId());
        notification.setTitle("Booking Cancelled");
        notification.setMessage("Your booking (ID: " + bookingId + ") has been cancelled successfully.");
        notification.setNotificationType("CANCELLATION");
        notification.setRead(false);
        notificationDAO.insertNotification(notification);
        
        // BONUS STORY: Check waitlist and promote first customer
        GymWaitList firstWaiting = waitListDAO.getFirstWaitingCustomer(booking.getSlotId());
        if (firstWaiting != null) {
            try {
                bookSlot(firstWaiting.getCustomerId(), booking.getSlotId(), firstWaiting.getRequestedDate());
                waitListDAO.updateWaitListStatus(firstWaiting.getWaitlistId(), "ALLOCATED");
                
                Notification promoNotif = new Notification();
                promoNotif.setNotificationId("NOT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                promoNotif.setUserId(firstWaiting.getCustomerId());
                promoNotif.setTitle("Promoted from Waitlist");
                promoNotif.setMessage("Great news! You have been promoted from waitlist and booked for slot " + 
                                     booking.getSlotId() + " on " + firstWaiting.getRequestedDate());
                promoNotif.setNotificationType("BOOKING");
                promoNotif.setRead(false);
                notificationDAO.insertNotification(promoNotif);
                
                System.out.println("Waitlist customer promoted automatically!");
            } catch (BookingFailedException e) {
                System.err.println("Failed to promote waitlist customer: " + e.getMessage());
            }
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
     * Add customer to waitlist for a full slot.
     * BONUS STORY: Waiting list implementation
     *
     * @param customerId the customer ID
     * @param slotId the slot ID
     * @param requestedDate the requested date
     * @return true if added to waitlist
     */
    @Override
    public boolean addToWaitList(String customerId, String slotId, LocalDate requestedDate) {
        GymWaitList waitList = new GymWaitList();
        waitList.setWaitlistId("WL" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        waitList.setCustomerId(customerId);
        waitList.setSlotId(slotId);
        waitList.setRequestedDate(requestedDate);
        waitList.setStatus("WAITING");
        
        boolean added = waitListDAO.insertWaitList(waitList);
        if (added) {
            // Send notification
            Notification notification = new Notification();
            notification.setNotificationId("NOT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            notification.setUserId(customerId);
            notification.setTitle("Added to Waitlist");
            notification.setMessage("You have been added to waitlist for slot " + slotId + 
                                   ". You'll be notified when a seat becomes available.");
            notification.setNotificationType("GENERAL");
            notification.setRead(false);
            notificationDAO.insertNotification(notification);
        }
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
}
