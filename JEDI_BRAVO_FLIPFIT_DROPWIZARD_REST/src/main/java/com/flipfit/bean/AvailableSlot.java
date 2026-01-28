package com.flipfit.bean;


public class AvailableSlot {
	
	private GymSlot gymSlot;
	private int SeatsAvailable;
	private String date;
	
	public GymSlot getGymSlot() {
		return gymSlot;
	}
	public void setGymSlot(GymSlot gymSlot) {
		this.gymSlot = gymSlot;
	}
	public int getSeatsAvailable() {
		return SeatsAvailable;
	}
	public void setSeatsAvailable(int seatsAvailable) {
		SeatsAvailable = seatsAvailable;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
