package com.luminn.firebase.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "supplier")
@Getter
@Setter
public class Supplier implements Serializable {


	//private Long id;
	@Id
	private String id;

	private String name;

	private String licenseNumber;
	private String address;

	/*private double latitude;
	private double longitude;
	private String phoneNumber;
	private String website;
	private String profilePictureUrl;*/

	private String driverName;
	private String driverphoneNumber;
	private String profilePictureBlobkey;


	private String userId;

	//@Index
	//private Ref<User> user;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private Date updatedOn;

	/*public String getTaxiCompanyName() {
		return taxiCompanyName;
	}

	public void setTaxiCompanyName(String taxiCompanyName) {
		this.taxiCompanyName = taxiCompanyName;
	}*/



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}



	public String getProfilePictureBlobkey() {
		return profilePictureBlobkey;
	}

	public void setProfilePictureBlobkey(String profilePictureBlobkey) {
		this.profilePictureBlobkey = profilePictureBlobkey;
	}

	//public Ref<User> getUser() {
	//	return user;
	//}

	//public void setUser(Ref<User> user) {
	//	this.user = user;
	//}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverphoneNumber() {
		return driverphoneNumber;
	}

	public void setDriverphoneNumber(String driverphoneNumber) {
		this.driverphoneNumber = driverphoneNumber;
	}


	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}




	public void updatedOn() {
		updatedOn = new Date();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
