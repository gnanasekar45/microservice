package com.luminn.firebase.view;



import com.luminn.firebase.dto.Taxi;
import com.luminn.firebase.entity.Supplier;
import com.luminn.firebase.entity.TRANSPORTTYPE;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.util.DRIVERSTATUS;
import com.luminn.firebase.util.DistanceTime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaxiView {

	private String cartype;
	private String id;

	private String name;
	private String phoneNumber;
	private float price;
	private float basePrice;
	private float waitingTime;
	private String imageUrl;
	private String blobkey;
	private String deviceId;
	private List<String> tag;
	private int star;
	private int hour;
	private int totalComments;
	private String notification;
	private String loginStatus;
	//private ImageInfo imageInfo;
	//private List<Date> deliveryTime;
	private float debit;

	private DistanceTime distanceTime;
	private String category;


	public String getOneSignalValue() {
		return oneSignalValue;
	}

	public void setOneSignalValue(String oneSignalValue) {
		this.oneSignalValue = oneSignalValue;
	}

	private String oneSignalValue;


	//private CARTYPE cartype;
	private TRANSPORTTYPE transporttype;
	private DRIVERSTATUS driverStatus;

	//private String driverStatus2;

    private String supplierId;

    private double latitude;
    private double longitude;
	private String driverId;
	private Long taxiDetailId;


	public double getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}




	public DRIVERSTATUS getDriverStatus() {
		return driverStatus;
	}

	public void setDriverStatus(DRIVERSTATUS driverStatus) {
		this.driverStatus = driverStatus;
	}


	public TaxiView(){
    	
    }

	/*public TaxiView(TaxiDetail entity){
		Taxi tx = null;
		try {
			 tx = taxiModel.getTaxi(entity.getTaxiId());e
			this(tx);
		}
		catch(DatabaseException b){

		}
	}*/
    public TaxiView(Taxi entity) {

		Supplier supplier = null;
    	this.id = entity.getId();

		//TaxiDetail detail = taxiDetailService.getTaxiById(entity.getId());
    	//this.description = entity.getDescription();
    	//this.price		= entity.getPrice();
    	//this.starRating = entity.getStarRating();

		/*if(entity.getGeoPt() != null && entity.getGeoPt() != null) {
			this.latitude = entity.getGeoPt().getLatitude();
			this.longitude = entity.getGeoPt().getLongitude();
		}*/


		//CK
		//this.driverStatus = entity.getStatus();

		if(entity.getCarType() != null)
			this.cartype = entity.getCarType();
		if(entity.getCategory() != null && !"".equalsIgnoreCase(entity.getCategory().name())  )
			this.category = entity.getCategory().name();

		/*if(detail.getImageInfos() != null && detail.getImageInfos().size() > 0){
			this.imageUrl = detail.getImageInfos().get(0).getImageUrl();
			this.blobkey = detail.getImageInfos().get(0).getBlobkey();
		}*/
    	//Supplier
		/*if(entity.getSupplierId() > 0 ){
    		 supplier = supplierModel.getById(entity.getSupplierId());
			 this.name = supplier.getName();
    	}*/
		//Driver phoneNUmber
		/*if(detail.getDriverPhoneNumber() != null){
			this.phoneNumber = detail.getDriverPhoneNumber();
			this.name = detail.getDriverName();
		}*/
    	
    }
	public TaxiView(Taxi entity, TaxiDetail detail) {
		
		this(entity);
			this.driverStatus = entity.getStatus();


		if(entity.getCarType() != null)
			this.cartype = entity.getCarType();
		if(entity.getCategory() != null && !"".equalsIgnoreCase(entity.getCategory().name())  )
			this.category = entity.getCategory().name();

       if(detail != null) {
		   if (detail.getDriverName() != null) {
			   if (!("".equals(detail.getDriverName()) && detail.getDriverName() != null ))
				   this.name = detail.getDriverName();
			   else
				   this.name = detail.getName();
		   }

		   else if((detail.getDriverName() != null && !detail.getDriverName().equals(""))){
			   this.name = detail.getDriverName();
		   }
		   else {
			   this.name = detail.getName();
		   }
		   //name
		   this.driverId = detail.getDriverId();



		   /*if (detail.getImageInfos() != null) {
			   this.setImageInfos(detail.getImageInfos());
		   }*/
	   }
    }

	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public List<String> getTag() {
		return tag;
	}

	public void setTag(List<String> tag) {
		this.tag = tag;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public TRANSPORTTYPE getTransporttype() {
		return transporttype;
	}

	public void setTransporttype(TRANSPORTTYPE transporttype) {
		this.transporttype = transporttype;
	}



	//public CARTYPE getCartype() {
	//	return cartype;
	//}

	//public void setCartype(CARTYPE cartype) {
	//	this.cartype = cartype;
	//}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getBlobkey() {
		return blobkey;
	}
	public void setBlobkey(String blobkey) {
		this.blobkey = blobkey;
	}

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public String toString(){
		String str = "id " + id +
		" cartype " + this.cartype  +
		" price " + this.price +
		" waitingTime " + this.waitingTime +
		" deviceId " + this.deviceId;
		return str;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public double getWaitingTime() {
		return waitingTime;
	}




	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}
	public int getTotalComments() {
		return totalComments;
	}

	public void setTotalComments(int totalComments) {
		this.totalComments = totalComments;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}


	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}

	public void setWaitingTime(float waitingTime) {
		this.waitingTime = waitingTime;
	}

	public Long getTaxiDetailId() {
		return taxiDetailId;
	}

	public void setTaxiDetailId(Long taxiDetailId) {
		this.taxiDetailId = taxiDetailId;
	}

	/*public List<Date> getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(List<Date> deliveryTime) {
		this.deliveryTime = deliveryTime;
	}*/

	public float getDebit() {
		return debit;
	}

	public void setDebit(float amount) {
		this.debit = amount;
	}

	public DistanceTime getDistanceTime() {
		return distanceTime;
	}

	public void setDistanceTime(DistanceTime distanceTime) {
		this.distanceTime = distanceTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}



	public String getSupplierId() {
		return supplierId;
	}
}
