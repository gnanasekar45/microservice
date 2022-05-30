package com.luminn.driver.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luminn.driver.model.User;
import com.luminn.driver.repository.UserRepository;
import com.luminn.driver.service.FirebaseUserService;

@RestController
public class UserController {
	
	@Autowired
	private FirebaseUserService firebaseService;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/getUsers")
	public List<User> getUsersFromFirebase() {
		/*List<User> users = this.userRepository.findAll();
		if (users.isEmpty()) {
			User[] frUsers = this.firebaseService.getUsers();
			users = Arrays.asList(frUsers);
			this.userRepository.saveAll(users);
		}
		return users;*/
		return null;
	}
	
}
