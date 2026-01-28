package com.flipfit.bean;

import com.flipfit.enums.BookingStatus;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * The Class Booking.
 * Represents a booking made by a customer for a gym slot.
 * 
 * @author JEDI-BRAVO
 */
public class Booking {
	private String bookingId;
	private String customerId;
	private String slotId;
	private LocalDate bookingDate;
	private Timestamp bookingTime;
	private BookingStatus bookingStatus;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	
	// Helper fields (not in DB but useful for display)
	private GymUser gymUser;
	private GymSlot gymSlot;
	private String dateAndTime;
	
	public String getBookingId() {
		return bookingId;
	}
	
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getSlotId() {
		return slotId;
	}
	
	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}
	
	public LocalDate getBookingDate() {
		return bookingDate;
	}
	
	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}
	
	public Timestamp getBookingTime() {
		return bookingTime;
	}
	
	public void setBookingTime(Timestamp bookingTime) {
		this.bookingTime = bookingTime;
	}
	
	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}
	
	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	// Helper getters/setters
	public GymUser getGymUser() {
		return gymUser;
	}
	
	public void setGymUser(GymUser gymUser) {
		this.gymUser = gymUser;
	}
	
	public GymSlot getGymSlot() {
		return gymSlot;
	}
	
	public void setGymSlot(GymSlot gymSlot) {
		this.gymSlot = gymSlot;
	}
	
	public String getDateAndTime() {
		return dateAndTime;
	}
	
	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
}

