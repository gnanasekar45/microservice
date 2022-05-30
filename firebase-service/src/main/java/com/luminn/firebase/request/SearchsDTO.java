package com.luminn.firebase.request;

import org.springframework.stereotype.Component;

@Component
public class SearchsDTO extends SearchDTO{
    public String coupan;

    public String getCoupan() {
        return coupan;
    }

    public void setCoupan(String coupan) {
        this.coupan = coupan;
    }
}
