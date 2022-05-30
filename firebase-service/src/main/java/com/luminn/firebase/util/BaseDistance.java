package com.luminn.firebase.util;

public class BaseDistance {
    float base;
    float distance;
    float finalPrice;
    float totalTravelTime;

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public float getTotalTravelTime() {
        return totalTravelTime;
    }

    public void setTotalTravelTime(float totalTravelTime) {
        this.totalTravelTime = totalTravelTime;
    }

    @Override
    public String toString() {
        return "BaseDistance{" +
                "base=" + base +
                ", distance=" + distance +
                ", finalPrice=" + finalPrice +
                ", totalTravelTime=" + totalTravelTime +
                '}';
    }
}
