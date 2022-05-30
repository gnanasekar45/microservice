package com.luminn.firebase.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="driver_img")
@Getter
@Setter
public class Driver {

    @Id
    private String id;
    public String name;
    public int status;
    public double latitude;
    public double longitude;
    //public double distance;
    public String namedKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Driver() {}
    public Driver(String nam, double lat, double lon, int st, String nKey){
        name = nam;
        latitude = lat;
        longitude = lon;
        status = st;
        namedKey = nKey;
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

    public String getNamedKey() {
        return namedKey;
    }

    public void setNamedKey(String namedKey) {
        this.namedKey = namedKey;
    }


}
