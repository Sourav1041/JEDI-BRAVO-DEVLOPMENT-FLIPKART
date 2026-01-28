package com.flipfit.business;

import com.flipfit.bean.GymUser;
import com.flipfit.enums.Role;

/**
 * Service interface for User operations
 * Provides business logic for user management
 */
public interface UserService {
    /**
     * Login user with credentials
     * @param email User email
     * @param password User password
     * @param role Expected user role
     * @return GymUser object if login successful, null otherwise
     */
    GymUser login(String email, String password, Role role);

    /**
     * Register a new user
     * @param name User name
     * @param email User email
     * @param password User password
     * @param address User address
     * @param role User role
     * @return Registered GymUser object
     */
    GymUser register(String name, String email, String password, String address, Role role);

    /**
     * Change user password
     * @param userId User ID
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if password changed successfully, false otherwise
     */
    boolean changePassword(String userId, String oldPassword, String newPassword);
    
    /**
     * Update user profile
     * @param user Updated GymUser object
     * @return true if update successful, false otherwise
     */
    boolean updateProfile(GymUser user);
    
    /**
     * Check if email exists
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    boolean emailExists(String email);
}
