package com.flipfit.business;

import com.flipfit.bean.GymUser;
import com.flipfit.enums.Role;

public interface UserService {
	GymUser login(String userId, String password, Role role);

	GymUser register(String name, String email, String password, Role role);

	boolean changePassword(String userId, String oldPassword, String newPassword);
}

