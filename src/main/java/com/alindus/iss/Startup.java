package com.alindus.iss;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alindus.iss.domain.Role;
import com.alindus.iss.domain.User;
import com.alindus.iss.service.UserService;

@Component
public class Startup {

	private static final String FIRSTNAME = "Bardan";
	private static final String LASTNAME = "Chalise";
	private static final String EMAIL = "bardan.chalise@alindus.com";
	private static final String PASSWORD = "admin@123";
	private static final String RE_PASSWORD = "admin@123";
	private static final Role ROLE = Role.ROLE_ADMIN;

	@Autowired
	private UserService userService;

	@PostConstruct
	public void init() {
		this.addAdminUser();
	}

	private void addAdminUser() {
		if (this.userService.findUserByEmail(EMAIL) == null) {
			User user = new User(EMAIL, PASSWORD, RE_PASSWORD, ROLE);
			user.setEnable(true);
			user.setFirstName(FIRSTNAME);
			user.setLastName(LASTNAME);
			userService.add(user);
		}
	}

}
