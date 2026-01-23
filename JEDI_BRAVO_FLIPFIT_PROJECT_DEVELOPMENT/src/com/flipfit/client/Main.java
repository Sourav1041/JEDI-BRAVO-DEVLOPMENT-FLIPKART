package com.flipfit.client;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Flipfit Application!");

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Login");
            System.out.println("2. Registration of the GymCustomer");
            System.out.println("3. Registration of the GymOwner");
            System.out.println("4. Change Password");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    System.out.println("GymCustomer Registration is not yet implemented.");
                    break;
                case 3:
                    System.out.println("GymOwner Registration is not yet implemented.");
                    break;
                case 4:
                    System.out.println("Change Password is not yet implemented.");
                    break;
                case 5:
                    System.out.println("exited!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void login(Scanner scanner) {
        System.out.println("\n--- Login ---");
        System.out.print("Enter Username: ");
        String username = scanner.next();
        System.out.print("Enter Password: ");
        String password = scanner.next();
        System.out.println("Choose your role:");
        System.out.println("1. GYMOwner");
        System.out.println("2. GymCustomer");
        System.out.println("3. GymAdmin");
        System.out.print("Enter role choice: ");
        int roleChoice = scanner.nextInt();

        
        if (username.equals("admin") && password.equals("admin")) {
            switch (roleChoice) {
                case 1:
                    GymOwnerMenu gymOwnerMenu = new GymOwnerMenu();
                    gymOwnerMenu.displayMenu();
                    break;
                case 2:
                    CustomerMenu customerMenu = new CustomerMenu();
                    customerMenu.displayMenu();
                    break;
                case 3:
                    AdminMenu adminMenu = new AdminMenu();
                    adminMenu.displayMenu();
                    break;
                default:
                    System.out.println("Invalid role choice.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }
}
