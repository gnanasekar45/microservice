package com.luminn.firebase.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by ch on 3/16/2016.
 */
@Document(collection = "ride")
@Getter
@Setter
public class Ride implements Serializable {

    @Id
    public String id;
    float latitude;
    float longitude;
    float km;
    float baseKm;
    Float radius;
    String source;
    String destination;
    String olddestination;
    String desc;
    String type;
    float tax;
    float localKm;
    float percentage;
    String tripId;


    String userId;

    String driverId;


    String createdBy;

    RIDESTATUS status;
  //  String userName;
  //  String driverName;

    String supplierId;

    float price;
    float totalPrice;
    float totalTravelTimePrice;
    float userTotalPrice;
    float basePrice;

    private float travelTime;
     float totalwaitTime;

    private Date startDate;
    private Date endDate;
    private float estimateTime;

    private float debit;
    private String payment;
    private String discount;
    private float discountPrice;
    private String driverIds;
    private String userIds;
    private String category;

    private String taxiDetailId;
  //  @Index
  //  private Ref<User> user;

  //  @Index
 //   private Ref<User> driver;


    public String getDriverIds() {
        return driverIds;
    }

    public void setDriverIds(String driverIds) {
        this.driverIds = driverIds;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }


    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    private Date updatedOn;

    public float getLocalKm() {
        return localKm;
    }

    public void setLocalKm(float localKm) {
        this.localKm = localKm;
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



    public void setKm(int km) {
        this.km = km;
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

    public void updatedOn() {
        updatedOn = new Date();
    }



    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
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





    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
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

        return "Ride{" +
                "distance="+km+"\n"+
                "km="+km+"\n"+
                "basePrice=" + basePrice + "\n"+
                "price=" + price + "\n"+
                "travelTime="+travelTime+ "\n"+
                "discount="+discount+ "\n"+
                "tax="+tax+ "\n"+
                "debit="+debit+ "\n"+
                "totalPrice=" + totalPrice + "\n"+
                "userTotalPrice=" + userTotalPrice + "\n"+
                "estimateTime="+estimateTime+ "\n"+
                '}';
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public float getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(float discountPrice) {
        this.discountPrice = discountPrice;
    }

    public float getTotalwaitTime() {
        return totalwaitTime;
    }

    public void setTotalwaitTime(float totalwaitTime) {
        this.totalwaitTime = totalwaitTime;
    }

    public float getBaseKm() {
        return baseKm;
    }

    public void setBaseKm(float baseKm) {
        this.baseKm = baseKm;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getTotalTravelTimePrice() {
        return totalTravelTimePrice;
    }

    public void setTotalTravelTimePrice(float totalTravelTimePrice) {
        this.totalTravelTimePrice = totalTravelTimePrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxiDetailId() {
        return taxiDetailId;
    }

    public void setTaxiDetailId(String taxiDetailId) {
        this.taxiDetailId = taxiDetailId;
    }
}
