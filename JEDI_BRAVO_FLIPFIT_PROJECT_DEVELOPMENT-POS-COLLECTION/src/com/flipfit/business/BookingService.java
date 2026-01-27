package com.flipfit.business;

import java.util.Date;
import java.time.LocalTime;

public interface BookingService {
    // Standard creation and cancellation
	String createBooking(String userId, String centreId, LocalTime startTime, LocalTime endTime, Date date);
    
    boolean cancelBooking(String bookingId);
    
    boolean modifyBooking(String userId, String oldBookingId, LocalTime startTime, LocalTime endTime, Date date);
}    