package com.flipfit.business.impl;

import com.flipfit.business.BookingService;
import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymUser;
import com.flipfit.enums.BookingStatus;
import com.flipfit.helper.DataStore;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {

	@Override
	public String createBooking(String userId, String centreId, LocalTime startTime, LocalTime endTime, Date date) {
		GymUser user = DataStore.getUser(userId);
		if (user == null) {
			System.out.println("User not found.");
			return null;
		}

		GymCenter center = DataStore.getCenter(centreId);
		if (center == null) {
			System.out.println("Center not found.");
			return null;
		}

		GymSlot slot = findSlot(center, startTime, endTime);
		if (slot == null) {
			System.out.println("No matching slot found.");
			return null;
		}

		LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
		boolean capacity = hasCapacity(slot, localDate);

		// Remove older booking if user already has one for the same start time on same date
		checkOverlapAndRemove(userId, localDate, startTime);

		String bookingId = DataStore.nextBookingId();
		Booking booking = new Booking();
		booking.setBookingId(bookingId);
		booking.setGymUser(user);
		booking.setGymSlot(slot);
		booking.setDateAndTime(localDate.toString() + " " + startTime.toString());
		if (capacity) {
			booking.setBookingStatus(BookingStatus.CONFIRMED);
			DataStore.saveBooking(booking);
			return bookingId;
		} else {
			booking.setBookingStatus(BookingStatus.WAITLIST);
			DataStore.addToWaitlist(slot.getSlotId(), booking);
			System.out.println("Slot full. Added to waitlist with id: " + bookingId);

			//suggesting nearest time slot
			LocalTime suggestion = nearestTimeSlot(center, localDate, startTime);
			if (suggestion != null) {
				System.out.println("Notification: The slot at " + startTime + " is full. " +
						"The next slot available is at " + suggestion + ".");
			} else {
				System.out.println("Notification: The slot at " + startTime + " is full. " +
						"There are no other time slots available for the day.");
			}

			return bookingId;
		}
	}

	@Override
	public boolean cancelBooking(String bookingId) {
		Booking booking = DataStore.removeBooking(bookingId);
		if (booking == null) {
			System.out.println("Booking not found.");
			return false;
		}
		if (booking.getGymSlot() != null) {
			booking.getGymSlot().increaseAvailability();
			promoteWaitlisted(booking.getGymSlot());
		}
		return true;
	}

	@Override
	public boolean modifyBooking(String userId, String oldBookingId, LocalTime startTime, LocalTime endTime, Date date) {
		String centreId = null;
		if (oldBookingId != null) {
			Booking existing = DataStore.getBooking(oldBookingId);
			if (existing != null && existing.getGymSlot() != null) {
				centreId = resolveCenterId(existing.getGymSlot());
			}
			cancelBooking(oldBookingId);
		}
		if (centreId == null) {
			centreId = "C1";
		}
		String newId = createBooking(userId, centreId, startTime, endTime, date);
		return newId != null;
	}

	private GymSlot findSlot(GymCenter center, LocalTime startTime, LocalTime endTime) {
		return center.getCenterSlot().stream()
				.filter(slot -> slot.getStartTime().equals(startTime) && slot.getEndTime().equals(endTime)).findFirst()
				.orElse(null);
	}

	private void checkOverlapAndRemove(String userId, LocalDate date, LocalTime time) {
		String key = date.toString() + " " + time.toString();
		Optional<Booking> existing = DataStore.getAllBookings().stream()
				.filter(b -> b.getGymUser() != null && userId.equals(b.getGymUser().getUserId()))
				.filter(b -> key.equals(b.getDateAndTime())).findFirst();
		existing.ifPresent(b -> {
			System.out.println("Conflict detected: existing booking " + b.getBookingId() + " at " + key
					+ ". Cancelling it before creating the new booking.");
			cancelBooking(b.getBookingId());
		});
	}

	private boolean hasCapacity(GymSlot slot, LocalDate date) {
		long bookedCount = DataStore.getAllBookings().stream()
				.filter(b -> b.getGymSlot() != null && slot.getSlotId().equals(b.getGymSlot().getSlotId()))
				.filter(b -> b.getDateAndTime() != null && b.getDateAndTime().startsWith(date.toString()))
				.count();
		return bookedCount < slot.getTotalSeats();
	}

	private void promoteWaitlisted(GymSlot slot) {
		Booking next = DataStore.popWaitlist(slot.getSlotId());
		if (next == null) {
			return;
		}
		next.setBookingStatus(BookingStatus.CONFIRMED);
		DataStore.saveBooking(next);
		System.out.println("Promoted waitlisted booking: " + next.getBookingId());
	}

	private String resolveCenterId(GymSlot slot) {
		for (GymCenter center : DataStore.getAllCenters()) {
			if (center.getCenterSlot().contains(slot)) {
				return center.getCenterId();
			}
		}
		return null;
	}

	private LocalTime nearestTimeSlot(GymCenter center, LocalDate date, LocalTime requestedStartTime) {
		return center.getCenterSlot().stream()
				.filter(slot -> slot.getStartTime().isAfter(requestedStartTime))
				.filter(slot -> hasCapacity(slot, date))
				.sorted((s1, s2) -> s1.getStartTime().compareTo(s2.getStartTime()))
				.map(GymSlot::getStartTime)
				.findFirst()
				.orElse(null); // Returns null if no later slot has capacity
	}
}
