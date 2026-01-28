package com.flipfit.business.impl;

import com.flipfit.bean.GymUser;
import com.flipfit.business.UserService;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.dao.impl.GymUserDAOImpl;
import com.flipfit.enums.Role;
import com.flipfit.exception.InvalidCredentialsException;
import com.flipfit.exception.UserNotFoundException;
import com.flipfit.exception.RegistrationFailedException;

import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class UserServiceImpl.
 * Implementation of UserService interface
 * Uses DAO layer for database operations
 *
 * @author JEDI-BRAVO
 * @ClassName UserServiceImpl
 */
public class UserServiceImpl implements UserService {

    /** The user DAO. */
    private GymUserDAO userDAO;
    
    /**
     * Instantiates a new user service impl.
     */
    public UserServiceImpl() {
        this.userDAO = new GymUserDAOImpl();
    }

    /**
     * Login.
     *
     * @param email the user email
     * @param password the user password
     * @param role the expected user role
     * @return the GymUser object if login successful
     * @throws InvalidCredentialsException if credentials are invalid
     */
    @Override
    public GymUser login(String email, String password, Role role) {
        try {
            // Authenticate user using DAO
            GymUser user = userDAO.authenticateUser(email, password);
            
            // Verify role matches
            if (user != null && user.getRole() == role) {
                return user;
            }
            
            throw new InvalidCredentialsException("Invalid email, password, or role");
            
        } catch (InvalidCredentialsException e) {
            System.err.println("Login failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Register.
     *
     * @param name the user name
     * @param email the user email
     * @param password the user password
     * @param address the user address
     * @param role the user role
     * @return the registered GymUser object
     * @throws RegistrationFailedException if registration fails
     */
    @Override
    public GymUser register(String name, String email, String password, String address, Role role) {
        try {
            // Check if email already exists
            if (userDAO.emailExists(email)) {
                throw new RegistrationFailedException("Email " + email + " already exists");
            }
            
            // Create new user
            GymUser user = new GymUser();
            user.setUserId(generateUserId());
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setAddress(address);
            user.setRole(role);
            
            // Insert user into database
            boolean success = userDAO.insertUser(user);
            
            if (success) {
                return user;
            } else {
                throw new RegistrationFailedException("Failed to register user in database");
            }
            
        } catch (RegistrationFailedException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Change password.
     *
     * @param userId the user ID
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return true, if successful
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        try {
            // Get user from database
            GymUser user = userDAO.getUserById(userId);
            
            // Verify user exists and old password is correct
            if (user == null) {
                throw new UserNotFoundException("User with ID " + userId + " not found");
            }
            
            if (!user.getPassword().equals(oldPassword)) {
                throw new InvalidCredentialsException("Old password is incorrect");
            }
            
            // Update password
            user.setPassword(newPassword);
            return userDAO.updateUser(user);
            
        } catch (UserNotFoundException | InvalidCredentialsException e) {
            System.err.println("Password change failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update profile.
     *
     * @param user the GymUser object with updated information
     * @return true, if successful
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public boolean updateProfile(GymUser user) {
        try {
            // Validate user exists
            GymUser existingUser = userDAO.getUserById(user.getUserId());
            if (existingUser == null) {
                throw new UserNotFoundException("User with ID " + user.getUserId() + " not found");
            }
            
            // Update user in database
            return userDAO.updateUser(user);
            
        } catch (UserNotFoundException e) {
            System.err.println("Profile update failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Email exists.
     *
     * @param email the email to check
     * @return true, if email exists
     */
    @Override
    public boolean emailExists(String email) {
        return userDAO.emailExists(email);
    }
    
    /**
     * Generate user id.
     * Helper method to generate unique user ID
     *
     * @return the generated user ID
     */
    private String generateUserId() {
        return "U" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
