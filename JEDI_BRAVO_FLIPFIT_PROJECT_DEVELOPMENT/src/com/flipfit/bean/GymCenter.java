package com.flipfit.bean;

import java.util.List;

public class GymCenter {

	private String centerId;
	private String centerLocn;
	private String centerCity;
	private List<GymSlot> centerSlot;
	
	public String getCenterId() {
		return centerId;
	}
	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}
	public String getCenterLocn() {
		return centerLocn;
	}
	public void setCenterLocn(String centerLocn) {
		this.centerLocn = centerLocn;
	}
	public String getCenterCity() {
		return centerCity;
	}
	public void setCenterCity(String centerCity) {
		this.centerCity = centerCity;
	}
	public List<GymSlot> getCenterSlot() {
		return centerSlot;
	}
	public void setCenterSlot(List<GymSlot> centerSlot) {
		this.centerSlot = centerSlot;
	}
}
