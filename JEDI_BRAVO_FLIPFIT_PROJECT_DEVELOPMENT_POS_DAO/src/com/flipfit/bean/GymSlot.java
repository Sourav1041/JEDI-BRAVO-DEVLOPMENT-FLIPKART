package com.flipfit.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;

/**
 * The Class GymSlot.
 * Represents a specific time slot at a gym center.
 * Handles availability logic for the booking service.
 * 
 * @author JEDI-BRAVO
 */
public class GymSlot {
	private String slotId;
	private String gymId;
	private LocalTime startTime;
	private LocalTime endTime;
	private int totalSeats;
	private int availableSeats;
	private BigDecimal price;
	private boolean isActive;
	private Timestamp createdAt;

	public String getSlotId() {
		return slotId;
	}

	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}

	public String getGymId() {
		return gymId;
	}

	public void setGymId(String gymId) {
		this.gymId = gymId;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isAvailable() {
		return availableSeats > 0 && isActive;
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
