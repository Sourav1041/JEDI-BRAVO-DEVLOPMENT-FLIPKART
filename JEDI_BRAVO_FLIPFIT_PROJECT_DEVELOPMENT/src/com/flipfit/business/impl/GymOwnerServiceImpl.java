package com.flipfit.business.impl;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.business.GymOwnerService;
import com.flipfit.helper.DataStore;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
	public void modifySlot(String centerId, String slotId, LocalTime startTime, LocalTime endTime, Integer totalSeats, Integer availableSeats)
	{
		GymCenter center = DataStore.getCenter(centerId);

    	if (center == null) {
        	System.out.println("Gym Center not found");
        	return;
    	}

    	List<GymSlot> slots = center.getCenterSlot();

    	if (slots == null || slots.isEmpty()) {
        	System.out.println("Gym Slot not found");
        	return;
    	}

    	for (GymSlot slot : slots) {

        	if (slot.getSlotId().equals(slotId)) {

            	if (startTime != null) {
                	slot.setStartTime(startTime);
            	}

            	if (endTime != null) {
                	slot.setEndTime(endTime);
            	}

            	if (totalSeats != null) {
                	slot.setTotalSeats(totalSeats);
            	}

            	if (availableSeats != null) {
                	slot.setAvailableSeats(availableSeats);
            	}

            	System.out.println("Slot modified successfully");
            	return;
        	}
    	}
		System.out.println("Slot ID not found");
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

