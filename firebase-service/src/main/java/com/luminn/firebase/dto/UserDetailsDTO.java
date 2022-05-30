package com.luminn.firebase.dto;

public class UserDetailsDTO extends MobileDTO {

    private int seats;
    private String vehicleBrand;
    private String taxiNumber;
    private int vehicleYear;

   public UserDetailsDTO(MobileDTO p){
        this.role = p.role;
        this.firstName = p.firstName;
        this.lastName = p.lastName;
        this.role = p.role;
        this.email= p.email;
        this.phoneNumber = p.phoneNumber;
       this.password = p.password;
        this.domain = p.domain;
        this.website = p.website;
        this.latitude= p.latitude;
        this.longitude=p.longitude;
        this.category=p.category;
    }

    public UserDetailsDTO(){
        super();
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getTaxiNumber() {
        return taxiNumber;
    }

    public void setTaxiNumber(String taxiNumber) {
        this.taxiNumber = taxiNumber;
    }

    public int getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(int vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    @Override
    public String toString() {
        return "UserDetailsDTO{" +
                "seats=" + seats +
                ", vehicleBrand='" + vehicleBrand + '\'' +
                ", taxiNumber='" + taxiNumber + '\'' +
                ", vehicleYear=" + vehicleYear +
                '}';
    }
}
