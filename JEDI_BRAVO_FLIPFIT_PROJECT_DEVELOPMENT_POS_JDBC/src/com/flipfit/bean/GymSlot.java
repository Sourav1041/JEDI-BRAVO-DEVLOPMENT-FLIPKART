package com.flipfit.bean;

import java.time.LocalTime;

/**
 * Represents a specific time slot at a gym center.
 * Handles availability logic for the booking service.
 */
public class GymSlot {
	private String slotId;
	private LocalTime startTime;
	private LocalTime endTime;
	private int totalSeats;
	private int availableSeats;

	public String getSlotId() {
		return slotId;
	}

	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public boolean isAvailable() {
		return availableSeats > 0;
	}

	public void decreaseAvailability() {
		if (availableSeats > 0) {
			availableSeats--;
		}
	}

	public void increaseAvailability() {
		if (availableSeats < totalSeats) {
			availableSeats++;
		}
	}
}