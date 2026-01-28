package com.flipfit.dao;

import com.flipfit.bean.GymUser;
import java.util.List;

/**
 * DAO interface for GymUser operations
 * Defines CRUD operations for User management
 */
public interface GymUserDAO {
    
    /**
     * Insert a new user into the database
     * @param user GymUser object to be inserted
     * @return true if insertion is successful, false otherwise
     */
    boolean insertUser(GymUser user);
    
    /**
     * Retrieve a user by user ID
     * @param userId User ID to search for
     * @return GymUser object if found, null otherwise
     */
    GymUser getUserById(String userId);
    
    /**
     * Retrieve a user by email
     * @param email Email address to search for
     * @return GymUser object if found, null otherwise
     */
    GymUser getUserByEmail(String email);
    
    /**
     * Retrieve all users from the database
     * @return List of all GymUser objects
     */
    List<GymUser> getAllUsers();
    
    /**
     * Update an existing user's information
     * @param user GymUser object with updated information
     * @return true if update is successful, false otherwise
     */
    boolean updateUser(GymUser user);
    
    /**
     * Delete a user by user ID
     * @param userId User ID to delete
     * @return true if deletion is successful, false otherwise
     */
    boolean deleteUser(String userId);
    
    /**
     * Authenticate user with email and password
     * @param email User's email
     * @param password User's password
     * @return GymUser object if authentication successful, null otherwise
     */
    GymUser authenticateUser(String email, String password);
    
    /**
     * Check if email already exists
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    boolean emailExists(String email);
}
