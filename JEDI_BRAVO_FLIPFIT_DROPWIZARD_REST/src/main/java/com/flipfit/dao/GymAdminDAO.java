package com.flipfit.dao;

import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymCenter;
import java.util.List;

/**
 * DAO interface for GymAdmin operations
 * Defines operations for admin functionalities
 */
public interface GymAdminDAO {
    
    /**
     * Retrieve all pending gym owner approvals
     * @return List of GymOwner objects waiting for approval
     */
    List<GymOwner> getPendingGymOwnerApprovals();
    
    /**
     * Retrieve all pending gym center approvals
     * @return List of GymCenter objects waiting for approval
     */
    List<GymCenter> getPendingGymCenterApprovals();
    
    /**
     * Approve a gym owner registration
     * @param ownerId Owner ID to approve
     * @return true if approval is successful, false otherwise
     */
    boolean approveGymOwner(String ownerId);
    
    /**
     * Reject a gym owner registration
     * @param ownerId Owner ID to reject
     * @param remarks Rejection remarks
     * @return true if rejection is successful, false otherwise
     */
    boolean rejectGymOwner(String ownerId, String remarks);
    
    /**
     * Approve a gym center
     * @param gymId Gym center ID to approve
     * @return true if approval is successful, false otherwise
     */
    boolean approveGymCenter(String gymId);
    
    /**
     * Reject a gym center
     * @param gymId Gym center ID to reject
     * @param remarks Rejection remarks
     * @return true if rejection is successful, false otherwise
     */
    boolean rejectGymCenter(String gymId, String remarks);
    
    /**
     * View all users in the system
     * @return List of all users
     */
    List<Object> getAllUsers();
    
    /**
     * View all bookings in the system
     * @return List of all bookings
     */
    List<Object> getAllBookings();
    
    /**
     * View all gym centers in the system
     * @return List of all gym centers
     */
    List<GymCenter> getAllGymCenters();
    
    /**
     * Get system statistics
     * @return Map containing various system statistics
     */
    java.util.Map<String, Integer> getSystemStatistics();
}
