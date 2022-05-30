package com.luminn.firebase.request;

import org.springframework.stereotype.Component;

/**
 * Created by ch on 3/16/2016.
 */
@Component
public class AdvanceSearchDTO extends SearchDTO {
 public String category;
public String region;
public String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
