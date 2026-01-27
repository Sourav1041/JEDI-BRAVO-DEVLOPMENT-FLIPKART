package com.flipfit.bean;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * The Class GymWaitList.
 * Represents a waitlist entry for a fully booked gym slot.
 * 
 * @author JEDI-BRAVO
 */
public class GymWaitList {
    private String waitlistId;
    private String customerId;
    private String slotId;
    private LocalDate requestedDate;
    private int priority;
    private String status;
    private Timestamp createdAt;
    
    public String getWaitlistId() {
        return waitlistId;
    }
    
    public void setWaitlistId(String waitlistId) {
        this.waitlistId = waitlistId;
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
    
    public LocalDate getRequestedDate() {
        return requestedDate;
    }
    
    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

