package com.flipfit.business.impl;

import java.util.Collection;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymUser;
import com.flipfit.business.AdminService;
import com.flipfit.helper.DataStore;

public class AdminServiceImpl implements AdminService {

	@Override
	public Collection<GymCenter> viewAllCenters() {
		return DataStore.getAllCenters();
	}

	@Override
	public Collection<GymUser> viewAllUsers() {
		return DataStore.getAllUsers();
	}

	@Override
	public Collection<Booking> viewAllBookings() {
		return DataStore.getAllBookings();
	}
}

