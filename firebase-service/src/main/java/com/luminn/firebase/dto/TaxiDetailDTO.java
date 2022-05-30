package com.luminn.firebase.dto;


import com.luminn.firebase.entity.Supplier;

import java.util.ArrayList;
import java.util.List;


public class TaxiDetailDTO extends  TaxiUpdateDTO{

    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
