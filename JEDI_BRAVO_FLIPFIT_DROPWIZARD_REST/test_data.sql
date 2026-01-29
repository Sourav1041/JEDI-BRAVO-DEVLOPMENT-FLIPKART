-- =====================================================
-- FLIPFIT TEST DATA
-- Test Scenario: 2 Customers, 1 Owner, 2 Gyms, 1 Slot each (Capacity: 1)
-- =====================================================

USE Flipfit_schema;

-- =====================================================
-- 1. CREATE ROLES
-- =====================================================
INSERT INTO Role (role_name, description) VALUES 
('CUSTOMER', 'Gym customer role'),
('GYM_OWNER', 'Gym owner role'),
('ADMIN', 'Admin role')
ON DUPLICATE KEY UPDATE role_name=role_name;

-- =====================================================
-- 2. CREATE TEST USERS
-- =====================================================

-- Customer A (Username: a@test.com, Password: password123)
INSERT INTO User (user_id, name, email, password, address, phone_number, role_id, is_active) 
VALUES 
('USERA001', 'Customer A', 'a@test.com', 'password123', 'Bangalore', '9876543210', 
 (SELECT role_id FROM Role WHERE role_name = 'CUSTOMER'), TRUE);

-- Customer B (Username: b@test.com, Password: password123)
INSERT INTO User (user_id, name, email, password, address, phone_number, role_id, is_active) 
VALUES 
('USERB001', 'Customer B', 'b@test.com', 'password123', 'Bangalore', '9876543211', 
 (SELECT role_id FROM Role WHERE role_name = 'CUSTOMER'), TRUE);

-- Gym Owner (Username: owner@test.com, Password: password123)
INSERT INTO User (user_id, name, email, password, address, phone_number, role_id, is_active) 
VALUES 
('USEROWNER1', 'Gym Owner Test', 'owner@test.com', 'password123', 'Bangalore', '9876543212', 
 (SELECT role_id FROM Role WHERE role_name = 'GYM_OWNER'), TRUE);

-- =====================================================
-- 3. CREATE GYM CUSTOMERS
-- =====================================================
INSERT INTO GymCustomer (customer_id, user_id, date_of_birth, fitness_goal, is_premium) 
VALUES 
('CUSTA001', 'USERA001', '1995-01-15', 'Weight Loss', FALSE),
('CUSTB001', 'USERB001', '1998-05-20', 'Muscle Gain', FALSE);

-- =====================================================
-- 4. CREATE GYM OWNER
-- =====================================================
INSERT INTO GymOwner (owner_id, user_id, pan_card, aadhar_card, gst_number, is_approved, approval_date) 
VALUES 
('OWNER001', 'USEROWNER1', 'TESTPAN123', 'TEST123456789012', 'TESTGST001', TRUE, NOW());

-- =====================================================
-- 5. CREATE TWO GYM CENTERS (Approved)
-- =====================================================

-- Gym 1: Morning Gym
INSERT INTO GymCenter (gym_id, owner_id, gym_name, gym_address, city, state, pincode, 
                       phone_number, email, total_slots, is_approved, approval_date) 
VALUES 
('GYMTEST001', 'OWNER001', 'FlipFit Morning Gym', 'Koramangala, Bangalore', 
 'Bangalore', 'Karnataka', '560001', '9988776655', 'morning@flipfit.com', 1, TRUE, NOW());

-- Gym 2: Evening Gym
INSERT INTO GymCenter (gym_id, owner_id, gym_name, gym_address, city, state, pincode, 
                       phone_number, email, total_slots, is_approved, approval_date) 
VALUES 
('GYMTEST002', 'OWNER001', 'FlipFit Evening Gym', 'HSR Layout, Bangalore', 
 'Bangalore', 'Karnataka', '560102', '9988776656', 'evening@flipfit.com', 1, TRUE, NOW());

-- =====================================================
-- 6. CREATE ONE SLOT PER GYM (Capacity: 1 seat each)
-- =====================================================

-- Slot for Morning Gym (6:00 AM - 7:00 AM, 1 seat)
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) 
VALUES 
('SLOTTEST001', 'GYMTEST001', '06:00:00', '07:00:00', 1, 1, 100.00, TRUE);

-- Slot for Evening Gym (6:00 PM - 7:00 PM, 1 seat)
INSERT INTO GymSlot (slot_id, gym_id, start_time, end_time, total_seats, available_seats, price, is_active) 
VALUES 
('SLOTTEST002', 'GYMTEST002', '18:00:00', '19:00:00', 1, 1, 150.00, TRUE);

-- =====================================================
-- VERIFY DATA INSERTED
-- =====================================================
SELECT '=== USERS ===' as '';
SELECT user_id, name, email, password, 
       (SELECT role_name FROM Role WHERE role_id = User.role_id) as role 
FROM User 
WHERE user_id IN ('USERA001', 'USERB001', 'USEROWNER1');

SELECT '\n=== CUSTOMERS ===' as '';
SELECT customer_id, user_id FROM GymCustomer;

SELECT '\n=== OWNER ===' as '';
SELECT owner_id, user_id, is_approved FROM GymOwner;

SELECT '\n=== GYM CENTERS ===' as '';
SELECT gym_id, gym_name, city, owner_id, is_approved FROM GymCenter;

SELECT '\n=== SLOTS ===' as '';
SELECT s.slot_id, s.gym_id, g.gym_name, s.start_time, s.end_time, 
       s.total_seats, s.available_seats, s.price 
FROM GymSlot s 
JOIN GymCenter g ON s.gym_id = g.gym_id;

-- =====================================================
-- LOGIN CREDENTIALS FOR TESTING
-- =====================================================
SELECT '\n=== LOGIN CREDENTIALS ===' as '';
SELECT 
    'Customer A' as 'User',
    'a@test.com' as 'Email',
    'password123' as 'Password',
    'CUSTOMER' as 'Role',
    'CUSTA001' as 'Customer_ID'
UNION ALL
SELECT 
    'Customer B',
    'b@test.com',
    'password123',
    'CUSTOMER',
    'CUSTB001'
UNION ALL
SELECT 
    'Gym Owner',
    'owner@test.com',
    'password123',
    'GYM_OWNER',
    'OWNER001';

-- =====================================================
-- TESTING SCENARIOS
-- =====================================================
/*

TEST SCENARIO 1: Date-Specific Availability
--------------------------------------------
1. Customer A books SLOTTEST001 (Morning Gym 6-7 AM) for 2026-01-30
2. Customer B tries to book SLOTTEST001 for 2026-01-30 → Should FAIL (full)
3. Customer B books SLOTTEST001 for 2026-01-31 → Should SUCCESS (different date!)

Expected Result:
- Same slot available on different dates ✅
- Slot full only for specific date ✅


TEST SCENARIO 2: Auto-Cancel Conflicting Bookings
-------------------------------------------------
1. Customer A books SLOTTEST001 (6-7 AM) for 2026-01-30 at Morning Gym
2. Customer A books SLOTTEST002 (6-7 PM) for 2026-01-30 at Evening Gym → SUCCESS (different time)
3. Customer A books another slot at 6-7 AM for 2026-01-30 → Old 6-7 AM booking auto-cancelled ✅

Expected Result:
- Only one booking per customer per time slot per date ✅


TEST SCENARIO 3: Waitlist & Auto-Promotion
------------------------------------------
1. Customer A books SLOTTEST001 for 2026-01-30 → SUCCESS (1/1 seat taken)
2. Customer B tries to book SLOTTEST001 for 2026-01-30 → FAILS, joins WAITLIST
3. Customer A cancels booking → Customer B AUTO-PROMOTED from waitlist ✅

Expected Result:
- Waitlist customer gets notification ✅
- Waitlist customer's booking auto-created ✅


TEST SCENARIO 4: View Gym Availability for Particular Day
---------------------------------------------------------
API: GET /customer/slots/available/GYMTEST001/2026-01-30

Expected Response:
{
  "slotId": "SLOTTEST001",
  "startTime": [6,0],
  "endTime": [7,0],
  "totalSeats": 1,
  "availableSeats": 1,  // Changes based on bookings for THIS date only
  "bookingDate": "2026-01-30"
}

*/
