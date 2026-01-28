package com.flipfit.constant;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLConstants.
 * Contains all SQL query constants for the FlipFit application
 *
 * @author JEDI-BRAVO
 * @ClassName SQLConstants
 */
public class SQLConstants {
    
    /**
     * Private constructor to prevent instantiation.
     */
    private SQLConstants() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
    
    // ========== USER QUERIES ==========
    
    /** The constant for inserting a new user. */
    public static final String INSERT_USER = 
        "INSERT INTO User (user_id, name, email, password, address, phone_number, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    /** The constant for selecting user by ID. */
    public static final String SELECT_USER_BY_ID = 
        "SELECT u.*, r.role_name FROM User u JOIN Role r ON u.role_id = r.role_id WHERE u.user_id = ?";
    
    /** The constant for selecting user by email. */
    public static final String SELECT_USER_BY_EMAIL = 
        "SELECT u.*, r.role_name FROM User u JOIN Role r ON u.role_id = r.role_id WHERE u.email = ?";
    
    /** The constant for selecting all users. */
    public static final String SELECT_ALL_USERS = 
        "SELECT u.*, r.role_name FROM User u JOIN Role r ON u.role_id = r.role_id";
    
    /** The constant for updating user. */
    public static final String UPDATE_USER = 
        "UPDATE User SET name = ?, email = ?, password = ?, address = ?, phone_number = ?, role_id = ? WHERE user_id = ?";
    
    /** The constant for deleting user. */
    public static final String DELETE_USER = 
        "DELETE FROM User WHERE user_id = ?";
    
    /** The constant for authenticating user. */
    public static final String AUTHENTICATE_USER = 
        "SELECT u.*, r.role_name FROM User u JOIN Role r ON u.role_id = r.role_id WHERE u.email = ? AND u.password = ? AND u.is_active = TRUE";
    
    /** The constant for checking email existence. */
    public static final String CHECK_EMAIL_EXISTS = 
        "SELECT COUNT(*) FROM User WHERE email = ?";
    
    // ========== GYM OWNER QUERIES ==========
    
    /** The constant for inserting gym owner. */
    public static final String INSERT_GYM_OWNER = 
        "INSERT INTO GymOwner (owner_id, user_id, pan_card, aadhar_card, gst_number) VALUES (?, ?, ?, ?, ?)";
    
    /** The constant for selecting gym owner by ID. */
    public static final String SELECT_GYM_OWNER_BY_ID = 
        "SELECT * FROM GymOwner WHERE owner_id = ?";
    
    /** The constant for selecting gym owner by user ID. */
    public static final String SELECT_GYM_OWNER_BY_USER_ID = 
        "SELECT * FROM GymOwner WHERE user_id = ?";
    
    /** The constant for selecting all gym owners. */
    public static final String SELECT_ALL_GYM_OWNERS = 
        "SELECT * FROM GymOwner";
    
    /** The constant for selecting pending gym owners. */
    public static final String SELECT_PENDING_GYM_OWNERS = 
        "SELECT * FROM GymOwner WHERE is_approved = FALSE";
    
    /** The constant for approving gym owner. */
    public static final String APPROVE_GYM_OWNER = 
        "UPDATE GymOwner SET is_approved = TRUE, approval_date = CURRENT_TIMESTAMP WHERE owner_id = ?";
    
    /** The constant for updating gym owner. */
    public static final String UPDATE_GYM_OWNER = 
        "UPDATE GymOwner SET pan_card = ?, aadhar_card = ?, gst_number = ? WHERE owner_id = ?";
    
    /** The constant for deleting gym owner. */
    public static final String DELETE_GYM_OWNER = 
        "DELETE FROM GymOwner WHERE owner_id = ?";
    
    // ========== GYM CUSTOMER QUERIES ==========
    
    /** The constant for inserting gym customer. */
    public static final String INSERT_GYM_CUSTOMER = 
        "INSERT INTO GymCustomer (customer_id, user_id, date_of_birth, fitness_goal) VALUES (?, ?, ?, ?)";
    
    /** The constant for selecting gym customer by ID. */
    public static final String SELECT_GYM_CUSTOMER_BY_ID = 
        "SELECT * FROM GymCustomer WHERE customer_id = ?";
    
    /** The constant for selecting gym customer by user ID. */
    public static final String SELECT_GYM_CUSTOMER_BY_USER_ID = 
        "SELECT * FROM GymCustomer WHERE user_id = ?";
    
    /** The constant for selecting all gym customers. */
    public static final String SELECT_ALL_GYM_CUSTOMERS = 
        "SELECT * FROM GymCustomer";
    
    /** The constant for updating gym customer. */
    public static final String UPDATE_GYM_CUSTOMER = 
        "UPDATE GymCustomer SET date_of_birth = ?, fitness_goal = ? WHERE customer_id = ?";
    
    /** The constant for updating membership. */
    public static final String UPDATE_MEMBERSHIP = 
        "UPDATE GymCustomer SET membership_start_date = ?, membership_end_date = ?, is_premium = ? WHERE customer_id = ?";
    
    /** The constant for deleting gym customer. */
    public static final String DELETE_GYM_CUSTOMER = 
        "DELETE FROM GymCustomer WHERE customer_id = ?";
    
    /** The constant for getting customer booking count. */
    public static final String GET_CUSTOMER_BOOKING_COUNT = 
        "SELECT COUNT(*) FROM Booking WHERE customer_id = ? AND booking_status = 'CONFIRMED'";
    
    // ========== GYM CENTER QUERIES ==========
    
    /** The constant for inserting gym center. */
    public static final String INSERT_GYM_CENTER = 
        "INSERT INTO GymCenter (gym_id, owner_id, gym_name, gym_address, city, state, pincode, phone_number, email, total_slots, is_approved) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    /** The constant for selecting gym center by ID. */
    public static final String SELECT_GYM_CENTER_BY_ID = 
        "SELECT * FROM GymCenter WHERE gym_id = ?";
    
    /** The constant for selecting gym centers by owner. */
    public static final String SELECT_GYM_CENTERS_BY_OWNER = 
        "SELECT * FROM GymCenter WHERE owner_id = ?";
    
    /** The constant for selecting gym centers by city. */
    public static final String SELECT_GYM_CENTERS_BY_CITY = 
        "SELECT * FROM GymCenter WHERE city = ? AND is_approved = TRUE";
    
    /** The constant for selecting all gym centers. */
    public static final String SELECT_ALL_GYM_CENTERS = 
        "SELECT * FROM GymCenter WHERE is_approved = TRUE";
    
    /** The constant for selecting pending gym centers. */
    public static final String SELECT_PENDING_GYM_CENTERS = 
        "SELECT * FROM GymCenter WHERE is_approved = FALSE";
    
    /** The constant for approving gym center. */
    public static final String APPROVE_GYM_CENTER = 
        "UPDATE GymCenter SET is_approved = TRUE, approval_date = CURRENT_TIMESTAMP WHERE gym_id = ?";
    
    /** The constant for updating gym center. */
    public static final String UPDATE_GYM_CENTER = 
        "UPDATE GymCenter SET gym_name = ?, gym_address = ?, city = ?, state = ?, pincode = ?, phone_number = ?, email = ?, total_slots = ? WHERE gym_id = ?";
    
    /** The constant for rejecting gym center. */
    public static final String REJECT_GYM_CENTER = 
        "UPDATE GymCenter SET is_approved = FALSE WHERE gym_id = ?";
    
    /** The constant for deleting gym center. */
    public static final String DELETE_GYM_CENTER = 
        "DELETE FROM GymCenter WHERE gym_id = ?";
    
    // ========== ADMIN QUERIES ==========
    
    /** The constant for getting total users count. */
    public static final String COUNT_TOTAL_USERS = 
        "SELECT COUNT(*) as count FROM User";
    
    /** The constant for getting total gym owners count. */
    public static final String COUNT_TOTAL_GYM_OWNERS = 
        "SELECT COUNT(*) as count FROM GymOwner";
    
    /** The constant for getting total customers count. */
    public static final String COUNT_TOTAL_CUSTOMERS = 
        "SELECT COUNT(*) as count FROM GymCustomer";
    
    /** The constant for getting total bookings count. */
    public static final String COUNT_TOTAL_BOOKINGS = 
        "SELECT COUNT(*) as count FROM Booking";
    
    /** The constant for getting total gym centers count. */
    public static final String COUNT_TOTAL_GYM_CENTERS = 
        "SELECT COUNT(*) as count FROM GymCenter";
    
    // ========== SLOT QUERIES ==========
    
    /** The constant for inserting a new slot. */
    public static final String INSERT_SLOT = 
        "INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    /** The constant for selecting slot by ID. */
    public static final String SELECT_SLOT_BY_ID = 
        "SELECT s.*, c.gym_name, c.city, c.gym_address FROM GymSlot s JOIN GymCenter c ON s.gym_id = c.gym_id WHERE s.slot_id = ?";
    
    /** The constant for selecting slots by gym. */
    public static final String SELECT_SLOTS_BY_GYM = 
        "SELECT s.*, c.gym_name, c.city, c.gym_address FROM GymSlot s JOIN GymCenter c ON s.gym_id = c.gym_id WHERE s.gym_id = ? AND s.is_active = TRUE ORDER BY s.start_time";
    
    /** The constant for selecting available slots by gym. */
    public static final String SELECT_AVAILABLE_SLOTS_BY_GYM = 
        "SELECT s.*, c.gym_name, c.city, c.gym_address FROM GymSlot s JOIN GymCenter c ON s.gym_id = c.gym_id WHERE s.gym_id = ? AND s.available_seats > 0 AND s.is_active = TRUE ORDER BY s.start_time";
    
    /** The constant for selecting slots by gym and time range. */
    public static final String SELECT_SLOTS_BY_GYM_TIME = 
        "SELECT s.*, c.gym_name, c.city, c.gym_address FROM GymSlot s JOIN GymCenter c ON s.gym_id = c.gym_id WHERE s.gym_id = ? AND s.start_time >= ? AND s.end_time <= ? AND s.is_active = TRUE ORDER BY s.start_time";
    
    /** The constant for checking slot availability. */
    public static final String CHECK_SLOT_AVAILABILITY = 
        "SELECT available_seats FROM GymSlot WHERE slot_id = ? AND is_active = TRUE";
    
    /** The constant for updating available seats. */
    public static final String UPDATE_AVAILABLE_SEATS = 
        "UPDATE GymSlot SET available_seats = available_seats + ? WHERE slot_id = ?";
    
    /** The constant for selecting all available slots. */
    public static final String SELECT_ALL_AVAILABLE_SLOTS = 
        "SELECT s.*, c.gym_name, c.city, c.gym_address FROM GymSlot s JOIN GymCenter c ON s.gym_id = c.gym_id WHERE s.available_seats > 0 AND s.is_active = TRUE ORDER BY c.gym_name, s.start_time";
    
    /** The constant for selecting slots by city. */
    public static final String SELECT_SLOTS_BY_CITY = 
        "SELECT s.*, c.gym_name, c.city, c.gym_address FROM GymSlot s JOIN GymCenter c ON s.gym_id = c.gym_id WHERE c.city = ? AND s.available_seats > 0 AND s.is_active = TRUE ORDER BY c.gym_name, s.start_time";
    
    /** The constant for deleting slot. */
    public static final String DELETE_SLOT = 
        "DELETE FROM GymSlot WHERE slot_id = ?";
    
    /** The constant for updating slot. */
    public static final String UPDATE_SLOT = 
        "UPDATE GymSlot SET start_time = ?, end_time = ?, total_seats = ?, available_seats = ?, price = ?, is_active = ? WHERE slot_id = ?";
    
    /** The constant for getting slot capacity. */
    public static final String GET_SLOT_CAPACITY = 
        "SELECT total_seats FROM GymSlot WHERE slot_id = ?";
    
    /** The constant for getting available seats. */
    public static final String GET_AVAILABLE_SEATS = 
        "SELECT available_seats FROM GymSlot WHERE slot_id = ?";
    
    // ========== BOOKING QUERIES ==========
    
    /** The constant for inserting a new booking. */
    public static final String INSERT_BOOKING = 
        "INSERT INTO Booking (booking_id, customer_id, slot_id, booking_date, booking_status) VALUES (?, ?, ?, ?, ?)";
    
    /** The constant for selecting booking by ID. */
    public static final String SELECT_BOOKING_BY_ID = 
        "SELECT b.*, s.start_time, s.end_time, c.gym_name, c.city, c.gym_address FROM Booking b JOIN GymSlot s ON b.slot_id = s.slot_id JOIN GymCenter c ON s.gym_id = c.gym_id WHERE b.booking_id = ?";
    
    /** The constant for selecting bookings by customer. */
    public static final String SELECT_BOOKINGS_BY_CUSTOMER = 
        "SELECT b.*, s.start_time, s.end_time, c.gym_name, c.city, c.gym_address FROM Booking b JOIN GymSlot s ON b.slot_id = s.slot_id JOIN GymCenter c ON s.gym_id = c.gym_id WHERE b.customer_id = ? ORDER BY b.booking_date DESC, s.start_time DESC";
    
    /** The constant for selecting bookings by slot. */
    public static final String SELECT_BOOKINGS_BY_SLOT = 
        "SELECT b.*, u.name, u.email FROM Booking b JOIN GymCustomer gc ON b.customer_id = gc.customer_id JOIN User u ON gc.user_id = u.user_id WHERE b.slot_id = ? AND b.booking_status = 'CONFIRMED'";
    
    /** The constant for selecting customer bookings by date. */
    public static final String SELECT_CUSTOMER_BOOKINGS_BY_DATE = 
        "SELECT b.*, s.start_time, s.end_time, c.gym_name, c.city FROM Booking b JOIN GymSlot s ON b.slot_id = s.slot_id JOIN GymCenter c ON s.gym_id = c.gym_id WHERE b.customer_id = ? AND b.booking_date = ? AND b.booking_status = 'CONFIRMED'";
    
    /** The constant for checking existing booking in same slot time. */
    public static final String CHECK_EXISTING_BOOKING_SAME_TIME = 
        "SELECT b.booking_id, b.slot_id, s.start_time, s.end_time FROM Booking b JOIN GymSlot s ON b.slot_id = s.slot_id WHERE b.customer_id = ? AND b.booking_date = ? AND b.booking_status = 'CONFIRMED'";
    
    /** The constant for updating booking status. */
    public static final String UPDATE_BOOKING_STATUS = 
        "UPDATE Booking SET booking_status = ? WHERE booking_id = ?";
    
    /** The constant for deleting booking. */
    public static final String DELETE_BOOKING = 
        "DELETE FROM Booking WHERE booking_id = ?";
    
    /** The constant for counting bookings by customer. */
    public static final String COUNT_BOOKINGS_BY_CUSTOMER = 
        "SELECT COUNT(*) as count FROM Booking WHERE customer_id = ? AND booking_status = 'CONFIRMED'";
    
    /** The constant for getting slot details from booking. */
    public static final String GET_SLOT_FROM_BOOKING = 
        "SELECT s.* FROM GymSlot s JOIN Booking b ON s.slot_id = b.slot_id WHERE b.booking_id = ?";
    
    // ========== WAITLIST QUERIES ==========
    
    /** The constant for inserting waitlist entry. */
    public static final String INSERT_WAITLIST = 
        "INSERT INTO WaitList (waitlist_id, customer_id, slot_id, requested_date, status) VALUES (?, ?, ?, ?, ?)";
    
    /** The constant for selecting waitlist by slot. */
    public static final String SELECT_WAITLIST_BY_SLOT = 
        "SELECT w.*, gc.user_id, u.name, u.email FROM WaitList w JOIN GymCustomer gc ON w.customer_id = gc.customer_id JOIN User u ON gc.user_id = u.user_id WHERE w.slot_id = ? AND w.status = 'WAITING' ORDER BY w.created_at";
    
    /** The constant for selecting first waitlist entry. */
    public static final String SELECT_FIRST_WAITLIST = 
        "SELECT * FROM WaitList WHERE slot_id = ? AND status = 'WAITING' ORDER BY created_at LIMIT 1";
    
    /** The constant for updating waitlist status. */
    public static final String UPDATE_WAITLIST_STATUS = 
        "UPDATE WaitList SET status = ? WHERE waitlist_id = ?";
    
    /** The constant for deleting waitlist entry. */
    public static final String DELETE_WAITLIST = 
        "DELETE FROM WaitList WHERE waitlist_id = ?";
    
    /** The constant for checking if customer is in waitlist. */
    public static final String CHECK_CUSTOMER_IN_WAITLIST = 
        "SELECT COUNT(*) FROM WaitList WHERE customer_id = ? AND slot_id = ? AND status = 'WAITING'";
    
    // ========== NOTIFICATION QUERIES ==========
    
    /** The constant for inserting notification. */
    public static final String INSERT_NOTIFICATION = 
        "INSERT INTO Notification (notification_id, user_id, title, message, notification_type, is_read) VALUES (?, ?, ?, ?, ?, ?)";
    
    /** The constant for selecting notifications by user. */
    public static final String SELECT_NOTIFICATIONS_BY_USER = 
        "SELECT * FROM Notification WHERE user_id = ? ORDER BY created_at DESC";
    
    /** The constant for selecting unread notifications by user. */
    public static final String SELECT_UNREAD_NOTIFICATIONS = 
        "SELECT * FROM Notification WHERE user_id = ? AND is_read = FALSE ORDER BY created_at DESC";
    
    /** The constant for marking notification as read. */
    public static final String MARK_NOTIFICATION_READ = 
        "UPDATE Notification SET is_read = TRUE WHERE notification_id = ?";
    
    /** The constant for marking all notifications as read. */
    public static final String MARK_ALL_NOTIFICATIONS_READ = 
        "UPDATE Notification SET is_read = TRUE WHERE user_id = ?";
    
    /** The constant for deleting notification. */
    public static final String DELETE_NOTIFICATION = 
        "DELETE FROM Notification WHERE notification_id = ?";
    
    // ========== PAYMENT QUERIES ==========
    
    /** The constant for inserting payment. */
    public static final String INSERT_PAYMENT = 
        "INSERT INTO Payment (payment_id, booking_id, customer_id, amount, payment_method, payment_status, transaction_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    /** The constant for selecting payment by ID. */
    public static final String SELECT_PAYMENT_BY_ID = 
        "SELECT * FROM Payment WHERE payment_id = ?";
    
    /** The constant for selecting payments by booking. */
    public static final String SELECT_PAYMENT_BY_BOOKING = 
        "SELECT * FROM Payment WHERE booking_id = ?";
    
    /** The constant for updating payment status. */
    public static final String UPDATE_PAYMENT_STATUS = 
        "UPDATE Payment SET payment_status = ? WHERE payment_id = ?";
}
