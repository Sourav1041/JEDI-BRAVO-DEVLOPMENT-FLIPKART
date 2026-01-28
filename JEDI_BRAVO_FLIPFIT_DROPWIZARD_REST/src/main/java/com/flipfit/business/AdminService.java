package com.flipfit.business;

import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymCenter;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Admin operations
 * Provides business logic for admin functionalities
 */
public interface AdminService {
    /**
     * View all pending gym owner approvals
     * @return List of pending gym owners
     */
    List<GymOwner> viewPendingGymOwners();
    
    /**
     * View all pending gym center approvals
     * @return List of pending gym centers
     */
    List<GymCenter> viewPendingGymCenters();
    
    /**
     * Approve a gym owner
     * @param ownerId Owner ID to approve
     * @return true if successful, false otherwise
     */
    boolean approveGymOwner(String ownerId);
    
    /**
     * Reject a gym owner
     * @param ownerId Owner ID to reject
     * @param remarks Rejection remarks
     * @return true if successful, false otherwise
     */
    boolean rejectGymOwner(String ownerId, String remarks);
    
    /**
     * Approve a gym center
     * @param gymId Gym center ID to approve
     * @return true if successful, false otherwise
     */
    boolean approveGymCenter(String gymId);
    
    /**
     * Reject a gym center
     * @param gymId Gym center ID to reject
     * @param remarks Rejection remarks
     * @return true if successful, false otherwise
     */
    boolean rejectGymCenter(String gymId, String remarks);
    
    /**
     * View all gym centers
     * @return List of all gym centers
     */
    List<GymCenter> viewAllGymCenters();
    
    /**
     * Get system statistics
     * @return Map containing system statistics
     */
    Map<String, Integer> getSystemStatistics();
}
