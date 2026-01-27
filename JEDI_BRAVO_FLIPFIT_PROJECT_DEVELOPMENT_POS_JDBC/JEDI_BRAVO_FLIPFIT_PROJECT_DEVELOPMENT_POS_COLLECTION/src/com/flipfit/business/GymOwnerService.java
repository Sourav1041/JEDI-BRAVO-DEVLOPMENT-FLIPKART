package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface GymOwnerService {
	List<GymCenter> viewMyCenters(String ownerId);

	void addCenter(GymCenter center);

	void addSlot(String centerId, GymSlot slot);

	void modifySlot(String centerId, String slotId, LocalTime startTime, LocalTime endTime, Integer totalSeats, Integer availableSeats);

	Collection<Booking> viewBookingsForOwner(String ownerId);
}

