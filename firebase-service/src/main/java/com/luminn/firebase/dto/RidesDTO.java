package com.luminn.firebase.dto;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RidesDTO {
    String userId;
    String driverId;
    Long bookingId;
    Long endTIme;
    float travelTime;
    public Date startDate;
    public Date endDate;
    public float totalPrice;
    public String VechileNumber;
    public String driverName;
    public String name;
    public String OTP;

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

    public Long getEndTIme() {
        return endTIme;
    }

    public void setEndTIme(Long endTIme) {
        this.endTIme = endTIme;
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

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }


    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getVechileNumber() {
        return VechileNumber;
    }

    public void setVechileNumber(String vechileNumber) {
        VechileNumber = vechileNumber;
    }
}
