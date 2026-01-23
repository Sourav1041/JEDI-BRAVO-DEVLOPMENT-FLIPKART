package com.flipfit.client;

import java.util.Scanner;

public class CustomerMenu {

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View all gyms");
            System.out.println("2. Book a slot");
            System.out.println("3. View my bookings");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Viewing all gyms (not implemented).");
                    break;
                case 2:
                    System.out.println("Booking a slot (not implemented).");
                    break;
                case 3:
                    System.out.println("Viewing my bookings (not implemented).");
                    break;
                case 4:
                    System.out.println("Logging out from Customer Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }
}
