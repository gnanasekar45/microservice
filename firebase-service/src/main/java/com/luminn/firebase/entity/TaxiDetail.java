package com.luminn.firebase.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ch on 1/20/2016.
 */
@Document(collection = "taxiDetail")
@Getter
@Setter
public class TaxiDetail {


    @Id
    private String id;


    private String taxiId;
    private String supplierId;
    private String supplierIds;
    private String driverId;


    //List<Key<P>> list = new ArrayList<Key<P>>();
    //private List<ImageInfo> imageInfos = new ArrayList<ImageInfo>(0);


    private List<String> tags = new ArrayList<String>(0);

    private String name;
    private String description;
    private Boolean acVehicle = false;
    private Integer maxPassengerCapacity = 0;

    private String meetingPoint;
    private String cancellationPolicy;
    private String additionalInformation;
    private String driverPhoneNumber;
    private String driverName;
    private String payment;

    private double basePrice = 0;
    private double waitingTime = 0;
    private float miniMumFare;


    private int year = 2016;
    private String vehicleBrand ;
    private int seats;
    private int hour;
    private int km;
    private String numberPlate;
    private String licenceNumber;
    private Date lastUpdate;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

//private Ref<Supplier> supplier;

    //@Index(IfNotDefault.class) Boolean active = false;

    //@Index(IfNotDefault.class) String currency = "CHF";

    //@Index(IfNotDefault.class) TRANSPORTTYPE transporttype = TRANSPORTTYPE.pointtopoint;

    //@Index
    private String source;

    //@Index
    private String destination;

    //@Index
    //private Ref<City> city;

    //@Index
    private Date updatedOn;

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }


    private PICK_DROP_LOCATION pickUpLocation;
    //private PICK_DROP_LOCATION dropOffLocation;

    TRANSPORTTYPE transporttype = TRANSPORTTYPE.pointtopoint;

    private double price;
    private double peakPrice;
    private double airPortprice;

    //private String commentDay;
    private Double perDay;
    private Double weekEndOffer;

    String currency = "CHF";
    String account;


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


    public void setPrice(double price) {
        this.price = price;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }



    private Integer starRating;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAcVehicle() {
        return acVehicle;
    }

    public void setAcVehicle(Boolean acVehicle) {
        this.acVehicle = acVehicle;
    }

    public Integer getMaxPassengerCapacity() {
        return maxPassengerCapacity;
    }

    public void setMaxPassengerCapacity(Integer maxPassengerCapacity) {
        this.maxPassengerCapacity = maxPassengerCapacity;
    }


    public String getMeetingPoint() {
        return meetingPoint;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }



    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }


    public PICK_DROP_LOCATION getPickUpLocation() {
        return pickUpLocation;
    }









    public TRANSPORTTYPE getTransporttype() {
        return transporttype;
    }

    public void setTransporttype(TRANSPORTTYPE transporttype) {
        this.transporttype = transporttype;
    }



    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }




    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }


    /*public List<ImageInfo> getImageInfos() {
        return imageInfos;
    }

    public void setImageInfos(List<ImageInfo> imageInfos) {
        this.imageInfos = imageInfos;
    }*/


    public void updatedOn() {
        updatedOn = new Date();
    }



    /*public Ref<Supplier> getSupplier() {
        return supplier;
    }

    public void setSupplier(Ref<Supplier> supplier) {
        this.supplier = supplier;
    }*/

    public double getAirPortprice() {
        return airPortprice;
    }

    public void setAirPortprice(double airPortprice) {
        this.airPortprice = airPortprice;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }
    public double getPrice() {
        return price;
    }

    public double getPeakPrice() {
        return peakPrice;
    }

    public void setPeakPrice(double peakPrice) {
        this.peakPrice = peakPrice;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public float getMiniMumFare() {
        return miniMumFare;
    }

    public void setMiniMumFare(float miniMumFare) {
        this.miniMumFare = miniMumFare;
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


    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getSupplierIds() {
        return supplierIds;
    }

    public void setSupplierIds(String supplierIds) {
        this.supplierIds = supplierIds;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
