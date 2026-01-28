package com.flipfit.dao.impl;

import com.flipfit.bean.Notification;
import com.flipfit.dao.NotificationDAO;
import com.flipfit.utils.DBConnection;
import com.flipfit.constant.SQLConstants;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class NotificationDAOImpl.
 * Implementation of NotificationDAO interface
 * Handles all database operations for notifications
 * 
 * @author JEDI-BRAVO
 */
public class NotificationDAOImpl implements NotificationDAO {
    
    /**
     * Insert a new notification.
     *
     * @param notification the notification object
     * @return true if insertion successful, false otherwise
     */
    @Override
    public boolean insertNotification(Notification notification) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.INSERT_NOTIFICATION)) {
            
            pstmt.setString(1, notification.getNotificationId());
            pstmt.setString(2, notification.getUserId());
            pstmt.setString(3, notification.getTitle());
            pstmt.setString(4, notification.getMessage());
            pstmt.setString(5, notification.getNotificationType());
            pstmt.setBoolean(6, notification.isRead());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting notification: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all notifications for a user.
     *
     * @param userId the user ID
     * @return list of notifications
     */
    @Override
    public List<Notification> getNotificationsByUser(String userId) {
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_NOTIFICATIONS_BY_USER)) {
            
            pstmt.setString(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting notifications: " + e.getMessage());
        }
        return notifications;
    }
    
    /**
     * Get unread notifications for a user.
     *
     * @param userId the user ID
     * @return list of unread notifications
     */
    @Override
    public List<Notification> getUnreadNotifications(String userId) {
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.SELECT_UNREAD_NOTIFICATIONS)) {
            
            pstmt.setString(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting unread notifications: " + e.getMessage());
        }
        return notifications;
    }
    
    /**
     * Mark a notification as read.
     *
     * @param notificationId the notification ID
     * @return true if update successful, false otherwise
     */
    @Override
    public boolean markAsRead(String notificationId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.MARK_NOTIFICATION_READ)) {
            
            pstmt.setString(1, notificationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mark all notifications as read for a user.
     *
     * @param userId the user ID
     * @return true if update successful, false otherwise
     */
    @Override
    public boolean markAllAsRead(String userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.MARK_ALL_NOTIFICATIONS_READ)) {
            
            pstmt.setString(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marking all notifications as read: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a notification.
     *
     * @param notificationId the notification ID
     * @return true if deletion successful, false otherwise
     */
    @Override
    public boolean deleteNotification(String notificationId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.DELETE_NOTIFICATION)) {
            
            pstmt.setString(1, notificationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting notification: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Helper method to map ResultSet to Notification object.
     *
     * @param rs the result set
     * @return the notification object
     * @throws SQLException the SQL exception
     */
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getString("notification_id"));
        notification.setUserId(rs.getString("user_id"));
        notification.setTitle(rs.getString("title"));
        notification.setMessage(rs.getString("message"));
        notification.setNotificationType(rs.getString("notification_type"));
        notification.setRead(rs.getBoolean("is_read"));
        notification.setCreatedAt(rs.getTimestamp("created_at"));
        return notification;
    }
}
