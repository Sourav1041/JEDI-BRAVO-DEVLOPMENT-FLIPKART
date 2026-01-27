package com.flipfit.client;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymUser;
import com.flipfit.business.BookingService;
import com.flipfit.business.impl.BookingServiceImpl;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.impl.GymUserDAOImpl;
import com.flipfit.dao.impl.GymOwnerDAOImpl;
import com.flipfit.dao.impl.GymCustomerDAOImpl;
import com.flipfit.dao.impl.GymAdminDAOImpl;
import com.flipfit.enums.Role;
import com.flipfit.exception.BookingFailedException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Client application demonstrating DAO operations
 * This class shows how to use the DAO interfaces to perform CRUD operations
 */
public class DAOClientApp {
    
    private static GymUserDAO userDAO = new GymUserDAOImpl();
    private static GymOwnerDAO ownerDAO = new GymOwnerDAOImpl();
    private static GymCustomerDAO customerDAO = new GymCustomerDAOImpl();
    private static GymAdminDAO adminDAO = new GymAdminDAOImpl();
    private static BookingService bookingService = new BookingServiceImpl();
    private static com.flipfit.dao.GymCenterDAO centerDAO = new com.flipfit.dao.impl.GymCenterDAOImpl();
    private static com.flipfit.dao.GymSlotDAO slotDAO = new com.flipfit.dao.impl.GymSlotDAOImpl();
    private static Scanner scanner = new Scanner(System.in);
    
    private static GymUser currentUser = null;
    private static String currentCustomerId = null;
    private static String currentOwnerId = null;
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("    Welcome to FlipFit Application");
        System.out.println("===========================================\n");
        
        boolean running = true;
        
        while (running) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Customer Login");
            System.out.println("2. Gym Owner Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Register New User");
            System.out.println("5. Exit");
            System.out.println("================================");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    customerLogin();
                    break;
                case 2:
                    gymOwnerLogin();
                    break;
                case 3:
                    adminLogin();
                    break;
                case 4:
                    registerUser();
                    break;
                case 5:
                    running = false;
                    System.out.println("\n✓ Thank you for using FlipFit!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Customer login
     */
    private static void customerLogin() {
        System.out.println("\n========== CUSTOMER LOGIN ==========");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        currentUser = userDAO.authenticateUser(email, password);
        
        if (currentUser != null && currentUser.getRole() == Role.CUSTOMER) {
            System.out.println("✓ Login successful! Welcome " + currentUser.getName());
            // Get customer ID for booking operations
            var customers = customerDAO.getAllGymCustomers();
            for (GymUser cust : customers) {
                if (cust.getEmail().equals(currentUser.getEmail())) {
                    currentCustomerId = cust.getUserId(); // This will be the customer_id from GymCustomer table
                    break;
                }
            }
            customerMenu();
        } else {
            System.out.println("✗ Invalid credentials or not a customer account!");
        }
        currentUser = null;
        currentCustomerId = null;
    }
    
    /**
     * Gym Owner login
     */
    private static void gymOwnerLogin() {
        System.out.println("\n========== GYM OWNER LOGIN ==========");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        currentUser = userDAO.authenticateUser(email, password);
        
        if (currentUser != null && currentUser.getRole() == Role.GYM_OWNER) {
            System.out.println("✓ Login successful! Welcome " + currentUser.getName());
            // Get owner ID
            var owner = ownerDAO.getGymOwnerByUserId(currentUser.getUserId());
            if (owner != null) {
                currentOwnerId = owner.getOwnerId();
            }
            gymOwnerMenu();
        } else {
            System.out.println("✗ Invalid credentials or not a gym owner account!");
        }
        currentUser = null;
        currentOwnerId = null;
    }
    
    /**
     * Admin login
     */
    private static void adminLogin() {
        System.out.println("\n========== ADMIN LOGIN ==========");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        currentUser = userDAO.authenticateUser(email, password);
        
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            System.out.println("✓ Login successful! Welcome Admin " + currentUser.getName());
            adminMenu();
        } else {
            System.out.println("✗ Invalid credentials or not an admin account!");
        }
        currentUser = null;
    }
    
    /**
     * Register new user
     */
    private static void registerUser() {
        System.out.println("\n========== USER REGISTRATION ==========");
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        
        System.out.println("\nSelect Role:");
        System.out.println("1. Customer");
        System.out.println("2. Gym Owner");
        System.out.print("Enter choice: ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();
        
        Role role = (roleChoice == 2) ? Role.GYM_OWNER : Role.CUSTOMER;
        
        // Generate user ID
        String userId = "USR" + System.currentTimeMillis();
        
        GymUser user = new GymUser();
        user.setUserId(userId);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setAddress(address);
        user.setRole(role);
        
        boolean result = userDAO.insertUser(user);
        
        if (result) {
            System.out.println("✓ Registration successful! You can now login.");
        } else {
            System.out.println("✗ Registration failed!");
        }
    }
    
    /**
     * Customer menu after login
     */
    private static void customerMenu() {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. View Available Slots by City");
            System.out.println("4. View Available Slots by Center");
            System.out.println("5. Book a Slot");
            System.out.println("6. View My Bookings");
            System.out.println("7. View My Plan (By Date)");
            System.out.println("8. Cancel a Booking");
            System.out.println("9. Join Waitlist");
            System.out.println("10. Find Nearest Available Slot");
            System.out.println("11. View My Notifications");
            System.out.println("12. View All Gym Centers");
            System.out.println("13. Logout");
            System.out.println("===================================");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    displayUser(currentUser);
                    break;
                case 2:
                    updateProfile();
                    break;
                case 3:
                    viewAvailableSlotsByCity();
                    break;
                case 4:
                    viewAvailableSlotsByCenter();
                    break;
                case 5:
                    bookSlot();
                    break;
                case 6:
                    viewMyBookings();
                    break;
                case 7:
                    viewMyPlanByDate();
                    break;
                case 8:
                    cancelBooking();
                    break;
                case 9:
                    joinWaitlist();
                    break;
                case 10:
                    findNearestSlot();
                    break;
                case 11:
                    viewNotifications();
                    break;
                case 12:
                    viewAllGymCenters();
                    break;
                case 13:
                    loggedIn = false;
                    System.out.println("✓ Logged out successfully!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
    }
    
    /**
     * Gym Owner menu after login
     */
    private static void gymOwnerMenu() {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n========== GYM OWNER MENU ==========");
            System.out.println("1. View Profile");
            System.out.println("2. Add Gym Center");
            System.out.println("3. View My Gym Centers");
            System.out.println("4. Add Gym Slots");
            System.out.println("5. View Approval Status");
            System.out.println("6. Logout");
            System.out.println("====================================");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    displayUser(currentUser);
                    break;
                case 2:
                    addGymCenter();
                    break;
                case 3:
                    viewMyGymCenters();
                    break;
                case 4:
                    addGymSlots();
                    break;
                case 5:
                    viewApprovalStatus();
                    break;
                case 6:
                    loggedIn = false;
                    System.out.println("✓ Logged out successfully!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
    }
    
    /**
     * Admin menu after login
     */
    private static void adminMenu() {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. View System Statistics");
            System.out.println("2. View All Users");
            System.out.println("3. Approve Gym Owners");
            System.out.println("4. Approve Gym Centers");
            System.out.println("5. View All Gym Centers");
            System.out.println("6. Logout");
            System.out.println("================================");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewSystemStatistics();
                    break;
                case 2:
                    viewAllUsers();
                    break;
                case 3:
                    approveGymOwnerMenu();
                    break;
                case 4:
                    approveGymCenterMenu();
                    break;
                case 5:
                    viewAllGymCenters();
                    break;
                case 6:
                    loggedIn = false;
                    System.out.println("✓ Logged out successfully!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
    }
    
    /**
     * Update profile
     */
    private static void updateProfile() {
        System.out.println("\n--- Update Profile ---");
        
        System.out.print("Enter new name (or press Enter to skip): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            currentUser.setName(name);
        }
        
        System.out.print("Enter new address (or press Enter to skip): ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) {
            currentUser.setAddress(address);
        }
        
        boolean result = userDAO.updateUser(currentUser);
        
        if (result) {
            System.out.println("✓ Profile updated successfully!");
        } else {
            System.out.println("✗ Failed to update profile!");
        }
    }
    
    /**
     * Approve gym owner menu
     */
    private static void approveGymOwnerMenu() {
        System.out.println("\n--- Pending Gym Owner Approvals ---");
        var owners = ownerDAO.getPendingGymOwners();
        
        if (owners.isEmpty()) {
            System.out.println("No pending approvals!");
            return;
        }
        
        System.out.println("Total Pending: " + owners.size());
        System.out.print("\nEnter Owner ID to approve (or 0 to cancel): ");
        String ownerId = scanner.nextLine();
        
        if (!ownerId.equals("0")) {
            boolean result = ownerDAO.approveGymOwner(ownerId);
            
            if (result) {
                System.out.println("✓ Gym Owner approved successfully!");
            } else {
                System.out.println("✗ Failed to approve gym owner!");
            }
        }
    }
    
    
    /**
     * View all users (Admin only)
     */
    private static void viewAllUsers() {
        System.out.println("\n--- All Users ---");
        List<GymUser> users = userDAO.getAllUsers();
        
        if (users.isEmpty()) {
            System.out.println("No users found!");
        } else {
            System.out.println("Total Users: " + users.size());
            for (GymUser user : users) {
                System.out.println("ID: " + user.getUserId() + " | Name: " + user.getName() + 
                                 " | Email: " + user.getEmail() + " | Role: " + user.getRole());
            }
        }
    }
    
    /**
     * View system statistics
     */
    private static void viewSystemStatistics() {
        System.out.println("\n========== SYSTEM STATISTICS ==========");
        Map<String, Integer> stats = adminDAO.getSystemStatistics();
        
        System.out.println("Total Users: " + stats.getOrDefault("total_users", 0));
        System.out.println("Total Gym Owners: " + stats.getOrDefault("total_gym_owners", 0));
        System.out.println("Total Customers: " + stats.getOrDefault("total_customers", 0));
        System.out.println("Total Bookings: " + stats.getOrDefault("total_bookings", 0));
        System.out.println("Total Gym Centers: " + stats.getOrDefault("total_gym_centers", 0));
        System.out.println("========================================");
    }
    
    /**
     * View all gym centers
     */
    private static void viewAllGymCenters() {
        System.out.println("\n--- All Gym Centers ---");
        System.out.print("Enter City (or press Enter for all cities): ");
        String city = scanner.nextLine();
        
        List<GymCenter> centers;
        if (city.isEmpty()) {
            centers = adminDAO.getAllGymCenters();
        } else {
            centers = bookingService.viewGymCentersByCity(city);
        }
        
        if (centers.isEmpty()) {
            System.out.println("No gym centers found!");
        } else {
            System.out.println("Total Gym Centers: " + centers.size());
            System.out.println("===============================================");
            for (GymCenter center : centers) {
                System.out.println("\nGym ID: " + center.getGymId());
                System.out.println("Name: " + center.getGymName());
                System.out.println("Address: " + center.getGymAddress());
                System.out.println("City: " + center.getCity() + ", " + center.getState() + " - " + center.getPincode());
                System.out.println("Phone: " + center.getPhoneNumber());
                System.out.println("Email: " + center.getEmail());
                System.out.println("Total Slots: " + center.getTotalSlots());
                System.out.println("Approved: " + (center.isApproved() ? "Yes" : "No"));
                System.out.println("-----------------------------------------------");
            }
        }
    }
    
    /**
     * Helper method to display user information
     */
    private static void displayUser(GymUser user) {
        System.out.println("\n--- User Details ---");
        System.out.println("User ID: " + user.getUserId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Role: " + user.getRole());
    }
    
    // ========== BOOKING FEATURES ==========
    
    /**
     * View available slots by city
     */
    private static void viewAvailableSlotsByCity() {
        System.out.println("\n--- View Available Slots by City ---");
        System.out.print("Enter City Name: ");
        String city = scanner.nextLine();
        
        List<GymSlot> slots = bookingService.viewAvailableSlotsByCity(city);
        
        if (slots.isEmpty()) {
            System.out.println("No available slots found for " + city);
        } else {
            System.out.println("\nAvailable Slots in " + city + ":");
            System.out.println("===============================================");
            for (GymSlot slot : slots) {
                System.out.println("Slot ID: " + slot.getSlotId());
                System.out.println("Gym ID: " + slot.getGymId());
                System.out.println("Time: " + slot.getStartTime() + " - " + slot.getEndTime());
                System.out.println("Available Seats: " + slot.getAvailableSeats() + "/" + slot.getTotalSeats());
                System.out.println("Price: Rs. " + slot.getPrice());
                System.out.println("-----------------------------------------------");
            }
        }
    }
    
    /**
     * View available slots by gym center
     */
    private static void viewAvailableSlotsByCenter() {
        System.out.println("\n--- View Available Slots by Gym ---");
        System.out.print("Enter Gym ID: ");
        String gymId = scanner.nextLine();
        
        List<GymSlot> slots = bookingService.viewAvailableSlots(gymId);
        
        if (slots.isEmpty()) {
            System.out.println("No available slots found for this gym");
        } else {
            System.out.println("\nAvailable Slots for Gym " + gymId + ":");
            System.out.println("===============================================");
            for (GymSlot slot : slots) {
                System.out.println("Slot ID: " + slot.getSlotId());
                System.out.println("Time: " + slot.getStartTime() + " - " + slot.getEndTime());
                System.out.println("Available Seats: " + slot.getAvailableSeats() + "/" + slot.getTotalSeats());
                System.out.println("Price: Rs. " + slot.getPrice());
                System.out.println("-----------------------------------------------");
            }
        }
    }
    
    /**
     * Book a slot
     */
    private static void bookSlot() {
        System.out.println("\n--- Book a Slot ---");
        System.out.print("Enter Slot ID: ");
        String slotId = scanner.nextLine();
        
        System.out.print("Enter Booking Date (yyyy-MM-dd) or press Enter for today: ");
        String dateStr = scanner.nextLine();
        
        LocalDate bookingDate = LocalDate.now();
        if (!dateStr.isEmpty()) {
            try {
                bookingDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Using today's date.");
            }
        }
        
        if (currentCustomerId == null) {
            // Fallback: use user ID as customer ID
            currentCustomerId = currentUser.getUserId();
        }
        
        try {
            Booking booking = bookingService.bookSlot(currentCustomerId, slotId, bookingDate);
            System.out.println("\n========================================");
            System.out.println("       BOOKING SUCCESSFUL!");
            System.out.println("========================================");
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Slot ID: " + booking.getSlotId());
            System.out.println("Booking Date: " + booking.getBookingDate());
            System.out.println("Status: " + booking.getBookingStatus());
            System.out.println("========================================");
        } catch (BookingFailedException e) {
            System.out.println("\n*** BOOKING FAILED ***");
            System.out.println("Reason: " + e.getMessage());
            System.out.println("**********************");
        }
    }
    
    /**
     * View my bookings
     */
    private static void viewMyBookings() {
        System.out.println("\n--- My Bookings ---");
        
        if (currentCustomerId == null) {
            currentCustomerId = currentUser.getUserId();
        }
        
        List<Booking> bookings = bookingService.viewMyBookings(currentCustomerId);
        
        if (bookings.isEmpty()) {
            System.out.println("You have no bookings yet.");
        } else {
            System.out.println("Total Bookings: " + bookings.size());
            System.out.println("===============================================");
            for (Booking booking : bookings) {
                System.out.println("Booking ID: " + booking.getBookingId());
                System.out.println("Slot ID: " + booking.getSlotId());
                System.out.println("Booking Date: " + booking.getBookingDate());
                System.out.println("Status: " + booking.getBookingStatus());
                System.out.println("-----------------------------------------------");
            }
        }
    }
    
    /**
     * View my plan by date
     */
    private static void viewMyPlanByDate() {
        System.out.println("\n--- My Plan for a Specific Date ---");
        System.out.print("Enter Date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Using today's date.");
            date = LocalDate.now();
        }
        
        if (currentCustomerId == null) {
            currentCustomerId = currentUser.getUserId();
        }
        
        List<Booking> bookings = bookingService.viewPlanByDate(currentCustomerId, date);
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found for " + date);
        } else {
            System.out.println("\nYour Plan for " + date + ":");
            System.out.println("===============================================");
            for (Booking booking : bookings) {
                System.out.println("Booking ID: " + booking.getBookingId());
                System.out.println("Slot ID: " + booking.getSlotId());
                System.out.println("Status: " + booking.getBookingStatus());
                System.out.println("-----------------------------------------------");
            }
        }
    }
    
    /**
     * Cancel a booking
     */
    private static void cancelBooking() {
        System.out.println("\n--- Cancel a Booking ---");
        System.out.print("Enter Booking ID: ");
        String bookingId = scanner.nextLine();
        
        System.out.print("Are you sure you want to cancel this booking? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            try {
                boolean cancelled = bookingService.cancelBooking(bookingId);
                if (cancelled) {
                    System.out.println("\n========================================");
                    System.out.println("   BOOKING CANCELLED SUCCESSFULLY!");
                    System.out.println("   Your seat has been released.");
                    System.out.println("========================================");
                }
            } catch (BookingFailedException e) {
                System.out.println("\n*** CANCELLATION FAILED ***");
                System.out.println("Reason: " + e.getMessage());
                System.out.println("***************************");
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
    }
    
    /**
     * Join waitlist for a full slot
     */
    private static void joinWaitlist() {
        System.out.println("\n--- Join Waitlist ---");
        System.out.print("Enter Slot ID: ");
        String slotId = scanner.nextLine();
        
        System.out.print("Enter Requested Date (yyyy-MM-dd) or press Enter for today: ");
        String dateStr = scanner.nextLine();
        
        LocalDate requestedDate = LocalDate.now();
        if (!dateStr.isEmpty()) {
            try {
                requestedDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Using today's date.");
            }
        }
        
        if (currentCustomerId == null) {
            currentCustomerId = currentUser.getUserId();
        }
        
        boolean added = bookingService.addToWaitList(currentCustomerId, slotId, requestedDate);
        
        if (added) {
            System.out.println("\n========================================");
            System.out.println("  ADDED TO WAITLIST SUCCESSFULLY!");
            System.out.println("  You will be notified if a seat becomes available.");
            System.out.println("========================================");
        } else {
            System.out.println("Failed to add to waitlist!");
        }
    }
    
    /**
     * Find nearest available slot
     */
    private static void findNearestSlot() {
        System.out.println("\n--- Find Nearest Available Slot ---");
        System.out.print("Enter Gym ID: ");
        String gymId = scanner.nextLine();
        
        System.out.print("Enter Preferred Time (HH:mm) e.g., 07:00: ");
        String timeStr = scanner.nextLine();
        
        java.time.LocalTime preferredTime = java.time.LocalTime.parse(timeStr);
        
        GymSlot nearestSlot = bookingService.findNearestAvailableSlot(gymId, preferredTime);
        
        if (nearestSlot != null) {
            System.out.println("\n========================================");
            System.out.println("    NEAREST AVAILABLE SLOT FOUND!");
            System.out.println("========================================");
            System.out.println("Slot ID: " + nearestSlot.getSlotId());
            System.out.println("Gym ID: " + nearestSlot.getGymId());
            System.out.println("Time: " + nearestSlot.getStartTime() + " - " + nearestSlot.getEndTime());
            System.out.println("Available Seats: " + nearestSlot.getAvailableSeats() + "/" + nearestSlot.getTotalSeats());
            System.out.println("Price: Rs. " + nearestSlot.getPrice());
            System.out.println("========================================");
        } else {
            System.out.println("No available slots found for this gym.");
        }
    }
    
    /**
     * View notifications
     */
    private static void viewNotifications() {
        System.out.println("\n--- My Notifications ---");
        
        if (currentUser == null) {
            System.out.println("User not found!");
            return;
        }
        
        List<com.flipfit.bean.Notification> notifications = bookingService.getNotifications(currentUser.getUserId());
        
        if (notifications.isEmpty()) {
            System.out.println("No notifications.");
        } else {
            System.out.println("Total Notifications: " + notifications.size());
            System.out.println("===============================================");
            int count = 1;
            for (com.flipfit.bean.Notification notification : notifications) {
                System.out.print(count++ + ". ");
                if (!notification.isRead()) {
                    System.out.print("[NEW] ");
                }
                System.out.println(notification.getTitle());
                System.out.println("   " + notification.getMessage());
                System.out.println("   Type: " + notification.getNotificationType());
                System.out.println();
            }
            System.out.println("===============================================");
        }
    }
    
    // ========== GYM OWNER FEATURES ==========
    
    /**
     * Add a new gym center
     */
    private static void addGymCenter() {
        System.out.println("\n========== ADD GYM CENTER ==========");
        
        if (currentOwnerId == null) {
            System.out.println("Error: Owner ID not found!");
            return;
        }
        
        System.out.print("Enter Gym Name: ");
        String gymName = scanner.nextLine();
        
        System.out.print("Enter Gym Address: ");
        String gymAddress = scanner.nextLine();
        
        System.out.print("Enter City: ");
        String city = scanner.nextLine();
        
        System.out.print("Enter State: ");
        String state = scanner.nextLine();
        
        System.out.print("Enter Pincode: ");
        String pincode = scanner.nextLine();
        
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Total Slots to offer: ");
        int totalSlots = scanner.nextInt();
        scanner.nextLine();
        
        // Create gym center object
        String gymId = "GYM" + System.currentTimeMillis();
        GymCenter center = new GymCenter();
        center.setGymId(gymId);
        center.setOwnerId(currentOwnerId);
        center.setGymName(gymName);
        center.setGymAddress(gymAddress);
        center.setCity(city);
        center.setState(state);
        center.setPincode(pincode);
        center.setPhoneNumber(phoneNumber);
        center.setEmail(email);
        center.setTotalSlots(totalSlots);
        center.setApproved(false);  // Pending approval
        
        boolean added = centerDAO.insertGymCenter(center);
        
        if (added) {
            System.out.println("\n========================================");
            System.out.println("   GYM CENTER ADDED SUCCESSFULLY!");
            System.out.println("========================================");
            System.out.println("Gym ID: " + gymId);
            System.out.println("Status: Pending Admin Approval");
            System.out.println("========================================");
        } else {
            System.out.println("Failed to add gym center!");
        }
    }
    
    /**
     * View gym centers owned by current owner
     */
    private static void viewMyGymCenters() {
        System.out.println("\n========== MY GYM CENTERS ==========");
        
        if (currentOwnerId == null) {
            System.out.println("Error: Owner ID not found!");
            return;
        }
        
        List<GymCenter> centers = centerDAO.getGymCentersByOwner(currentOwnerId);
        
        if (centers.isEmpty()) {
            System.out.println("You don't have any gym centers yet.");
            System.out.println("Add a new gym center from the menu!");
        } else {
            System.out.println("Total Gym Centers: " + centers.size());
            System.out.println("===============================================");
            for (GymCenter center : centers) {
                System.out.println("\nGym ID: " + center.getGymId());
                System.out.println("Name: " + center.getGymName());
                System.out.println("Address: " + center.getGymAddress());
                System.out.println("City: " + center.getCity() + ", " + center.getState() + " - " + center.getPincode());
                System.out.println("Phone: " + center.getPhoneNumber());
                System.out.println("Email: " + center.getEmail());
                System.out.println("Total Slots: " + center.getTotalSlots());
                System.out.println("Status: " + (center.isApproved() ? "APPROVED" : "PENDING APPROVAL"));
                if (center.getApprovalDate() != null) {
                    System.out.println("Approved On: " + center.getApprovalDate());
                }
                System.out.println("-----------------------------------------------");
            }
        }
    }
    
    /**
     * Add slots to a gym center
     */
    private static void addGymSlots() {
        System.out.println("\n========== ADD GYM SLOTS ==========");
        
        if (currentOwnerId == null) {
            System.out.println("Error: Owner ID not found!");
            return;
        }
        
        // First show owner's gym centers
        List<GymCenter> centers = centerDAO.getGymCentersByOwner(currentOwnerId);
        
        if (centers.isEmpty()) {
            System.out.println("You don't have any gym centers yet.");
            System.out.println("Please add a gym center first!");
            return;
        }
        
        System.out.println("\nYour Gym Centers:");
        for (int i = 0; i < centers.size(); i++) {
            GymCenter center = centers.get(i);
            System.out.println((i + 1) + ". " + center.getGymName() + " (" + center.getGymId() + ") - " +
                             (center.isApproved() ? "APPROVED" : "PENDING"));
        }
        
        System.out.print("\nSelect Gym Center (1-" + centers.size() + "): ");
        int centerChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (centerChoice < 1 || centerChoice > centers.size()) {
            System.out.println("Invalid choice!");
            return;
        }
        
        GymCenter selectedCenter = centers.get(centerChoice - 1);
        
        System.out.print("Enter Start Time (HH:mm, e.g., 06:00): ");
        String startTimeStr = scanner.nextLine();
        
        System.out.print("Enter End Time (HH:mm, e.g., 07:00): ");
        String endTimeStr = scanner.nextLine();
        
        System.out.print("Enter Total Seats: ");
        int totalSeats = scanner.nextInt();
        
        System.out.print("Enter Price (Rs.): ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        
        // Create slot
        String slotId = "SLT" + System.currentTimeMillis();
        GymSlot slot = new GymSlot();
        slot.setSlotId(slotId);
        slot.setGymId(selectedCenter.getGymId());
        slot.setStartTime(java.time.LocalTime.parse(startTimeStr));
        slot.setEndTime(java.time.LocalTime.parse(endTimeStr));
        slot.setTotalSeats(totalSeats);
        slot.setAvailableSeats(totalSeats);
        slot.setPrice(java.math.BigDecimal.valueOf(price));
        slot.setActive(true);
        
        boolean added = slotDAO.insertSlot(slot);
        
        if (added) {
            System.out.println("\n========================================");
            System.out.println("    SLOT ADDED SUCCESSFULLY!");
            System.out.println("========================================");
            System.out.println("Slot ID: " + slotId);
            System.out.println("Gym: " + selectedCenter.getGymName());
            System.out.println("Time: " + startTimeStr + " - " + endTimeStr);
            System.out.println("Seats: " + totalSeats);
            System.out.println("Price: Rs. " + price);
            System.out.println("========================================");
        } else {
            System.out.println("Failed to add slot!");
        }
    }
    
    /**
     * View approval status of gym centers
     */
    private static void viewApprovalStatus() {
        System.out.println("\n========== APPROVAL STATUS ==========");
        
        if (currentOwnerId == null) {
            System.out.println("Error: Owner ID not found!");
            return;
        }
        
        // Check gym owner approval
        var owner = ownerDAO.getGymOwnerById(currentOwnerId);
        if (owner != null) {
            System.out.println("\nGym Owner Account:");
            System.out.println("Status: " + (owner.isApproved() ? "APPROVED" : "PENDING APPROVAL"));
            if (owner.getApprovalDate() != null) {
                System.out.println("Approved On: " + owner.getApprovalDate());
            }
        }
        
        // Check gym centers approval
        List<GymCenter> centers = centerDAO.getGymCentersByOwner(currentOwnerId);
        
        if (centers.isEmpty()) {
            System.out.println("\nNo gym centers registered yet.");
        } else {
            System.out.println("\n===============================================");
            System.out.println("GYM CENTERS APPROVAL STATUS:");
            System.out.println("===============================================");
            
            int approved = 0;
            int pending = 0;
            
            for (GymCenter center : centers) {
                System.out.println("\nGym: " + center.getGymName());
                System.out.println("ID: " + center.getGymId());
                System.out.println("Status: " + (center.isApproved() ? "APPROVED" : "PENDING APPROVAL"));
                if (center.getApprovalDate() != null) {
                    System.out.println("Approved On: " + center.getApprovalDate());
                }
                System.out.println("-----------------------------------------------");
                
                if (center.isApproved()) {
                    approved++;
                } else {
                    pending++;
                }
            }
            
            System.out.println("\nSummary: " + approved + " Approved, " + pending + " Pending");
        }
    }
    
    // ========== ADMIN FEATURES ==========
    
    /**
     * Approve gym centers
     */
    private static void approveGymCenterMenu() {
        System.out.println("\n========== APPROVE GYM CENTERS ==========");
        
        List<GymCenter> pendingCenters = adminDAO.getPendingGymCenterApprovals();
        
        if (pendingCenters.isEmpty()) {
            System.out.println("No pending gym center approvals.");
            return;
        }
        
        System.out.println("Pending Gym Centers: " + pendingCenters.size());
        System.out.println("===============================================");
        
        for (int i = 0; i < pendingCenters.size(); i++) {
            GymCenter center = pendingCenters.get(i);
            System.out.println("\n" + (i + 1) + ". Gym ID: " + center.getGymId());
            System.out.println("   Name: " + center.getGymName());
            System.out.println("   Owner ID: " + center.getOwnerId());
            System.out.println("   Address: " + center.getGymAddress());
            System.out.println("   City: " + center.getCity() + ", " + center.getState());
            System.out.println("   Phone: " + center.getPhoneNumber());
            System.out.println("   Email: " + center.getEmail());
            System.out.println("   Total Slots: " + center.getTotalSlots());
        }
        
        System.out.println("\n===============================================");
        System.out.print("Select gym center to approve (1-" + pendingCenters.size() + ") or 0 to cancel: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 0) {
            return;
        }
        
        if (choice < 1 || choice > pendingCenters.size()) {
            System.out.println("Invalid choice!");
            return;
        }
        
        GymCenter selectedCenter = pendingCenters.get(choice - 1);
        
        System.out.print("Approve this gym center? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            boolean approved = adminDAO.approveGymCenter(selectedCenter.getGymId());
            
            if (approved) {
                System.out.println("\n========================================");
                System.out.println("   GYM CENTER APPROVED SUCCESSFULLY!");
                System.out.println("========================================");
                System.out.println("Gym: " + selectedCenter.getGymName());
                System.out.println("ID: " + selectedCenter.getGymId());
                System.out.println("========================================");
            } else {
                System.out.println("Failed to approve gym center!");
            }
        } else {
            System.out.println("Approval cancelled.");
        }
    }
}
