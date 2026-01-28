package com.flipfit.business.impl;

import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymCenter;
import com.flipfit.business.AdminService;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.impl.GymAdminDAOImpl;

import java.util.List;
import java.util.Map;

/**
 * Implementation of AdminService interface
 * Uses DAO layer for database operations
 */
public class AdminServiceImpl implements AdminService {
    
    private GymAdminDAO adminDAO;
    
    public AdminServiceImpl() {
        this.adminDAO = new GymAdminDAOImpl();
    }
    
    @Override
    public List<GymOwner> viewPendingGymOwners() {
        return adminDAO.getPendingGymOwnerApprovals();
    }
    
    @Override
    public List<GymCenter> viewPendingGymCenters() {
        return adminDAO.getPendingGymCenterApprovals();
    }
    
    @Override
    public boolean approveGymOwner(String ownerId) {
        if (ownerId == null || ownerId.isEmpty()) {
            System.out.println("Owner ID is required!");
            return false;
        }
        
        boolean success = adminDAO.approveGymOwner(ownerId);
        
        if (success) {
            System.out.println("Gym Owner approved successfully!");
        } else {
            System.out.println("Failed to approve gym owner!");
        }
        
        return success;
    }
    
    @Override
    public boolean rejectGymOwner(String ownerId, String remarks) {
        if (ownerId == null || ownerId.isEmpty()) {
            System.out.println("Owner ID is required!");
            return false;
        }
        
        boolean success = adminDAO.rejectGymOwner(ownerId, remarks);
        
        if (success) {
            System.out.println("Gym Owner rejected successfully!");
        } else {
            System.out.println("Failed to reject gym owner!");
        }
        
        return success;
    }
    
    @Override
    public boolean approveGymCenter(String gymId) {
        if (gymId == null || gymId.isEmpty()) {
            System.out.println("Gym ID is required!");
            return false;
        }
        
        boolean success = adminDAO.approveGymCenter(gymId);
        
        if (success) {
            System.out.println("Gym Center approved successfully!");
        } else {
            System.out.println("Failed to approve gym center!");
        }
        
        return success;
    }
    
    @Override
    public boolean rejectGymCenter(String gymId, String remarks) {
        if (gymId == null || gymId.isEmpty()) {
            System.out.println("Gym ID is required!");
            return false;
        }
        
        boolean success = adminDAO.rejectGymCenter(gymId, remarks);
        
        if (success) {
            System.out.println("Gym Center rejected successfully!");
        } else {
            System.out.println("Failed to reject gym center!");
        }
        
        return success;
    }
    
    @Override
    public List<GymCenter> viewAllGymCenters() {
        return adminDAO.getAllGymCenters();
    }
    
    @Override
    public Map<String, Integer> getSystemStatistics() {
        return adminDAO.getSystemStatistics();
    }
}
