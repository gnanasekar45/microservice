package com.luminn.firebase.model;

import lombok.Getter;
import lombok.Setter;


public class PushPhoneNotificationRequest extends  PushNotificationRequest {
  String phoneNumber;
  String name;

   public PushPhoneNotificationRequest(){

   }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
