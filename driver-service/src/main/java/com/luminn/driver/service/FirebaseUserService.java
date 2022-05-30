package com.luminn.driver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.luminn.driver.model.User;

@Service
public class FirebaseUserService {
	
	@Value("${firebase.service}")
	private String firebaseService;
	
	@Autowired
	private RestTemplate mainRest;
	
	public User[] getUsers() {
		User[] response = this.mainRest.getForObject(this.firebaseService, User[].class);
		return response;
	}
	
}
