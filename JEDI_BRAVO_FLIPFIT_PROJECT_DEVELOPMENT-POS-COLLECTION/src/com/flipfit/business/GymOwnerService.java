package com.flipfit.business;

import java.util.Collection;
import java.util.List;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;

public interface GymOwnerService {
	List<GymCenter> viewMyCenters(String ownerId);

	void addCenter(GymCenter center);

	void addSlot(String centerId, GymSlot slot);

	Collection<Booking> viewBookingsForOwner(String ownerId);
}

