package com.company.backend.resource;

import com.company.backend.model.entity.User;
import com.company.backend.shared.CurrentUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.backend.model.vm.UserVM;

@RestController
public class LoginResource {
	
	@PostMapping("/api/1.0/login")
	UserVM handleLogin(@CurrentUser User loggedInUser) {
		return new UserVM(loggedInUser);
	}
	
}
