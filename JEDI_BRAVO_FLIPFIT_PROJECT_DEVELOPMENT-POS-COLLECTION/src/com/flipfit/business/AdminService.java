package com.flipfit.business;

import java.util.Collection;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymUser;

public interface AdminService {
	Collection<GymCenter> viewAllCenters();

	Collection<GymUser> viewAllUsers();

	Collection<Booking> viewAllBookings();
}

