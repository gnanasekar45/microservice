package com.luminn.firebase.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hrlum on 20.10.2017.
 */


@Document(collection = "price")
@Getter
@Setter
public class Price implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private float km;

    private float price;
    private float kmTwo;
    private float priceTwo;
    private float waitingTimeTwo;

    private float kmThree;
    private float priceThree;
    private float waitingTimeThree;

    private float kmFour;
    private float priceFour;
    private float waitingTimeFour;

    private float kmFive;
    private float priceFive;
    private float waitingTimeFive;

    private float kmSix;
    private float priceSix;
    private float waitingTimeSix;

    private float peakPrice;
    private String carType;
    private float waitingTime;
    public float cancellation;
    private float basePrice;
    private float baseTime;
    private float baseAddPrice;


    public String supplierId;


    private String region;

    private int zipCode;

    private String domain;
    private float minimumPrice;

    private Date updatedOn;
    private float percentage;

    private float tax;
    private String category;
    private float travelTime;
    private float bonus;



    public float getCancellation() {
        return cancellation;
    }

    public void setCancellation(float cancellation) {
        this.cancellation = cancellation;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }


    public float getPrice() {
        return price;
    }

    public float getWaitingTime() {
        return waitingTime;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setWaitingTime(float waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public float getPeakPrice() {
        return peakPrice;
    }

    public void setPeakPrice(float peakPrice) {
        this.peakPrice = peakPrice;
    }

    public float getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(float minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
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

    public float getKmTwo() {
        return kmTwo;
    }

    public void setKmTwo(float kmTwo) {
        this.kmTwo = kmTwo;
    }

    public float getPriceTwo() {
        return priceTwo;
    }

    public void setPriceTwo(float priceTwo) {
        this.priceTwo = priceTwo;
    }

    public float getWaitingTimeTwo() {
        return waitingTimeTwo;
    }

    public void setWaitingTimeTwo(float waitingTimeTwo) {
        this.waitingTimeTwo = waitingTimeTwo;
    }

    public float getKmThree() {
        return kmThree;
    }

    public void setKmThree(float kmThree) {
        this.kmThree = kmThree;
    }

    public float getPriceThree() {
        return priceThree;
    }

    public void setPriceThree(float priceThree) {
        this.priceThree = priceThree;
    }

    public float getWaitingTimeThree() {
        return waitingTimeThree;
    }

    public void setWaitingTimeThree(float waitingTimeThree) {
        this.waitingTimeThree = waitingTimeThree;
    }

    public float getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(float travelTime) {
        this.travelTime = travelTime;
    }

    public float getKmFour() {
        return kmFour;
    }

    public void setKmFour(float kmFour) {
        this.kmFour = kmFour;
    }

    public float getPriceFour() {
        return priceFour;
    }

    public void setPriceFour(float priceFour) {
        this.priceFour = priceFour;
    }

    public float getWaitingTimeFour() {
        return waitingTimeFour;
    }

    public void setWaitingTimeFour(float waitingTimeFour) {
        this.waitingTimeFour = waitingTimeFour;
    }

    public float getKmFive() {
        return kmFive;
    }

    public void setKmFive(float kmFive) {
        this.kmFive = kmFive;
    }

    public float getPriceFive() {
        return priceFive;
    }

    public void setPriceFive(float priceFive) {
        this.priceFive = priceFive;
    }

    public float getWaitingTimeFive() {
        return waitingTimeFive;
    }

    public void setWaitingTimeFive(float waitingTimeFive) {
        this.waitingTimeFive = waitingTimeFive;
    }

    public float getKmSix() {
        return kmSix;
    }

    public void setKmSix(float kmSix) {
        this.kmSix = kmSix;
    }

    public float getPriceSix() {
        return priceSix;
    }

    public void setPriceSix(float priceSix) {
        this.priceSix = priceSix;
    }

    public float getWaitingTimeSix() {
        return waitingTimeSix;
    }

    public void setWaitingTimeSix(float waitingTimeSix) {
        this.waitingTimeSix = waitingTimeSix;
    }

    public float getBaseAddPrice() {
        return baseAddPrice;
    }

    public void setBaseAddPrice(float baseAddPrice) {
        this.baseAddPrice = baseAddPrice;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }

    public float getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(float baseTime) {
        this.baseTime = baseTime;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}
