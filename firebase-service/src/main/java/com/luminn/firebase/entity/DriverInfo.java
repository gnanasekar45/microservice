package com.luminn.firebase.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="driverinfo")
@Getter
@Setter
public class DriverInfo {

    @Id
    String id;

    public String name;
    public String phoneNumber;

    public String fatherNo;
    public String wifeNo;
    public String dateofBirth;
    public String isPhoto;
    public String photoId;
    public String fatherName;
    public String wifeName;
    public String referenceName;
    public String licenceNumber;
    public String aadhar;
    public String domain;
    public String driverId;
    public String address;
    public String localAddress;
    public List<String> previousCompany = new ArrayList<>();
    public String review;
    public String remarks;
    public String rating;
    public boolean isjoined;
    public String olaExp;
    public String uberExp;
    public String othercompany;
    public String companyName;
    public String policeRemark;
    private double latitude;
    private double longtitude;
    public String type;
    public String income;
    public String perday;
    public String perHour;
    public String supplierId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFatherNo() {
        return fatherNo;
    }

    public void setFatherNo(String fatherNo) {
        this.fatherNo = fatherNo;
    }

    public String getWifeNo() {
        return wifeNo;
    }

    public void setWifeNo(String wifeNo) {
        this.wifeNo = wifeNo;
    }

    public String getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public String getIsPhoto() {
        return isPhoto;
    }

    public void setIsPhoto(String isPhoto) {
        this.isPhoto = isPhoto;
    }


    public String getWifeName() {
        return wifeName;
    }

    public void setWifeName(String wifeName) {
        this.wifeName = wifeName;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getPreviousCompany() {
        return previousCompany;
    }

    public void setPreviousCompany(List<String> previousCompany) {
        this.previousCompany = previousCompany;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isIsjoined() {
        return isjoined;
    }

    public void setIsjoined(boolean isjoined) {
        this.isjoined = isjoined;
    }

    public String getOlaExp() {
        return olaExp;
    }

    public void setOlaExp(String olaExp) {
        this.olaExp = olaExp;
    }

    public String getUberExp() {
        return uberExp;
    }

    public void setUberExp(String uberExp) {
        this.uberExp = uberExp;
    }

    public String getOthercompany() {
        return othercompany;
    }

    public void setOthercompany(String othercompany) {
        this.othercompany = othercompany;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getPoliceRemark() {
        return policeRemark;
    }

    public void setPoliceRemark(String policeRemark) {
        this.policeRemark = policeRemark;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getPerday() {
        return perday;
    }

    public void setPerday(String perday) {
        this.perday = perday;
    }

    public String getPerHour() {
        return perHour;
    }

    public void setPerHour(String perHour) {
        this.perHour = perHour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}
