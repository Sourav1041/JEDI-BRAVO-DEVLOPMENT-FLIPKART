package com.flipfit.client;

import java.util.Scanner;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymUser;
import com.flipfit.business.AdminService;

public class AdminMenu {

	private final AdminService adminService;

	public AdminMenu(AdminService adminService) {
		this.adminService = adminService;
	}

	public void displayMenu() {
		Scanner scanner = new Scanner(System.in);
		int choice;

		do {
			System.out.println("\n--- Admin Menu ---");
			System.out.println("1. View all gyms");
			System.out.println("2. View all users");
			System.out.println("3. View all bookings");
			System.out.println("4. Logout");
			System.out.print("Enter your choice: ");

			if (!scanner.hasNextInt()) {
				System.out.println("No more input detected. Returning to previous menu.");
				return;
			}
			choice = scanner.nextInt();

			switch (choice) {
			case 1:
				printGyms();
				break;
			case 2:
				printUsers();
				break;
			case 3:
				printBookings();
				break;
			case 4:
				System.out.println("Logging out from Admin Menu.");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 4);
	}

	private void printGyms() {
		System.out.println("---- All Centers ----");
		for (GymCenter center : adminService.viewAllCenters()) {
			System.out.println(center.getCenterId() + " | " + center.getCenterLocn() + " | " + center.getCenterCity());
			for (GymSlot slot : center.getCenterSlot()) {
				System.out.println("   Slot " + slot.getSlotId() + " " + slot.getStartTime() + "-" + slot.getEndTime()
						+ " Seats:" + slot.getAvailableSeats() + "/" + slot.getTotalSeats());
			}
		}
	}

	private void printUsers() {
		System.out.println("---- All Users ----");
		for (GymUser user : adminService.viewAllUsers()) {
			System.out.println(user.getUserId() + " | " + user.getName() + " | " + user.getRole());
		}
	}

	private void printBookings() {
		System.out.println("---- All Bookings ----");
		for (Booking booking : adminService.viewAllBookings()) {
			System.out.println(booking.getBookingId() + " | User:" + booking.getGymUser().getUserId() + " | Slot:"
					+ booking.getGymSlot().getSlotId() + " | " + booking.getDateAndTime() + " | Status:"
					+ booking.getBookingStatus());
		}
	}
}
