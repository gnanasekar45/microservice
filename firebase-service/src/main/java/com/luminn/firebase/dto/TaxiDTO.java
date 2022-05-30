package com.luminn.firebase.dto;


import com.luminn.firebase.entity.Supplier;

import java.util.ArrayList;
import java.util.List;


public class TaxiDTO {

	private String id;

	private String taxiId;
	private String supplierId;
	private String userId;



	private String name;
	private String description;
	private int hour;
	private int km;
	public double distance;

	private String additionalInformation;
	private float latitude;
	private float longitude;
	private String phoneNumber;
	private String taxiNumber;
	private String drivername;
	private String driverPhonenumber;
	private String status;

	//private List<String> inclusions = new ArrayList<String>(0);
	//private List<String> exclusions = new ArrayList<String>(0);
	//private List<ImageInfo> imageInfos = new ArrayList<ImageInfo>(0);
	private List<String> Tags = new ArrayList<String>(0);
	private String currency;
	//private TRANSPORTTYPE transporttype;
	private String carType;

	private double airPortprice;
	private Double perDay;
	private Double weekEndOffer;
	private Double price;
	private Double peakPrice;
	private double basePrice;
	private double waitingTime;
	private float minimumFare;
	//private String owner;
	//private String commentDay;
	//NO
	private String source;

	private CityDTO cityDTO;

	private String destination;

	//private PICK_DROP_LOCATION pickUpLocation;
	//private PICK_DROP_LOCATION dropOffLocation;

	//private CityDTO cityDTO;
	private Supplier supplier;
	//private List<Review> reviews;
	private int seats;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaxiId() {
		return taxiId;
	}

	public void setTaxiId(String taxiId) {
		this.taxiId = taxiId;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	private Integer starRating;
	private Boolean active;
	private String updatedOn;

	private String city;

	private int year;
	private String vehicleBrand;

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}



	//private VehicleTypeDTO vehicleTypeDTO;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Double getPerDay() {
		return perDay;
	}

	public void setPerDay(Double perDay) {
		this.perDay = perDay;
	}

	public Double getWeekEndOffer() {
		return weekEndOffer;
	}

	public void setWeekEndOffer(Double weekEndOffer) {
		this.weekEndOffer = weekEndOffer;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}



	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setAirPortprice(double airPortprice) {
		this.airPortprice = airPortprice;
	}

	public double getAirPortprice() {
		return airPortprice;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}






	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}




	public String getTaxiNumber() {
		return taxiNumber;
	}

	public void setTaxiNumber(String taxiNumber) {
		this.taxiNumber = taxiNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}




	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	//public List<ImageInfo> getImageInfos() {
	//	return imageInfos;
	//}

	//public void setImageInfos(List<ImageInfo> imageInfos) {
	//	this.imageInfos = imageInfos;
	//}




	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}



	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	//public TRANSPORTTYPE getTransporttype() {
	//	return transporttype;
	//}

	//public void setTransporttype(TRANSPORTTYPE transporttype) {
	//	this.transporttype = transporttype;
	//}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	//public PICK_DROP_LOCATION getPickUpLocation() {
	//	return pickUpLocation;
	//}

	//public void setPickUpLocation(PICK_DROP_LOCATION pickUpLocation) {
	//	this.pickUpLocation = pickUpLocation;
	//}


	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getDrivername() {
		return drivername;
	}

	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}

	public String getDriverPhonenumber() {
		return driverPhonenumber;
	}

	public void setDriverPhonenumber(String driverPhonenumber) {
		this.driverPhonenumber = driverPhonenumber;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public double getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(double waitingTime) {
		this.waitingTime = waitingTime;
	}

	//public VehicleTypeDTO getVehicleTypeDTO() {
	//	return vehicleTypeDTO;
	//}

	//public void setVehicleTypeDTO(VehicleTypeDTO vehicleTypeDTO) {
	//	this.vehicleTypeDTO = vehicleTypeDTO;
	//}

	public List<String> getTags() {
		return Tags;
	}

	public void setTags(List<String> tags) {
		Tags = tags;
	}

	public int getYear() {
		return year;
	}

	public void setVehicleYear(int vehicleYear) {
		this.year = vehicleYear;
	}

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	public Double getPeakPrice() {
		return peakPrice;
	}

	public void setPeakPrice(Double peakPrice) {
		this.peakPrice = peakPrice;
	}

	public float getMinimumFare() {
		return minimumFare;
	}

	public void setMinimumFare(float minimumFare) {
		this.minimumFare = minimumFare;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getKm() {
		return km;
	}

	public void setKm(int km) {
		this.km = km;
	}

	public CityDTO getCityDTO() {
		return cityDTO;
	}

	public void setCityDTO(CityDTO cityDTO) {
		this.cityDTO = cityDTO;
	}


}
