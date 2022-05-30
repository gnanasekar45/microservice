package com.luminn.firebase.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
public class JsonDTO {
    private float km;
    private float totalPrice;
    private float userTotalPrice;
    private float basePrice;
    private  float totalTime;
    private float totalTravelTimePrice;

    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
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

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }

    public float getTotalTravelTimePrice() {
        return totalTravelTimePrice;
    }

    public void setTotalTravelTimePrice(float totalTravelTimePrice) {
        this.totalTravelTimePrice = totalTravelTimePrice;
    }

    @Override
    public String toString() {
        return "JsonDTO{" +
                "km=" + km +
                ", totalPrice=" + totalPrice +
                ", userTotalPrice=" + userTotalPrice +
                ", basePrice=" + basePrice +
                ", totalTime=" + totalTime +
                ", totalTravelTimePrice=" + totalTravelTimePrice +
                '}';
    }
}
