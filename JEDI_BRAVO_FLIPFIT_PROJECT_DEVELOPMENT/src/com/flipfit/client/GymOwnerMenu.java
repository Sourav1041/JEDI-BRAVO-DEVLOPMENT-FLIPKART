package com.flipfit.client;

import java.util.Scanner;

public class GymOwnerMenu {

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Gym Owner Menu ---");
            System.out.println("1. View my gyms");
            System.out.println("2. Add a new gym");
            System.out.println("3. View bookings for my gyms");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Viewing my gyms (not implemented).");
                    break;
                case 2:
                    System.out.println("Adding a new gym (not implemented).");
                    break;
                case 3:
                    System.out.println("Viewing bookings for my gyms (not implemented).");
                    break;
                case 4:
                    System.out.println("Logging out from Gym Owner Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }
}
