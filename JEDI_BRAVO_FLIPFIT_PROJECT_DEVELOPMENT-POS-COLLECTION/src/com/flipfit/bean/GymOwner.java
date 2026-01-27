package com.flipfit.bean;

public class GymOwner {
	private GymUser user;
	private GymCenter gym;
	
	public GymUser getUser() {
		return user;
	}
	public void setUser(GymUser user) {
		this.user = user;
	}
	public GymCenter getGym() {
		return gym;
	}
	public void setGym(GymCenter gym) {
		this.gym = gym;
	}
	
}
