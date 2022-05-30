package com.luminn.firebase.dto;

public class RideDetailDTO extends RideDTO {

    String userName;
    String driverName;
    String userEmailId;
    public String km;


    public RideDetailDTO(RideDTO rideDTO){
    /*    this.id = rideDTO.getId();
        this.km = rideDTO.getKm();
        this.distance = rideDTO.getDistance();
        this.totalPrice = rideDTO.getTotalPrice();
        this.distance = rideDTO.getDistance();
        this.userTotalPrice = rideDTO.getUserTotalPrice();*/
    }
    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getDriverName() {
        return driverName;
    }

    @Override
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }
}
