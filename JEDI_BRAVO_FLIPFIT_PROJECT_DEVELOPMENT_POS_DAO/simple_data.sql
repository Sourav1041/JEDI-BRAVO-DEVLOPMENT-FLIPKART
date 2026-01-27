-- ======================================
-- FLIPFIT SIMPLE DATA
-- ======================================

USE Flipfit_schema;

-- ======================================
-- USERS
-- ======================================

-- Admin (already exists, just update password)
UPDATE User SET password = 'admin123' WHERE user_id = 'ADMIN001';

-- Gym Owners
INSERT INTO User (user_id, name, email, password, address, role_id) VALUES
('OWN001', 'Rajesh Kumar', 'rajesh.kumar@flipfit.com', 'owner123', 'MG Road, Bangalore', 2),
('OWN002', 'Priya Sharma', 'priya.sharma@flipfit.com', 'owner123', 'Indiranagar, Bangalore', 2);

-- Customers
INSERT INTO User (user_id, name, email, password, address, role_id) VALUES
('CUST001', 'Arjun Reddy', 'arjun.reddy@gmail.com', 'cust123', 'Koramangala, Bangalore', 3),
('CUST002', 'Sneha Iyer', 'sneha.iyer@gmail.com', 'cust123', 'HSR Layout, Bangalore', 3),
('CUST003', 'Vikram Singh', 'vikram.singh@gmail.com', 'cust123', 'Whitefield, Bangalore', 3),
('CUST004', 'Anjali Desai', 'anjali.desai@gmail.com', 'cust123', 'Jayanagar, Bangalore', 3),
('CUST005', 'Karthik Menon', 'karthik.menon@gmail.com', 'cust123', 'BTM Layout, Bangalore', 3);

-- ======================================
-- GYM OWNERS
-- ======================================

INSERT INTO GymOwner (owner_id, user_id, pan_card, aadhar_card, gst_number, is_approved) VALUES
('OWN001', 'OWN001', 'ABCDE1234F', '123456789012', '29ABCDE1234F1Z5', TRUE),
('OWN002', 'OWN002', 'FGHIJ5678K', '987654321098', '29FGHIJ5678K1Z5', TRUE);

-- ======================================
-- GYM CUSTOMERS
-- ======================================

INSERT INTO GymCustomer (customer_id, user_id, date_of_birth, fitness_goal) VALUES
('CUST001', 'CUST001', '1995-03-15', 'Weight Loss'),
('CUST002', 'CUST002', '1992-07-22', 'Muscle Building'),
('CUST003', 'CUST003', '1988-11-30', 'General Fitness'),
('CUST004', 'CUST004', '1990-05-18', 'Cardio Training'),
('CUST005', 'CUST005', '1993-09-25', 'Strength Training');

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
    CURDATE(), 
    CURDATE()
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
    CURDATE(), 
    CURDATE()
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
    CURDATE(), 
    CURDATE()
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
    CURDATE(), 
    CURDATE()
);

-- ======================================
-- GYM SLOTS
-- ======================================

-- Koramangala - Morning & Evening slots (time templates, no dates)
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES
('SLT001', 'GYM001', '06:00:00', '07:00:00', 20, 20, 299.00, TRUE),
('SLT002', 'GYM001', '07:00:00', '08:00:00', 20, 20, 299.00, TRUE),
('SLT003', 'GYM001', '08:00:00', '09:00:00', 20, 20, 299.00, TRUE),
('SLT004', 'GYM001', '18:00:00', '19:00:00', 25, 25, 349.00, TRUE),
('SLT005', 'GYM001', '19:00:00', '20:00:00', 25, 25, 349.00, TRUE),
('SLT006', 'GYM001', '20:00:00', '21:00:00', 25, 25, 349.00, TRUE);

-- Indiranagar - Morning & Evening slots
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES
('SLT013', 'GYM002', '06:00:00', '07:00:00', 15, 15, 399.00, TRUE),
('SLT014', 'GYM002', '07:00:00', '08:00:00', 15, 15, 399.00, TRUE),
('SLT015', 'GYM002', '08:00:00', '09:00:00', 15, 15, 399.00, TRUE),
('SLT016', 'GYM002', '18:00:00', '19:00:00', 20, 20, 449.00, TRUE),
('SLT017', 'GYM002', '19:00:00', '20:00:00', 20, 20, 449.00, TRUE),
('SLT018', 'GYM002', '20:00:00', '21:00:00', 20, 20, 449.00, TRUE);

-- Whitefield - Morning & Evening slots
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES
('SLT019', 'GYM003', '06:00:00', '07:00:00', 30, 30, 349.00, TRUE),
('SLT020', 'GYM003', '07:00:00', '08:00:00', 30, 30, 349.00, TRUE),
('SLT021', 'GYM003', '08:00:00', '09:00:00', 30, 30, 349.00, TRUE),
('SLT022', 'GYM003', '18:00:00', '19:00:00', 30, 30, 399.00, TRUE),
('SLT023', 'GYM003', '19:00:00', '20:00:00', 30, 30, 399.00, TRUE),
('SLT024', 'GYM003', '20:00:00', '21:00:00', 30, 30, 399.00, TRUE);

-- HSR Layout - Morning & Evening slots
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) VALUES
('SLT025', 'GYM004', '06:00:00', '07:00:00', 18, 18, 329.00, TRUE),
('SLT026', 'GYM004', '07:00:00', '08:00:00', 18, 18, 329.00, TRUE),
('SLT027', 'GYM004', '08:00:00', '09:00:00', 18, 18, 329.00, TRUE),
('SLT028', 'GYM004', '18:00:00', '19:00:00', 22, 22, 379.00, TRUE),
('SLT029', 'GYM004', '19:00:00', '20:00:00', 22, 22, 379.00, TRUE),
('SLT030', 'GYM004', '20:00:00', '21:00:00', 22, 22, 379.00, TRUE);

-- ======================================
-- SUMMARY
-- ======================================

SELECT 'Data loaded successfully!' AS Status;

SELECT 'Users' AS Type, COUNT(*) AS Count FROM User
UNION ALL
SELECT 'Gym Owners', COUNT(*) FROM GymOwner
UNION ALL
SELECT 'Customers', COUNT(*) FROM GymCustomer
UNION ALL
SELECT 'Gym Centers', COUNT(*) FROM GymCenter
UNION ALL
SELECT 'Gym Slots', COUNT(*) FROM GymSlot;

-- ======================================
-- LOGIN CREDENTIALS
-- ======================================

SELECT '
Login Credentials:
------------------
Admin: admin@flipfit.com / admin123

Gym Owners:
- rajesh.kumar@flipfit.com / owner123
- priya.sharma@flipfit.com / owner123

Customers:
- arjun.reddy@gmail.com / cust123
- sneha.iyer@gmail.com / cust123
- vikram.singh@gmail.com / cust123
- anjali.desai@gmail.com / cust123
- karthik.menon@gmail.com / cust123
' AS Credentials;
