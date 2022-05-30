package com.luminn.firebase.controller.firebase;

public class TaxiDriver {

	private String fbToken, name, carName;
	
	public TaxiDriver() {
	}

	public String getFbToken() {
		return fbToken;
	}

	public void setFbToken(String fbToken) {
		this.fbToken = fbToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	@Override
	public String toString() {
		return "TaxiDriver [fbToken=" + fbToken + ", name=" + name + ", carName=" + carName + "]";
	}
	
	

}
