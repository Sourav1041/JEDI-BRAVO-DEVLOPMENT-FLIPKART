package com.flipfit.bean;

import java.sql.Timestamp;

/**
 * The Class GymOwner.
 * Represents a gym owner with their details.
 * 
 * @author JEDI-BRAVO
 */
public class GymOwner {
	private String ownerId;
	private String userId;
	private String panCard;
	private String aadharCard;
	private String gstNumber;
	private boolean isApproved;
	private Timestamp approvalDate;
	private Timestamp createdAt;
	
	// Helper fields (not in DB)
	private GymUser user;
	private GymCenter gym;
	
	public String getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPanCard() {
		return panCard;
	}
	
	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}
	
	public String getAadharCard() {
		return aadharCard;
	}
	
	public void setAadharCard(String aadharCard) {
		this.aadharCard = aadharCard;
	}
	
	public String getGstNumber() {
		return gstNumber;
	}
	
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	
	public boolean isApproved() {
		return isApproved;
	}
	
	public void setApproved(boolean approved) {
		isApproved = approved;
	}
	
	public Timestamp getApprovalDate() {
		return approvalDate;
	}
	
	public void setApprovalDate(Timestamp approvalDate) {
		this.approvalDate = approvalDate;
	}
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	// Helper getters/setters
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
