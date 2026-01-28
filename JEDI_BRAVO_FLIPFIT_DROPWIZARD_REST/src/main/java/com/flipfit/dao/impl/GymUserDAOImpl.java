package com.flipfit.dao.impl;

import com.flipfit.bean.GymUser;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.enums.Role;
import com.flipfit.utils.DBConnection;
import com.flipfit.constant.SQLConstants;
import com.flipfit.exception.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class GymUserDAOImpl.
 * Implementation of GymUserDAO interface
 * Handles all database operations for User management
 *
 * @author JEDI-BRAVO
 * @ClassName GymUserDAOImpl
 */
public class GymUserDAOImpl implements GymUserDAO {
    
    /**
     * Insert user.
     *
     * @param user the GymUser object to be inserted
     * @return true, if successful
     */
    @Override
    public boolean insertUser(GymUser user) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.INSERT_USER)) {
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getAddress());
            pstmt.setString(6, null);
            pstmt.setInt(7, getRoleId(user.getRole()));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets the user by id.
     *
     * @param userId the user ID to search for
     * @return the GymUser object if found
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public GymUser getUserById(String userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_USER_BY_ID)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching user by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Gets the user by email.
     *
     * @param email the email address to search for
     * @return the GymUser object if found
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public GymUser getUserByEmail(String email) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_USER_BY_EMAIL)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching user by email: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Gets all users.
     *
     * @return the list of all GymUser objects
     */
    @Override
    public List<GymUser> getAllUsers() {
        List<GymUser> users = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_ALL_USERS);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Update user.
     *
     * @param user the GymUser object with updated information
     * @return true, if successful
     */
    @Override
    public boolean updateUser(GymUser user) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.UPDATE_USER)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getAddress());
            pstmt.setInt(5, getRoleId(user.getRole()));
            pstmt.setString(6, user.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete user.
     *
     * @param userId the user ID to delete
     * @return true, if successful
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public boolean deleteUser(String userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.DELETE_USER)) {
            
            pstmt.setString(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new UserNotFoundException("User with ID " + userId + " not found");
            }
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        } catch (UserNotFoundException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    
    /**
     * Authenticate user.
     *
     * @param email the user email
     * @param password the user password
     * @return the GymUser object if authentication successful
     */
    @Override
    public GymUser authenticateUser(String email, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.AUTHENTICATE_USER)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Email exists.
     *
     * @param email the email to check
     * @return true, if email exists
     */
    @Override
    public boolean emailExists(String email) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.CHECK_EMAIL_EXISTS)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Map result set to user.
     * Helper method to map ResultSet to GymUser object
     *
     * @param rs the ResultSet containing user data
     * @return the GymUser object
     * @throws SQLException if SQL error occurs
     */
    private GymUser mapResultSetToUser(ResultSet rs) throws SQLException {
        GymUser user = new GymUser();
        user.setUserId(rs.getString("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setAddress(rs.getString("address"));
        user.setRole(Role.valueOf(rs.getString("role_name")));
        return user;
    }
    
    /**
     * Gets the role id.
     * Helper method to get role ID from Role enum
     *
     * @param role the Role enum
     * @return the role ID
     */
    private int getRoleId(Role role) {
        switch (role) {
            case ADMIN:
                return 1;
            case GYM_OWNER:
                return 2;
            case CUSTOMER:
                return 3;
            default:
                return 3;
        }
    }
}
