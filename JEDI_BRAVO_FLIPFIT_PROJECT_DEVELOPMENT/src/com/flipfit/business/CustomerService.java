package com.flipfit.business;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;

public interface CustomerService {
	List<GymCenter> viewCentersByCity(String city);

	Collection<GymSlot> viewSlotsForCenter(String centerId);

	String bookSlot(String userId, String centerId, LocalTime startTime, LocalTime endTime, Date date);

	List<Booking> viewBookings(String userId);

	boolean cancelBooking(String bookingId);

}

