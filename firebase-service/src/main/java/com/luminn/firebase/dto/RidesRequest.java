package com.luminn.firebase.dto;

public class RidesRequest extends  RideOneRequest {
    String coupan;

    public String getCoupan() {
        return coupan;
    }

    public void setCoupan(String coupan) {
        this.coupan = coupan;
    }
}
