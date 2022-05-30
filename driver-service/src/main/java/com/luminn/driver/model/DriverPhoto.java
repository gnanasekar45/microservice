package com.luminn.driver.model;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;


@Document(collection = "driver_images")
@Getter @Setter
public class DriverPhoto {
	@Indexed(unique = true)
	private String driverName;
	private Binary driverImage;
}
