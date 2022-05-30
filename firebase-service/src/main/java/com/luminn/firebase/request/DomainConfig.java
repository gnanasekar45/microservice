package com.luminn.firebase.request;

public class DomainConfig {

    String domain;
    String currency;
    int isPriceFlag;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getIsPriceFlag() {
        return isPriceFlag;
    }

    public void setIsPriceFlag(int isPriceFlag) {
        this.isPriceFlag = isPriceFlag;
    }
}
