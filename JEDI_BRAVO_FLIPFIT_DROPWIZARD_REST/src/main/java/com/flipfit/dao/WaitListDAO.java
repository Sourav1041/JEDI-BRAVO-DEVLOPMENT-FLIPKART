package com.flipfit.dao;

import com.flipfit.bean.GymWaitList;
import java.time.LocalDate;
import java.util.List;

public interface WaitListDAO {
    boolean insertWaitList(GymWaitList waitList);
    List<GymWaitList> getWaitListBySlot(String slotId);
    boolean updateWaitListStatus(String waitListId, String status);
    boolean deleteWaitList(String waitListId);
    GymWaitList getFirstWaitingCustomer(String slotId);
    
    /**
     * Get first waiting customer for a specific slot on a specific date.
     * This is date-specific to ensure proper waitlist promotion.
     *
     * @param slotId the slot ID
     * @param requestedDate the requested date
     * @return the first waitlist entry for that slot and date, or null
     */
    GymWaitList getFirstWaitingCustomerByDate(String slotId, LocalDate requestedDate);
    
    /**
     * Check if customer is already in waitlist for a specific slot on a specific date.
     *
     * @param customerId the customer ID
     * @param slotId the slot ID
     * @param requestedDate the requested date
     * @return true if customer is already in waitlist
     */
    boolean isCustomerInWaitlistByDate(String customerId, String slotId, LocalDate requestedDate);
}
