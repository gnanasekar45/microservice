package com.luminn.firebase.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;


@Document(collection = "PhoneBooking")
@Getter
@Setter
public class PhoneBooking implements Serializable {

    @Id
    String id;

    Float latitude;
    Float longitude;
    //float km;
    Float radius;
    String source;
    String destination;
    String olddestination;
    String desc;
    String type;
    float tax;


    String destintation;

    String userPhoneNumber;

    String userId;

    String driverId;


    Long createdBy;

    RIDESTATUS status;
    //  String userName;
    //  String driverName;

    String supplierId;

    float price;
    float totalPrice;
    float userTotalPrice;
    float basePrice;

    private float travelTime;

    private Date startDate;
    private Date endDate;
    private float estimateTime;

    private float debit;
    private String payment;
    private String userName;
    String comments;
    String isMobile;

    //  @Index
    //  private Ref<User> user;

    //  @Index
    //   private Ref<User> driver;




    private Date updatedOn;


    public double sourceLatitude;
    public double sourceLongitude;
    public double destLatitude;
    public double destLongitude;
    public String category;
    public float km;

    public String getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(String isMobile) {
        this.isMobile = isMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getPrice() {
        return price;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public float getUserTotalPrice() {
        return userTotalPrice;
    }

    public String getOlddestination() {
        return olddestination;
    }

    public void setOlddestination(String olddestination) {
        this.olddestination = olddestination;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }





    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public void updatedOn() {
        updatedOn = new Date();
    }



    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RIDESTATUS getStatus() {
        return status;
    }

    public void setStatus(RIDESTATUS status) {
        this.status = status;
    }


    public void setUpdateOnDate() {
        updatedOn = new Date();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }






    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(float travelTime) {
        this.travelTime = travelTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setUserTotalPrice(float userTotalPrice) {
        this.userTotalPrice = userTotalPrice;
    }



    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public float getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(float estimateTime) {
        this.estimateTime = estimateTime;
    }

    public float getDebit() {
        return debit;
    }

    public void setDebit(float debit) {
        this.debit = debit;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDestintation() {
        return destintation;
    }

    public void setDestintation(String destintation) {
        this.destintation = destintation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    @Override
    public String toString(){

        return "Ride{" + "price=" + price + ",totalPrice=" + totalPrice + ",userTotalPrice=" + userTotalPrice +
                ",basePrice=" + basePrice +
                ",travelTime="+travelTime+
                ",estimateTime="+estimateTime+
                '}';
    }


    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public double getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(double sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public double getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(double sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public double getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(double destLatitude) {
        this.destLatitude = destLatitude;
    }

    public double getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(double destLongitude) {
        this.destLongitude = destLongitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
    }
}
