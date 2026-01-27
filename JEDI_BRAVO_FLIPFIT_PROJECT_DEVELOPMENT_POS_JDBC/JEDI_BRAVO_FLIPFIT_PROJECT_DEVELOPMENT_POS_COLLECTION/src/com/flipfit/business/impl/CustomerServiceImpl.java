package com.flipfit.business.impl;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.business.CustomerService;
import com.flipfit.business.BookingService;
import com.flipfit.helper.DataStore;

public class CustomerServiceImpl implements CustomerService {

	private final BookingService bookingService;

	public CustomerServiceImpl(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@Override
	public List<GymCenter> viewCentersByCity(String city) {
		return DataStore.getAllCenters().stream()
				.filter(center -> center.getCenterCity().equalsIgnoreCase(city))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<GymSlot> viewSlotsForCenter(String centerId) {
		GymCenter center = DataStore.getCenter(centerId);
		if (center == null) {
			return List.of();
		}
		return center.getCenterSlot();
	}

	@Override
	public String bookSlot(String userId, String centerId, LocalTime startTime, LocalTime endTime, Date date) {
		return bookingService.createBooking(userId, centerId, startTime, endTime, date);
	}

	@Override
	public List<Booking> viewBookings(String userId) {
		return DataStore.getAllBookings().stream()
				.filter(b -> b.getGymUser() != null && userId.equals(b.getGymUser().getUserId()))
				.collect(Collectors.toList());
	}

	@Override
	public boolean cancelBooking(String bookingId) {
		return bookingService.cancelBooking(bookingId);
	}


}

