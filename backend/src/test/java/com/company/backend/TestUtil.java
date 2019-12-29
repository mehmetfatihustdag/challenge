package com.company.backend;

import com.company.backend.model.entity.Order;
import com.company.backend.model.entity.User;

public class TestUtil {

	public static User createValidUser() {
		User user = new User();
		user.setUsername("test-user");
		user.setDisplayName("test-display");
		user.setPassword("Java1988");
		user.setImage("profile-image.png");
		return user;
	}
	
	public static User createValidUser(String username) {
		User user = createValidUser();
		user.setUsername(username);
		return user;
	}


	public static Order createValidOrder() {
		Order order = new Order();
		order.setContent("test content for the test order");
		return order;
	}


}
