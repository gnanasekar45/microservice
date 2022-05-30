package com.luminn.firebase.request;



public class PhoneDTO {

    private String phoneNumber;

    private String countryCode;

    private String token;

    private String userId;

    public PhoneDTO() {
    }

    public PhoneDTO(String phoneNumber, String countryCode, String token) {
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.token = token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
