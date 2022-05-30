package com.luminn.firebase.repository;

import com.luminn.firebase.entity.DriverBill;
import com.luminn.firebase.entity.DriverInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DriverInfoRepository extends MongoRepository<DriverInfo, String> {

    Optional<DriverInfo> findByName(String name);
    Optional<DriverInfo> findByPhoneNumber(String phoneNumber1);
    Optional<DriverInfo> findByAadhar(String aadhar);
    Optional<DriverInfo> findByLicenceNumber(String licenceNumber);
    List<DriverInfo> findBySupplierId(String supplierId);

}
