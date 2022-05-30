package com.luminn.driver.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection="drivers")
@Getter @Setter
public class Driver {
	private String name;
	private double latitude;
	private double longitude;
	private int status;
}
