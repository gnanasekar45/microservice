package com.luminn.firebase.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.luminn.firebase.exception.UserExistException;
import com.luminn.firebase.request.User;
import com.luminn.firebase.service.UserService;


public class UserOldController {
	
	/*@Autowired
	private UserService userService;
	
	@GetMapping("/user/{email}")
	public User getUserByEmail(@PathVariable String email) throws InterruptedException, ExecutionException {
		User result = this.userService.getUser(email);
		return result;
	}
	
	@GetMapping("/user/all")
	public List<User> getAllUsers() throws InterruptedException, ExecutionException {
		return this.userService.getAllUsers();
	}
	
	@PostMapping("/user")
	public ResponseEntity<String> insertNewUser(@RequestBody User newUser) throws InterruptedException, ExecutionException {
		try {
			this.userService.insertUser(newUser);
		} catch(UserExistException e) {
			return new ResponseEntity<String>(HttpStatus.FOUND);
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}*/
}
