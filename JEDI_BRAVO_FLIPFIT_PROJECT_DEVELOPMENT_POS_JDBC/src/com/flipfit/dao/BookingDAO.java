package com.flipfit.dao;

import com.flipfit.bean.Booking;
import com.flipfit.enums.BookingStatus;
import com.flipfit.helper.DataStore;

/**
 * Minimal DAO backed by in-memory collections to satisfy interface usage.
 */
public class BookingDAO {
	public void save(Booking booking) {
		DataStore.saveBooking(booking);
	}

	public void updateStatus(String bookingId, BookingStatus status) {
		Booking booking = DataStore.getBooking(bookingId);
		if (booking != null) {
			booking.setBookingStatus(status);
		}
	}
}

