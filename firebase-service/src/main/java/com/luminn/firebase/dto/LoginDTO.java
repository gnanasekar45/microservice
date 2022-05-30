package com.luminn.firebase.dto;

/**
 * Created by ch on 2/3/2016.
 */
public class LoginDTO {

    private String id;


    private String supplierId;
   // private String supplierName;
    private String role;
    private String taxiId;
    private String lan;
    private double hourly;
    private double price;
    private double peakPrice;
    private double basePrice;
    private String token;
    private String status;
    private String isApproved;
    private String type;
    private String phoneNumber;
    private String email;
    public String isImage;
    private String name;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getOneSignalValue() {
        return oneSignalValue;
    }

    public void setOneSignalValue(String oneSignalValue) {
        this.oneSignalValue = oneSignalValue;
    }

    private String oneSignalValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getHourly() {
        return hourly;
    }

    public void setHourly(double hourly) {
        this.hourly = hourly;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPeakPrice() {
        return peakPrice;
    }

    public void setPeakPrice(double peakPrice) {
        this.peakPrice = peakPrice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(String taxiId) {
        this.taxiId = taxiId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIsImage() {
        return isImage;
    }

    public void setIsImage(String isImage) {
        this.isImage = isImage;
    }
}
