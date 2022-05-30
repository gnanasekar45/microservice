package com.luminn.driver.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="location")
@Getter
@Setter
public class Location {
    String route;           //Dhule Moghan Road
    String locality;        //Ranmala
    String area1;           //Dhule    String area2;           //Maharashtra
    String country;         //India
    String postalCode;      //424311


    @GeoSpatialIndexed(type= GeoSpatialIndexType.GEO_2D)
    double[] locationCoord;  //order should be <longitude,latitude>   //get and set properties


    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getArea1() {
        return area1;
    }

    public void setArea1(String area1) {
        this.area1 = area1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public double[] getLocationCoord() {
        return locationCoord;
    }

    public void setLocationCoord(double[] locationCoord) {
        this.locationCoord = locationCoord;
    }
}
