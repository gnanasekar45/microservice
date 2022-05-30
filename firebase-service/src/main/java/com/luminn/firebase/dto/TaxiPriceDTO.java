package com.luminn.firebase.dto;


import com.luminn.firebase.entity.Supplier;

import java.util.ArrayList;
import java.util.List;


public class TaxiPriceDTO extends TaxisDTO{
	public String price;
	public String category;
	public String region;
	//public String company;
	public TaxiPriceDTO(TaxisDTO dto){
		name = dto.getName();
		carType = dto.getCarType();
		latitude = dto.getLatitude();
		longitude = dto.getLongitude();
		status = dto.getStatus();
		phonenumber= dto.getPhonenumber();
		token = dto.getToken();
		notificationToken = dto.getNotificationToken();
		driverId= dto.driverId;
	}

	public TaxiPriceDTO(){
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	/*public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}*/
}
