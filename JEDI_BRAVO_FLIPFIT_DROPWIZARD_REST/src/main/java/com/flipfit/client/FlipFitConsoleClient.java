package com.flipfit.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * FlipFit Console Client Application
 * User-friendly console interface to interact with FlipFit REST API
 * 
 * @author JEDI-BRAVO
 * @version 1.0
 * @since 2026-01-29
 */
public class FlipFitConsoleClient {
    
    private static final String API_BASE_URL = "http://localhost:8080";
    private static final Scanner scanner = new Scanner(System.in);
    private static String currentUserId = null;
    private static String currentCustomerId = null; // For customer bookings
    private static String currentUserRole = null;
    private static String currentUserName = null;
    
    /**
     * Main method to start the console client
     */
    public static void main(String[] args) {
        // Check if running in an interactive terminal
        try {
            if (System.console() == null && !scanner.hasNextLine()) {
                System.err.println("Error: This application requires an interactive terminal.");
                System.err.println("Please run directly from your terminal, not through background processes.");
                System.exit(1);
            }
        } catch (Exception e) {
            // Continue if check fails - might still work
        }
        
        showWelcomeScreen();
        mainMenu();
    }
    
    /**
     * Display welcome screen with ASCII art
     */
    private static void showWelcomeScreen() {
        clearScreen();
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                   ║");
        System.out.println("║   ███████╗██╗     ██╗██████╗ ███████╗██╗████████╗                ║");
        System.out.println("║   ██╔════╝██║     ██║██╔══██╗██╔════╝██║╚══██╔══╝                ║");
        System.out.println("║   █████╗  ██║     ██║██████╔╝█████╗  ██║   ██║                   ║");
        System.out.println("║   ██╔══╝  ██║     ██║██╔═══╝ ██╔══╝  ██║   ██║                   ║");
        System.out.println("║   ██║     ███████╗██║██║     ██║     ██║   ██║                   ║");
        System.out.println("║   ╚═╝     ╚══════╝╚═╝╚═╝     ╚═╝     ╚═╝   ╚═╝                   ║");
        System.out.println("║                                                                   ║");
        System.out.println("║              Your Fitness Booking Companion                      ║");
        System.out.println("║                                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        pauseForEffect();
    }
    
    /**
     * Main menu for the application
     */
    private static void mainMenu() {
        while (true) {
            clearScreen();
            printHeader("FLIPFIT MAIN MENU");
            
            // Show login status
            if (currentUserId != null) {
                System.out.println("╔═════════════════════════════════════════════════╗");
                System.out.println("║  🟢 Logged in as: " + currentUserName + " (" + currentUserRole + ")");
                System.out.println("╚═════════════════════════════════════════════════╝");
                System.out.println();
            }
            
            // Show different menus based on login status
            if (currentUserId == null) {
                // Not logged in - show only login and registration options
                System.out.println("┌─────────────────────────────────────────────────┐");
                System.out.println("│  1. Login                                       │");
                System.out.println("│  2. Register as Customer                        │");
                System.out.println("│  3. Register as Gym Owner                       │");
                System.out.println("│  0. Exit                                         │");
                System.out.println("└─────────────────────────────────────────────────┘");
                System.out.print("\nEnter your choice: ");
                
                int choice = getIntInput();
                
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        registerCustomer();
                        break;
                    case 3:
                        registerGymOwner();
                        break;
                    case 0:
                        exitApplication();
                        return;
                    default:
                        showError("Invalid choice! Please try again.");
                }
            } else {
                // Logged in - show role-specific menu
                if ("CUSTOMER".equals(currentUserRole)) {
                    showCustomerMainMenu();
                } else if ("GYM_OWNER".equals(currentUserRole)) {
                    showOwnerMainMenu();
                } else if ("ADMIN".equals(currentUserRole)) {
                    showAdminMainMenu();
                }
            }
        }
    }
    
    /**
     * Main menu for logged-in customers
     */
    private static void showCustomerMainMenu() {
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│  1. Customer Menu                               │");
        System.out.println("│  2. Change Password                             │");
        System.out.println("│  3. Logout                                      │");
        System.out.println("│  0. Exit                                         │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("\nEnter your choice: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                customerMenu();
                break;
            case 2:
                changePassword();
                break;
            case 3:
                logout();
                break;
            case 0:
                exitApplication();
                return;
            default:
                showError("Invalid choice! Please try again.");
        }
    }
    
    /**
     * Main menu for logged-in gym owners
     */
    private static void showOwnerMainMenu() {
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│  1. Gym Owner Menu                              │");
        System.out.println("│  2. Change Password                             │");
        System.out.println("│  3. Logout                                      │");
        System.out.println("│  0. Exit                                         │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("\nEnter your choice: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                gymOwnerMenu();
                break;
            case 2:
                changePassword();
                break;
            case 3:
                logout();
                break;
            case 0:
                exitApplication();
                return;
            default:
                showError("Invalid choice! Please try again.");
        }
    }
    
    /**
     * Main menu for logged-in admin
     */
    private static void showAdminMainMenu() {
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│  1. Admin Menu                                  │");
        System.out.println("│  2. Change Password                             │");
        System.out.println("│  3. Logout                                      │");
        System.out.println("│  0. Exit                                         │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("\nEnter your choice: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                adminMenu();
                break;
            case 2:
                changePassword();
                break;
            case 3:
                logout();
                break;
            case 0:
                exitApplication();
                return;
            default:
                showError("Invalid choice! Please try again.");
        }
    }
    
    /**
     * Login functionality with Date/Time API feature
     */
    private static void login() {
        clearScreen();
        printHeader("LOGIN");
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        System.out.println("\nSelect Your Role:");
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│  1. Customer                                    │");
        System.out.println("│  2. Gym Owner                                   │");
        System.out.println("│  3. Admin                                       │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("Enter choice (1-3): ");
        
        int roleChoice = getIntInput();
        String role;
        
        switch (roleChoice) {
            case 1:
                role = "CUSTOMER";
                break;
            case 2:
                role = "GYM_OWNER";
                break;
            case 3:
                role = "ADMIN";
                break;
            default:
                showError("Invalid role selection!");
                pause();
                return;
        }
        
        String jsonPayload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"role\":\"%s\"}", 
                                          email, password, role);
        
        showLoading("Authenticating");
        String response = sendPostRequest("/auth/login", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            // Parse response to get user details and login time
            currentUserId = extractJsonValue(response, "userId");
            currentCustomerId = extractJsonValue(response, "customerId"); // For customers
            currentUserName = extractJsonValue(response, "username");
            currentUserRole = extractJsonValue(response, "role");
            String loginTime = extractJsonValue(response, "loginTime");
            String welcomeMsg = extractJsonValue(response, "welcomeMessage");
            
            showSuccess("Login Successful!");
            System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
            System.out.println("║  " + welcomeMsg);
            System.out.println("║  Login Time: " + loginTime);
            System.out.println("╚═══════════════════════════════════════════════════════════╝");
        } else {
            showError("Login Failed! " + extractJsonValue(response, "error"));
        }
        
        pause();
    }
    
    /**
     * Customer menu
     */
    private static void customerMenu() {
        // Check if user is logged in and has correct role
        if (currentUserId == null) {
            showError("Please login first!");
            pause();
            return;
        }
        
        if (!"CUSTOMER".equals(currentUserRole)) {
            showError("Access denied! This menu is only for customers.");
            pause();
            return;
        }
        
        while (true) {
            clearScreen();
            printHeader("CUSTOMER MENU");
            
            System.out.println("┌─────────────────────────────────────────────────┐");
            System.out.println("│  1. View Gym Centers by City                   │");
            System.out.println("│  2. View Available Slots                        │");
            System.out.println("│  3. Book a Slot                                 │");
            System.out.println("│  4. View My Bookings                            │");
            System.out.println("│  5. Cancel Booking                              │");
            System.out.println("│  6. View Notifications                          │");
            System.out.println("│  0. Back to Main Menu                           │");
            System.out.println("└─────────────────────────────────────────────────┘");
            System.out.print("\nEnter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewGymCentersByCity();
                    break;
                case 2:
                    viewAvailableSlots();
                    break;
                case 3:
                    bookSlot();
                    break;
                case 4:
                    viewMyBookings();
                    break;
                case 5:
                    cancelBooking();
                    break;
                case 6:
                    viewNotifications();
                    break;
                case 0:
                    return;
                default:
                    showError("Invalid choice!");
            }
        }
    }
    
    /**
     * Admin menu with Stream API features
     */
    private static void adminMenu() {
        // Check if user is logged in and has correct role
        if (currentUserId == null) {
            showError("Please login first!");
            pause();
            return;
        }
        
        if (!"ADMIN".equals(currentUserRole)) {
            showError("Access denied! This menu is only for administrators.");
            pause();
            return;
        }
        
        while (true) {
            clearScreen();
            printHeader("ADMIN MENU");
            
            System.out.println("┌─────────────────────────────────────────────────┐");
            System.out.println("│  1. View Approved Gym Owners (Stream API)       │");
            System.out.println("│  2. View Pending Gym Owners (Stream API)        │");
            System.out.println("│  3. View Approved Gym Centers (Stream API)      │");
            System.out.println("│  4. View Pending Gym Centers (Stream API)       │");
            System.out.println("│  5. Approve Gym Owner                           │");
            System.out.println("│  6. Approve Gym Center                          │");
            System.out.println("│  7. View System Statistics                      │");
            System.out.println("│  0. Back to Main Menu                           │");
            System.out.println("└─────────────────────────────────────────────────┘");
            System.out.print("\nEnter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewApprovedOwners();
                    break;
                case 2:
                    viewPendingOwners();
                    break;
                case 3:
                    viewApprovedCenters();
                    break;
                case 4:
                    viewPendingCenters();
                    break;
                case 5:
                    approveGymOwner();
                    break;
                case 6:
                    approveGymCenter();
                    break;
                case 7:
                    viewStatistics();
                    break;
                case 0:
                    return;
                default:
                    showError("Invalid choice!");
            }
        }
    }
    
    /**
     * Gym Owner menu
     */
    private static void gymOwnerMenu() {
        // Check if user is logged in and has correct role
        if (currentUserId == null) {
            showError("Please login first!");
            pause();
            return;
        }
        
        if (!"GYM_OWNER".equals(currentUserRole)) {
            showError("Access denied! This menu is only for gym owners.");
            pause();
            return;
        }
        
        while (true) {
            clearScreen();
            printHeader("GYM OWNER MENU");
            
            System.out.println("┌─────────────────────────────────────────────────┐");
            System.out.println("│  1. View My Profile                             │");
            System.out.println("│  2. View All Gym Owners                         │");
            System.out.println("│  3. View Pending Approvals                      │");
            System.out.println("│  0. Back to Main Menu                           │");
            System.out.println("└─────────────────────────────────────────────────┘");
            System.out.print("\nEnter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewOwnerProfile();
                    break;
                case 2:
                    viewAllOwners();
                    break;
                case 3:
                    viewOwnerPendingApprovals();
                    break;
                case 0:
                    return;
                default:
                    showError("Invalid choice!");
            }
        }
    }
    
    // ==================== Customer Functions ====================
    
    private static void viewGymCentersByCity() {
        clearScreen();
        printHeader("VIEW GYM CENTERS BY CITY");
        
        System.out.print("Enter City Name: ");
        String city = scanner.nextLine();
        
        showLoading("Fetching gym centers");
        String response = sendGetRequest("/customer/centers/city/" + city);
        
        if (response == null || response.equals("[]")) {
            showError("No gym centers found in " + city);
        } else {
            showSuccess("Gym Centers in " + city + ":");
            displaySelectableList(response, "gymId", "gymName", "gymAddress");
        }
        
        pause();
    }
    
    private static void viewAvailableSlots() {
        clearScreen();
        printHeader("VIEW AVAILABLE SLOTS");
        
        // Step 1: Ask for city
        System.out.print("Enter City Name: ");
        String city = scanner.nextLine();
        
        showLoading("Fetching gym centers");
        String centersResponse = sendGetRequest("/customer/centers/city/" + city);
        
        if (centersResponse == null || centersResponse.equals("[]")) {
            showError("No gym centers found in " + city);
            pause();
            return;
        }
        
        // Step 2: Display centers and let user select
        showSuccess("Gym Centers in " + city + ":");
        String[] centerIds = displaySelectableList(centersResponse, "gymId", "gymName", "gymAddress");
        
        if (centerIds.length == 0) {
            pause();
            return;
        }
        
        System.out.print("\nSelect Center (Enter number): ");
        int centerChoice = getIntInput();
        
        if (centerChoice < 1 || centerChoice > centerIds.length) {
            showError("Invalid selection!");
            pause();
            return;
        }
        
        String selectedGymId = centerIds[centerChoice - 1];
        
        // Step 3: Show slots for selected center
        showLoading("Fetching available slots");
        String slotsResponse = sendGetRequest("/customer/slots/available/" + selectedGymId);
        
        if (slotsResponse != null && !slotsResponse.equals("[]")) {
            clearScreen();
            printHeader("AVAILABLE SLOTS");
            showSuccess("Slots at selected gym:");
            
            displaySlotsList(slotsResponse);
        } else {
            showError("No available slots found! Response: " + slotsResponse);
        }
        
        pause();
    }
    
    private static void bookSlot() {
        clearScreen();
        printHeader("BOOK A SLOT");
        
        if (currentUserId == null) {
            showError("Please login first!");
            pause();
            return;
        }
        
        // Step 1: Ask for booking date FIRST
        System.out.print("Enter Booking Date (YYYY-MM-DD): ");
        String bookingDate = scanner.nextLine();
        
        // Step 2: Ask for city
        System.out.print("\nEnter City Name: ");
        String city = scanner.nextLine();
        
        showLoading("Fetching gym centers");
        String centersResponse = sendGetRequest("/customer/centers/city/" + city);
        
        if (centersResponse == null || centersResponse.equals("[]")) {
            showError("No gym centers found in " + city);
            pause();
            return;
        }
        
        // Step 3: Display centers and let user select
        clearScreen();
        printHeader("SELECT GYM CENTER - " + bookingDate);
        showSuccess("Gym Centers in " + city + ":");
        String[] centerIds = displaySelectableList(centersResponse, "gymId", "gymName", "gymAddress");
        
        if (centerIds.length == 0) {
            pause();
            return;
        }
        
        System.out.print("\nSelect Center (Enter number): ");
        int centerChoice = getIntInput();
        
        if (centerChoice < 1 || centerChoice > centerIds.length) {
            showError("Invalid selection!");
            pause();
            return;
        }
        
        String selectedGymId = centerIds[centerChoice - 1];
        
        // Step 4: Show available slots for the specific date (date-specific availability)
        showLoading("Fetching available slots for " + bookingDate);
        String slotsResponse = sendGetRequest("/customer/slots/available/" + selectedGymId + "/" + bookingDate);
        
        if (slotsResponse == null || slotsResponse.equals("[]")) {
            showError("No available slots found!");
            pause();
            return;
        }
        
        // Step 5: Display slots and let user select
        clearScreen();
        printHeader("SELECT SLOT");
        System.out.println("Booking Date: " + bookingDate);
        System.out.println("Gym ID: " + selectedGymId + "\n");
        showSuccess("Available Slots:");
        String[] slotIds = displaySlotsList(slotsResponse);
        
        if (slotIds.length == 0) {
            pause();
            return;
        }
        
        System.out.print("\nSelect Slot (Enter number): ");
        int slotChoice = getIntInput();
        
        if (slotChoice < 1 || slotChoice > slotIds.length) {
            showError("Invalid selection!");
            pause();
            return;
        }
        
        String selectedSlotId = slotIds[slotChoice - 1];
        
        // Step 6: Book the slot
        // Use customerId if available, otherwise fall back to userId
        String customerIdForBooking = (currentCustomerId != null && !currentCustomerId.isEmpty()) 
                                       ? currentCustomerId : currentUserId;
        
        String jsonPayload = String.format("{\"customerId\":\"%s\",\"slotId\":\"%s\",\"bookingDate\":\"%s\"}", 
                                          customerIdForBooking, selectedSlotId, bookingDate);
        
        showLoading("Booking slot");
        String response = sendPostRequest("/customer/booking/create", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            clearScreen();
            showSuccess("Slot Booked Successfully!");
            System.out.println("╔═══════════════════════════════════════════════════╗");
            System.out.println("║  Booking ID: " + extractJsonValue(response, "bookingId"));
            System.out.println("║  Date: " + bookingDate);
            System.out.println("║  Slot ID: " + selectedSlotId);
            System.out.println("╚═══════════════════════════════════════════════════╝");
        } else {
            String errorMsg = extractJsonValue(response, "error");
            showError("Booking failed: " + errorMsg);
            
            // Check if slot is full and offer waitlist
            if (errorMsg != null && (errorMsg.toLowerCase().contains("no seats") || 
                                     errorMsg.toLowerCase().contains("fully booked") ||
                                     errorMsg.toLowerCase().contains("full"))) {
                System.out.println("\nThis slot is currently full.");
                System.out.print("Would you like to join the WAITLIST? (yes/no): ");
                String joinWaitlist = scanner.nextLine().trim().toLowerCase();
                
                if (joinWaitlist.equals("yes") || joinWaitlist.equals("y")) {
                    String waitlistPayload = String.format("{\"customerId\":\"%s\",\"slotId\":\"%s\",\"requestedDate\":\"%s\"}",
                                                          customerIdForBooking, selectedSlotId, bookingDate);
                    
                    showLoading("Adding to waitlist");
                    String waitlistResponse = sendPostRequest("/customer/waitlist/join", waitlistPayload);
                    
                    if (waitlistResponse != null && !waitlistResponse.contains("error")) {
                        showSuccess("Added to waitlist successfully!");
                        System.out.println("💡 You'll be automatically promoted and notified when a seat becomes available!");
                    } else {
                        showError("Failed to join waitlist: " + extractJsonValue(waitlistResponse, "error"));
                    }
                }
            }
        }
        
        pause();
    }
    
    private static void viewMyBookings() {
        clearScreen();
        printHeader("MY BOOKINGS");
        
        // Use logged-in customer ID
        String customerIdForBooking = (currentCustomerId != null && !currentCustomerId.isEmpty()) 
                                       ? currentCustomerId : currentUserId;
        
        System.out.println("Customer: " + currentUserName + " (" + customerIdForBooking + ")\n");
        
        showLoading("Fetching your bookings");
        String response = sendGetRequest("/customer/bookings/" + customerIdForBooking);
        
        if (response != null && !response.equals("[]")) {
            showSuccess("Your Bookings:");
            displayBookingsList(response);
        } else {
            showError("No bookings found!");
        }
        
        pause();
    }
    
    private static void cancelBooking() {
        clearScreen();
        printHeader("CANCEL BOOKING");
        
        // Use logged-in customer ID
        String customerIdForBooking = (currentCustomerId != null && !currentCustomerId.isEmpty()) 
                                       ? currentCustomerId : currentUserId;
        
        System.out.println("Customer: " + currentUserName + " (" + customerIdForBooking + ")\n");
        
        showLoading("Fetching your bookings");
        String response = sendGetRequest("/customer/bookings/" + customerIdForBooking);
        
        if (response == null || response.equals("[]")) {
            showError("No bookings found to cancel!");
            pause();
            return;
        }
        
        // Display bookings and get booking IDs
        String[] bookingIds = displayBookingsListWithSelection(response);
        
        if (bookingIds.length == 0) {
            showError("No bookings available!");
            pause();
            return;
        }
        
        System.out.print("\n🎫 Enter booking number to cancel (or 0 to go back): ");
        int choice = getIntInput();
        
        if (choice <= 0 || choice > bookingIds.length) {
            System.out.println("Cancelled - returning to menu");
            pause();
            return;
        }
        
        String bookingId = bookingIds[choice - 1];
        
        System.out.print("\nAre you sure you want to cancel booking " + bookingId + "? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (!confirm.equals("yes") && !confirm.equals("y")) {
            System.out.println("Cancelled - booking not removed");
            pause();
            return;
        }
        
        showLoading("Cancelling booking");
        String cancelResponse = sendDeleteRequest("/customer/booking/" + bookingId);
        
        if (cancelResponse != null && !cancelResponse.contains("error")) {
            showSuccess("Booking cancelled successfully!");
        } else {
            showError("Cancellation failed: " + extractJsonValue(cancelResponse, "error"));
        }
        
        pause();
    }
    
    private static void viewNotifications() {
        clearScreen();
        printHeader("NOTIFICATIONS");
        
        // Use logged-in customer ID
        String customerIdForBooking = (currentCustomerId != null && !currentCustomerId.isEmpty()) 
                                       ? currentCustomerId : currentUserId;
        
        System.out.println("Customer: " + currentUserName + " (" + customerIdForBooking + ")\n");
        
        showLoading("Fetching notifications");
        String response = sendGetRequest("/customer/notifications/" + customerIdForBooking);
        
        if (response != null) {
            showSuccess("Your Notifications:");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void joinWaitlist() {
        clearScreen();
        printHeader("JOIN WAITLIST");
        
        // Use logged-in customer ID
        String customerIdForBooking = (currentCustomerId != null && !currentCustomerId.isEmpty()) 
                                       ? currentCustomerId : currentUserId;
        
        System.out.println("Customer: " + currentUserName + " (" + customerIdForBooking + ")\n");
        
        System.out.print("Slot ID: ");
        String slotId = scanner.nextLine();
        
        System.out.print("Requested Date (YYYY-MM-DD): ");
        String requestedDate = scanner.nextLine();
        
        String jsonPayload = String.format("{\"customerId\":\"%s\",\"slotId\":\"%s\",\"requestedDate\":\"%s\"}",
                                          customerIdForBooking, slotId, requestedDate);
        
        showLoading("Adding to waitlist");
        String response = sendPostRequest("/customer/waitlist/join", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            showSuccess("Added to waitlist successfully!");
        } else {
            showError("Failed to join waitlist!");
        }
        
        pause();
    }
    
    private static void findNearestSlot() {
        clearScreen();
        printHeader("FIND NEAREST SLOT");
        
        System.out.print("Gym ID: ");
        String gymId = scanner.nextLine();
        
        System.out.print("Preferred Time (HH:MM): ");
        String preferredTime = scanner.nextLine();
        
        showLoading("Finding nearest slot");
        String response = sendGetRequest("/customer/slots/nearest/" + gymId + "/" + preferredTime);
        
        if (response != null) {
            showSuccess("Nearest Available Slot:");
            System.out.println(response);
        }
        
        pause();
    }
    
    // ==================== Admin Functions with Stream API ====================
    
    private static void viewApprovedOwners() {
        clearScreen();
        printHeader("APPROVED GYM OWNERS (Stream API Filter)");
        
        showLoading("Fetching approved gym owners using Stream API");
        String response = sendGetRequest("/admin/owners/approved");
        
        if (response != null) {
            showSuccess("Approved Gym Owners (Filtered by Stream API):");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void viewPendingOwners() {
        clearScreen();
        printHeader("PENDING GYM OWNERS (Stream API Filter)");
        
        showLoading("Fetching pending gym owners using Stream API");
        String response = sendGetRequest("/admin/owners/pending");
        
        if (response != null) {
            showSuccess("Pending Gym Owners (Filtered by Stream API):");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void viewApprovedCenters() {
        clearScreen();
        printHeader("APPROVED GYM CENTERS (Stream API Filter)");
        
        showLoading("Fetching approved gym centers using Stream API");
        String response = sendGetRequest("/admin/centers/approved");
        
        if (response != null) {
            showSuccess("Approved Gym Centers (Filtered by Stream API):");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void viewPendingCenters() {
        clearScreen();
        printHeader("PENDING GYM CENTERS (Stream API Filter)");
        
        showLoading("Fetching pending gym centers using Stream API");
        String response = sendGetRequest("/admin/centers/pending");
        
        if (response != null) {
            showSuccess("Pending Gym Centers (Filtered by Stream API):");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void approveGymOwner() {
        clearScreen();
        printHeader("APPROVE GYM OWNER");
        
        System.out.print("Enter Owner ID: ");
        String ownerId = scanner.nextLine();
        
        showLoading("Approving gym owner");
        String response = sendPutRequest("/admin/owner/approve/" + ownerId, "{}");
        
        if (response != null && !response.contains("error")) {
            showSuccess("Gym owner approved successfully!");
        } else {
            showError("Approval failed!");
        }
        
        pause();
    }
    
    private static void approveGymCenter() {
        clearScreen();
        printHeader("APPROVE GYM CENTER");
        
        System.out.print("Enter Gym ID: ");
        String gymId = scanner.nextLine();
        
        showLoading("Approving gym center");
        String response = sendPutRequest("/admin/center/approve/" + gymId, "{}");
        
        if (response != null && !response.contains("error")) {
            showSuccess("Gym center approved successfully!");
        } else {
            showError("Approval failed!");
        }
        
        pause();
    }
    
    private static void viewStatistics() {
        clearScreen();
        printHeader("SYSTEM STATISTICS");
        
        showLoading("Fetching statistics");
        String response = sendGetRequest("/admin/statistics");
        
        if (response != null) {
            showSuccess("System Statistics:");
            System.out.println(response);
        }
        
        pause();
    }
    
    // ==================== Owner Functions ====================
    
    private static void viewOwnerProfile() {
        clearScreen();
        printHeader("GYM OWNER PROFILE");
        
        System.out.print("Enter Owner ID: ");
        String ownerId = scanner.nextLine();
        
        showLoading("Fetching profile");
        String response = sendGetRequest("/owner/profile/" + ownerId);
        
        if (response != null) {
            showSuccess("Owner Profile:");
            System.out.println(response);
        }
        
        pause();
    }
    
    private static void viewAllOwners() {
        clearScreen();
        printHeader("ALL GYM OWNERS");
        
        showLoading("Fetching all gym owners");
        String response = sendGetRequest("/owner/all");
        
        if (response != null) {
            showSuccess("All Gym Owners:");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void viewOwnerPendingApprovals() {
        clearScreen();
        printHeader("PENDING APPROVALS");
        
        showLoading("Fetching pending approvals");
        String response = sendGetRequest("/owner/pending");
        
        if (response != null) {
            showSuccess("Pending Gym Owners:");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    // ==================== Registration & Auth Functions ====================
    
    private static void registerCustomer() {
        clearScreen();
        printHeader("REGISTER AS CUSTOMER");
        
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("📧 Email: ");
        String email = scanner.nextLine();
        
        System.out.print("🔒 Password: ");
        String password = scanner.nextLine();
        
        System.out.print("🏠 Address: ");
        String address = scanner.nextLine();
        
        String jsonPayload = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"address\":\"%s\"}", 
                                          name, email, password, address);
        
        showLoading("Registering");
        String response = sendPostRequest("/auth/register/customer", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            showSuccess("Registration successful!");
            System.out.println(response);
        } else {
            showError("Registration failed: " + extractJsonValue(response, "error"));
        }
        
        pause();
    }
    
    private static void registerGymOwner() {
        clearScreen();
        printHeader("REGISTER AS GYM OWNER");
        
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("📧 Email: ");
        String email = scanner.nextLine();
        
        System.out.print("🔒 Password: ");
        String password = scanner.nextLine();
        
        System.out.print("🏠 Address: ");
        String address = scanner.nextLine();
        
        System.out.print("💳 PAN Card: ");
        String panCard = scanner.nextLine();
        
        System.out.print("🪪 Aadhar Card: ");
        String aadharCard = scanner.nextLine();
        
        System.out.print("GST Number: ");
        String gstNumber = scanner.nextLine();
        
        String jsonPayload = String.format(
            "{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"address\":\"%s\"," +
            "\"panCard\":\"%s\",\"aadharCard\":\"%s\",\"gstNumber\":\"%s\"}", 
            name, email, password, address, panCard, aadharCard, gstNumber);
        
        showLoading("Registering");
        String response = sendPostRequest("/auth/register/owner", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            showSuccess("Registration successful! Pending admin approval.");
            System.out.println(response);
        } else {
            showError("Registration failed: " + extractJsonValue(response, "error"));
        }
        
        pause();
    }
    
    private static void changePassword() {
        clearScreen();
        printHeader("CHANGE PASSWORD");
        
        if (currentUserId == null) {
            showError("Please login first!");
            pause();
            return;
        }
        
        System.out.print("🔒 Old Password: ");
        String oldPassword = scanner.nextLine();
        
        System.out.print("🔐 New Password: ");
        String newPassword = scanner.nextLine();
        
        String jsonPayload = String.format("{\"userId\":\"%s\",\"oldPassword\":\"%s\",\"newPassword\":\"%s\"}", 
                                          currentUserId, oldPassword, newPassword);
        
        showLoading("Changing password");
        String response = sendPutRequest("/auth/password/change", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            showSuccess("Password changed successfully!");
        } else {
            showError("Password change failed!");
        }
        
        pause();
    }
    
    private static void logout() {
        currentUserId = null;
        currentUserRole = null;
        currentUserName = null;
        showSuccess("Logged out successfully!");
        pause();
    }
    
    private static void exitApplication() {
        clearScreen();
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                               ║");
        System.out.println("║            Thank you for using FlipFit!                        ║");
        System.out.println("║                Stay Fit, Stay Healthy! 💪                     ║");
        System.out.println("║                                                               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.exit(0);
    }
    
    // ==================== HTTP Request Methods ====================
    
    private static String sendGetRequest(String endpoint) {
        try {
            URL url = new URL(API_BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            }
        } catch (Exception e) {
            showError("Connection error: " + e.getMessage());
        }
        return null;
    }
    
    private static String sendPostRequest(String endpoint, String jsonPayload) {
        try {
            URL url = new URL(API_BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            InputStream inputStream = (responseCode < 400) ? conn.getInputStream() : conn.getErrorStream();
            
            if (inputStream != null) {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            }
        } catch (Exception e) {
            showError("Connection error: " + e.getMessage());
        }
        return null;
    }
    
    private static String sendPutRequest(String endpoint, String jsonPayload) {
        try {
            URL url = new URL(API_BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            InputStream inputStream = (responseCode < 400) ? conn.getInputStream() : conn.getErrorStream();
            
            if (inputStream != null) {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            }
        } catch (Exception e) {
            showError("Connection error: " + e.getMessage());
        }
        return null;
    }
    
    private static String sendDeleteRequest(String endpoint) {
        try {
            URL url = new URL(API_BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            }
        } catch (Exception e) {
            showError("Connection error: " + e.getMessage());
        }
        return null;
    }
    
    // ==================== Utility Methods ====================
    
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private static void printHeader(String title) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║  " + centerText(title, 59) + "  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text + " ".repeat(Math.max(0, padding));
    }
    
    private static void showSuccess(String message) {
        System.out.println("\n" + message + "\n");
    }
    
    private static void showError(String message) {
        System.out.println("\nERROR: " + message + "\n");
    }
    
    private static void showLoading(String message) {
        System.out.print("\n⏳ " + message);
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(300);
                System.out.print(".");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }
    
    private static void pause() {
        System.out.print("\n🔄 Press Enter to continue...");
        scanner.nextLine();
    }
    
    private static void pauseForEffect() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static int getIntInput() {
        try {
            while (!scanner.hasNextInt()) {
                if (!scanner.hasNext()) {
                    System.err.println("\nError: No input available. Please run in an interactive terminal.");
                    System.exit(1);
                }
                scanner.next();
                System.out.print("Invalid input! Please enter a number: ");
            }
            int input = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            return input;
        } catch (NoSuchElementException e) {
            System.err.println("\nError: Input stream closed. Please run in an interactive terminal.");
            System.exit(1);
            return -1; // Never reached
        }
    }
    
    private static String extractJsonValue(String json, String key) {
        if (json == null) return "";
        
        // Try string value first: "key":"value"
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex != -1) {
            startIndex += searchKey.length();
            int endIndex = json.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return json.substring(startIndex, endIndex);
            }
        }
        
        // Try numeric/boolean/array value: "key":value or "key":[1,2,3]
        searchKey = "\"" + key + "\":";
        startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return "";
        
        startIndex += searchKey.length();
        // Skip whitespace
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }
        
        // Check if it's an array value
        if (startIndex < json.length() && json.charAt(startIndex) == '[') {
            // Find matching closing bracket
            int bracketCount = 1;
            int endIndex = startIndex + 1;
            while (endIndex < json.length() && bracketCount > 0) {
                if (json.charAt(endIndex) == '[') {
                    bracketCount++;
                } else if (json.charAt(endIndex) == ']') {
                    bracketCount--;
                }
                endIndex++;
            }
            if (bracketCount == 0) {
                return json.substring(startIndex, endIndex).trim();
            }
        }
        
        // Find end (comma, closing brace)
        int endIndex = startIndex;
        while (endIndex < json.length()) {
            char c = json.charAt(endIndex);
            if (c == ',' || c == '}') {
                break;
            }
            endIndex++;
        }
        
        if (endIndex > startIndex) {
            return json.substring(startIndex, endIndex).trim();
        }
        
        return "";
    }
    
    private static void displayJsonArray(String jsonArray) {
        if (jsonArray == null || jsonArray.equals("[]")) {
            System.out.println("   No data found");
            return;
        }
        
        // Simple pretty print for JSON array
        String formatted = jsonArray.replace("[{", "[\n  {")
                                     .replace("},{", "},\n  {")
                                     .replace("}]", "}\n]")
                                     .replace(",\"", ",\n    \"");
        
        System.out.println(formatted);
    }
    
    /**
     * Display a numbered list of items and return their IDs
     * 
     * @param jsonArray JSON array string
     * @param idField Field name for ID
     * @param nameField Field name for display name
     * @param addressField Field name for address
     * @return Array of IDs in order
     */
    private static String[] displaySelectableList(String jsonArray, String idField, 
                                                   String nameField, String addressField) {
        if (jsonArray == null || jsonArray.equals("[]")) {
            System.out.println("   No data found");
            return new String[0];
        }
        
        // Split by objects
        String[] items = jsonArray.split("\\},\\{");
        String[] ids = new String[items.length];
        
        System.out.println("\n┌─────────────────────────────────────────────────────────────────┐");
        
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            
            // Extract values
            String id = extractJsonValue("{" + item + "}", idField);
            String name = extractJsonValue("{" + item + "}", nameField);
            String address = extractJsonValue("{" + item + "}", addressField);
            
            ids[i] = id;
            
            // Display numbered item
            System.out.printf("│ %2d. %-20s │ %-35s │%n", (i + 1), name, address);
        }
        
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        
        return ids;
    }
    
    /**
     * Display slots with serial numbers
     * 
     * @param slotsJson JSON array of slots
     * @return Array of slot IDs
     */
    private static String[] displaySlotsList(String slotsJson) {
        if (slotsJson == null || slotsJson.equals("[]")) {
            System.out.println("   No slots available");
            return new String[0];
        }
        
        // Split by objects
        String[] slots = slotsJson.split("\\},\\{");
        String[] slotIds = new String[slots.length];
        
        System.out.println("\n┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ No. │  Start Time  │   End Time   │ Seats │  Price   │  Status    │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");
        
        for (int i = 0; i < slots.length; i++) {
            String slot = slots[i];
            
            // Extract values
            String slotId = extractJsonValue("{" + slot + "}", "slotId");
            String startTime = extractJsonValue("{" + slot + "}", "startTime");
            String endTime = extractJsonValue("{" + slot + "}", "endTime");
            String totalSeats = extractJsonValue("{" + slot + "}", "totalSeats");
            String availSeats = extractJsonValue("{" + slot + "}", "availableSeats");
            String price = extractJsonValue("{" + slot + "}", "price");
            
            // Parse time from array format [hour,minute,second] to HH:MM:SS
            startTime = parseTimeFromArray(startTime);
            endTime = parseTimeFromArray(endTime);
            
            // Handle empty values
            if (availSeats == null || availSeats.isEmpty()) availSeats = "0";
            if (totalSeats == null || totalSeats.isEmpty()) totalSeats = "0";
            if (price == null || price.isEmpty()) price = "0";
            if (startTime == null || startTime.isEmpty()) startTime = "N/A";
            if (endTime == null || endTime.isEmpty()) endTime = "N/A";
            
            slotIds[i] = slotId;
            
            // Format seats info
            String seatsInfo = availSeats + "/" + totalSeats;
            int availSeatsInt = 0;
            try {
                availSeatsInt = Integer.parseInt(availSeats);
            } catch (NumberFormatException e) {
                // Default to 0 if parsing fails
            }
            String status = availSeatsInt > 0 ? "Available" : "Full";
            
            // Display numbered slot
            System.out.printf("│ %3d │  %10s  │  %10s  │ %5s │ ₹%-7s │ %-10s │%n", 
                            (i + 1), startTime, endTime, seatsInfo, price, status);
        }
        
        System.out.println("└──────────────────────────────────────────────────────────────────────┘");
        
        return slotIds;
    }
    
    /**
     * Parse time from JSON array format [hour,minute,second] to HH:MM:SS string
     */
    private static String parseTimeFromArray(String timeArray) {
        if (timeArray == null || timeArray.isEmpty() || !timeArray.startsWith("[")) {
            return timeArray; // Return as-is if not array format
        }
        
        try {
            // Remove brackets and split
            String cleaned = timeArray.replace("[", "").replace("]", "");
            String[] parts = cleaned.split(",");
            
            if (parts.length >= 2) {
                int hour = Integer.parseInt(parts[0].trim());
                int minute = Integer.parseInt(parts[1].trim());
                int second = parts.length >= 3 ? Integer.parseInt(parts[2].trim()) : 0;
                
                return String.format("%02d:%02d:%02d", hour, minute, second);
            }
        } catch (Exception e) {
            // If parsing fails, return original
        }
        
        return timeArray;
    }
    
    /**
     * Display bookings in a formatted table with gym center and time details
     */
    private static void displayBookingsList(String bookingsJson) {
        displayBookingsListWithSelection(bookingsJson);
    }
    
    /**
     * Display bookings in a formatted table and return booking IDs for selection
     */
    private static String[] displayBookingsListWithSelection(String bookingsJson) {
        if (bookingsJson == null || bookingsJson.equals("[]")) {
            System.out.println("   No bookings found");
            return new String[0];
        }
        
        // Remove outer brackets and split by booking objects
        String cleaned = bookingsJson.trim();
        if (cleaned.startsWith("[")) cleaned = cleaned.substring(1);
        if (cleaned.endsWith("]")) cleaned = cleaned.substring(0, cleaned.length() - 1);
        
        String[] bookings = cleaned.split("\\},\\{");
        String[] bookingIds = new String[bookings.length];
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║ No. │  Booking ID   │   Gym Center    │    Date    │    Time Slot    │  Status   ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        for (int i = 0; i < bookings.length; i++) {
            String booking = bookings[i];
            
            // Extract values from the enriched response
            String bookingId = extractJsonValue("{" + booking + "}", "bookingId");
            String dateStr = extractJsonValue("{" + booking + "}", "bookingDate");
            String status = extractJsonValue("{" + booking + "}", "bookingStatus");
            String gymName = extractJsonValue("{" + booking + "}", "gymName");
            
            // Store booking ID
            bookingIds[i] = bookingId;
            
            // Extract slot times
            String startTimeStr = extractJsonValue("{" + booking + "}", "startTime");
            String endTimeStr = extractJsonValue("{" + booking + "}", "endTime");
            
            // Parse times from array format
            String startTime = parseTimeFromArray(startTimeStr);
            String endTime = parseTimeFromArray(endTimeStr);
            String timeSlot = "N/A";
            if (!startTime.isEmpty() && !endTime.isEmpty() && !startTime.equals("N/A") && !startTime.equals("null")) {
                // Safely extract HH:MM from time strings
                String startHHMM = startTime.length() >= 5 ? startTime.substring(0, 5) : startTime;
                String endHHMM = endTime.length() >= 5 ? endTime.substring(0, 5) : endTime;
                timeSlot = startHHMM + "-" + endHHMM;
            }
            
            // Parse date from array format [2026,1,30] to readable format
            String formattedDate = parseDateFromArray(dateStr);
            
            // Default gym name if not available
            if (gymName == null || gymName.isEmpty() || gymName.equals("null")) {
                gymName = "Unknown";
            }
            
            // Truncate gym name if too long
            if (gymName.length() > 15) {
                gymName = gymName.substring(0, 12) + "...";
            }
            
            // Display booking row
            System.out.printf("║ %3d │ %13s │ %-15s │ %10s │ %15s │ %9s ║%n", 
                            (i + 1), bookingId, gymName, formattedDate, timeSlot, status);
        }
        
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════════════╝");
        
        return bookingIds;
    }
    
    /**
     * Parse date from JSON array format [year,month,day] to YYYY-MM-DD
     */
    private static String parseDateFromArray(String dateArray) {
        if (dateArray == null || dateArray.isEmpty() || !dateArray.startsWith("[")) {
            return dateArray;
        }
        
        try {
            String cleaned = dateArray.replace("[", "").replace("]", "");
            String[] parts = cleaned.split(",");
            
            if (parts.length >= 3) {
                int year = Integer.parseInt(parts[0].trim());
                int month = Integer.parseInt(parts[1].trim());
                int day = Integer.parseInt(parts[2].trim());
                
                return String.format("%04d-%02d-%02d", year, month, day);
            }
        } catch (Exception e) {
            // If parsing fails, return original
        }
        
        return dateArray;
    }
}
