package com.luminn.firebase.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "driver_images")
@Getter @Setter
public class DriverPhoto {
	@Indexed(unique = true)
	private String driverName;
	private String userId;
	private String cituUserId;
	private String type;
	private Binary driverImage;
	@Id
	public String id;

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Binary getDriverImage() {
		return driverImage;
	}

	public void setDriverImage(Binary driverImage) {
		this.driverImage = driverImage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCituUserId() {
		return cituUserId;
	}

	public void setCituUserId(String cituUserId) {
		this.cituUserId = cituUserId;
	}
}
