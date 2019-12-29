package com.company.backend.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.company.backend.model.entity.User;
import com.company.backend.repository.UserRepository;
import com.company.backend.shared.UniqueUsername;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		User inDB = userRepository.findByUsername(value);
		if(inDB == null) {
			return true;
		}

		return false;
	}

}
