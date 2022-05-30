package com.luminn.firebase.request;

/**
 * Created by hrlum on 10.04.2017.
 */
public class LongDTO {
    private double latitude;
    private String values;
   public LongDTO(){

    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
