package com.luminn.firebase.dto;

import com.luminn.firebase.entity.Payment;
import com.luminn.firebase.entity.RIDESTATUS;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * Created by ch on 3/16/2016.
 */
@Component
public class RideDTO {
    String id;
    float latitude;
    float longitude;
    Float distance;

    //Date endTIme;
    String endTIme;
    String startTIme;
    float estimateTime;
    float km;
    float userKm;
    Float radius;
    String source;
    String destination;
    String userId;
    String driverId;

    Long taxiDetailId;
    Long dealId;

    String userName;
    String driverName;

    String desc;
    String type;

    Payment payment;

    float price;
    float totalPrice;


    //private ImageInfo imageInfo = new ImageInfo();

    private int star;
    private int totalComments;
    private String comments;

    private float base;
    private float travelTime;
    float totalwaitTime;
    float userTotalPrice;
    String discount;
    float tax;
    String category;
    float percentage;

    RIDESTATUS rideStatus;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }



    public RideDTO(){

    }
   public RideDTO(String id, String driverName, String userName){
        this.id = id;
        this.driverName = driverName;
        this.userName = userName;
    }

    public RideDTO(String id, String driverName){
        this.id = id;
        this.driverName = driverName;

    }


    public float getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(float travelTime) {
        this.travelTime = travelTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTaxiDetailId() {
        return taxiDetailId;
    }

    public void setTaxiDetailId(Long taxiDetailId) {
        this.taxiDetailId = taxiDetailId;
    }



    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    /*public RideDTO(@JsonProperty("id") Long id){
                    this.id = id;
                }*/
    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
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











    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }





    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public RIDESTATUS getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(RIDESTATUS rideStatus) {
        this.rideStatus = rideStatus;
    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }



    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getUserTotalPrice() {
        return userTotalPrice;
    }

    public void setUserTotalPrice(float userTotalPrice) {
        this.userTotalPrice = userTotalPrice;
    }

    public float getBase() {
        return base;
    }

    public float getUserKm() {
        return userKm;
    }

    public void setUserKm(float userKm) {
        this.userKm = userKm;
    }

    public float getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(float estimateTime) {
        this.estimateTime = estimateTime;
    }

    public String getEndTIme() {
        return endTIme;
    }

    public void setEndTIme(String endTIme) {
        this.endTIme = endTIme;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public float getTotalwaitTime() {
        return totalwaitTime;
    }

    public void setTotalwaitTime(float totalwaitTime) {
        this.totalwaitTime = totalwaitTime;
    }

    public float getTax() {
        return tax;
    }



    public void setTax(float tax) {
        this.tax = tax;
    }
    @Override
    public String toString(){

        return "RideDTO{" +
                "distance="+km+"\n"+
                "km="+km+"\n"+
                "basePrice=" + base + "\n"+
                "price=" + price + "\n"+
                "travelTime="+travelTime+ "\n"+
                "discount="+discount+ "\n"+
                "tax="+tax+ "\n"+
                "percentage="+percentage+ "\n"+
                "totalPrice=" + totalPrice + "\n"+
                "userTotalPrice=" + userTotalPrice + "\n"+
                "estimateTime="+estimateTime+ "\n"+

                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RideDTO rideDTO = (RideDTO) o;
        return Float.compare(rideDTO.latitude, latitude) == 0 &&
                Float.compare(rideDTO.longitude, longitude) == 0 &&
                endTIme == rideDTO.endTIme &&
                Float.compare(rideDTO.estimateTime, estimateTime) == 0 &&
                Float.compare(rideDTO.km, km) == 0 &&
                Float.compare(rideDTO.userKm, userKm) == 0 &&
                Float.compare(rideDTO.price, price) == 0 &&
                Float.compare(rideDTO.totalPrice, totalPrice) == 0 &&
                star == rideDTO.star &&
                totalComments == rideDTO.totalComments &&
                Float.compare(rideDTO.base, base) == 0 &&
                Float.compare(rideDTO.travelTime, travelTime) == 0 &&
                Float.compare(rideDTO.totalwaitTime, totalwaitTime) == 0 &&
                Float.compare(rideDTO.userTotalPrice, userTotalPrice) == 0 &&
                Float.compare(rideDTO.tax, tax) == 0 &&
                Objects.equals(id, rideDTO.id) &&
                Objects.equals(distance, rideDTO.distance) &&
                Objects.equals(radius, rideDTO.radius) &&
                Objects.equals(source, rideDTO.source) &&
                Objects.equals(destination, rideDTO.destination) &&
                Objects.equals(userId, rideDTO.userId) &&
                Objects.equals(driverId, rideDTO.driverId) &&
                Objects.equals(taxiDetailId, rideDTO.taxiDetailId) &&
                Objects.equals(dealId, rideDTO.dealId) &&
                //rideStatus == rideDTO.rideStatus &&
                Objects.equals(userName, rideDTO.userName) &&
                Objects.equals(driverName, rideDTO.driverName) &&
                Objects.equals(desc, rideDTO.desc) &&
                Objects.equals(type, rideDTO.type) &&
                //payment == rideDTO.payment &&
                //Objects.equals(imageInfo, rideDTO.imageInfo) &&
                Objects.equals(comments, rideDTO.comments) &&
                Objects.equals(discount, rideDTO.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude, distance, endTIme, estimateTime, km, userKm, radius, source, destination, userId, driverId, taxiDetailId, dealId, rideStatus, userName, driverName,  desc, type,  price, totalPrice,   star, totalComments, comments,  base, travelTime, totalwaitTime, userTotalPrice, discount, tax);
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

    public String getStartTIme() {
        return startTIme;
    }

    public void setStartTIme(String startTIme) {
        this.startTIme = startTIme;
    }
}
