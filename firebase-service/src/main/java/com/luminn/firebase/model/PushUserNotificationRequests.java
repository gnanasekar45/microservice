package com.luminn.firebase.model;

public class PushUserNotificationRequests extends PushNotificationRequests {

    String isAndroid;
    String phoneNumber;
    String action;
    String rideKeyLocal;
    String paymentType;
    String coupen;
    String category;
    String latitude;
    String longitude;
    String userToken;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCoupen() {
        return coupen;
    }

    public void setCoupen(String coupen) {
        this.coupen = coupen;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIsAndroid() {
        return isAndroid;
    }

    public void setIsAndroid(String isAndroid) {
        this.isAndroid = isAndroid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRideKeyLocal() {
        return rideKeyLocal;
    }

    public void setRideKeyLocal(String rideKeyLocal) {
        this.rideKeyLocal = rideKeyLocal;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
