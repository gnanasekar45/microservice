package com.luminn.firebase.entity;

public class Drivers {
    public String name;
    public int status;
    public double latitude;
    public double longitude;
    public String carType;
    public boolean online;

    public Drivers() {}
    public Drivers(String nam, double lat, double lon, int st){
        name = nam;
        latitude = lat;
        longitude = lon;
        status = st;
    }

    public Drivers(String nam, double lat, double lon, String carType){
        name = nam;
        latitude = lat;
        longitude = lon;
        carType = carType;
    }


    public Drivers(boolean online,String name, String carType){
        this.name = name;
        this.carType=carType;
        this.online = online;

    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
