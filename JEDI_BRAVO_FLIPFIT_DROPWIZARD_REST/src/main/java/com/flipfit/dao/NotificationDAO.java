package com.flipfit.dao;

import com.flipfit.bean.Notification;
import java.util.List;

/**
 * The Interface NotificationDAO.
 * Defines operations for notification management
 * 
 * @author JEDI-BRAVO
 */
public interface NotificationDAO {
    
    /**
     * Insert a new notification.
     *
     * @param notification the notification object
     * @return true if insertion successful, false otherwise
     */
    boolean insertNotification(Notification notification);
    
    /**
     * Get all notifications for a user.
     *
     * @param userId the user ID
     * @return list of notifications
     */
    List<Notification> getNotificationsByUser(String userId);
    
    /**
     * Get unread notifications for a user.
     *
     * @param userId the user ID
     * @return list of unread notifications
     */
    List<Notification> getUnreadNotifications(String userId);
    
    /**
     * Mark a notification as read.
     *
     * @param notificationId the notification ID
     * @return true if update successful, false otherwise
     */
    boolean markAsRead(String notificationId);
    
    /**
     * Mark all notifications as read for a user.
     *
     * @param userId the user ID
     * @return true if update successful, false otherwise
     */
    boolean markAllAsRead(String userId);
    
    /**
     * Delete a notification.
     *
     * @param notificationId the notification ID
     * @return true if deletion successful, false otherwise
     */
    boolean deleteNotification(String notificationId);
}
