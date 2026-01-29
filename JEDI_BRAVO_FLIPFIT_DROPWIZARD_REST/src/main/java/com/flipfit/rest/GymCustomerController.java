package com.flipfit.rest;

import com.flipfit.bean.*;
import com.flipfit.business.BookingService;
import com.flipfit.business.impl.BookingServiceImpl;
import com.flipfit.exception.BookingFailedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for gym customer operations.
 * Handles slot viewing, booking, cancellation, and notifications.
 * Uses for-each loops for iterations as per Java 8+ standards.
 * 
 * @author JEDI-BRAVO
 * @version 1.0
 * @since 2026-01-28
 */
@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymCustomerController {
    
    private final BookingService bookingService;
    
    /**
     * Constructor initializing booking service.
     */
    public GymCustomerController() {
        this.bookingService = new BookingServiceImpl();
    }
    
    /**
     * Get all available gym slots for a specific gym center.
     * 
     * @param gymId The gym center ID
     * @return Response with list of available slots
     */
    @GET
    @Path("/slots/available/{gymId}")
    public Response getAvailableSlots(@PathParam("gymId") String gymId) {
        try {
            List<GymSlot> slots = bookingService.viewAvailableSlots(gymId);
            return Response.ok(slots).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch slots: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get available gym slots by city.
     * 
     * @param city The city to filter slots
     * @return Response with list of available slots in the city
     */
    @GET
    @Path("/slots/city/{city}")
    public Response getSlotsByCity(@PathParam("city") String city) {
        try {
            List<GymSlot> slots = bookingService.viewAvailableSlotsByCity(city);
            return Response.ok(slots).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch slots: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get available slots for a gym center on a specific date with date-specific availability.
     * This is the key API for viewing "gym's availability for a particular day".
     * 
     * @param gymId The gym center ID
     * @param dateStr The date (YYYY-MM-DD format)
     * @return Response with slots showing date-specific availability
     */
    @GET
    @Path("/slots/available/{gymId}/{date}")
    public Response getAvailableSlotsForDate(
            @PathParam("gymId") String gymId,
            @PathParam("date") String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            List<GymSlot> slots = bookingService.viewAvailableSlots(gymId);
            
            // Enhance each slot with date-specific availability
            List<Map<String, Object>> slotsWithAvailability = new java.util.ArrayList<>();
            
            for (GymSlot slot : slots) {
                Map<String, Object> slotInfo = new HashMap<>();
                slotInfo.put("slotId", slot.getSlotId());
                slotInfo.put("gymId", slot.getGymId());
                slotInfo.put("startTime", slot.getStartTime());
                slotInfo.put("endTime", slot.getEndTime());
                slotInfo.put("totalSeats", slot.getTotalSeats());
                slotInfo.put("price", slot.getPrice());
                slotInfo.put("isActive", slot.isActive());
                
                // Calculate date-specific availability
                int bookedSeats = bookingService.countBookingsForSlotOnDate(slot.getSlotId(), date);
                int availableSeats = slot.getTotalSeats() - bookedSeats;
                
                slotInfo.put("availableSeats", availableSeats);
                slotInfo.put("bookedSeats", bookedSeats);
                slotInfo.put("bookingDate", dateStr);
                
                slotsWithAvailability.add(slotInfo);
            }
            
            return Response.ok(slotsWithAvailability).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch slots: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get all gym centers in a specific city.
     * 
     * @param city The city to search
     * @return Response with list of gym centers
     */
    @GET
    @Path("/centers/city/{city}")
    public Response getGymCentersByCity(@PathParam("city") String city) {
        try {
            List<GymCenter> centers = bookingService.viewGymCentersByCity(city);
            return Response.ok(centers).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch gym centers: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Book a gym slot.
     * 
     * @param bookingData Map containing customerId, slotId, and bookingDate
     * @return Response with booking confirmation
     */
    @POST
    @Path("/booking/create")
    public Response bookSlot(Map<String, String> bookingData) {
        try {
            String customerId = bookingData.get("customerId");
            String slotId = bookingData.get("slotId");
            String dateStr = bookingData.get("bookingDate");
            
            System.out.println("[DEBUG] Booking Request - Customer: " + customerId + ", Slot: " + slotId + ", Date: " + dateStr);
            
            LocalDate bookingDate = LocalDate.parse(dateStr);
            
            Booking booking = bookingService.bookSlot(customerId, slotId, bookingDate);
            
            if (booking != null) {
                System.out.println("[SUCCESS] Booking created: " + booking.getBookingId());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Slot booked successfully");
                response.put("bookingId", booking.getBookingId());
                response.put("customerId", customerId);
                response.put("slotId", slotId);
                response.put("bookingDate", bookingDate.toString());
                return Response.status(Response.Status.CREATED).entity(response).build();
            } else {
                System.out.println("[ERROR] Booking returned null");
                Map<String, String> error = new HashMap<>();
                error.put("error", "Booking failed. Slot may be full or invalid.");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
        } catch (BookingFailedException e) {
            System.out.println("[ERROR] BookingFailedException: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } catch (Exception e) {
            System.out.println("[ERROR] Exception: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Booking failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * View all bookings for a customer.
     * 
     * @param customerId The customer ID
     * @return Response with list of bookings
     */
    @GET
    @Path("/bookings/{customerId}")
    public Response viewBookings(@PathParam("customerId") String customerId) {
        try {
            List<Booking> bookings = bookingService.viewMyBookings(customerId);
            List<Map<String, Object>> enrichedBookings = new java.util.ArrayList<>();
            
            // Enhance each booking with slot and gym center details
            for (Booking booking : bookings) {
                Map<String, Object> bookingMap = new HashMap<>();
                bookingMap.put("bookingId", booking.getBookingId());
                bookingMap.put("customerId", booking.getCustomerId());
                bookingMap.put("slotId", booking.getSlotId());
                bookingMap.put("bookingDate", booking.getBookingDate());
                bookingMap.put("bookingStatus", booking.getBookingStatus());
                
                try {
                    java.sql.Connection conn = com.flipfit.utils.DBConnection.getConnection();
                    java.sql.PreparedStatement stmt = conn.prepareStatement(
                        "SELECT s.start_time, s.end_time, g.gym_name " +
                        "FROM GymSlot s " +
                        "JOIN GymCenter g ON s.gym_id = g.gym_id " +
                        "WHERE s.slot_id = ?"
                    );
                    stmt.setString(1, booking.getSlotId());
                    java.sql.ResultSet rs = stmt.executeQuery();
                    
                    if (rs.next()) {
                        bookingMap.put("gymName", rs.getString("gym_name"));
                        bookingMap.put("startTime", rs.getTime("start_time").toLocalTime());
                        bookingMap.put("endTime", rs.getTime("end_time").toLocalTime());
                    } else {
                        bookingMap.put("gymName", "Unknown");
                        bookingMap.put("startTime", null);
                        bookingMap.put("endTime", null);
                    }
                    rs.close();
                    stmt.close();
                } catch (Exception e) {
                    System.err.println("Error fetching slot details for " + booking.getSlotId() + ": " + e.getMessage());
                    e.printStackTrace();
                    bookingMap.put("gymName", "Error");
                    bookingMap.put("startTime", null);
                    bookingMap.put("endTime", null);
                }
                
                enrichedBookings.add(bookingMap);
            }
            
            return Response.ok(enrichedBookings).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch bookings: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Cancel a booking.
     * 
     * @param bookingId The booking ID to cancel
     * @return Response with cancellation status
     */
    @DELETE
    @Path("/booking/{bookingId}")
    public Response cancelBooking(@PathParam("bookingId") String bookingId) {
        try {
            boolean success = bookingService.cancelBooking(bookingId);
            
            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Booking cancelled successfully");
                response.put("bookingId", bookingId);
                return Response.ok(response).build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Cancellation failed. Booking may not exist.");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Cancellation failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Join waitlist for a slot.
     * 
     * @param waitlistData Map containing customerId, slotId, and requestedDate
     * @return Response with waitlist confirmation
     */
    @POST
    @Path("/waitlist/join")
    public Response joinWaitlist(Map<String, String> waitlistData) {
        try {
            String customerId = waitlistData.get("customerId");
            String slotId = waitlistData.get("slotId");
            LocalDate requestedDate = LocalDate.parse(waitlistData.get("requestedDate"));
            
            boolean success = bookingService.addToWaitList(customerId, slotId, requestedDate);
            
            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Added to waitlist successfully");
                response.put("customerId", customerId);
                response.put("slotId", slotId);
                return Response.status(Response.Status.CREATED).entity(response).build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Failed to join waitlist");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Waitlist operation failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Find nearest available slot.
     * 
     * @param gymId The gym center ID
     * @param preferredTime The preferred time
     * @return Response with nearest available slot
     */
    @GET
    @Path("/slots/nearest/{gymId}/{preferredTime}")
    public Response findNearestSlot(@PathParam("gymId") String gymId, 
                                   @PathParam("preferredTime") String preferredTime) {
        try {
            LocalTime time = LocalTime.parse(preferredTime);
            GymSlot slot = bookingService.findNearestAvailableSlot(gymId, time);
            
            if (slot != null) {
                return Response.ok(slot).build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("message", "No available slots found");
                return Response.status(Response.Status.NOT_FOUND).entity(error).build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Search failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get notifications for a customer.
     * 
     * @param customerId The customer ID
     * @return Response with list of notifications
     */
    @GET
    @Path("/notifications/{customerId}")
    public Response getNotifications(@PathParam("customerId") String customerId) {
        try {
            List<Notification> notifications = bookingService.getNotifications(customerId);
            return Response.ok(notifications).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch notifications: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
}
