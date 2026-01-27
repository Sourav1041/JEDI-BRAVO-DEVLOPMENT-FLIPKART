package com.flipfit.business.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.business.GymOwnerService;
import com.flipfit.helper.DataStore;

public class GymOwnerServiceImpl implements GymOwnerService {

	@Override
	public List<GymCenter> viewMyCenters(String ownerId) {
		return DataStore.getAllCenters().stream().filter(center -> ownerId.equals(center.getOwnerId()))
				.collect(Collectors.toList());
	}

	@Override
	public void addCenter(GymCenter center) {
		DataStore.addCenter(center);
	}

	@Override
	public void addSlot(String centerId, GymSlot slot) {
		GymCenter center = DataStore.getCenter(centerId);
		if (center != null) {
			center.getCenterSlot().add(slot);
		}
	}

	@Override
	public Collection<Booking> viewBookingsForOwner(String ownerId) {
		List<GymCenter> centers = viewMyCenters(ownerId);
		return DataStore.getAllBookings().stream()
				.filter(b -> b.getGymSlot() != null && b.getGymUser() != null)
				.filter(b -> centers.stream().anyMatch(c -> c.getCenterSlot().contains(b.getGymSlot())))
				.collect(Collectors.toList());
	}
}

