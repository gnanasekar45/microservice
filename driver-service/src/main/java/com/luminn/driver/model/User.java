package com.luminn.driver.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection="users")
@Getter @Setter
public class User {
	private String email;
	private String password;
}
