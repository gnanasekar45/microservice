package com.luminn.firebase.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;




@Document(collection = "ConfigVersion")
@Getter
@Setter
public class ConfigVersion {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;


    private String androidUser;
    private String androidDriver;
    private String paymentType = null;
    private String country = null;
    private String currency = null;

    private String iosUser;
    private String iosDriver;
    @Indexed
    public String domain;
    public String googlePay;
    public String googlepayKey;
    public String customerPhoneNumber;

    private boolean isMandatoryAndroidUser;
    private boolean isMandatoryAndroidDriver;
    private boolean isMandatoryIOSUser;
    private boolean isMandatoryIOSDriver;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAndroidUser() {
        return androidUser;
    }

    public void setAndroidUser(String androidUser) {
        this.androidUser = androidUser;
    }

    public String getAndroidDriver() {
        return androidDriver;
    }

    public void setAndroidDriver(String androidDriver) {
        this.androidDriver = androidDriver;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIosUser() {
        return iosUser;
    }

    public void setIosUser(String iosUser) {
        this.iosUser = iosUser;
    }

    public String getIosDriver() {
        return iosDriver;
    }

    public void setIosDriver(String iosDriver) {
        this.iosDriver = iosDriver;
    }

    public boolean isMandatoryAndroidUser() {
        return isMandatoryAndroidUser;
    }

    public void setMandatoryAndroidUser(boolean mandatoryAndroidUser) {
        isMandatoryAndroidUser = mandatoryAndroidUser;
    }

    public boolean isMandatoryAndroidDriver() {
        return isMandatoryAndroidDriver;
    }

    public void setMandatoryAndroidDriver(boolean mandatoryAndroidDriver) {
        isMandatoryAndroidDriver = mandatoryAndroidDriver;
    }

    public boolean isMandatoryIOSUser() {
        return isMandatoryIOSUser;
    }

    public void setMandatoryIOSUser(boolean mandatoryIOSUser) {
        isMandatoryIOSUser = mandatoryIOSUser;
    }

    public boolean isMandatoryIOSDriver() {
        return isMandatoryIOSDriver;
    }

    public void setMandatoryIOSDriver(boolean mandatoryIOSDriver) {
        isMandatoryIOSDriver = mandatoryIOSDriver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
