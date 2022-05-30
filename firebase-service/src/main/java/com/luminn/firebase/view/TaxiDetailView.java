package com.luminn.firebase.view;

import com.luminn.firebase.dto.Taxi;
import com.luminn.firebase.entity.TaxiDetail;
import org.springframework.stereotype.Component;

@Component
public class TaxiDetailView extends TaxiView{

    String licenceNumber;
    String numberPlate;
    float amount;
    int km;
    String vehicleBrand;
    String status;
     boolean active;
    boolean isImage;


    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public TaxiDetailView(){

    }
    public TaxiDetailView(Taxi entity){

    }
    public TaxiDetailView(Taxi entity, TaxiDetail detail){
        super(entity,detail);
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }
}
