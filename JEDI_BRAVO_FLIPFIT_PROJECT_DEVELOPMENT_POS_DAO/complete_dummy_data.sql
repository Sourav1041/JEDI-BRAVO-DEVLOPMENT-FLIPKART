-- ======================================
-- COMPLETE DUMMY DATA FOR FLIPFIT
-- ======================================
-- This script populates all tables with realistic test data
-- Run this after creating the schema with flipfit_schema.sql

USE Flipfit_schema;

-- Clean existing data (in correct order due to foreign key constraints)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Notification;
TRUNCATE TABLE Payment;
TRUNCATE TABLE WaitList;
TRUNCATE TABLE Booking;
TRUNCATE TABLE GymSlot;
TRUNCATE TABLE GymCenter;
TRUNCATE TABLE GymCustomer;
TRUNCATE TABLE GymOwner;
TRUNCATE TABLE Registration;
TRUNCATE TABLE User;
TRUNCATE TABLE Role;
SET FOREIGN_KEY_CHECKS = 1;

-- ======================================
-- ROLES (Already created by schema but let's ensure they exist)
-- ======================================
INSERT IGNORE INTO Role (role_id, role_name, description) VALUES
(1, 'ADMIN', 'System Administrator with full access'),
(2, 'GYM_OWNER', 'Gym owner who manages gym centers'),
(3, 'CUSTOMER', 'Customer who books gym slots');

-- ======================================
-- USERS
-- ======================================

-- Admin User
INSERT INTO User (user_id, name, email, password, address, phone_number, role_id) VALUES
('U001', 'Rajesh Kumar', 'admin@flipfit.com', 'admin123', 'FlipFit HQ, MG Road, Bangalore', '9876543210', 1);

-- Gym Owner Users
INSERT INTO User (user_id, name, email, password, address, phone_number, role_id) VALUES
('U002', 'Priya Sharma', 'priya.sharma@gmail.com', 'priya123', 'Koramangala, Bangalore', '9123456789', 2),
('U003', 'Vikram Patel', 'vikram.patel@gmail.com', 'vikram123', 'Indiranagar, Bangalore', '9234567890', 2);

-- Customer Users
INSERT INTO User (user_id, name, email, password, address, phone_number, role_id) VALUES
('U004', 'Ananya Reddy', 'ananya.reddy@gmail.com', 'ananya123', 'Whitefield, Bangalore', '9345678901', 3),
('U005', 'Arjun Mehta', 'arjun.mehta@gmail.com', 'arjun123', 'HSR Layout, Bangalore', '9456789012', 3),
('U006', 'Sneha Iyer', 'sneha.iyer@gmail.com', 'sneha123', 'Marathahalli, Bangalore', '9567890123', 3),
('U007', 'Rahul Verma', 'rahul.verma@gmail.com', 'rahul123', 'BTM Layout, Bangalore', '9678901234', 3),
('U008', 'Kavya Nair', 'kavya.nair@gmail.com', 'kavya123', 'Electronic City, Bangalore', '9789012345', 3),
('U009', 'Aditya Singh', 'aditya.singh@gmail.com', 'aditya123', 'Jayanagar, Bangalore', '9890123456', 3),
('U010', 'Ishita Gupta', 'ishita.gupta@gmail.com', 'ishita123', 'Bellandur, Bangalore', '9901234567', 3),
('U011', 'Rohan Das', 'rohan.das@gmail.com', 'rohan123', 'Sarjapur Road, Bangalore', '8012345678', 3);

-- ======================================
-- GYM OWNERS
-- ======================================
INSERT INTO GymOwner (owner_id, user_id, pan_card, aadhar_card, gst_number, is_approved, approval_date) VALUES
('OWN001', 'U002', 'ABCDE1234F', '123456789012', '29ABCDE1234F1Z5', TRUE, CURDATE()),
('OWN002', 'U003', 'FGHIJ5678K', '234567890123', '29FGHIJ5678K1Z5', TRUE, CURDATE());

-- ======================================
-- GYM CUSTOMERS
-- ======================================
INSERT INTO GymCustomer (customer_id, user_id, date_of_birth, fitness_goal, membership_start_date, membership_end_date, is_premium) VALUES
('CUST001', 'U004', '1995-05-15', 'Weight loss and cardio fitness', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 6 MONTH), TRUE),
('CUST002', 'U005', '1998-08-22', 'Muscle building and strength training', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 3 MONTH), FALSE),
('CUST003', 'U006', '1992-03-10', 'General fitness and flexibility', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 6 MONTH), TRUE),
('CUST004', 'U007', '1997-11-30', 'Weight loss', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), FALSE),
('CUST005', 'U008', '1994-07-18', 'CrossFit and endurance', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 12 MONTH), TRUE),
('CUST006', 'U009', '1996-02-25', 'Yoga and wellness', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 3 MONTH), FALSE),
('CUST007', 'U010', '1999-09-14', 'Athletic training', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 6 MONTH), TRUE),
('CUST008', 'U011', '1993-12-05', 'General fitness', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), FALSE);

-- ======================================
-- GYM CENTERS
-- ======================================
INSERT INTO GymCenter (
    gym_id, 
    owner_id, 
    gym_name, 
    gym_address, 
    city, 
    state, 
    pincode, 
    phone_number, 
    email, 
    total_slots, 
    is_approved, 
    approval_date, 
    created_at, 
    updated_at
) VALUES
(
    'GYM001', 
    'OWN001', 
    'FlipFit Koramangala', 
    '80 Feet Road, Koramangala 5th Block', 
    'Bangalore', 
    'Karnataka', 
    '560095', 
    '080-12345678', 
    'koramangala@flipfit.com', 
    6, 
    TRUE, 
    CURDATE(), 
    NOW(), 
    NOW()
),
(
    'GYM002', 
    'OWN001', 
    'FlipFit Indiranagar', 
    '100 Feet Road, Indiranagar', 
    'Bangalore', 
    'Karnataka', 
    '560038', 
    '080-23456789', 
    'indiranagar@flipfit.com', 
    6, 
    TRUE, 
    CURDATE(), 
    NOW(), 
    NOW()
),
(
    'GYM003', 
    'OWN002', 
    'FlipFit Whitefield', 
    'ITPL Main Road, Whitefield', 
    'Bangalore', 
    'Karnataka', 
    '560066', 
    '080-34567890', 
    'whitefield@flipfit.com', 
    6, 
    TRUE, 
    CURDATE(), 
    NOW(), 
    NOW()
),
(
    'GYM004', 
    'OWN002', 
    'FlipFit HSR Layout', 
    '27th Main Road, HSR Layout Sector 1', 
    'Bangalore', 
    'Karnataka', 
    '560102', 
    '080-45678901', 
    'hsr@flipfit.com', 
    6, 
    TRUE, 
    CURDATE(), 
    NOW(), 
    NOW()
),
(
    'GYM005', 
    'OWN001', 
    'FlipFit Marathahalli', 
    'Outer Ring Road, Marathahalli', 
    'Bangalore', 
    'Karnataka', 
    '560037', 
    '080-56789012', 
    'marathahalli@flipfit.com', 
    4, 
    TRUE, 
    CURDATE(), 
    NOW(), 
    NOW()
),
(
    'GYM006', 
    'OWN002', 
    'FlipFit Bellandur', 
    'Sarjapur Road, Bellandur', 
    'Bangalore', 
    'Karnataka', 
    '560103', 
    '080-67890123', 
    'bellandur@flipfit.com', 
    4, 
    TRUE, 
    CURDATE(), 
    NOW(), 
    NOW()
);

-- ======================================
-- GYM SLOTS
-- ======================================

-- GYM001 - Koramangala (6 slots: 3 morning + 3 evening)
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES
('SLT001', 'GYM001', '06:00:00', '07:00:00', 20, 20, 299.00, TRUE),
('SLT002', 'GYM001', '07:00:00', '08:00:00', 20, 20, 299.00, TRUE),
('SLT003', 'GYM001', '08:00:00', '09:00:00', 20, 20, 299.00, TRUE),
('SLT004', 'GYM001', '18:00:00', '19:00:00', 25, 25, 349.00, TRUE),
('SLT005', 'GYM001', '19:00:00', '20:00:00', 25, 25, 349.00, TRUE),
('SLT006', 'GYM001', '20:00:00', '21:00:00', 25, 25, 349.00, TRUE);

-- GYM002 - Indiranagar (6 slots: 3 morning + 3 evening)
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES
('SLT007', 'GYM002', '06:00:00', '07:00:00', 15, 15, 399.00, TRUE),
('SLT008', 'GYM002', '07:00:00', '08:00:00', 15, 15, 399.00, TRUE),
('SLT009', 'GYM002', '08:00:00', '09:00:00', 15, 15, 399.00, TRUE),
('SLT010', 'GYM002', '18:00:00', '19:00:00', 20, 20, 449.00, TRUE),
('SLT011', 'GYM002', '19:00:00', '20:00:00', 20, 20, 449.00, TRUE),
('SLT012', 'GYM002', '20:00:00', '21:00:00', 20, 20, 449.00, TRUE);

-- GYM003 - Whitefield (6 slots: 3 morning + 3 evening)
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES
('SLT013', 'GYM003', '06:00:00', '07:00:00', 30, 30, 349.00, TRUE),
('SLT014', 'GYM003', '07:00:00', '08:00:00', 30, 30, 349.00, TRUE),
('SLT015', 'GYM003', '08:00:00', '09:00:00', 30, 30, 349.00, TRUE),
('SLT016', 'GYM003', '18:00:00', '19:00:00', 30, 30, 399.00, TRUE),
('SLT017', 'GYM003', '19:00:00', '20:00:00', 30, 30, 399.00, TRUE),
('SLT018', 'GYM003', '20:00:00', '21:00:00', 30, 30, 399.00, TRUE);

-- GYM004 - HSR Layout (6 slots: 3 morning + 3 evening)
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES
('SLT019', 'GYM004', '06:00:00', '07:00:00', 18, 18, 329.00, TRUE),
('SLT020', 'GYM004', '07:00:00', '08:00:00', 18, 18, 329.00, TRUE),
('SLT021', 'GYM004', '08:00:00', '09:00:00', 18, 18, 329.00, TRUE),
('SLT022', 'GYM004', '18:00:00', '19:00:00', 22, 22, 379.00, TRUE),
('SLT023', 'GYM004', '19:00:00', '20:00:00', 22, 22, 379.00, TRUE),
('SLT024', 'GYM004', '20:00:00', '21:00:00', 22, 22, 379.00, TRUE);

-- GYM005 - Marathahalli (4 slots: 2 morning + 2 evening)
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES
('SLT025', 'GYM005', '06:00:00', '07:00:00', 25, 25, 279.00, TRUE),
('SLT026', 'GYM005', '07:00:00', '08:00:00', 25, 25, 279.00, TRUE),
('SLT027', 'GYM005', '18:00:00', '19:00:00', 28, 28, 329.00, TRUE),
('SLT028', 'GYM005', '19:00:00', '20:00:00', 28, 28, 329.00, TRUE);

-- GYM006 - Bellandur (4 slots: 2 morning + 2 evening)
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES
('SLT029', 'GYM006', '06:00:00', '07:00:00', 22, 22, 299.00, TRUE),
('SLT030', 'GYM006', '07:00:00', '08:00:00', 22, 22, 299.00, TRUE),
('SLT031', 'GYM006', '18:00:00', '19:00:00', 25, 25, 349.00, TRUE),
('SLT032', 'GYM006', '19:00:00', '20:00:00', 25, 25, 349.00, TRUE);

-- ======================================
-- SAMPLE BOOKINGS (for testing)
-- ======================================

-- Ananya booked morning slot at Koramangala
INSERT INTO Booking (booking_id, customer_id, slot_id, booking_date, booking_status) VALUES
('BK001', 'CUST001', 'SLT001', CURDATE(), 'CONFIRMED');

-- Update available seats after booking
UPDATE GymSlot SET available_seats = available_seats - 1 WHERE slot_id = 'SLT001';

-- Arjun booked evening slot at Indiranagar
INSERT INTO Booking (booking_id, customer_id, slot_id, booking_date, booking_status) VALUES
('BK002', 'CUST002', 'SLT010', CURDATE(), 'CONFIRMED');

UPDATE GymSlot SET available_seats = available_seats - 1 WHERE slot_id = 'SLT010';

-- Sneha booked morning slot at Whitefield
INSERT INTO Booking (booking_id, customer_id, slot_id, booking_date, booking_status) VALUES
('BK003', 'CUST003', 'SLT013', CURDATE(), 'CONFIRMED');

UPDATE GymSlot SET available_seats = available_seats - 1 WHERE slot_id = 'SLT013';

-- ======================================
-- SAMPLE PAYMENTS
-- ======================================
INSERT INTO Payment (payment_id, booking_id, customer_id, amount, payment_method, payment_status, transaction_id) VALUES
('PAY001', 'BK001', 'CUST001', 299.00, 'UPI', 'SUCCESS', 'TXN20260127001'),
('PAY002', 'BK002', 'CUST002', 449.00, 'CREDIT_CARD', 'SUCCESS', 'TXN20260127002'),
('PAY003', 'BK003', 'CUST003', 349.00, 'DEBIT_CARD', 'SUCCESS', 'TXN20260127003');

-- ======================================
-- SAMPLE NOTIFICATIONS
-- ======================================
INSERT INTO Notification (notification_id, user_id, title, message, notification_type, is_read) VALUES
('NOT001', 'U004', 'Booking Confirmed', 'Your booking at FlipFit Koramangala for 06:00-07:00 on ' + CURDATE() + ' has been confirmed.', 'BOOKING', FALSE),
('NOT002', 'U005', 'Booking Confirmed', 'Your booking at FlipFit Indiranagar for 18:00-19:00 on ' + CURDATE() + ' has been confirmed.', 'BOOKING', FALSE),
('NOT003', 'U006', 'Booking Confirmed', 'Your booking at FlipFit Whitefield for 06:00-07:00 on ' + CURDATE() + ' has been confirmed.', 'BOOKING', FALSE),
('NOT004', 'U002', 'Gym Owner Approved', 'Congratulations! Your gym owner account has been approved.', 'APPROVAL', TRUE),
('NOT005', 'U003', 'Gym Owner Approved', 'Congratulations! Your gym owner account has been approved.', 'APPROVAL', TRUE);

-- ======================================
-- SAMPLE REGISTRATIONS
-- ======================================
INSERT INTO Registration (registration_id, user_id, registration_type, status, submitted_date, reviewed_date, reviewed_by) VALUES
('REG001', 'U002', 'GYM_OWNER', 'APPROVED', DATE_SUB(CURDATE(), INTERVAL 7 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'U001'),
('REG002', 'U003', 'GYM_OWNER', 'APPROVED', DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(CURDATE(), INTERVAL 8 DAY), 'U001');

-- ======================================
-- DATA INSERTION COMPLETE
-- ======================================

SELECT 'Dummy data inserted successfully!' AS Status;
SELECT 
    (SELECT COUNT(*) FROM User) AS Total_Users,
    (SELECT COUNT(*) FROM GymOwner) AS Total_Owners,
    (SELECT COUNT(*) FROM GymCustomer) AS Total_Customers,
    (SELECT COUNT(*) FROM GymCenter) AS Total_Centers,
    (SELECT COUNT(*) FROM GymSlot) AS Total_Slots,
    (SELECT COUNT(*) FROM Booking) AS Total_Bookings;
